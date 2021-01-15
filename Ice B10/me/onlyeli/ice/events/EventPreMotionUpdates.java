package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public class EventPreMotionUpdates extends EventCancellable {
    private float yaw;
    private float pitch;
    private double x;
    private double y;
    private double z;
    private boolean ground;

    public EventPreMotionUpdates(float yaw, float pitch, double x, double y, double z, boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ground = ground;
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

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isGround() {
        return this.ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }
}