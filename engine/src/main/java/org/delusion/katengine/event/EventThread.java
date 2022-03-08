package org.delusion.katengine.event;

import static org.delusion.katengine.KatEngine.LOGGER;

public class EventThread implements Runnable {
    private volatile boolean running = true; // only updated on the dispatcher side, read on the event thread. no harmful race conditions can occur
    private Thread internalThread;
    private final Object internalThreadSync = new Object();
    private final Object runningSync = new Object();
    private EventDispatcher dispatcher;

    public EventThread(EventDispatcher eventDispatcher) {
        this.dispatcher = eventDispatcher;
    }

    @Override
    public void run() {
        LOGGER.debug("Started Event Thread [{}]", Thread.currentThread().getId());
        synchronized (internalThreadSync) {
            internalThread = Thread.currentThread();
            internalThreadSync.notifyAll();
        }

        while (isRunning()) {
            Event event = dispatcher.waitForEvent();
            if (event != null && dispatcher.hasHandlersForEvent(event.getClass())) {
                dispatcher.getHandlers(event.getClass()).forEach(eventHandler -> eventHandler.handle_(event));
            }
        }

        LOGGER.debug("Shutting down Event Thread [{}]", Thread.currentThread().getId());
    }

    private boolean isRunning() {
        synchronized (runningSync) {
            return running;
        }
    }

    public void kill() {
        synchronized (runningSync) {
            this.running = false;
        }
    }

    public Thread getInternalThread() {
        synchronized (internalThreadSync) {
            return internalThread;
        }
    }
}
