package cn.kody.debug.events;

import com.darkmagician6.eventapi.types.EventType;
import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;

public class EventPreMotion implements Event, Cancellable
{
    public double y;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean cancel;
    public EventType type;
    
    public EventPreMotion(final double y, final float yaw, final float pitch, final boolean onGround, EventType type) {
        super();
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = type;
    }
    
    public EventType getEventType() {
        return this.type;
    }
    
    public EventPreMotion(final EventType type) {
        this.type = type;
    }
    
    public boolean isPre() {
        boolean b;
        if (this.type == EventType.PRE) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }
    
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
