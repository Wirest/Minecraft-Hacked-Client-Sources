package me.ihaq.iClient.event.events;

import me.ihaq.iClient.event.Event;

public class EventMouse extends Event{
	
	private int key;

	public EventMouse(int i) {
		this.key = i;
	}

	public int getKey() {
		return this.key;
		
	}

}
