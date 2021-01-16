/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.Event;

public final class EventKeyPressed
implements Event {
    private final int eventKey;

    public EventKeyPressed(int eventKey) {
        this.eventKey = eventKey;
    }

    public int getEventKey() {
        return this.eventKey;
    }
}

