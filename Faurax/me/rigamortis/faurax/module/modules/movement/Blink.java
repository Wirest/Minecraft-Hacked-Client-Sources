package me.rigamortis.faurax.module.modules.movement;

import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import me.rigamortis.faurax.module.*;
import com.mojang.authlib.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;

public class Blink extends Module
{
    public ArrayList<Packet> packets;
    private EntityOtherPlayerMP fakePlayer;
    
    public Blink() {
        this.packets = new ArrayList<Packet>();
        this.setName("Blink");
        this.setKey("R");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        (this.fakePlayer = new EntityOtherPlayerMP(Blink.mc.theWorld, new GameProfile(Blink.mc.session.getProfile().getId(), Blink.mc.thePlayer.getName()))).setPositionAndRotation(Blink.mc.thePlayer.posX, Blink.mc.thePlayer.posY, Blink.mc.thePlayer.posZ, Blink.mc.thePlayer.rotationYaw, Blink.mc.thePlayer.rotationPitch);
        this.fakePlayer.rotationYawHead = Blink.mc.thePlayer.rotationYawHead;
        this.fakePlayer.inventory = Blink.mc.thePlayer.inventory;
        this.fakePlayer.setSneaking(Blink.mc.thePlayer.isSneaking());
        Blink.mc.theWorld.addEntityToWorld(-1338, this.fakePlayer);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        for (final Packet packet : this.packets) {
            if (packet instanceof C02PacketUseEntity || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging) {
                Blink.mc.thePlayer.swingItem();
            }
            Blink.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        this.packets.clear();
        try {
            if (this.fakePlayer != null) {
                Blink.mc.theWorld.removeEntity(this.fakePlayer);
            }
        }
        catch (Exception ex) {}
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + this.packets.size());
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled()) {
            if (!(e.getPacket() instanceof C01PacketChatMessage) && !(e.getPacket() instanceof C16PacketClientStatus) && !(e.getPacket() instanceof C00PacketKeepAlive) && Blink.mc.theWorld != null) {
                e.setCancelled(true);
            }
            final boolean input = Blink.mc.gameSettings.keyBindForward.pressed || Blink.mc.gameSettings.keyBindBack.pressed || Blink.mc.gameSettings.keyBindRight.pressed || Blink.mc.gameSettings.keyBindLeft.pressed;
            if (input && e.getPacket() instanceof C03PacketPlayer) {
                this.packets.add(e.getPacket());
            }
            if (e.getPacket() instanceof C02PacketUseEntity || e.getPacket() instanceof C08PacketPlayerBlockPlacement || e.getPacket() instanceof C07PacketPlayerDigging) {
                this.packets.add(e.getPacket());
            }
        }
    }
}
