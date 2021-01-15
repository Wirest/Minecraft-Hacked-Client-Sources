package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class SprintEvent extends Event
{
    public boolean sprint;
    
    public SprintEvent(boolean sprint)
    {
	this.sprint = sprint;
    }
}
