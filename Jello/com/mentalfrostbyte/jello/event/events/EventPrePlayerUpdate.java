package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventPrePlayerUpdate
extends EventCancellable {
    private boolean cancel;
    public float yaw;
    public float pitch;
    public double y;

    public EventPrePlayerUpdate(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public double getY() {
        return this.y;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}

