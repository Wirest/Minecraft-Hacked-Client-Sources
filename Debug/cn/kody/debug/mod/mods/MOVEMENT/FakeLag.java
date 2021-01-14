package cn.kody.debug.mod.mods.MOVEMENT;

import java.util.Iterator;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.concurrent.CopyOnWriteArrayList;

public class FakeLag extends Mod
{
    Value<Double> lagValue;
    CopyOnWriteArrayList<C03PacketPlayer> packetList;
    TimeHelper lagHelper;
    C03PacketPlayer lastPacket;
    
    public FakeLag() {
        super("FakeLag", "Fake Lag", Category.MOVEMENT);
        this.lagValue = new Value<Double>("FakeLag_Delay", 3000.0, 300.0, 5000.0);
        this.packetList = new CopyOnWriteArrayList<C03PacketPlayer>();
        this.lagHelper = new TimeHelper();
    }
    
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        Packet packet = eventPacket.getPacket();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
            if (this.packetList.contains(c03PacketPlayer)) {
                this.packetList.remove(c03PacketPlayer);
            }
            else {
                this.packetList.add(c03PacketPlayer);
                eventPacket.setCancelled(true);
            }
        }
    }
    
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (this.lagHelper.isDelayComplete(this.lagValue.getValueState().longValue())) {
            if (this.mc.theWorld.getEntityByID(-1) != null) {
                this.mc.theWorld.removeEntityFromWorld(-1);
            }
            for (C03PacketPlayer c03PacketPlayer : this.packetList) {
                this.mc.thePlayer.sendQueue.addToSendQueue(c03PacketPlayer);
                this.lastPacket = c03PacketPlayer;
            }
            if (this.lastPacket != null && this.mc.gameSettings.thirdPersonView != 0) {
                EntityOtherPlayerMP p_addEntityToWorld_2_ = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
                p_addEntityToWorld_2_.setPositionAndRotation(this.lastPacket.getPositionX(), this.lastPacket.getPositionY(), this.lastPacket.getPositionZ(), this.lastPacket.getYaw(), this.lastPacket.getPitch());
                p_addEntityToWorld_2_.inventory = this.mc.thePlayer.inventory;
                p_addEntityToWorld_2_.inventoryContainer = this.mc.thePlayer.inventoryContainer;
                p_addEntityToWorld_2_.rotationYawHead = this.mc.thePlayer.rotationYawHead;
                this.mc.theWorld.addEntityToWorld(-1, p_addEntityToWorld_2_);
            }
            this.lagHelper.reset();
        }
    }
}
