package cn.kody.debug.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C02PacketUseEntity;
import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventPacket.EventPacketType;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;

public class WTap extends Mod
{
    
    public WTap() {
        super("WTap", Category.COMBAT);
    }
    
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getType() == EventPacketType.RECEIVE && this.mc.theWorld != null && this.mc.thePlayer != null && eventPacket.packet instanceof C02PacketUseEntity) {
            final C02PacketUseEntity c02PacketUseEntity = (C02PacketUseEntity)eventPacket.packet;
            if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK && c02PacketUseEntity.getEntityFromWorld(this.mc.theWorld) != this.mc.thePlayer && this.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                if (this.mc.thePlayer.isSprinting()) {
                    this.mc.thePlayer.setSprinting(false);
                    this.mc.thePlayer.setSprinting(true);
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            }
        }
    }
}
