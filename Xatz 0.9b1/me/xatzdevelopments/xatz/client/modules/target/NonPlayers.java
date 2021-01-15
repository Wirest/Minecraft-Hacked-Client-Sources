package me.xatzdevelopments.xatz.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class NonPlayers extends Module {

	public NonPlayers() {
		super("NonPlayers", Keyboard.KEY_NONE, Category.TARGET);
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}
}
