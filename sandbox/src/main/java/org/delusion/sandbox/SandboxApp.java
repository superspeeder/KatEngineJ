package org.delusion.sandbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.delusion.katengine.app.App;
import org.delusion.katengine.app.TestEvent;
import org.delusion.katengine.event.SubscribeEvent;

public class SandboxApp extends App {

    private static final Logger LOGGER = LogManager.getLogger("sandbox");

    public SandboxApp() {
        getDispatcher().registerHandlers(this);
    }

    @SubscribeEvent
    public void test(TestEvent event) {
        LOGGER.info("Test!");
    }

    @Override
    protected void create() {

    }

    @Override
    protected void update(double dt) {

    }

    @Override
    protected void cleanup() {

    }

    public static void main(String[] args) {
        LOGGER.info("Starting!");
        new SandboxApp().run();
    }
}
