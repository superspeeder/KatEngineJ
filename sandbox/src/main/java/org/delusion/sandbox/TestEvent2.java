package org.delusion.sandbox;

import org.delusion.katengine.event.Event;

public class TestEvent2 extends Event {
    public int i;

    public TestEvent2(int i) {
        this.i = i;
    }
}
