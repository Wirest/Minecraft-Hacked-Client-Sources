package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.module.Module;

public class AntiDesync extends Module {

	public AntiDesync() {
		super("AntiDesync", Keyboard.KEY_NONE, Category.PLAYER, "Prevents desync from the server");
	}

	@Override
	public void onDisable() {
		
		super.onDisable();
	}

	@Override
	public void onEnable() {
		
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		  

		super.onUpdate();
	}

}
