package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public class EventUpdate extends EventCancellable {

	public double y;
	public float yaw, pitch;
	public boolean onGround;
	public me.onlyeli.ice.events.Event2.State state;

	public EventUpdate(double y, float yaw, float pitch, boolean onGround) {
		this.y = y;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.state = Event2.State.PRE;
	}

	public EventUpdate() {
		this.state = Event2.State.POST;
	}

}