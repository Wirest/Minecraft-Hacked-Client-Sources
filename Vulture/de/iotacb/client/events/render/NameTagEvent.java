package de.iotacb.client.events.render;

import com.darkmagician6.eventapi.events.Event;

public class NameTagEvent implements Event {
	
	private boolean canceled;
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

}
