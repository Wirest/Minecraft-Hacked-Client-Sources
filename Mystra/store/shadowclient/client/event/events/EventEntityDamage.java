package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;
import net.minecraft.entity.Entity;

public class EventEntityDamage extends Event {

	Entity entity;
	
	public EventEntityDamage(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}
