package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class AutoWalk extends Module {

	public AutoWalk() {
		super("AutoWalk", Keyboard.KEY_NONE, Category.MOVEMENT, "Walks automatically.");
	}

	@Override
	public void onDisable() {

		mc.gameSettings.keyBindForward.unpressKey();
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		mc.gameSettings.keyBindForward.pressed = true;

		super.onUpdate();
	}

}
