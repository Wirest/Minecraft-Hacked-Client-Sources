package de.iotacb.client.events.world;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;

public class EntityDamageEvent implements Event {

	Entity entity;
	
	public EntityDamageEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}
