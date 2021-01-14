package de.iotacb.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "NoBob", description = "Disables the hand bobbing when moving", category = Category.RENDER)
public class NoBob extends Module {

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
		getMc().thePlayer.distanceWalkedModified = 0;
	}

}
