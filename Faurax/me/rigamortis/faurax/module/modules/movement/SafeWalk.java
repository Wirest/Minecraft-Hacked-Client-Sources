package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class SafeWalk extends Module implements MovementHelper
{
    public SafeWalk() {
        this.setName("SafeWalk");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void eventSafeWalk(final EventSafeWalk e) {
        if (this.isToggled()) {
            if (SafeWalk.mc.gameSettings.keyBindJump.pressed || !SafeWalk.mc.thePlayer.onGround) {
                e.setCancelled(false);
            }
            else {
                e.setCancelled(true);
            }
        }
    }
}
