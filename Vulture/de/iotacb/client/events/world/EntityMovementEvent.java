package de.iotacb.client.events.world;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;

public class EntityMovementEvent implements Event {

	private final Entity movedEntity;
	
	public EntityMovementEvent(Entity movedEntity) {
		this.movedEntity = movedEntity;
	}
	
	public Entity getMovedEntity() {
		return movedEntity;
	}
	
}
