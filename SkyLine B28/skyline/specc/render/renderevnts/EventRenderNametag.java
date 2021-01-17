package skyline.specc.render.renderevnts;

import net.minecraft.entity.Entity;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class EventRenderNametag extends Event {

	private Entity entity;

	public EventRenderNametag(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
		
}
