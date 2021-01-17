// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.events;

import net.minecraft.entity.Entity;
import com.darkmagician6.eventapi.events.Event;

public class EventRender implements Event
{
    private Entity e;
    
    public EventRender(final Entity e) {
        this.e = null;
        this.setE(e);
    }
    
    public Entity getE() {
        return this.e;
    }
    
    public void setE(final Entity e) {
        this.e = e;
    }
}
