package org.delusion.katengine.app;

import org.delusion.katengine.event.EventDispatcher;
import org.delusion.katengine.event.IEventDispatcher;

import static org.delusion.katengine.KatEngine.LOGGER;

public abstract class App {

    private final EventDispatcher dispatcher;
    private final Ticker clock = new Ticker();

    public App() {
        dispatcher = new EventDispatcher();
    }

    public final void run() {
        LOGGER.info("Hello!");

        dispatcher.fire(new TestEvent());

        internalCreate();



        internalCleanup();

    }

    private void internalCreate() {

    }

    private void internalUpdate() {

    }

    private void internalCleanup() {
        dispatcher.stop();
    }

    protected abstract void create();
    protected abstract void update(double dt);
    protected abstract void cleanup();

    public IEventDispatcher getDispatcher() {
        return dispatcher;
    }
}
