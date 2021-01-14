package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventDimensionChange
extends EventCancellable {
    int dimension;

    public EventDimensionChange(int dimension) {
        this.dimension = dimension;
    }

    public int getDimension() {
        return this.dimension;
    }
}

