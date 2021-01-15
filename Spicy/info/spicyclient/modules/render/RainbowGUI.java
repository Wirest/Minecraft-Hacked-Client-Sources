package info.spicyclient.modules.render;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;

public class RainbowGUI extends Module {
	
	public NumberSetting speed = new NumberSetting("Speed", 95, 1, 100, 0.2);
	
	public RainbowGUI() {
		super("RainbowGUI", Keyboard.KEY_NONE, Category.RENDER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(speed);
	}
	
	public void onEnable() {
		TabGUI t = SpicyClient.config.tabgui;
		t.rainbowEnabled = true;
		SpicyClient.hud.rainbowEnabled = true;
	}
	
	public void onDisable() {
		TabGUI t = SpicyClient.config.tabgui;
		t.rainbowEnabled = false;
		SpicyClient.hud.rainbowEnabled = false;
	}
	
	private Random rand = new Random();
	private double counter = 0;
	private int count = 0;
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				TabGUI t = SpicyClient.config.tabgui;
				
				t.rainbowEnabled = true;
				t.rainbowTimer = 100.5f - speed.getValue();
				
				SpicyClient.hud.rainbowEnabled = true;
				SpicyClient.hud.rainbowTimer = 100.5f - speed.getValue();
				
			}
			
		}
		
	}
	
}