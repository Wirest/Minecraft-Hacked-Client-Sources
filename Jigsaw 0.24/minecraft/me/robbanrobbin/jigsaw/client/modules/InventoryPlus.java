package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class InventoryPlus extends Module {

	public InventoryPlus() {
		super("Inventory+", Keyboard.KEY_NONE, Category.PLAYER,
				"Enables you to use your crafting area as inventory.");
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

		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C0DPacketCloseWindow) {
			packet.cancel();
		}
		super.onPacketSent(packet);
	}

	public static boolean shouldCancel() {
		return Jigsaw.getModuleByName("Inventory+").isToggled();
	}

}
