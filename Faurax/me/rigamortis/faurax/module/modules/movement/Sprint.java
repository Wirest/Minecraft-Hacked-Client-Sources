package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class Sprint extends Module implements MovementHelper
{
    public Sprint() {
        this.setName("Sprint");
        this.setKey("L");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled() && Sprint.movementUtils.shouldSprint()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}
