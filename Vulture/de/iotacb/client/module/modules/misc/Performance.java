package de.iotacb.client.module.modules.misc;

import de.iotacb.client.Client;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.render.ChestESP;
import de.iotacb.client.module.modules.render.EntityESP;
import de.iotacb.client.module.modules.render.HUD;

@ModuleInfo(name = "Performance", description = "Changes settings of the client to achieve better performance", category = Category.MISC)
public class Performance extends Module {

	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
		Client.INSTANCE.getModuleManager().getModuleByClass(EntityESP.class).getValueByName("EntityESPModes").setComboValue("Box");
		Client.INSTANCE.getModuleManager().getModuleByClass(ChestESP.class).getValueByName("ChestESPModes").setComboValue("Box");
		Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDBlur").setBooleanValue(false);
		toggle();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}

}
