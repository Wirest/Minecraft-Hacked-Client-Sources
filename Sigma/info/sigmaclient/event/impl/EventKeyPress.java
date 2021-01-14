package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;

public class EventKeyPress extends Event {
    private int key;

    public void fire(int key) {
        this.key = key;
        super.fire();
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
