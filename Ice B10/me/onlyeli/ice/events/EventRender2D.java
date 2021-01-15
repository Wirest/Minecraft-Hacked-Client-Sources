package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventRender2D implements Event {

	private float partialTicks;

	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}

}
