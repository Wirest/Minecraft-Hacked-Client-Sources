package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class PlaceAnywhere extends Module
{
    public PlaceAnywhere() {
        this.setName("PlaceAnywhere");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void eventPushOutOfBlock(final EventPushOutOfBlock e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void eventEntityBoundingBox(final EventEntityBoundingBox e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
}
