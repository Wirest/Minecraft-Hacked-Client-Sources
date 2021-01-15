package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;


public class FullbrightTest extends Module {
	private float oldVal;

	public FullbrightTest() {
		super("FullbrightTest", Keyboard.KEY_NONE, Category.RENDER);
	}

	@Override
	public void onEnable() {
		this.oldVal = Wrapper.mc.gameSettings.gammaSetting;
		Wrapper.mc.gameSettings.gammaSetting = 10000.0f;
	}

	@Override
	public void onDisable() {
		Wrapper.mc.gameSettings.gammaSetting = this.oldVal;
	}
}
