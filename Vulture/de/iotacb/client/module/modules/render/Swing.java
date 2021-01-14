package de.iotacb.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;

@ModuleInfo(name = "Swing", description = "Changes the swing and blockhit animation", category = Category.RENDER)
public class Swing extends Module {

	@Override
	public void onInit() {
		addValue(new Value("SwingModes", "Normal", "Lowswing"));
		addValue(new Value("SwingBlockModes", "Normal", "Stab"));
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
	public void onUpdate(UpdateEvent event) {
		setSettingInfo(getValueByName("SwingModes").getComboValue());
	}

}
