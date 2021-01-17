package net.minecraft.MoveEvents;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class UpdateEvent extends Event
{
    public double y;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType state;
    public boolean alwaysSend;
    private double stepHeight;
    private boolean active;
	public boolean sendAnyway;
    
    public UpdateEvent (final double y, final float yaw, final float pitch, final boolean onGround) {
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.state = skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType.PRE;
    }
    
    public UpdateEvent() {
        this.state = skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType.POST;
    }
    
    public double getStepHeight() {
        return this.stepHeight;
    }
    
    
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setStepHeight(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
    
    public void setActive(final boolean bypass) {
        this.active = bypass;
    }
    
    public void setAlwaysSend(final boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }

    public double getY()
    {
      return this.y;
    }

    public void setY(double y)
    {
      this.y = y;
    }

    public float getYaw() {
       return this.yaw;
    }

    public float getPitch() {
       return this.pitch;
    }

    
    
  
  }
