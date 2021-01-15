package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventPreMotion implements Event {

	private double motionX, motionY, motionZ;

	public double getMotionX() {
		return motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public void setMotion(double motionX, double motionY, double motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

}
