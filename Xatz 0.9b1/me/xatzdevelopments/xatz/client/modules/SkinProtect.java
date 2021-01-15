package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class SkinProtect extends Module {

	public SkinProtect() {
		super("SkinProtect", Keyboard.KEY_NONE, Category.WORLD, "Hides skins");
	}

}
