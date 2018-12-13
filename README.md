# Upstox WebSocket Delay Test

### Running the application

1. Ensure you have __JDK 9__ (or above) and __Git__ installed.
2. Clone this project.
3. `cd upstox-ws-test`
4. Update `com.github.rishabh9.rikostarter.constants.RikoStarterConstants` with your Upstox's `API Key` and `API Secret`.
4. `./gradlew build`
5. Run using Upstox production: `java -Driko.server.url=api.upstox.com -Driko.ws.server.url=ws-api.upstox.com -jar riko-starter-0.0.1-SNAPSHOT.jar` 
6. Run using Upstox staging: `java -Driko.server.url=staging-api.upstox.com -Driko.ws.server.url=staging-ws-api.upstox.com -jar riko-starter-0.0.1-SNAPSHOT.jar`
7. Open your browser and visit `http://localhost:8080`.
8. Click `Login` and log into Upstox
9. Click `Subscribe`
10. Click `Connect WebSocket`

Keep an eye on the console logs to see what happens when you click a button on the UI.

### Reading the Output

Once you have the app running and you have logged in, subscribed to symbols and connected to websocket,
you'll see output in the following format on your console"

```bash
2018-12-13 12:56:25.042  INFO 5605 --- [Pool-1-worker-1] c.g.r.r.s.UpstoxWebSocketSubscriber      : INDUSINDBK18DECFUT,1544685979000,1544685985028,6028
```

`INDUSINDBK18DECFUT,1544685979000,1544685985028,6028` represents the Symbol, Snapshot timestamp (provided by upstox), Timestamp when snapshot received, Difference in time between the two timestamps, respectively.
