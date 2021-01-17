package me.ihaq.iClient.event.events;

import me.ihaq.iClient.event.Event;
import net.minecraft.entity.Entity;

public class EventEntityStep extends Event {

	private float stepHeight;
	private Entity entity;

	public EventEntityStep(Entity entity, float stepHeight) {
		this.entity = entity;
		this.stepHeight = stepHeight;
	}

	public float getStepHeight() {
		return this.stepHeight;
	}

	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

	public Entity getEntity() {
		return this.entity;
	}

}
