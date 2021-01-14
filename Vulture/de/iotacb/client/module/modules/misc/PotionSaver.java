package de.iotacb.client.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.PotionEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.player.MovementUtil;

@ModuleInfo(name = "PotionSaver", description = "Potions won't dissapear when standing still", category = Category.MISC)
public class PotionSaver extends Module {

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
	public void onPotion(PotionEvent event) {
		if (!Client.MOVEMENT_UTIL.isMoving() && getMc().thePlayer.onGround) {
			event.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (getMc().thePlayer.getActivePotionEffects().size() == 0) return;
			if (!Client.MOVEMENT_UTIL.isMoving() && getMc().thePlayer.onGround && !getMc().thePlayer.isUsingItem()) {
				event.setCancelled(true);
			}
		}
	}

}
