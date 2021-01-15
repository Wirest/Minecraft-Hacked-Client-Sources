package nivia.events.events;

import net.minecraft.entity.Entity;
import nivia.events.Event;

public class EventAttack implements Event {
	private Entity ent;
	public EventAttack(Entity e){
		this.ent = e;
	}
	public Entity getEntity(){
		return this.ent;
	}
}
