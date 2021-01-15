package me.xatzdevelopments.xatz.client.modules;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.GuiXatzBypassList;
import me.xatzdevelopments.xatz.module.Module;

public class OpenBypasses extends Module {

	public OpenBypasses() {
		super("Open Bypasses List", 0, Category.BYPASSES, "Opens a list of all bypass settings.");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		mc.displayGuiScreen(new GuiXatzBypassList(mc.currentScreen));
		
		this.setToggled(false, true);
	}
	
}
