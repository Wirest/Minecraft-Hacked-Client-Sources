package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

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
			for (Module m : Jigsaw.getModuleGroupMananger().getModuleGroupByName("AirGroup").getModules()) {
				if (m.isToggled()) {
					return;
				}
			}
			mc.thePlayer.motionY -= 0.1;
		}

		super.onUpdate();
	}

}
