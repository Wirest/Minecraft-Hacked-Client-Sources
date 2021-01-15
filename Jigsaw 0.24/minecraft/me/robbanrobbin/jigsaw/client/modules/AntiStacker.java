package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class AntiStacker extends Module {
	int timer;

	public AntiStacker() {
		super("AntiStacker", Keyboard.KEY_NONE, Category.PLAYER,
				"Automatically dismounts from the entity you are riding.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer = 4;
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (timer < 4) {
			timer++;
			if (timer >= 1) {
				mc.gameSettings.keyBindSneak.unpressKey();
			}
			return;
		}
		if (mc.thePlayer.isRiding()) {
			mc.gameSettings.keyBindSneak.pressed = true;
			timer = 0;
		}

		super.onUpdate();
	}

}
