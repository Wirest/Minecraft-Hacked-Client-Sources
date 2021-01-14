package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class MoveEvent extends EventCancellable {

	private double motionX, motionY, motionZ;
	
	public MoveEvent(double motionX, double motionY, double motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}
	
	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}
	
	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}
	
	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}
	
	public double getMotionX() {
		return motionX;
	}
	
	public double getMotionY() {
		return motionY;
	}
	
	public double getMotionZ() {
		return motionZ;
	}
	
	public void zero() {
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
	}
	
}
