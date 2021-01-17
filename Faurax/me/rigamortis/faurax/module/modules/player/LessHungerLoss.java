package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import com.darkmagician6.eventapi.*;

public class LessHungerLoss extends Module
{
    public LessHungerLoss() {
        this.setName("NoHunger");
        this.setKey("NUMPAD0");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C03PacketPlayer && LessHungerLoss.mc.thePlayer.fallDistance <= 2.0f) {
            final C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            packet.field_149474_g = false;
        }
    }
}
