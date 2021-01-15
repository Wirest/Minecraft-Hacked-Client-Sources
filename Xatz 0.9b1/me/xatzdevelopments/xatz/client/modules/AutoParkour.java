package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class AutoParkour extends Module {

	public AutoParkour() {
		super("AutoParkour", Keyboard.KEY_NONE, Category.AUTOMATION,
				"Tries to jump to a block automatically. (Select block by right clicking it)");
	}

}
