package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class Ghost extends Module
{
    public Ghost() {
        this.setKey("");
        this.setName("Ghost");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onDeath(final EventOnDeath e) {
        if (this.isToggled()) {
            e.setCancelled(true);
            Ghost.mc.thePlayer.setHealth(20.0f);
            Ghost.mc.thePlayer.getFoodStats().setFoodLevel(20);
            Ghost.mc.thePlayer.isDead = false;
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Ghost.mc.thePlayer.respawnPlayer();
    }
}
