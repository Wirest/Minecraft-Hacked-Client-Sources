// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventGuiClose extends EventCancellable
{
    private boolean cancel;
    
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancel = state;
    }
}
