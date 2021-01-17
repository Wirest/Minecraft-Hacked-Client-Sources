package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class Scaffold extends Module implements MovementHelper
{
    public Scaffold() {
        this.setName("SafeWalk");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void eventSafeWalk(final EventSafeWalk e) {
        if (this.isToggled()) {
            if (Scaffold.mc.gameSettings.keyBindJump.pressed || !Scaffold.mc.thePlayer.onGround) {
                e.setCancelled(false);
            }
            else {
                e.setCancelled(true);
            }
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        this.isToggled();
    }
}
