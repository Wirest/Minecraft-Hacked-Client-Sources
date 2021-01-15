package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class Panic extends Module {

	public Panic() {
		super("Panic", Keyboard.KEY_NONE, Category.PLAYER,
				"Disables all mods instantly.");
	}

	@Override
	public void onEnable() {
		for(Module m : Xatz.getToggledModules()) {
			m.setToggled(false, true);
		}
		this.setToggled(false, false);
	}

}
