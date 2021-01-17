package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;

public class EventLiquidBoundingBox extends EventCancellable implements Event
{
    private float height;
    
    public EventLiquidBoundingBox(final float height) {
        this.setHeight(height);
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
}
