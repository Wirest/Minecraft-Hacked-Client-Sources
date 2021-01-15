package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class StepEvent extends Event
{
    public double stepHeight;
    public boolean bypass;
    
    public StepEvent(double stepHeight)
    {
	this.stepHeight = stepHeight;
    }
    
}
