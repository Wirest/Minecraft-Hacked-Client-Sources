package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;

public class EventVelocity extends Event {
    private double x, y, z;

    public void fire(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        super.fire();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
