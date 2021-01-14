package de.iotacb.client.events.input;

import com.darkmagician6.eventapi.events.Event;

public class MouseEvent implements Event {
	
	private final int mouseButton;
	
	public MouseEvent(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}

}
