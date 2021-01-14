package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Blink extends Module {
    private long deltaTime;
    private long startTime;
    TimeHelper timer;
    private EntityOtherPlayerMP player;
    private ArrayList<Packet> packets;
    
    public Blink() {
        super("Blink", 0, Category.MOVEMENT);
        this.packets = new ArrayList<Packet>();
    }
    
    @Override
    public void onEnable() {
        this.deltaTime = 0L;
        this.timer = new TimeHelper();
        EventManager.register(this);
        (this.player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile())).clonePlayer(this.mc.thePlayer, true);
        this.player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        this.player.rotationYawHead = this.mc.thePlayer.rotationYaw;
        this.player.rotationPitch = this.mc.thePlayer.rotationPitch;
        this.player.setSneaking(this.mc.thePlayer.isSneaking());
        this.mc.theWorld.addEntityToWorld(-1337, this.player);
        this.startTime = this.timer.getCurrentMS();
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0BPacketEntityAction || event.packet instanceof C0APacketAnimation || event.packet instanceof C02PacketUseEntity || event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
            this.packets.add(event.packet);
            this.packets.trimToSize();
            this.deltaTime = this.timer.getCurrentMS() - this.startTime;
        }
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        this.mc.theWorld.removeEntity(this.player);
        for (final Packet packet : this.packets) {
            this.mc.getNetHandler().getNetworkManager().dispatchPacket(packet, null);
        }
    }
}
