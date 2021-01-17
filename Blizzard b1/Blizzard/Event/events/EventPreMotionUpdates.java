/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event.events;

import Blizzard.Event.Event;

public class EventPreMotionUpdates
extends Event {
    private boolean cancel;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public double y;

    public EventPreMotionUpdates(float yaw, float pitch, double y) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

