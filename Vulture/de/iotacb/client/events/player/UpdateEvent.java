package de.iotacb.client.events.player;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import de.iotacb.client.events.states.UpdateState;

public class UpdateEvent extends EventCancellable {
	
	private UpdateState state;
	
	private float yaw, pitch;
	private double x, y, z;
	
	public UpdateEvent(float yaw, float pitch, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.state = UpdateState.PRE;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setRotation(float yaw, float pitch) {
		if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90) return;
		
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void setRotations(float[] rotations) {
		setRotation(rotations[0], rotations[1]);
	}
	
	public void setState(UpdateState state) {
		this.state = state;
	}
	
	public float[] getRotations() {
		return new float[] {yaw, pitch};
	}
	
	public UpdateState getState() {
		return state;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
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
