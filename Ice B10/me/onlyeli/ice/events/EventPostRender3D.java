package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventPostRender3D
  implements Event
{
    private float ticks;
    public float getPartialTicks()
    {
        return this.ticks;
    }
}
