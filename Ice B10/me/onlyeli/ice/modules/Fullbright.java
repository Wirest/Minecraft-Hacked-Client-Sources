package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

public class Fullbright extends Module {
	
	private float gammaSetting = 0F;
	
	public Fullbright() {
		super("Fullbright", Keyboard.KEY_L, Category.RENDER);
	}
	
	@Override
	public void onEnable() {
		this.gammaSetting = Wrapper.mc.gameSettings.gammaSetting;
		super.onEnable();
	}
	
	
	
	
	@Override
	public void onDisable() {
		Wrapper.mc.gameSettings.gammaSetting = this.gammaSetting;
		super.onDisable();
	}
	
	@Override
	public void onUpdate() {
		
		if(!this.isToggled())
			return;
		
		Wrapper.mc.gameSettings.gammaSetting = 10000.0F;
		
		super.onUpdate();
	}
}
