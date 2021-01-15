package me.xatzdevelopments.xatz.client.bypasses;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMultiset;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.module.Module;

public abstract class Bypass {
	
	protected boolean enabled = false;
	protected ArrayList<Module> allowedMods = new ArrayList<Module>();
	public Bypass() {
		setBypassableMods();
	}
	public void setBypassableMods() {
		this.allowedMods.add(Xatz.getModuleByName("Open Bypasses List"));
	}
	
	public ArrayList<Module> getAllowedModules() {
		return allowedMods;
	}
	
	public void onEnable() {
		//Xatz.getGUIMananger().reloadSettingScreens();
		for(Module mod : Xatz.getToggledModules()) {
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
