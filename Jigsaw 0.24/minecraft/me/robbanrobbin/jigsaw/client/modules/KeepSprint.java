package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.AbstractPacket;
import net.minecraft.realms.RealmsBridge;

public class KeepSprint extends Module {

	public KeepSprint() {
		super("KeepSprint", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to keep sprinting after you hit someone.");
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
	public void onPacketRecieved(AbstractPacket packetIn) {
		
		super.onPacketRecieved(packetIn);
	}
}
