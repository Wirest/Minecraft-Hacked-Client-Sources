// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.event.events;

import me.aristhena.event.Event;

public class MoveEvent extends Event
{
    private double x;
    private double y;
    private double z;
    
    public MoveEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
}
