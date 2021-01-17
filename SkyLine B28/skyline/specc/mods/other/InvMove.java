package skyline.specc.mods.other;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiChat;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.helper.Player;

public class InvMove extends Module {

	public InvMove() {
		super(new ModData("InventoryMove", Keyboard.KEY_NONE, new Color(40, 255, 10)), ModType.PLAYER);
	}

	@EventListener
	public void onMotion(EventMotion event) {
		if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
			if (Keyboard.isKeyDown(200)) {
				Player thePlayer = this.mc.thePlayer;
				thePlayer.rotationPitch -= 2.0f;
			}
			if (Keyboard.isKeyDown(208)) {
				Player thePlayer2 = this.mc.thePlayer;
				thePlayer2.rotationPitch += 2.0f;
			}
			if (Keyboard.isKeyDown(203)) {
				Player thePlayer3 = this.mc.thePlayer;
				thePlayer3.rotationYaw -= 3.0f;
			}
			if (Keyboard.isKeyDown(205)) {
				Player thePlayer4 = this.mc.thePlayer;
				thePlayer4.rotationYaw += 3.0f;
			}
			this.mc.gameSettings.keyBindForward.pressed = Keyboard
					.isKeyDown(this.mc.gameSettings.keyBindForward.getKeyCode());
			this.mc.gameSettings.keyBindBack.pressed = Keyboard
					.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode());
			this.mc.gameSettings.keyBindLeft.pressed = Keyboard
					.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode());
			this.mc.gameSettings.keyBindRight.pressed = Keyboard
					.isKeyDown(this.mc.gameSettings.keyBindRight.getKeyCode());
			this.mc.gameSettings.keyBindJump.pressed = Keyboard
					.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode());
		}
	}
}
