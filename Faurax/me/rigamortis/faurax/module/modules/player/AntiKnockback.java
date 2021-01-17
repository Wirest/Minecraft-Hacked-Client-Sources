package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import com.darkmagician6.eventapi.*;

public class AntiKnockback extends Module
{
    public static int velocity;
    
    public AntiKnockback() {
        this.setName("AntiVelocity");
        this.setKey("K");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled() && e.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity vel = (S12PacketEntityVelocity)e.getPacket();
            if (e.getPacket() instanceof S27PacketExplosion) {
                e.setCancelled(true);
            }
            if (vel.func_149412_c() == AntiKnockback.mc.thePlayer.getEntityId()) {
                e.setCancelled(true);
            }
        }
    }
}
