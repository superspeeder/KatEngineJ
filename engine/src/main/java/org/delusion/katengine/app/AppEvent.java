package org.delusion.katengine.app;

import org.delusion.katengine.event.Event;

public abstract class AppEvent extends Event {

    private final App app;

    public AppEvent(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public static class Create extends AppEvent {

        public Create(App app) {
            super(app);
        }
    }

    public static class Cleanup extends AppEvent {

        public Cleanup(App app) {
            super(app);
        }
    }
}
