package info.spicyclient.modules.render;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;

public class Fullbright extends Module {

	public Fullbright() {
		super("Fullbright", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	private float old_gamma;
	
	public void onEnable() {
		old_gamma = mc.gameSettings.gammaSetting;
		mc.gameSettings.gammaSetting = 100;
	}
	
	public void onDisable() {
		if (mc.gameSettings.gammaSetting == old_gamma) {
			mc.gameSettings.gammaSetting = 0.5f;
		}else {
			mc.gameSettings.gammaSetting = old_gamma;
		}
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
			}
			
		}
		
	}
	
}
