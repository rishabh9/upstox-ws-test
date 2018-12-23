package com.github.rishabh9.rikostarter.controllers;

import com.github.rishabh9.riko.upstox.login.LoginService;
import com.github.rishabh9.riko.upstox.login.models.AccessToken;
import com.github.rishabh9.riko.upstox.login.models.TokenRequest;
import com.github.rishabh9.rikostarter.services.MasterContractService;
import com.github.rishabh9.rikostarter.services.UpstoxWebSocketService;
import com.github.rishabh9.rikostarter.utilities.Cache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ExecutionException;

import static com.github.rishabh9.rikostarter.constants.RikoStarterConstants.GRANT_TYPE;
import static com.github.rishabh9.rikostarter.constants.RikoStarterConstants.REDIRECT_URI;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Log4j2
@Controller
public class MainController {

    @Autowired
    private Cache cache;

    @Autowired
    private MasterContractService masterContractService;

    @Autowired
    private UpstoxWebSocketService upstoxWebSocketService;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/callback", method = GET)
    public ModelAndView callback(@RequestParam(required = false) String code)
            throws Exception {
        log.info("Receiving code from Upstox - {}", code);

        final TokenRequest tokenRequest = new TokenRequest(code, GRANT_TYPE, REDIRECT_URI);
        try {
            final AccessToken accessToken = loginService.getAccessToken(tokenRequest).get();
            // Save 'accessToken' into a database or cache
            cache.updateAccessToken(accessToken);
            log.info("AccessToken: {}", cache.getAccessToken().get().getToken());
        } catch (ExecutionException | InterruptedException e) {
            log.fatal("Error obtaining access token", e);
            throw e;
        }

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "You've logged into Upstox!");
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/single", method = GET)
    public ModelAndView fetchSingleMasterContract() {
        log.info("Triggered download of single master contract");
        masterContractService.downloadIndividualMasterContract();
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/all", method = GET)
    public ModelAndView fetchAllMasterContracts() {
        log.info("Triggered download of all master contracts");
        masterContractService.downloadAllContracts();
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/connect", method = GET)
    public ModelAndView wsConnect() {
        log.info("Triggered websocket connect request");
        upstoxWebSocketService.connect();
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/disconnect", method = GET)
    public ModelAndView wsDisconnect() {
        log.info("Triggered websocket disconnect request");
        upstoxWebSocketService.disconnect();
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/subscribe", method = GET)
    public ModelAndView subscribe() {
        log.info("Triggeredsubscribe request");
        masterContractService.subscribe();
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/unsubscribe", method = GET)
    public ModelAndView unsubscribe() {
        log.info("Triggered unsubscribe request");
        masterContractService.unsubscribe();
        return new ModelAndView("redirect:/index");
    }
}


