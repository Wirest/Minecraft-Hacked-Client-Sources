package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.module.Module;

public class TabGUI extends Module {

	public TabGUI() {
		super("TabGUI", Keyboard.KEY_NONE, Category.RENDER, "Toggle the TabGUI");
	}

	@Override
	public void onDisable() {
		ClientSettings.tabGui = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		ClientSettings.tabGui = true;
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		  

		super.onUpdate();
	}

}
