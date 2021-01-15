/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.world.World
 */
package me.aristhena.lucid.modules.movement;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketSendEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;

@Mod
public class Blink
extends Module {
    private EntityOtherPlayerMP playerCopy;
    private List<Packet> packetList = new ArrayList<Packet>();

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.playerCopy = new EntityOtherPlayerMP((World)this.mc.theWorld, this.mc.thePlayer.getGameProfile());
        this.playerCopy.inventory = this.mc.thePlayer.inventory;
        this.playerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
        this.playerCopy.setPositionAndRotation(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
        this.playerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
        this.mc.theWorld.addEntityToWorld(-1, (Entity)this.playerCopy);
        super.onEnable();
    }

    @EventTarget
    private void onPacketSend(PacketSendEvent event) {
        if (event.packet instanceof C0BPacketEntityAction || event.packet instanceof C03PacketPlayer || event.packet instanceof C02PacketUseEntity || event.packet instanceof C0APacketAnimation || event.packet instanceof C08PacketPlayerBlockPlacement) {
            this.packetList.add(event.packet);
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Packet packet : this.packetList) {
            this.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        this.packetList.clear();
        this.mc.theWorld.removeEntityFromWorld(-1);
    }
}

