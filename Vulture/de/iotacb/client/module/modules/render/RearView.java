package de.iotacb.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;

@ModuleInfo(name = "RearView", description = "Draws a cam that shows the view from behind", category = Category.RENDER)
public class RearView extends Module {

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
	public void onRender(RenderEvent event) {
	}

}
