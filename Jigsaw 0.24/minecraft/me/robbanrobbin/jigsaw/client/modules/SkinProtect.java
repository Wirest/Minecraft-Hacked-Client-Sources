package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class SkinProtect extends Module {

	public SkinProtect() {
		super("SkinProtect", Keyboard.KEY_NONE, Category.WORLD, "Hides skins");
	}

}
