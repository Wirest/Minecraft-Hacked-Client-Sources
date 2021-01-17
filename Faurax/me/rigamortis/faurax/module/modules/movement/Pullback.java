package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class Pullback extends Module
{
    public int delay;
    public int health;
    public boolean hasDamaged;
    
    public Pullback() {
        this.setName("PullBack");
        this.setType(ModuleType.MOVEMENT);
        this.setKey("NUMPAD1");
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.health = (int)Pullback.mc.thePlayer.getHealth();
        this.delay = 0;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        this.isToggled();
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket e) {
        this.isToggled();
    }
}
