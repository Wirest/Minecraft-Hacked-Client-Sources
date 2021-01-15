package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class KeyboardEvent extends Event
{
    public int key;
    
    public KeyboardEvent(int key)
    {
	this.key = key;
    }
}
