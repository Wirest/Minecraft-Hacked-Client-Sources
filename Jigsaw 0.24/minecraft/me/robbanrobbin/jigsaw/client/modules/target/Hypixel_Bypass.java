package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Hypixel_Bypass extends Module {

	public Hypixel_Bypass() {
		super("AntiBot(Watchdog)", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake players on hypixel");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}

}
