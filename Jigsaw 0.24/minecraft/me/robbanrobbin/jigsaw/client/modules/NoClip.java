package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;

public class NoClip extends Module {

	public NoClip() {
		super("NoClip", Keyboard.KEY_O, Category.EXPLOITS, "Enables you to fly through blocks. Note: You need a sand block to f"
				+ "all on your head, or close a door while inside it to get this to work.");
	}

	@Override
	public void onDisable() {

		mc.thePlayer.noClip = false;

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		

		super.onUpdate();
	}
	@Override
	public void onLivingUpdate() {
		Utils.spectator = true;
		mc.thePlayer.noClip = true;
		// mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		// mc.thePlayer.motionZ = 0;
		// mc.thePlayer.fallDistance = 0;
		mc.thePlayer.onGround = false;
		mc.thePlayer.capabilities.isFlying = false;
		
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer.motionY += 0.3;
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.thePlayer.motionY -= 0.3;
		}
		super.onLivingUpdate();
	}

}
