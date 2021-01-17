package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import com.darkmagician6.eventapi.*;

public class KeepSprint extends Module
{
    public KeepSprint() {
        this.setName("KeepSprint");
        this.setKey("");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C0BPacketEntityAction && e.getPacket() instanceof C02PacketUseEntity) {
            final C0BPacketEntityAction packet = (C0BPacketEntityAction)e.getPacket();
            final C02PacketUseEntity useEntity = (C02PacketUseEntity)e.getPacket();
            if (useEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                final C0BPacketEntityAction.Action func_180764_b = packet.func_180764_b();
                packet.func_180764_b();
                if (func_180764_b == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
