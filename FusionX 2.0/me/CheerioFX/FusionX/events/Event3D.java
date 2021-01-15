// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.Event;

public class Event3D implements Event
{
    private static float entityT;
    
    public Event3D(final float entityT) {
        Event3D.entityT = entityT;
    }
    
    public static float getPartialTicks() {
        return Event3D.entityT;
    }
}
