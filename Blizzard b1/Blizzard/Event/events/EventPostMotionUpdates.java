/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event.events;

import Blizzard.Event.Event;

public class EventPostMotionUpdates
extends Event {
    public float yaw;
    public float pitch;
    public boolean ground;
    public double y;

    public EventPostMotionUpdates(float yaw, float pitch, boolean ground, double y) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
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

    public boolean isGround() {
        return this.ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

