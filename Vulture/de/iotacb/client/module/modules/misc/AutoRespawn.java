package de.iotacb.client.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", description = "Respawn automatically", category = Category.MISC)
public class AutoRespawn extends Module {

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
		if (getMc().thePlayer.isDead) {
			getMc().thePlayer.respawnPlayer();
		}
	}

}
