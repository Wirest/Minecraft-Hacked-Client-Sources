package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Bettersprint extends Module {

	public Bettersprint() {
		super("Bettersprint", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes sprinting better!");
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

	@Override
	public String[] getModes() {
		return new String[] { "Multidir" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

}
