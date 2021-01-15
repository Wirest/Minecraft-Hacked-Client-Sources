package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;

public class XRay extends Module {

	public XRay() {
		super("XRay", Keyboard.KEY_NONE, Category.RENDER, "Only renders ores");
	}

	@Override
	public void onDisable() {

		mc.gameSettings.gammaSetting = 0.5f;
		mc.renderGlobal.loadRenderers();
		super.onDisable();
	}

	@Override
	public void onEnable() {

		mc.gameSettings.gammaSetting = 10f;
		mc.renderGlobal.loadRenderers();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		Utils.spectator = true;
		mc.gameSettings.gammaSetting = 10f;
		super.onUpdate();
	}

}
