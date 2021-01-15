// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.Event;

public final class KeyPressedEvent implements Event
{
    private boolean canceled;
    private final int eventKey;
    
    public KeyPressedEvent(final int eventKey) {
        this.eventKey = eventKey;
    }
    
    public int getEventKey() {
        return this.eventKey;
    }
    
    public boolean isCancelled() {
        return this.canceled;
    }
    
    public void setCancelled(final boolean state) {
        this.canceled = state;
    }
}
