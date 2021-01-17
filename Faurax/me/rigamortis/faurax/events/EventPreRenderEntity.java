package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.entity.*;

public class EventPreRenderEntity extends EventCancellable implements Event
{
    private Entity e;
    
    public EventPreRenderEntity(final Entity e) {
        this.e = null;
        this.setE(e);
    }
    
    public Entity getE() {
        return this.e;
    }
    
    public void setE(final Entity e) {
        this.e = e;
    }
}
