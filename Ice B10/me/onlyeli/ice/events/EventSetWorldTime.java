package me.onlyeli.ice.events;

import me.onlyeli.ice.events.Events;

public class EventSetWorldTime extends Events {

	private long time;

	public EventSetWorldTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
}
