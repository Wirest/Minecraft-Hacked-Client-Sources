package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class Revive extends Module
{
    public Revive() {
        this.setKey("");
        this.setName("Revive");
        this.setType(ModuleType.MISC);
        this.setModInfo("");
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onDeath(final EventOnDeath e) {
        if (this.isToggled()) {
            Revive.mc.thePlayer.sendChatMessage("/sethome");
            Revive.mc.thePlayer.respawnPlayer();
            Revive.mc.thePlayer.sendChatMessage("/home");
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
