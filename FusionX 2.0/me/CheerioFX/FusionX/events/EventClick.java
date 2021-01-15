// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventClick extends EventCancellable
{
    private boolean canceled;
    
    @Override
    public boolean isCancelled() {
        return this.canceled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.canceled = state;
    }
}
