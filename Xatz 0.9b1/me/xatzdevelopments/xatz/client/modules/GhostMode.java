package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class GhostMode extends Module {

	public GhostMode() {
		super("GhostMode", Keyboard.KEY_P, Category.SETTINGS, "Hides all Xatz-related things in your Minecraft. Press p to turn it off!");
	}

	@Override
	public void onClientLoad() {
		Display.setTitle("[Minecraft 1.8.8] " +  Xatz.getClientName() + " " + Xatz.getClientVersion());
		super.onClientLoad();
	}

	@Override
	public void onDisable() {
		Display.setTitle("[Minecraft 1.8.8] " +  Xatz.getClientName() + " " + Xatz.getClientVersion());
		Xatz.ghostMode(false);
		super.onDisable();
	}

	@Override
	public void onEnable() {
		Display.setTitle("Minecraft 1.8.8");
		Xatz.ghostMode(true);
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
