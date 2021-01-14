package com.mentalfrostbyte.jello.event.events;


public class EventPostPlayerUpdate
implements Event {
    public static float yaw;
    public static float pitch;
    public static double y;
    public static boolean ground;

    public void UpdateEvent() {
    }

    public void UpdateEvent(double y, float yaw, float pitch, boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.ground = ground;
    }
    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getY() {
        return this.y;
    }

    public boolean isOnground() {
        return this.ground;
    }

    

    public void setYaw(float yaw) {
    	EventPostPlayerUpdate.yaw = yaw;
    }

    public void setPitch(float pitch) {
    	EventPostPlayerUpdate.pitch = pitch;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    
}

