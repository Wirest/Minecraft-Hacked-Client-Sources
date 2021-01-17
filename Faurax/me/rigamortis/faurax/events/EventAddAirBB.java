package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;

public class EventAddAirBB extends EventCancellable implements Event
{
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    
    public double getMinX() {
        return this.minX;
    }
    
    public void setMinX(final double minX) {
        this.minX = minX;
    }
    
    public double getMinY() {
        return this.minY;
    }
    
    public void setMinY(final double minY) {
        this.minY = minY;
    }
    
    public double getMinZ() {
        return this.minZ;
    }
    
    public void setMinZ(final double minZ) {
        this.minZ = minZ;
    }
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    public void setMaxY(final double maxY) {
        this.maxY = maxY;
    }
    
    public double getMaxZ() {
        return this.maxZ;
    }
    
    public void setMaxZ(final double maxZ) {
        this.maxZ = maxZ;
    }
}
