package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class FastPlace extends Module
{
    public FastPlace() {
        this.setName("FastPlace");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        FastPlace.mc.rightClickDelayTimer = 4;
    }
}
