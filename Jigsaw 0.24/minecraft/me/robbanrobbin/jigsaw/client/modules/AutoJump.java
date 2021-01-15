package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class AutoJump extends Module {

	public AutoJump() {
		super("AutoJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Jumps all the time.");
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

		if (mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		}

		super.onUpdate();
	}

}
