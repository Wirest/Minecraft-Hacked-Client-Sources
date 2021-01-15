package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventStep implements Event
{
    public double stepHeight;
    public boolean bypass;
    
    public EventStep(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
}