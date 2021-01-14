package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;

public class SprintEvent implements Event {
	
	private boolean sprinting;
	
	public SprintEvent(boolean sprinting) {
		this.sprinting = sprinting;
	}
	
	public boolean isSprinting() {
		return sprinting;
	}
	
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

}
