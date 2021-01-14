package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventRotation
extends EventCancellable {
    private boolean cancel;
    private float yaw;
    private float pitch;

    public boolean isCancel() {
        return this.cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}

