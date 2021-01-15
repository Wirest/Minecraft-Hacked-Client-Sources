package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class FastFall extends Module {

	public FastFall() {
		super("FastFall", Keyboard.KEY_NONE, Category.MOVEMENT, "This makes you fall faster.");
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

		if (!mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && !mc.thePlayer.isInWater()
				&& !mc.thePlayer.isInLava() && !mc.thePlayer.isOnLadder() && !mc.thePlayer.capabilities.isFlying) {
			for (Module m : Xatz.getModuleGroupMananger().getModuleGroupByName("AirGroup").getModules()) {
				if (m.isToggled()) {
					return;
				}
			}
			mc.thePlayer.motionY -= 0.1;
		}

		super.onUpdate();
	}

}
