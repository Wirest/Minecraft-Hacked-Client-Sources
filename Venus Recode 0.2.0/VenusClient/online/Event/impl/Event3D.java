package VenusClient.online.Event.impl;

import VenusClient.online.Event.Event;

public class Event3D extends Event {
    private float partialTicks;

    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}