package de.iotacb.client.module.modules.render;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "FOV", description = "Changes the FOV", category = Category.RENDER)
public class FOV extends Module {

	@Override
	public void onInit() {
		addValue(new Value("FOVValue", 1, new ValueMinMax(0, 2, .1)));
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

}
