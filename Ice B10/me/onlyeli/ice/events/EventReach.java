package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventReach
  implements Event
{
  private float reach;
  
  public EventReach(float reach)
  {
    this.reach = reach;
  }
  
  public float getReach()
  {
    return this.reach;
  }
  
  public void setReach(float reach)
  {
    this.reach = reach;
  }
}