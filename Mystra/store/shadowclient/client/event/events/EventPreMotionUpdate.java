package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;

public class EventPreMotionUpdate extends Event {
    public float yaw;
	public float pitch;
    private boolean ground;
    private boolean pre;
    public double x, y, z;
    
    public EventPreMotionUpdate(float yaw, float pitch, boolean ground, double x, double y, double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setRotation(float yaw, float pitch) {
		if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90) return;
		
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void setRotations(float[] rotations) {
		setRotation(rotations[0], rotations[1]);
	}
	
	public float[] getRotations() {
		return new float[] {yaw, pitch};
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

    public boolean onGround() {
        return ground;
    }
    public void setGround(boolean ground) {
        this.ground = ground;
    }

	public boolean isPre() {
		return pre;
	}

	public boolean isPost() {
        return !pre;
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
