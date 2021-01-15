package me.robbanrobbin.jigsaw.gui;

import net.minecraft.client.gui.Gui;

public class Notification extends Gui {
	
	private String text;
	private Level level;
	
	public Notification(Level level, String text) {
		this.level = level;
		this.text = text;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public String getText() {
		return text;
	}
	
}
