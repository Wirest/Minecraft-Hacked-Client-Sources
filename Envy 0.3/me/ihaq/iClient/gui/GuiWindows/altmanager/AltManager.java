package me.ihaq.iClient.gui.GuiWindows.altmanager;

import java.util.ArrayList;

public class AltManager {
	public static Alt lastAlt;
	public static ArrayList<Alt> registry = new ArrayList();

	public ArrayList<Alt> getRegistry() {
		return registry;
	}

	public void setLastAlt(Alt alt) {
		lastAlt = alt;
	}
}
