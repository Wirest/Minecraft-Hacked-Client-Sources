package de.iotacb.client.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.DisconnectEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "AutoRegister", description = "Auto register and login on servers", category = Category.MISC)
public class AutoRegister extends Module {
	
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
	public void onDisconnect(DisconnectEvent event) {
	}

}
