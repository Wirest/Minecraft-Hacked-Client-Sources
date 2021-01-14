package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "Step", description = "Enables you to step up blocks", category = Category.MOVEMENT)
public class Step extends Module {

	@Override
	public void onInit() {
		addValue(new Value("StepHeight", 3F, new ValueMinMax(1, 20, 1)));
		addValue(new Value("StepLadders", false));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null) return;
		getMc().thePlayer.stepHeight = .6F;
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		setSettingInfo(""+getValueByName("StepHeight").getNumberValue());
		if ((getMc().thePlayer.isOnLadder() && !getValueByName("StepLadders").getBooleanValue()) || getMc().gameSettings.keyBindSneak.pressed || !getMc().thePlayer.onGround) {
			getMc().thePlayer.stepHeight = .6F;
			return;
		}
		getMc().thePlayer.stepHeight = (float) getValueByName("StepHeight").getNumberValue();
	}

}
