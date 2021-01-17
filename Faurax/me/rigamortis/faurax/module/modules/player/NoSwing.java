package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class NoSwing extends Module
{
    public NoSwing() {
        this.setName("NoSwing");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSwing(final EventSwingItem e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
}
