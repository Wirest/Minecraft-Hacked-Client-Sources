package dev.astroclient.client.event.impl.input;

import awfdd.ksksk.ap.zajkb.rgds.Event;

public class EventKey extends Event {

    public int getKey() {
        return key;
    }

    public EventKey(int key) {
        this.key = key;
    }

    private int key;
}
