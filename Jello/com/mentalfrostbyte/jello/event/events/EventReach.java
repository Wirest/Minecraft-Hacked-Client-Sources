package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventReach
extends EventCancellable {
	private float range;
    
    public EventReach(final float f) {
        this.range = f;
    }
    
    public float getRange() {
        return this.range;
    }
    
    public void setRange(final float range) {
        this.range = range;
    }
}

