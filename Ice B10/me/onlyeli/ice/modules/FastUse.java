package me.onlyeli.ice.modules;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class FastUse extends Module {
	public FastUse() {
		super("FastUse", Keyboard.KEY_PERIOD, Category.PLAYER);
	}

	public void onUpdate() {
		if (this.isToggled()) {
			Wrapper.mc.rightClickDelayTimer = 0;
		}
	}

	public void onDisable() {
		Wrapper.mc.rightClickDelayTimer = 6;
	}
}
