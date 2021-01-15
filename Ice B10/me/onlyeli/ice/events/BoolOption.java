package me.onlyeli.ice.events;

import java.util.ArrayList;

public class BoolOption {
	private String name;
	private boolean enabled;
	private static ArrayList<BoolOption> vals = new ArrayList<BoolOption>();

	public static ArrayList<BoolOption> getVals() {
		return BoolOption.vals;
	}

	public BoolOption(String name) {
		this.setName(name);
		getVals().add(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

}
