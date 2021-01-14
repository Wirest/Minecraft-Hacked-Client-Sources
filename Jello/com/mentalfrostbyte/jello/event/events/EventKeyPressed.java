package com.mentalfrostbyte.jello.event.events;


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

