package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "Safewalk", description = "Prevents you from walking off blocks", category = Category.MOVEMENT)
public class Safewalk extends Module {

	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onSafe(SafewalkEvent event) {
		if (getMc().thePlayer.onGround) {
			event.setSafe(true);
		}
	}

}
