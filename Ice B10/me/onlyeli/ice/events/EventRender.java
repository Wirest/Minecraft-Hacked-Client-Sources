package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventRender
  implements Event
{
  public int particalTicks;
  
  public EventRender(int particalTicks)
  {
    this.particalTicks = particalTicks;
  }
}
