package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;
import net.minecraft.entity.Entity;

public class NametagRenderEvent extends Event
{
    public Entity entity;
    public String string;
    public double x, y, z;
    
    public NametagRenderEvent(Entity entity, String string, double x, double y, double z)
    {
	this.entity = entity;
	this.string = string;
	this.x = x;
	this.y = y;
	this.z = z;
    }
}
