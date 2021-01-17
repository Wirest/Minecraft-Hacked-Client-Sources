// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.events;

import com.darkmagician6.eventapi.events.Event;

public class PreUpdate implements Event
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    
    public PreUpdate(final double x, final double y, final double z, final float yaw, final float pitch, final boolean ground) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setRotations(yaw, pitch);
    }
    
    public float getPitch() {
        return this.pitch;
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
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public float[] getRotations() {
        return new float[] { this.getYaw(), this.getPitch() };
    }
    
    public void setRotations(final float yaw, final float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
    }
}
