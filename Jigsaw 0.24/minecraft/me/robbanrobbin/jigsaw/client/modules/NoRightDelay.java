package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class NoRightDelay extends Module {

	public NoRightDelay() {
		super("NoRightDelay", Keyboard.KEY_NONE, Category.PLAYER, "Disables the right click delay. Enables you to place blocks a lot faster.");
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
