// 
// Decompiled by Procyon v0.5.30
// 

package com.darkmagician6.eventapi.events;

public abstract class EventStoppable implements Event
{
    private boolean stopped;
    
    public void stop() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
}
