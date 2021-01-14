package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Timer;

@ModuleInfo(name = "WaterSpeed", description = "Be faster in water", category = Category.MOVEMENT)
public class WaterSpeed extends Module {

	private Timer timer;

	@Override
	public void onInit() {
		this.timer = new Timer();
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
		if (getMc().thePlayer.isInWater()) {
			if (getMc().thePlayer.isCollided)
				return;
			getMc().thePlayer.motionX *= 1.19D;
			getMc().thePlayer.motionZ *= 1.19D;

			if (timer.delay(95)) {
				if (getMc().gameSettings.keyBindSneak.pressed) {
					getMc().thePlayer.motionY -= 0.011;
				} else {
					getMc().thePlayer.motionY = 0.011;
				}
			}
		}
	}

}
