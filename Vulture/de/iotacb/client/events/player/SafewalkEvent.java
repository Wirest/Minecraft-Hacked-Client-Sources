package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;

public class SafewalkEvent implements Event {
	
	private boolean isSafe;
	
	public boolean isSafe() {
		return isSafe;
	}
	
	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}

}
