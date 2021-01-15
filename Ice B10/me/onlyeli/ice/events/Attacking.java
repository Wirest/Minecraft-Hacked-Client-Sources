package me.onlyeli.ice.events;

import net.minecraft.entity.Entity;
import me.onlyeli.ice.events.Events;

public class Attacking extends Events
{
    private Entity entity;
    
    public Attacking(final Entity entity) {
        this.entity = entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
