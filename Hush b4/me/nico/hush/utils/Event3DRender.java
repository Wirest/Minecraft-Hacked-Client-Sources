// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import com.darkmagician6.eventapi.events.Event;

public class Event3DRender implements Event
{
    private final float partialTicks;
    
    public Event3DRender(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
