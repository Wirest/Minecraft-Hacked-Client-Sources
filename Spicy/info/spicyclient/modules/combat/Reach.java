package info.spicyclient.modules.combat;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventGetBlockReach;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.Timer;

public class Reach extends Module {
	
	public NumberSetting reach = new NumberSetting("Reach", 3, 3, 6, 0.1);
	
	public Reach() {
		super("Reach", Keyboard.KEY_NONE, Category.COMBAT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(reach);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		mc.gameSettings.keyBindUseItem.pressed = false;
	}
	
	public Timer timer = new Timer();
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = "" + reach.getValue();
				
			}
			
		}
		
		if (e instanceof EventGetBlockReach) {
			
			EventGetBlockReach event = (EventGetBlockReach) e;
			
			event.setCanceled(true);
			event.reach = (float) reach.getValue();
			
		}
		
	}
	
}
