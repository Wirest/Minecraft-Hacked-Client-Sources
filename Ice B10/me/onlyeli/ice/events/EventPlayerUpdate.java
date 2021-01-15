package me.onlyeli.ice.events;

import me.onlyeli.ice.events.Events;
import me.onlyeli.ice.events.EventType;

public class EventPlayerUpdate extends Events {

	private EventType type;

	public EventPlayerUpdate(EventType type){
		this.type = type;
	}

	public EventType getType(){
		return type;
	}

}
