/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;

public class EventMove
implements Event {
    public double motionX;
    public double motionY;
    public double motionZ;

    public EventMove(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public void setMotionX(double d) {
        this.motionX = d;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public void setMotionY(double d) {
        this.motionY = d;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionZ(double d) {
        this.motionZ = d;
    }
}

