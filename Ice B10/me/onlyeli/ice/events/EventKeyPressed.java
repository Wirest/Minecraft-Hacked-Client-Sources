package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventKeyPressed implements Event {

	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

}
