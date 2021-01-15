package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class UseItemEvent extends Event
{
    public State state;
    
    public UseItemEvent(State state)
    {
	this.state = state;
    }
}
