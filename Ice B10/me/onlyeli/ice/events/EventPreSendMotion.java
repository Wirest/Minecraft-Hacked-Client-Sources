package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public class EventPreSendMotion
  extends EventCancellable
{
  public float yaw;
  public float pitch;
  
  public EventPreSendMotion(float yaw, float pitch)
  {
    this.yaw = yaw;
    this.pitch = pitch;
  }
  
  public float getPitch()
  {
    return this.pitch;
  }
  
  public float getYaw()
  {
    return this.yaw;
  }
  
  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }
  
  public void setYaw(float yaw)
  {
    this.yaw = yaw;
  }
}
