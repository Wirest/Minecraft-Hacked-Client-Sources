package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;

public class MouseEvent extends Event
{
    public int key;
    
    public MouseEvent(int key)
    {
	this.key = key;
    }
}
