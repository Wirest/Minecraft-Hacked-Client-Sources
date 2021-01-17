/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.event.events.other.EventIsNormalBlock
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.world.World
 */
package delta.module.modules;

import delta.utils.MovementUtils;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.event.events.other.EventIsNormalBlock;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;

public class Freecam
extends Module {
    private EntityOtherPlayerMP freecamEntity;

    public Freecam() {
        super("Freecam", Category.Movement);
        this.setDescription("Permet de sortir de son corps et d'explorer les environs");
    }

    public void onEnable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, 2));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, 5));
        this.freecamEntity = new EntityOtherPlayerMP((World)this.mc.theWorld, this.mc.getSession().func_148256_e());
        this.freecamEntity.inventory = this.mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = this.mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(this.mc.thePlayer.posX, this.mc.thePlayer.boundingBox.minY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = this.mc.thePlayer.rotationYawHead;
        this.freecamEntity.onGround = this.mc.thePlayer.onGround;
        this.mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), (Entity)this.freecamEntity);
        this.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getState() == Event.State.RECEIVE) {
            return;
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer) {
            eventPacket.setCancelled(true);
        } else if (eventPacket.getPacket() instanceof C0BPacketEntityAction && (((C0BPacketEntityAction)eventPacket.getPacket()).func_149513_d() == 4 || ((C0BPacketEntityAction)eventPacket.getPacket()).func_149513_d() == 5 || ((C0BPacketEntityAction)eventPacket.getPacket()).func_149513_d() == 1 || ((C0BPacketEntityAction)eventPacket.getPacket()).func_149513_d() == 2)) {
            eventPacket.setCancelled(true);
        }
    }

    public void onDisable() {
        if (this.freecamEntity != null) {
            this.mc.thePlayer.setPosition(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ);
            this.mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
        }
        this.mc.renderGlobal.loadRenderers();
        this.mc.thePlayer.noClip = false;
        this.mc.thePlayer.motionZ = 0.0;
        this.mc.thePlayer.motionY = 0.0;
        this.mc.thePlayer.motionX = 0.0;
        super.onDisable();
    }

    @EventTarget
    public void onNormalBlock(EventIsNormalBlock eventIsNormalBlock) {
        eventIsNormalBlock.setCancelled(true);
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.motionY = this.mc.gameSettings.keyBindJump.getIsKeyPressed() ? (double)(!this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 1 : 0) : (double)(this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -1 : 0);
        MovementUtils.setSpeed(0.0);
        if (MovementUtils.isMoving()) {
            MovementUtils.setSpeed(MovementUtils.getSpeed() + 1.0);
        }
    }
}

