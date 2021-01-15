package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module {

	public AutoRespawn() {
		super("AutoRespawn", Keyboard.KEY_NONE, Category.COMBAT, "Respawns automatically when you die.");
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

		if (mc.thePlayer.isDead) {
			mc.thePlayer.respawnPlayer();
			mc.displayGuiScreen((GuiScreen) null);
		}

		super.onUpdate();
	}

}
