package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.*;

public class EventRenderWorld implements Event
{
    private float ticks;
    
    public EventRenderWorld(final float ticks) {
        this.setTicks(ticks);
    }
    
    public float getTicks() {
        return this.ticks;
    }
    
    public void setTicks(final float ticks) {
        this.ticks = ticks;
    }
}
