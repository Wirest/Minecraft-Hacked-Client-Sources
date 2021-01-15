package me.xatzdevelopments.xatz.client.events;

import net.minecraft.network.Packet;

public class UpdateEvent extends Event {
	
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

	private  Packet packet; 
	public float yaw;
	public float getYaw() {
		return yaw;
	}
	
	public void setRotation(float yaw, float pitch) {
		if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90) return;
		
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void setRotations(float[] rotations) {
		setRotation(rotations[0], rotations[1]);
	}

	public float pitch;
	public double x;
	public double y;
	public double z;
	public boolean onGround;
	public boolean autopot;
	private boolean cancelled;
	private boolean onground;
	
	public UpdateEvent(float yaw, float pitch, double x, double y, double z, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.y = y;
		this.x = x;
		this.z = z;
		this.onGround = onGround;
	}
	
	public void setOnGround(boolean onground) {
        this.onground = onground;
    }
	
	public void setYaw(float yaw) {
		     this.yaw = yaw;
		   }
		   
		  public void setPitch(float pitch) {
		     this.pitch = pitch;
		   }
		  
		  public Packet getPacket() {
				return packet;
			}
		 
}
