package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class Zoom extends Module {

	float fovbefore;

	public Zoom() {
		super("Zoom", Keyboard.KEY_Z, Category.RENDER);
	}

	@Override
	public void onDisable() {

		mc.gameSettings.fovSetting = fovbefore;

		super.onDisable();
	}

	@Override
	public void onEnable() {

		fovbefore = mc.gameSettings.fovSetting;

		mc.gameSettings.fovSetting = 15;

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
