package org.delusion.katengine.event;

/**
 * API Access to the Event Dispatcher
 *
 * Declared the methods required for applications to function and work with these events.
 *
 */
public interface IEventDispatcher {
    void registerHandler(EventHandler<?> handler, Class<? extends Event> eventClass);

    void registerHandlers(Class<?> cls);

    <T> void registerHandlers(T obj);

    void fire(Event event);
    void fireImmediately(Event event);
}
