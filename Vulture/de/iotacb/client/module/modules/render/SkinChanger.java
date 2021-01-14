package de.iotacb.client.module.modules.render;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "SkinChanger", description = "Change your skin", category = Category.RENDER)
public class SkinChanger extends Module {

	@Override
	public void onInit() {
		addValue("All", true);
		addValue("Skin", "Eagler", "Gomme", "Custom");
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
