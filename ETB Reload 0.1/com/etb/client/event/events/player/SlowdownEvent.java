package com.etb.client.event.events.player;

import com.etb.client.event.CancelableEvent;

public class SlowdownEvent extends CancelableEvent {

	private Type type;
	
	public SlowdownEvent(final Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		Item, Sprinting, SoulSand, Water
	}
}
