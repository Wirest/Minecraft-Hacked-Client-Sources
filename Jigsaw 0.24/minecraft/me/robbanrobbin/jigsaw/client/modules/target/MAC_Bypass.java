package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class MAC_Bypass extends Module {

	public MAC_Bypass() {
		super("AntiBot(Gwen)", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake player on Mineplex.");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}

}
