package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class StrafeEvent extends EventCancellable {

	private float strafe, forward, friction;
	
	public StrafeEvent(float strafe, float forward, float friction) {
		this.strafe = strafe;
		this.forward = forward;
		this.friction = friction;
	}
	
	public float getStrafe() {
		return strafe;
	}
	
	public float getForward() {
		return forward;
	}
	
	public float getFriction() {
		return friction;
	}
	
	public void setStrafe(float strafe) {
		this.strafe = strafe;
	}
	
	public void setForward(float forward) {
		this.forward = forward;
	}
	
	public void setFriction(float friction) {
		this.friction = friction;
	}
	
}
