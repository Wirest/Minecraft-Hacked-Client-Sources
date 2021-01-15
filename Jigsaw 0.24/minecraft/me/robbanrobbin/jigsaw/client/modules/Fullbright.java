package me.robbanrobbin.jigsaw.client.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Fullbright extends Module {
	private float gammabefore = 0.5f;

	public Fullbright() {
		super("Fullbright", Keyboard.KEY_B, Category.RENDER, "Makes everything bright.");
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = gammabefore;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		mc.gameSettings.gammaSetting = 100;
		
		super.onUpdate();
	}

}
