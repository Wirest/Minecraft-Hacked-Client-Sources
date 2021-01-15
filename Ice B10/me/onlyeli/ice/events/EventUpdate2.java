package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;
import me.onlyeli.eventapi.events.Event.State;

public class EventUpdate2 implements Event
{
  public Event.State state;
  public float yaw;
  public float pitch;
  public double y;
  public boolean ground;
  
  public EventUpdate2()
  {
    this.state = Event.State.POST;
  }
  
  public EventUpdate2(double y, float yaw, float pitch, boolean ground)
  {
    this.state = Event.State.PRE;
    this.yaw = yaw;
    this.pitch = pitch;
    this.y = y;
    this.ground = ground;
  }
}