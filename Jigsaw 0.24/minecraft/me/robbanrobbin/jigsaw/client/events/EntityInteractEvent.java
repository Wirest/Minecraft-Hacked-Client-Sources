package me.robbanrobbin.jigsaw.client.events;

import net.minecraft.entity.Entity;

public class EntityInteractEvent extends Event {

	public Entity entity;

	public EntityInteractEvent(Entity en) {
		this.entity = en;
	}
}
