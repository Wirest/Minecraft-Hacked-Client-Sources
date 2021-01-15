package me.xatzdevelopments.xatz.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class Hypixel_Bypass extends Module {

	public Hypixel_Bypass() {
		super("AntiBot(Watchdog)", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake players on hypixel");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}

}
