package de.iotacb.client.module.modules.world;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "Clip", description = "Clips you vertically or horizontally through blocks", category = Category.WORLD)
public class Clip extends Module {

	@Override
	public void onInit() {
		addValue(new Value("ClipModes", "Horizontal", "Vertical"));
		addValue(new Value("ClipNegative", false));
		addValue(new Value("ClipStrength", 4, new ValueMinMax(.5, 20, .5)));
	}

	@Override
	public void onEnable() {
		final double strength = getValueByName("ClipStrength").getNumberValue() * (getValueByName("ClipNegative").getBooleanValue() ? -1 : 1);
		switch (getValueByName("ClipModes").getComboValue()) {
		case "Horizontal":
			final double yaw = Math.toRadians(getMc().thePlayer.rotationYaw + 90);
			getMc().thePlayer.setPosition(getMc().thePlayer.posX + Math.cos(yaw) * strength, getMc().thePlayer.posY, getMc().thePlayer.posZ + Math.sin(yaw) * strength);
			break;

		case "Vertical":
			getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + strength, getMc().thePlayer.posZ);
			break;
		}
		toggle();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}

}
