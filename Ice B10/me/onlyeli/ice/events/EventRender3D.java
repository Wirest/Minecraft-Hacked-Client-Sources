package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventRender3D implements Event {

	public float partialTicks;

	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}

}
