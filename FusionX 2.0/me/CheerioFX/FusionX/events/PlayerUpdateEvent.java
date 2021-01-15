// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public abstract class PlayerUpdateEvent extends EventCancellable implements Event
{
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean sneaking;
    
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
    
    public boolean isSneaking() {
        return this.sneaking;
    }
    
    public void setSneaking(final boolean sneaking) {
        this.sneaking = sneaking;
    }
}
