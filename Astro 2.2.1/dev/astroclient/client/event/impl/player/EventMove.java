package dev.astroclient.client.event.impl.player;

import awfdd.ksksk.ap.zajkb.rgds.Event;

/**
* @author Zane for PublicBase
* @since 10/24/19
*/

public class EventMove extends Event {

    private double x;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    private double y;
    private double z;
}
