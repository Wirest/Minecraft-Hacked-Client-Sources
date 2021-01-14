package com.etb.client.event.events.player;

import com.etb.client.event.Event;

public class MotionEvent extends Event {
	private double x,y,z;
	
	public MotionEvent(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getZ() {
		return z;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
