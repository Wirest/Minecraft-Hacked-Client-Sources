package me.ihaq.iClient.event.events;

import me.ihaq.iClient.event.Event;

/**
 * Created by Hexeption on 07/01/2017.
 */
public class EventPreMotionUpdates extends Event {

    private boolean cancel;

    public float yaw, pitch;

    public double y;

    public EventPreMotionUpdates(float yaw, float pitch, double y) {

        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
    }

    public boolean isCancel() {

        return cancel;
    }

    public void setCancel(boolean cancel) {

        this.cancel = cancel;
    }

    public float getYaw() {

        return yaw;
    }

    public void setYaw(float yaw) {

        this.yaw = yaw;
    }

    public float getPitch() {

        return pitch;
    }

    public void setPitch(float pitch) {

        this.pitch = pitch;
    }

    public double getY() {

        return y;
    }

    public void setY(double y) {

        this.y = y;
    }
}
