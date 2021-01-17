package Blizzard.Mod.mods.misc;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;

public class InvMove extends Mod {
	public InvMove() {
		super("InvMove", "InvMove", Keyboard.KEY_M, Category.PLAYER);

	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
			if (Keyboard.isKeyDown(17)) {
				mc.gameSettings.keyBindForward.pressed = true;
			} else {
				mc.gameSettings.keyBindForward.pressed = false;
			}
			if (Keyboard.isKeyDown(31)) {
				mc.gameSettings.keyBindBack.pressed = true;
			} else {
				mc.gameSettings.keyBindBack.pressed = false;
			}
			if (Keyboard.isKeyDown(32)) {
				mc.gameSettings.keyBindRight.pressed = true;
			} else {
				mc.gameSettings.keyBindRight.pressed = false;
			}
			if (Keyboard.isKeyDown(30)) {
				mc.gameSettings.keyBindLeft.pressed = true;
			} else {
				mc.gameSettings.keyBindLeft.pressed = false;
			}
			if (Keyboard.isKeyDown(203)) {
				final EntityPlayerSP thePlayer = mc.thePlayer;
				thePlayer.rotationYaw -= 4.0f;
			}
			if (Keyboard.isKeyDown(205)) {
				final EntityPlayerSP thePlayer2 = mc.thePlayer;
				thePlayer2.rotationYaw += 4.0f;
			}
			if (Keyboard.isKeyDown(200)) {
				final EntityPlayerSP thePlayer3 = mc.thePlayer;
				thePlayer3.rotationPitch -= 4.0f;
			}
			if (Keyboard.isKeyDown(208)) {
				final EntityPlayerSP thePlayer4 = mc.thePlayer;
				thePlayer4.rotationPitch += 4.0f;
			}
			if (mc.thePlayer.rotationPitch >= 90.0f) {
				mc.thePlayer.rotationPitch = 90.0f;
			}
			if (mc.thePlayer.rotationPitch <= -90.0f) {
				mc.thePlayer.rotationPitch = -90.0f;
			}
			if (Keyboard.isKeyDown(57) && mc.thePlayer.onGround && !mc.thePlayer.isInWater()) {
				mc.thePlayer.jump();
			}
		}
	}
}