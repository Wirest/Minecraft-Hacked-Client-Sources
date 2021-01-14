
package me.memewaredevs.client.event.events;

import me.memewaredevs.client.event.Event;

public class MovementEvent extends Event {
    private double x;
    private double y;
    private double z;

    public MovementEvent(final double x, final double y2, final double z) {
        this.x = x;
        this.y = y2;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y2) {
        this.y = y2;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
