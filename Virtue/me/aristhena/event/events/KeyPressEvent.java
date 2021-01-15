// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.event.events;

import me.aristhena.event.Event;

public class KeyPressEvent extends Event
{
    private int key;
    
    public KeyPressEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
}
