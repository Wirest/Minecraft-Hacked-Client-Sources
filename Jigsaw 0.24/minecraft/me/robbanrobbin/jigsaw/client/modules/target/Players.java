package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Players extends Module {

	public Players() {
		super("Players", Keyboard.KEY_NONE, Category.TARGET);
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}
}
