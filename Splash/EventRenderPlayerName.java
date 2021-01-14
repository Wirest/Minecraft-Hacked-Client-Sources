package splash.client.events.render;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.entity.Entity;

public class EventRenderPlayerName extends Cancelable {
	public Entity p;

	public EventRenderPlayerName(final Entity p2) {
		this.p = p2;
	}
}
