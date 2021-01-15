package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventMove
  implements Event
{
  public double x;
  public double y;
  public double z;
  
  public EventMove(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public double getMotionX()
  {
    return this.x;
  }
  
  public double getMotionY()
  {
    return this.y;
  }
  
  public double getMotionZ()
  {
    return this.z;
  }
  
  public void setMotionX(double motionX)
  {
    this.x = motionX;
  }
  
  public void setMotionY(double motionY)
  {
    this.y = motionY;
  }
  
  public void setMotionZ(double motionZ)
  {
    this.z = motionZ;
  }
}