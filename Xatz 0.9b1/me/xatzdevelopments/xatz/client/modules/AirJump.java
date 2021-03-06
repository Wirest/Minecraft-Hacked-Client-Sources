package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", Keyboard.KEY_H, Category.MOVEMENT, "Enables you to jump in the air.");
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

		if (this.currentMode.equals("Vanilla")) {
			if (mc.gameSettings.keyBindJump.isPressed()) {
				mc.thePlayer.motionY = 1;
			}
		}
		if (this.currentMode.equals("NCP")) {
			mc.thePlayer.onGround = true;
			mc.thePlayer.isAirBorne = false;
		}

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Vanilla", "NCP" };
	}
	
	public String getModeName() {
		return "Mode: ";
	}
}
