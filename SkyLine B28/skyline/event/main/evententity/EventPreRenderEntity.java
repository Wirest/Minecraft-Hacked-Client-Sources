package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.evententity;

import net.minecraft.entity.Entity;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class EventPreRenderEntity extends Event {

	private Entity e;

	public EventPreRenderEntity(final Entity e) {
		this.e = null;
		this.setEntity(e);
	}
	 public Entity getEntity() {
	        return this.e;
	    }
	    
	public void setEntity(final Entity e) {
		this.e = e;
	}
}
