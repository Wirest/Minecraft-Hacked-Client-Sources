package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;
import net.minecraft.entity.Entity;

public class PreAttackEvent extends Event
{
    public Entity attacked;
    
    public PreAttackEvent(Entity attacked)
    {
	this.attacked = attacked;
    }
    
}
