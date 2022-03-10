package org.delusion.katengine.app;

import org.delusion.katengine.event.EventDispatcher;
import org.delusion.katengine.event.IEventDispatcher;

import static org.delusion.katengine.KatEngine.LOGGER;

public abstract class App {

    private final EventDispatcher dispatcher;
    private final Ticker clock = new Ticker();
    protected AppConfig config = new AppConfig();

    public App() {
        dispatcher = new EventDispatcher();
    }

    public final void run() {
        LOGGER.info("Hello!");

        dispatcher.fire(new TestEvent());

        internalCreate();

        while (isOpen()) {
            internalUpdate(clock.getTimeSinceLastTickd());
            clock.tickAt(config.maxFPS);
        }

        internalCleanup();

    }

    private boolean isOpen() {
        return clock.getTicks() < 10000;
    }

    private void internalCreate() {
        dispatcher.fireImmediately(new AppEvent.Create(this));
    }

    private void internalUpdate(double dt) {
        update(dt);
    }

    protected void postCleanup() {

    }

    private void internalCleanup() {
        dispatcher.fireImmediately(new AppEvent.Cleanup(this));

        dispatcher.stop();

        postCleanup();
    }

    protected abstract void update(double dt);

    public IEventDispatcher getDispatcher() {
        return dispatcher;
    }
}
