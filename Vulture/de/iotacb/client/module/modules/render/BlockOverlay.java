package de.iotacb.client.module.modules.render;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;

@ModuleInfo(name = "BlockOverlay", description = "Draws a block overlay", category = Category.RENDER)
public class BlockOverlay extends Module {

	@Override
	public void onInit() {
		addValue(new Value("BlockOverlayDepth", false));
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
