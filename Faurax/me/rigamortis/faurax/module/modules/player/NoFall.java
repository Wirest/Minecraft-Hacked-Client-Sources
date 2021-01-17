package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import com.darkmagician6.eventapi.*;

public class NoFall extends Module implements PlayerHelper
{
    public NoFall() {
        this.setName("NoFall");
        this.setType(ModuleType.PLAYER);
        this.setKey("PERIOD");
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C03PacketPlayer && NoFall.mc.thePlayer.fallDistance >= 2.0f) {
            final C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            packet.field_149474_g = true;
        }
    }
}
