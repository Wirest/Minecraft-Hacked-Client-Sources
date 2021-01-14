package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;

public class AttackEvent implements Event {

	private Entity attackedEntity;
	
	public AttackEvent(Entity entity) {
		this.attackedEntity = entity;
	}
	
	public Entity getAttackedEntity() {
		return attackedEntity;
	}
	
}
