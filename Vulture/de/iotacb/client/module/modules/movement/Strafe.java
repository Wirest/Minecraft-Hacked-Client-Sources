package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.player.MovementUtil;

@ModuleInfo(name = "Strafe", description = "Enables strafe movement", category = Category.MOVEMENT)
public class Strafe extends Module {

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
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (Client.MOVEMENT_UTIL.isMoving()) {
			Client.MOVEMENT_UTIL.doStrafe();
		}
	}

}
