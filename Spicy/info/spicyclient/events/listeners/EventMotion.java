package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;

public class EventMotion extends Event<EventMotion> {
	
	public double x, y, z, motionX, motionY, motionZ;
	public float yaw, pitch;
	public boolean onGround;
	public EventMotion(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, boolean onGround) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	public boolean isOnGround() {
		return onGround;
	}
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
}
