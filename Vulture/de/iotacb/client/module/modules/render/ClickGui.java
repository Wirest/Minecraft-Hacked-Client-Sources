package de.iotacb.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.gui.click.GuiClick;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "ClickGui", description = "Opens the clickable client gui", category = Category.RENDER)
public class ClickGui extends Module {
	
	@Override
	public void onInit() {
		addValue(new Value("ClickGuiAnimations", true));
		addValue(new Value("ClickGuiParticles", true));
		addValue(new Value("ClickGuiFlow", true));
		addValue(new Value("ClickGuiFlow factor", 3, new ValueMinMax(1, 8, .5)));
		addValue(new Value("ClickGuiScale factor", 1, new ValueMinMax(.1, 1, .1)));
		addValue(new Value("ClickGuiTheme", "Dark", "Light"));
	}

	@Override
	public void onEnable() {
		getMc().displayGuiScreen(Client.INSTANCE.getClickGui());
		toggle();
	}
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("ClickGuiFlow factor").setEnabled(getValueByName("ClickGuiFlow").getBooleanValue());
		super.updateValueStates();
	}

}
