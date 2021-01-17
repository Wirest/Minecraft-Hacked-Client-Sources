package skyline.specc.render.renderevnts;

import net.minecraft.entity.EntityLivingBase;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;

public class EventRenderEntity extends Event {

	private EntityLivingBase entity;
	private EventType type;
	public EventRenderEntity(EntityLivingBase entity, EventType type) {
		this.entity = entity;
		this.type = type;
	}
	public EntityLivingBase getEntity() {
		return entity;
	}
	public EventType getType() {
		return type;
	}
	
}
