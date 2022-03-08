package org.delusion.katengine.event;

@FunctionalInterface
public interface EventHandler<E extends Event> {

    void handle(E event);

    @SuppressWarnings("unchecked")
    default void handle_(Event event) {
        handle((E)event);
    }
}
