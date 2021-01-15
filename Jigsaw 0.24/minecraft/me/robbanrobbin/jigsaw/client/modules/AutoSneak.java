package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

public class AutoSneak extends Module {

	public AutoSneak() {
		super("AutoSneak", Keyboard.KEY_NONE, Category.MOVEMENT, "Sneaks all the time.");
	}

	@Override
	public void onDisable() {

		if (this.currentMode.equalsIgnoreCase("Packet")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING));
		}
		if (this.currentMode.equalsIgnoreCase("Client")) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		if (this.currentMode.equalsIgnoreCase("Packet")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING));
		}
		if (this.currentMode.equalsIgnoreCase("Client")) {
			mc.gameSettings.keyBindSneak.pressed = true;
		}

		super.onUpdate();
	}

	@Override
	public void onModeChanged(String modeBefore, String newMode) {
		if (modeBefore.equalsIgnoreCase("Client")) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}
	}

	@Override
	public String[] getModes() {
		return new String[] { "Packet", "Client" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}
}
