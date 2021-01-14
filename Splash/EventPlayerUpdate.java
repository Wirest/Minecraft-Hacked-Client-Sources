package splash.client.events.player;

import me.hippo.systems.lwjeb.event.MultiStage;

public class EventPlayerUpdate extends MultiStage {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean ground;

	public EventPlayerUpdate(double x, double y, double z, float yaw, float pitch, boolean ground) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.ground = ground;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean getGroundState() {
		return this.ground;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

}
