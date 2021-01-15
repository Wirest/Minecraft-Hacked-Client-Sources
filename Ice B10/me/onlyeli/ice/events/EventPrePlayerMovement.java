package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventPrePlayerMovement
  implements Event
{
  public double x;
  public double y;
  public double z;
  
  public EventPrePlayerMovement(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public double getX()
  {
    return this.x;
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
  public double getY()
  {
    return this.y;
  }
  
  public double getZ()
  {
    return this.z;
  }
  
  public void setX(double x)
  {
    this.x = x;
  }
  
  public void setY(double y)
  {
    this.y = y;
  }
  
  public void setZ(double z)
  {
    this.z = z;
  }
}


