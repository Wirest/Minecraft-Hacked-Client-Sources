package de.iotacb.client.events.world;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;

public class EntityDeathEvent implements Event {

	private final Entity entity;
	
	public EntityDeathEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}
