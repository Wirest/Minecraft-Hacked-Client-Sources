package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

public class EventPreMotionUpdates
extends EventCancellable {
    private boolean cancel;
    public float yaw;
    public float pitch;
    //private Location location;
    public boolean onGround;

    public EventPreMotionUpdates(/*Location location, */boolean onGround, float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
       // this.location = location;
    }

    //public Location getLocation() {
    //    return this.location;
    //}

    //public void setLocation(Location location) {
    //    this.location = location;
    //}
    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }
    public boolean isOnGround() {
        return this.onGround;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
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

