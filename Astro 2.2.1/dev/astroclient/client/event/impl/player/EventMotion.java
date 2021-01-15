package dev.astroclient.client.event.impl.player;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;

/**
* @author Zane for PublicBase
* @since 10/23/19
*/

public class EventMotion extends Event {

    private double x;
    private double y;
    private double z;
    private double prevX;
    private double prevY;
    private double prevZ;
    private float yaw;

    private float pitch;
    private boolean onGround;

    public EventMotion(EventType type, double x, double y, double z, double prevX, double prevY, double prevZ, float yaw, float pitch, boolean onGround) {
        super(type);
        this.x = x;
        this.y = y;
        this.z = z;
        this.prevX = prevX;
        this.prevY = prevY;
        this.prevZ = prevZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public double getPrevZ() {
        return prevZ;
    }

    public void setPrevZ(double prevZ) {
        this.prevZ = prevZ;
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
