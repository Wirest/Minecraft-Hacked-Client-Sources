package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.*;

public class EventPlayerReach implements Event
{
    private float range;
    
    public EventPlayerReach(final float f) {
        this.range = f;
    }
    
    public float getRange() {
        return this.range;
    }
    
    public void setRange(final int range) {
        this.range = range;
    }
}
