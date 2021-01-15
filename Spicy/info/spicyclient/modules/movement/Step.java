package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.util.MovementUtils;

public class Step extends Module {
	
	private boolean stepped = false;
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
	
	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
		if (e.setting.equals(mode)) {
			
			if (mode.is("Vanilla") && this.isToggled()) {
				mc.thePlayer.stepHeight = 4f;
				mc.timer.ticksPerSecond = 20f;
			}
			else if (mode.is("NCP") && this.isToggled()) {
				mc.thePlayer.stepHeight = 0.5f;
			}
			
		}
		
	}
	
	public void onEnable() {
		if (mode.is("Vanilla")) {
			mc.thePlayer.stepHeight = 4f;
		}
	}
	
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.5f;
		mc.timer.ticksPerSecond = 20f;
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = mode.getMode();
				
			}
			
		}
		
		if (e instanceof EventUpdate) {
			
			Double offset = mc.thePlayer.posY - ((int)mc.thePlayer.posY);
			
			if (offset < 0) {
				offset *= -1;
			}
			
			if (e.isPre() && mode.is("Vanilla")) {
				mc.thePlayer.stepHeight = 4f;
			}
			
			else if (e.isBeforePre() && mode.is("NCP") && MovementUtils.canStep(1)) {
				
				mc.thePlayer.motionY = 0.37;
				
				/*
            	if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            		mc.timer.timerSpeed = 1.5f;
            		mc.thePlayer.onGround = true;
            		mc.thePlayer.jump();
            		stepped = true;
            	}
            	else if (stepped && mc.thePlayer.onGround) {
            		mc.timer.timerSpeed = 1.0f;
            		mc.thePlayer.motionY = 0;
            		stepped = false;
            	}
				*/
			}
			
		}
		
	}
	
}
