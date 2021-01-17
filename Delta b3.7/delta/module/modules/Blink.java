/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.world.World
 */
package delta.module.modules;

import java.util.ArrayDeque;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.World;

public class Blink
extends Module {
    private EntityOtherPlayerMP blinkEntity;
    private ArrayDeque<C03PacketPlayer> packetList = new ArrayDeque();

    public Blink() {
        super("Blink", Category.Movement);
        this.setDescription("Mets en attente les mouvements du joueur, permet de simuler du lag.");
    }

    public void onDisable() {
        if (this.blinkEntity != null) {
            this.mc.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
        }
        this.mc.renderGlobal.loadRenderers();
        this.packetList.forEach(((NetHandlerPlayClient)this.mc.thePlayer.sendQueue)::addToSendQueue);
        this.packetList.clear();
        super.onDisable();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getState() != Event.State.SEND) {
            return;
        }
        Packet packet = eventPacket.getPacket();
        if (!(packet instanceof C03PacketPlayer)) {
            return;
        }
        C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)eventPacket.getPacket();
        C03PacketPlayer c03PacketPlayer2 = this.packetList.peekLast();
        if (c03PacketPlayer2 != null && c03PacketPlayer.func_149465_i() == c03PacketPlayer2.func_149465_i() && c03PacketPlayer.func_149462_g() == c03PacketPlayer2.func_149462_g() && c03PacketPlayer.func_149470_h() == c03PacketPlayer2.func_149470_h() && c03PacketPlayer.func_149464_c() == c03PacketPlayer2.func_149464_c() && c03PacketPlayer.func_149471_f() == c03PacketPlayer2.func_149471_f() && c03PacketPlayer.func_149472_e() == c03PacketPlayer2.func_149472_e()) {
            return;
        }
        this.packetList.addLast(c03PacketPlayer);
        eventPacket.setCancelled(true);
    }

    public void onEnable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.blinkEntity = new EntityOtherPlayerMP((World)this.mc.theWorld, this.mc.getSession().func_148256_e());
        this.blinkEntity.inventory = this.mc.thePlayer.inventory;
        this.blinkEntity.inventoryContainer = this.mc.thePlayer.inventoryContainer;
        this.blinkEntity.setPositionAndRotation(this.mc.thePlayer.posX, this.mc.thePlayer.boundingBox.minY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
        this.blinkEntity.rotationYawHead = this.mc.thePlayer.rotationYawHead;
        this.blinkEntity.onGround = this.mc.thePlayer.onGround;
        this.mc.theWorld.addEntityToWorld(this.blinkEntity.getEntityId(), (Entity)this.blinkEntity);
        this.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }
}

