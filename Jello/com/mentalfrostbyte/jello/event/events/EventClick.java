package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventClick
extends EventCancellable {
    private boolean canceled;

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.canceled = state;
    }
}

