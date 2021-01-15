// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.Event;

public class StepEvent implements Event
{
    public double stepHeight;
    public boolean bypass;
    
    public StepEvent(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
}
