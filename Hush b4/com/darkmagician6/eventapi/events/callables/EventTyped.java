// 
// Decompiled by Procyon v0.5.36
// 

package com.darkmagician6.eventapi.events.callables;

import com.darkmagician6.eventapi.events.Typed;
import com.darkmagician6.eventapi.events.Event;

public abstract class EventTyped implements Event, Typed
{
    private final byte type;
    
    protected EventTyped(final byte eventType) {
        this.type = eventType;
    }
    
    @Override
    public byte getType() {
        return this.type;
    }
}
