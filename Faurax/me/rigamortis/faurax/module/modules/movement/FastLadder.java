package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class FastLadder extends Module implements MovementHelper
{
    public FastLadder() {
        this.setName("FastLadder");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        if (this.isToggled() && e.motionY > 0.0 && FastLadder.mc.thePlayer.isOnLadder() && !FastLadder.mc.thePlayer.isInWater()) {
            e.motionY *= 2.4430201053619385;
        }
    }
}
