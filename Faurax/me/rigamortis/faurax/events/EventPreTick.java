package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;

public class EventPreTick extends EventCancellable implements Event
{
    public double posX;
    public double minY;
    public double posY;
    public double posZ;
    
    public EventPreTick(final double posX, final double minY, final double posY, final double posZ) {
        this.posX = posX;
        this.minY = minY;
        this.posY = posY;
        this.posZ = posZ;
    }
}
