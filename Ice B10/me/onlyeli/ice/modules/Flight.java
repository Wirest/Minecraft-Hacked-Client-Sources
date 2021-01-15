package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

public class Flight extends Module {
	
	public Flight() {
		super("Flight", Keyboard.KEY_LMENU, Category.MOVEMENT);
	}
	
	@Override
	public void onDisable() {
		Wrapper.mc.thePlayer.capabilities.isFlying = false;
		super.onDisable();
	}
	
	@Override
	public void onUpdate() {
		
		if(!this.isToggled())
			return;
		
		Wrapper.mc.thePlayer.capabilities.isFlying = true;
		super.onUpdate();
	}
}
