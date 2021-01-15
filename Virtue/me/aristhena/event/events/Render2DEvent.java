// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.event.events;

import me.aristhena.event.Event;

public class Render2DEvent extends Event
{
    private int width;
    private int height;
    
    public Render2DEvent(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
}
