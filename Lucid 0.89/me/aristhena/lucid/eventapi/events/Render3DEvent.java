package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class Render3DEvent extends Event
{
    public float partialTicks;
    
    public Render3DEvent(float partialTicks)
    {
	this.partialTicks = partialTicks;
    }
}
