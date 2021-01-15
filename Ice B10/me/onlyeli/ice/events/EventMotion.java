package me.onlyeli.ice.events;

import me.onlyeli.ice.events.Location;
import me.onlyeli.ice.events.Events;
import me.onlyeli.ice.events.EventType;

public class EventMotion extends Events {

	private EventType type;
	private Location location;
	private boolean onGround;

	public EventMotion(EventType type, Location location, boolean onGround){
		this.type = type;
		this.location = location;
		this.onGround = onGround;
	}

	public EventType getType(){
		return type;
	}

	public void setType(EventType type){
		this.type = type;
	}

	public Location getLocation(){
		return location;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public boolean isOnGround(){
		return onGround;
	}

	public void setOnGround(boolean onGround){
		this.onGround = onGround;
	}

}
