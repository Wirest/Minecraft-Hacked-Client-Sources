package nivia.events.events;

import nivia.events.EventDelayable;

public class EventPostMotionUpdates extends EventDelayable {
	private float yaw,pitch;
	
	public EventPostMotionUpdates(float yaw, float pitch){
		this.yaw = yaw;
		this.pitch = pitch;
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
}
