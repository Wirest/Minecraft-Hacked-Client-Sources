package me.xatzdevelopments.xatz.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class MAC_Bypass extends Module {

	public MAC_Bypass() {
		super("AntiBot", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake player on Mineplex.");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}

}
