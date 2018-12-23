package com.github.rishabh9.rikostarter.services;

import com.github.rishabh9.riko.upstox.websockets.MessageSubscriber;
import com.github.rishabh9.riko.upstox.websockets.messages.*;
import com.github.rishabh9.rikostarter.exceptions.WebSocketError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;

@Log4j2
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpstoxWebSocketSubscriber implements MessageSubscriber {

    private Flow.Subscription subscription;

    @Autowired
    private UpstoxWebSocketService upstoxWebSocketService;

    @Override
    public void onSubscribe(final Flow.Subscription subscription) {
        log.info("Subscribed! Ready to receive messages!");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(WebSocketMessage item) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (item instanceof BinaryMessage) {
            final String message = ((BinaryMessage) item).getMessageAsString();
            final String[] rows = message.split(";");
            for (String row : rows) {
                final String[] cols = row.split(",");
                final long diff = currentTimeMillis - Long.parseLong(cols[0]);
                String data = cols[2] + "," + cols[0] + "," + currentTimeMillis + "," + diff;
                if (diff > 2000) log.info(data);
            }
        } else if (item instanceof ConnectedMessage) {
            final ConnectedMessage message = (ConnectedMessage) item;
            log.info("Connected to Upstox: {}", message.getMessage());
        } else if (item instanceof DisconnectedMessage) {
            final DisconnectedMessage message = (DisconnectedMessage) item;
            log.info("Disconnected from Upstox:  Code: {}, Reason: {}", message.getCode(), message.getReason());
        } else if (item instanceof ClosingMessage) {
            final ClosingMessage message = (ClosingMessage) item;
            log.warn("Closing the web-socket connection:  Code: {}, Reason: {}", message.getCode(), message.getReason());
        } else if (item instanceof ErrorMessage) {
            final ErrorMessage message = (ErrorMessage) item;
            // Reusing the 'onError()'
            onError(new WebSocketError("Error from Upstox: " + message.getReason(), message.getThrowable()));
        } else {
            // if (item instanceof TextMessage) {
            final TextMessage message = (TextMessage) item;
            log.info("Text message received: {}", message);
        }
        // Ask for the next message (do not miss this line)
        this.subscription.request(1);
    }

    @Override
    public void onError(final Throwable throwable) {
        log.fatal("Error occurred: {}", throwable);
        try {
            cleanUp();
            Thread.sleep(1000);
            upstoxWebSocketService.connect();
        } catch (InterruptedException e) {
            log.fatal("Error sleeping", e);
        }
    }

    @Override
    public void onComplete() {
        log.info("Subscription is now complete - no more messages from Upstox.");
    }

    @Override
    public String getName() {
        return "RikoWsSubscriber"; // Provide a unique name. Helps with debugging.
    }

    public void cleanUp() {
        if (null != subscription) {
            this.subscription.cancel();
        }
    }
}
