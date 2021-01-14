package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;

public class EventKey extends Event {
    int key;
    public EventKey(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
