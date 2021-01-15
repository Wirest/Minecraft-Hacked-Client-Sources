package me.xatzdevelopments.xatz.client.bypasses;

import java.util.ArrayList;

import org.newdawn.slick.state.transition.SelectTransition;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;

public class BypassManager {

	private ArrayList<Bypass> bypasses = new ArrayList<Bypass>();

	public BypassManager() {
		bypasses.add(new AntiGCheat());
		bypasses.add(new AntiGwen());
		bypasses.add(new AntiWatchdog());
	}

	public ArrayList<Bypass> getBypasses() {
		return bypasses;
	}

	public boolean isBypassing() {
		for (Bypass bypass : bypasses) {
			if (bypass.isEnabled()) {
				return true;
			}
		}
		return false;
	}

	public Bypass getEnabledBypass() {
		for (Bypass bypass : bypasses) {
			if (bypass.isEnabled()) {
				return bypass;
			}
		}
		return null;
	}
	
	public boolean disableBadMod(Module mod) {
		if(!this.isBypassing()) {
			return false;
		}
		if(this.getEnabledBypass().getAllowedModules().contains(mod)) {
			return false;
		}
		this.getEnabledBypass().onEnable();
		Xatz.getNotificationManager().addNotification(new Notification(Level.WARNING, mod.getName() + " doesn't bypass!"));
		mod.setToggled(false, false);
		return true;
	}

	private void disableBypass(Bypass bypass) {
		bypass.setEnabled(false);
	}

	public void disableCurrentBypass() {
		disableBypass(getEnabledBypass());
	}
	
	public void toggleBypass(String name) {
		for (Bypass bypass : bypasses) {
			if (bypass.getName().equals(name)) {
				if(bypass.isEnabled()) {
					disableBypass(bypass);
				}
				else {
					enableBypass(bypass);
				}
				break;
			}
		}
	}
	
	public void render() {
		Bypass bypass = getEnabledBypass();
		if(bypass != null) {
			Xatz.getUIRenderer().addToQueue("§b" + bypass.getName() + " enabled", ScreenPos.LEFTUP);
		}
	}

	private void enableBypass(Bypass bypass) {
		if (isBypassing()) {
			disableCurrentBypass();
		}
		bypass.setEnabled(true);
	}

	public void setBypass(String name) {
		for (Bypass bypass : bypasses) {
			if (bypass.getName().equals(name)) {
				enableBypass(bypass);
				break;
			}
		}
	}

}
