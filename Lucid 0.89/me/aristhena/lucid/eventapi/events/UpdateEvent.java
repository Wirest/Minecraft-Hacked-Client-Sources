package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class UpdateEvent extends Event
{
    public State state;
    public float yaw, pitch;
    public double y;
    public boolean ground;
    
    public UpdateEvent()
    {
	this.state = State.POST;
    }
    
    public UpdateEvent(double y, float yaw, float pitch, boolean ground)
    {
	this.state = State.PRE;
	this.yaw = yaw;
	this.pitch = pitch;
	this.y = y;
	this.ground = ground;
    }
}
