package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.gui.GuiScreen;

public class GuiCloseEvent implements Event {

	private final GuiScreen screen;
	
	public GuiCloseEvent(GuiScreen screen) {
		this.screen = screen;
	}
	
	public GuiScreen getScreen() {
		return screen;
	}
}
