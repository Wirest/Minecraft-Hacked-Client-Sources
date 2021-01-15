package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class MoveEvent extends Event
{
    public double x, y, z;
    
    public MoveEvent(double x, double y, double z)
    {
	this.x = x;
	this.y = y;
	this.z = z;
    }
}
