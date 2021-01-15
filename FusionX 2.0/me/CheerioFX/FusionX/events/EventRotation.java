// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventRotation extends EventCancellable
{
    private boolean cancel;
    private float yaw;
    private float pitch;
    
    public boolean isCancel() {
        return this.cancel;
    }
    
    public void setCancel(final boolean cancel) {
        this.cancel = cancel;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
}
