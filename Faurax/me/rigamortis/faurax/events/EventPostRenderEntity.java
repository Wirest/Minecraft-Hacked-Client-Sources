package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.entity.*;

public class EventPostRenderEntity extends EventCancellable implements Event
{
    private Entity e;
    
    public EventPostRenderEntity(final Entity e) {
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
