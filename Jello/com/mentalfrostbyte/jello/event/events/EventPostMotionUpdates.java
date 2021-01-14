package com.mentalfrostbyte.jello.event.events;


public class EventPostMotionUpdates
implements Event {
    public float yaw;
    public float pitch;
    public double y;
    public boolean ground;

    public void UpdateEvent() {
    }

    public void UpdateEvent(double y, float yaw, float pitch, boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.ground = ground;
    }
}

