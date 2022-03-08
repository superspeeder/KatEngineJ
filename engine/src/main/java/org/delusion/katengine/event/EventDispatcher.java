package org.delusion.katengine.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.delusion.katengine.KatEngine.LOGGER;

public class EventDispatcher implements IEventDispatcher {

    private static final int THREAD_COUNT = 10;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    private final ConcurrentLinkedDeque<Event> eventQueue = new ConcurrentLinkedDeque<>();

    private final Map<Class<? extends Event>, List<EventHandler<?>>> eventHandlers = new HashMap<>();
    private final Set<EventThread> eventThreads = new HashSet<>();
    private boolean shuttingDown = false;
    private final Object notificationObj = new Object();

    @Override
    public void registerHandler(EventHandler<?> handler, Class<? extends Event> eventClass) {
        if (!eventHandlers.containsKey(eventClass)) eventHandlers.put(eventClass, new ArrayList<>());
        eventHandlers.get(eventClass).add(handler);
        LOGGER.debug("Registered handler for event {}", eventClass.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerHandlers(Class<?> cls) {
        Arrays.stream(cls.getMethods())
                .filter(method -> method.isAnnotationPresent(SubscribeEvent.class) && validateParameters(method) && Modifier.isStatic(method.getModifiers()))
                .forEach(method -> {
                    Parameter[] parameters = method.getParameters();
                    Class<? extends Event> et = (Class<? extends Event>) parameters[0].getType();
                    registerHandler(event -> {
                        try {
                            method.invoke(null, et);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            LOGGER.catching(e);
                            LOGGER.error("Failed to invoke event handler {} for event {}", method.getName(), et.getName());
                        }
                    }, et);
                });

        LOGGER.debug("Registered Event Handlers for {}", cls.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerHandlers(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();

        Arrays.stream(cls.getMethods())
                .filter(method -> method.isAnnotationPresent(SubscribeEvent.class) && validateParameters(method) && !Modifier.isStatic(method.getModifiers()))
                .forEach(method -> {
                    Parameter[] parameters = method.getParameters();
                    Class<? extends Event> et = (Class<? extends Event>) parameters[0].getType();
                    registerHandler(event -> {
                        try {
                            method.invoke(obj, et.cast(event));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            LOGGER.catching(e);
                            LOGGER.error("Failed to invoke event handler {} for event {}", method.getName(), et.getName());
                        }
                    }, et);
                });

        LOGGER.debug("Registered Event Handlers for {}", cls.getName());
    }

    private boolean validateParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        return parameters.length == 1 && validateEventClass(parameters[0].getType()) && !parameters[0].getType().isInterface();
    }

    private boolean validateEventClass(Class<?> cls) {
        return Event.class.isAssignableFrom(cls);
    }

    public EventDispatcher() {
        LOGGER.debug("Starting Event Dispatcher");

        EventThread eventThread;
        for (int t = 0 ; t < THREAD_COUNT ; t++) {
            eventThread = new EventThread(this);
            eventThreads.add(eventThread);
            executor.execute(eventThread);
        }

        LOGGER.info("Started Event Dispatcher");
    }

    public void stop() {
        LOGGER.info("Stopping Event Dispatcher");
        shuttingDown = true;
        executor.shutdown();
        eventThreads.forEach(EventThread::kill);
        synchronized (notificationObj) {
            notificationObj.notifyAll();
        }
        try {
            if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
                LOGGER.warn("Event Threads failed to shutdown in a timely manner, trying to force shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
        }
    }

    public Event waitForEvent() {
        synchronized (eventQueue) {
            if (!eventQueue.isEmpty()) {
                return eventQueue.pop();
            }
        }

        while (true) {
            if (shuttingDown) {
                return null;
            }

            try {
                synchronized (notificationObj) {
                   notificationObj.wait();
                }
            } catch (InterruptedException ignored) {
            }

            synchronized (eventQueue) {
                if (!eventQueue.isEmpty()) {
                    return eventQueue.pop();
                }
            }
        }
    }

    public List<EventHandler<?>> getHandlers(Class<? extends Event> cls) {
        return eventHandlers.get(cls);
    }

    @Override
    public void fire(Event event) {
        if (shuttingDown) return;
        eventQueue.addLast(event);
        synchronized (notificationObj) {
            notificationObj.notify();
        }
    }

    @Override
    public void fireImmediately(Event event) {
        if (shuttingDown) return;
        if (eventHandlers.containsKey(event.getClass())) {
            eventHandlers.get(event.getClass()).forEach(eventHandler -> {eventHandler.handle_(event);});
        }
    }

    public boolean hasHandlersForEvent(Class<? extends Event> et) {
        return eventHandlers.containsKey(et);
    }
}

