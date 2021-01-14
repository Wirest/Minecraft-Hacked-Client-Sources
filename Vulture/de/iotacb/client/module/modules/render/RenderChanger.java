package de.iotacb.client.module.modules.render;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;import de.iotacb.client.utilities.values.Value;

@ModuleInfo(name = "RenderChanger", description = "Changes stuff about rendering (rotating item to your view for example)", category = Category.RENDER)
public class RenderChanger extends Module {

	@Override
	public void onInit() {
		addValue(new Value("RenderChangerItem to view", true));
		addValue(new Value("RenderChangerHigher held item", true));
		addValue(new Value("RenderChangerLittle entities", false));
		addValue(new Value("RenderChangerFlip entities", false));
		addValue(new Value("RenderChangerBig heads", false));
		addValue(new Value("RenderChangerItem block", false));
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
