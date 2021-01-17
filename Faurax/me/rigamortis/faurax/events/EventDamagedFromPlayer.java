package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.entity.player.*;

public class EventDamagedFromPlayer extends EventCancellable implements Event
{
    private EntityPlayer ep;
    
    public EventDamagedFromPlayer(final EntityPlayer e) {
        this.ep = null;
        this.setEp(e);
    }
    
    public EntityPlayer getEp() {
        return this.ep;
    }
    
    public void setEp(final EntityPlayer ep) {
        this.ep = ep;
    }
}
