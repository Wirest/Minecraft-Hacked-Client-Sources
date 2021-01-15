package VenusClient.online.Event.impl;

import VenusClient.online.Event.Event;

public class EventKey extends Event {

    private int key;

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
