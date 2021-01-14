package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventPushOutOfBlock
extends EventCancellable {
    private boolean cancel;

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}

