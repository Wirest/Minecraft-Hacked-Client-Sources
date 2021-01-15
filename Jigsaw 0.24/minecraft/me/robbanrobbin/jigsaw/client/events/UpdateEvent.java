package me.robbanrobbin.jigsaw.client.events;

public class UpdateEvent extends Event {
	
	public float yaw;
	public float pitch;
	public double x;
	public double y;
	public double z;
	public boolean onGround;
	public boolean autopot;
	
	public UpdateEvent(float yaw, float pitch, double x, double y, double z, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.y = y;
		this.x = x;
		this.z = z;
		this.onGround = onGround;
	}
	
}
