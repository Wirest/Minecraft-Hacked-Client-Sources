package me.robbanrobbin.jigsaw.client.bypasses;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMultiset;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;

public abstract class Bypass {
	
	protected boolean enabled = false;
	protected ArrayList<Module> allowedMods = new ArrayList<Module>();
	public Bypass() {
		setBypassableMods();
	}
	public void setBypassableMods() {
		this.allowedMods.add(Jigsaw.getModuleByName("Open Bypasses List"));
	}
	
	public ArrayList<Module> getAllowedModules() {
		return allowedMods;
	}
	
	public void onEnable() {
		//Jigsaw.getGUIMananger().reloadSettingScreens();
		for(Module mod : Jigsaw.getToggledModules()) {
			if(!allowedMods.contains(mod)) {
				mod.setToggled(false, true);
			}
		}
	}
	
	public void onDisable() {
		
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled) {
			onEnable();
		}
		else {
			onDisable();
		}
	}
	
	public String getName() {
		return null;
	}
	
}
