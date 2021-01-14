package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class SlowDownEvent extends EventCancellable {

	private double strafeFactor;
	private double forwardFactor;
	
	public SlowDownEvent(float strafeFactor, float forwardFactor) {
		this.strafeFactor = strafeFactor;
		this.forwardFactor = forwardFactor;
	}

	public double getStrafeFactor() {
		return strafeFactor;
	}
	
	public double getForwardFactor() {
		return forwardFactor;
	}
	
	public void setStrafeFactor(double strafeFactor) {
		this.strafeFactor = strafeFactor;
	}
	
	public void setForwardFactor(double forwardFactor) {
		this.forwardFactor = forwardFactor;
	}
	
}
