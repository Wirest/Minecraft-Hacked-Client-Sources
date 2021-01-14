package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventUpdate;

/**
 * Author: Ice Created: 17:53, 30-May-20 Project: Client
 */
public class Sprint extends Module {

	private boolean shouldSkipNextUpdate = false;

	public Sprint() {
		super("Sprint", "Sprints for you", ModuleCategory.MOVEMENT);
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventUpdate) {
		mc.thePlayer.setSprinting(mc.thePlayer.isMoving());

	}
}
