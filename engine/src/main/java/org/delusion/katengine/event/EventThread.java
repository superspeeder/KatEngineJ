package org.delusion.katengine.event;

import static org.delusion.katengine.KatEngine.LOGGER;

public class EventThread implements Runnable {
    private volatile boolean running = true; // only updated on the dispatcher side, read on the event thread. no harmful race conditions can occur
    private Thread internalThread;
    private final Object internalThreadSync = new Object();
    private final Object runningSync = new Object();
    private EventDispatcher dispatcher;

    private final Object shutdownSignaller = new Object();
    private final Object doneSync = new Object();
    private boolean done = false;

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
        synchronized (doneSync) {
            done = true;
        }
        synchronized (shutdownSignaller) {
            shutdownSignaller.notifyAll();
        }
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

    public void join() {
        Thread et = getInternalThread();
        if (et == null) {
            synchronized (internalThreadSync) {
                try {
                    internalThreadSync.wait();
                } catch (InterruptedException ignored) {
                }
                et = internalThread;
            }
        }

        try {
            et.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void waitToShutdown() {
        if (!isDone()) {
            try {
                synchronized (shutdownSignaller) {
                    shutdownSignaller.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDone() {
        synchronized (doneSync) {
            return done;
        }
    }
}
