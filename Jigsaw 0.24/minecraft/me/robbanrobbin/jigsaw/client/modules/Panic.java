package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Panic extends Module {

	public Panic() {
		super("Panic", Keyboard.KEY_NONE, Category.PLAYER,
				"Disables all mods instantly.");
	}

	@Override
	public void onEnable() {
		for(Module m : Jigsaw.getToggledModules()) {
			m.setToggled(false, true);
		}
		this.setToggled(false, false);
	}

}
