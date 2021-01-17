package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class KeepSneak extends Module
{
    public KeepSneak() {
        this.setName("KeepSneak");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
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
        KeepSneak.mc.gameSettings.keyBindSneak.pressed = false;
        KeepSneak.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(KeepSneak.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction packet = (C0BPacketEntityAction)e.getPacket();
            final C0BPacketEntityAction.Action func_180764_b = packet.func_180764_b();
            packet.func_180764_b();
            if (func_180764_b == C0BPacketEntityAction.Action.STOP_SNEAKING) {
                e.setCancelled(true);
                KeepSneak.mc.gameSettings.keyBindSneak.pressed = true;
            }
        }
    }
}
