/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.world.World
 */
package delta.module.modules;

import delta.Class8;
import delta.client.DeltaClient;
import delta.utils.TimeHelper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.World;

public class KillAura
extends Module {
    private TimeHelper delay = new TimeHelper();

    public void onMotion(EventMotion eventMotion) {
        for (Object e : this.mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityLivingBase) || (EntityLivingBase)e == null || (EntityLivingBase)e == this.mc.thePlayer || !((EntityLivingBase)e).isEntityAlive() || e instanceof EntityPlayer && DeltaClient.instance.managers.friendsManager.contains(((EntityPlayer)e).getCommandSenderName()) || !((double)((EntityLivingBase)e).getDistanceToEntity((Entity)this.mc.thePlayer) <= this.getSetting("Reach").getSliderValue())) continue;
            if (!this.getSetting("NoSwing").getCheckValue()) {
                this.mc.thePlayer.swingItem();
            }
            EntityLivingBase entityLivingBase = (EntityLivingBase)e;
            float[] arrf = Class8._brakes(entityLivingBase);
            eventMotion.setYaw(arrf[0]);
            eventMotion.setPitch(arrf[1]);
            if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.getSetting("BlockHit").getCheckValue() || this.mc.thePlayer.isBlocking()) {
                this.mc.playerController.sendUseItem((EntityPlayer)this.mc.thePlayer, (World)this.mc.theWorld, this.mc.thePlayer.getHeldItem());
            }
            if (!this.delay.hasReached((long)(1000.0 / this.getSetting("CPS").getSliderValue()))) continue;
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)((EntityLivingBase)e), C02PacketUseEntity.Action.ATTACK));
        }
        if (!this.delay.hasReached((long)(1000.0 / this.getSetting("CPS").getSliderValue()))) {
            return;
        }
        this.delay.setLastMS();
    }

    public KillAura() {
        super("KillAura", 19, Category.Combat);
        this.setDescription("Attaque les joueurs autour de soi");
        this.addSetting(new Setting("CPS", (IModule)this, 20.0, 0.0, 20.0, true));
        this.addSetting(new Setting("Reach", (IModule)this, 7.0, 3.0, 7.0, false));
        this.addSetting(new Setting("BlockHit", (IModule)this, true));
        this.addSetting(new Setting("NoSwing", (IModule)this, false));
    }

    @EventTarget
    public void onMotion2(EventMotion eventMotion) {
        if (eventMotion.getType() == Event.State.POST) {
            return;
        }
        try {
            this.onMotion(eventMotion);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

