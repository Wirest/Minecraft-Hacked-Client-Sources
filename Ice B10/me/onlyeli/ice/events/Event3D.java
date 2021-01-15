package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class Event3D implements Event {
    private float ticks;
    private boolean isUsingShaders;

    public Event3D(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}
