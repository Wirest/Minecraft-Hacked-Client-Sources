// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import java.util.Iterator;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import cf.euphoria.euphorical.Events.EventPacketSend;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventTick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.Mod;

public class Blink extends Mod
{
    private long deltaTime;
    private long startTime;
    TimeHelper timer;
    private EntityOtherPlayerMP player;
    private ArrayList<Packet> packets;
    
    public Blink() {
        super("Blink", Category.PLAYER);
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
        this.startTime = this.timer.getTime();
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        this.setRenderName(String.format("%s §7%sms", this.getModName(), this.deltaTime));
    }
    
    @EventTarget
    public void onPacketSend(final EventPacketSend event) {
        if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0BPacketEntityAction || event.packet instanceof C0APacketAnimation || event.packet instanceof C02PacketUseEntity || event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
            this.packets.add(event.packet);
            this.packets.trimToSize();
            this.deltaTime = this.timer.getTime() - this.startTime;
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
