/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.MovingObjectPosition
 *  org.lwjgl.input.Mouse
 */
package me.aristhena.lucid.modules.combat;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.friend.FriendManager;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

@Mod
public class TriggerBot
extends Module {
    @Val(increment=0.5, max=20.0)
    private double speed = 4.5;
    @Val(increment=0.25, max=8.0)
    public static double range = 4.5;
    @Val(min=0.0, increment=0.25, max=4.0)
    public static double hitboxSize = 1.5;
    @Op
    public static boolean autoclicker;
    @Op
    private boolean players = true;
    @Op
    private boolean monsters;
    @Op
    private boolean criticals;
    private Timer timer = new Timer();

    @EventTarget
    private void onPostUpdate(UpdateEvent event) {
        if (event.state == Event.State.POST && (autoclicker && Mouse.isButtonDown((int)0) || !autoclicker) && this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.attackChecks(this.mc.objectMouseOver.entityHit) && this.timer.delay((float)(1000.0 / this.speed))) {
            if (this.criticals) {
                this.crit();
            }
            this.mc.thePlayer.swingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
            this.timer.reset();
        }
    }

    private void crit() {
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.05, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.012511, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    }

    private boolean attackChecks(Entity ent) {
        boolean isVehicle;
        if (ent == null) {
            return false;
        }
        if (!(ent instanceof EntityLivingBase)) {
            return false;
        }
        if (ent == this.mc.thePlayer) {
            return false;
        }
        if ((double)ent.getDistanceToEntity((Entity)this.mc.thePlayer) > range) {
            return false;
        }
        if (!ent.isEntityAlive()) {
            return false;
        }
        boolean bl = isVehicle = ent instanceof EntityMinecart || ent instanceof EntityBoat;
        if (ent instanceof EntityPlayer) {
            if (!this.players) {
                return false;
            }
            EntityPlayer player = (EntityPlayer)ent;
            if (!this.players) {
                return false;
            }
            if (FriendManager.isFriend(player.getCommandSenderName())) {
                return false;
            }
        }
        if (isVehicle) {
            return false;
        }
        if (ent instanceof EntityMob && !this.monsters) {
            return false;
        }
        if (ent instanceof EntitySlime && !this.monsters) {
            return false;
        }
        return true;
    }
}

