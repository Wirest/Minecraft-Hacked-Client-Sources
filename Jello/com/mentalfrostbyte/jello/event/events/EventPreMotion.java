package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventPreMotion
extends EventCancellable {
    public double x;
    public double y;
    public double z;

    public EventPreMotion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

