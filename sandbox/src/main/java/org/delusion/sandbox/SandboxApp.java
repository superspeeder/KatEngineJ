package org.delusion.sandbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.delusion.katengine.app.App;
import org.delusion.katengine.app.AppEvent;
import org.delusion.katengine.app.TestEvent;
import org.delusion.katengine.event.SubscribeEvent;

public class SandboxApp extends App {

    private static final Logger LOGGER = LogManager.getLogger("sandbox");

    public SandboxApp() {
        getDispatcher().registerHandlers(this);
    }

    @SubscribeEvent
    public void create(AppEvent.Create event) {
    }

    @Override
    protected void update(double dt) {
        LOGGER.info("fps {}", 1.0 / dt);
    }

    @SubscribeEvent
    public void cleanup(AppEvent.Cleanup event) {


    }

    @Override
    protected void postCleanup() {
    }

    public static void main(String[] args) {
        LOGGER.info("Starting!");
        new SandboxApp().run();
    }
}
