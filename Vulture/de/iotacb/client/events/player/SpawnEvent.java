package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;

public class SpawnEvent implements Event {

	private final Entity spawnedEntity;
	
	public SpawnEvent(Entity entity) {
		this.spawnedEntity = entity;
	}
	
	public Entity getSpawnedEntity() {
		return spawnedEntity;
	}
	
}
