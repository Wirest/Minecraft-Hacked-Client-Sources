/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.misc.Teams;
import me.slowly.client.mod.mods.render.Trajectories;
import me.slowly.client.mod.mods.world.AntiBots;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.friendmanager.FriendManager;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BowAimbot
extends Mod {
    public Value<Boolean> attackPlayers = new Value<Boolean>("BowAimbot_AttackPlayers", true);
    public Value<Boolean> attackAnimals = new Value<Boolean>("BowAimbot_AttackAnimals", true);
    public Value<Boolean> attackMobs = new Value<Boolean>("BowAimbot_AttackMobs", true);
    Value<Boolean> prediction = new Value<Boolean>("BowAimbot_Prediction", true);
    public Value<Boolean> auto = new Value<Boolean>("BowAimbot_AutoShot", true);
    private Value<String> mode = new Value("BowAimbot", "Mode", 0);
    private EntityLivingBase currentTarget;
    private Random random = new Random();
    private float oldYaw;
    private float oldPitch;

    public BowAimbot() {
        super("BowAimbot", Mod.Category.COMBAT, Colors.DARKRED.c);
        this.mode.addValue("Normal");
        this.mode.addValue("YawHead");
        this.mode.addValue("Silent");
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("BowAimbot Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("BotAimbot Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(-564656);
        this.showValue = this.mode;
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && this.mc.thePlayer.isUsingItem()) {
            if (this.currentTarget == null) {
                this.currentTarget = this.getClosestEntity();
            } else if (this.isValid(this.currentTarget)) {
                this.doAimbot(event);
            } else {
                this.currentTarget = null;
            }
        } else {
            this.currentTarget = null;
        }
        if (this.currentTarget == null) {
            this.oldPitch = 0.0f;
            this.oldYaw = 0.0f;
        }
    }

    private void doAimbot(EventPreMotion event) {
        float[] rotations = CombatUtil.getRotations(this.currentTarget);
        int i = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration() - this.mc.thePlayer.getItemInUseCount();
        float f = (float)i / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        double diffX = this.currentTarget.posX - this.mc.thePlayer.posX;
        double diffY = this.currentTarget.posY + (double)(this.currentTarget.getEyeHeight() / 2.0f) - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight());
        double diffZ = this.currentTarget.posZ - this.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float step = 0.006f;
        float rotYaw = rotations[0];
        float addPitch = (float)(Math.pow(f, 4.0) - (double)step * ((double)step * (dist * dist) + 2.0 * diffY * (double)(f * f)));
        float rotPitch = (float)(- (double)((float)Math.atan(((double)(f * f) - Math.sqrt(addPitch)) / ((double)step * dist)) * 180.0f) / 3.141592653589793);
        if (Float.isNaN(rotPitch)) {
            return;
        }
        float oldYaw_ = rotYaw;
        float oldPitch_ = rotPitch;
        if (this.prediction.getValueState().booleanValue()) {
            rotYaw += (rotYaw - this.oldYaw) * Math.min(this.mc.thePlayer.getDistanceToEntity(this.currentTarget), 15.0f);
        }
        this.oldYaw = oldYaw_;
        this.oldPitch = oldPitch_;
        if (PlayerUtil.getSpeed() > 0.0) {
            this.oldYaw = (float)((double)this.oldYaw + PlayerUtil.getSpeed() / 2.0 * (double)Math.min(this.mc.thePlayer.getDistanceToEntity(this.currentTarget), 15.0f) * (double)(rotYaw - this.oldYaw) / (double)Math.abs(rotYaw - this.oldYaw));
        }
        if (this.mode.isCurrentMode("Normal")) {
            this.mc.thePlayer.rotationYaw = rotYaw;
            this.mc.thePlayer.rotationPitch = rotPitch;
        } else {
            if (this.mode.isCurrentMode("YawHead")) {
                this.mc.thePlayer.rotationYawHead = rotYaw;
            }
            event.yaw = rotYaw;
            event.pitch = rotPitch;
        }
        Trajectories.yaw = rotYaw;
        Trajectories.pitch = rotPitch;
        if (this.mc.thePlayer.getItemInUseDuration() > 20 && this.auto.getValueState().booleanValue()) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.fromAngle(this.mc.thePlayer.rotationYaw)));
            this.mc.thePlayer.stopUsingItem();
        }
    }

    private EntityLivingBase getClosestEntity() {
        EntityLivingBase closest = null;
        Iterator var3 = this.mc.theWorld.playerEntities.iterator();

        while(true) {
           Entity e;
           do {
              do {
                 do {
                    if (!var3.hasNext()) {
                       return closest;
                    }

                    e = (Entity)var3.next();
                 } while(!(e instanceof EntityLivingBase));
              } while(!this.isValid((EntityLivingBase)e));
           } while(FriendManager.isFriend(e.getName()) && !ModManager.getModByName("NoFriends").isEnabled());

           if (closest == null) {
              closest = (EntityLivingBase)e;
           } else if (this.mc.thePlayer.getDistanceToEntity(e) < this.mc.thePlayer.getDistanceToEntity(closest)) {
              closest = (EntityLivingBase)e;
           }
        }
     }

    private boolean isValid(EntityLivingBase entity) {
        if (entity.isDead) {
            return false;
        }
        if (entity == this.mc.thePlayer) {
            return false;
        }
        if (entity instanceof EntityAnimal && !this.attackAnimals.getValueState().booleanValue()) {
            return false;
        }
        if (entity instanceof EntityMob && !this.attackMobs.getValueState().booleanValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer && !this.attackPlayers.getValueState().booleanValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer && AntiBots.isBot((EntityPlayer)entity)) {
            return false;
        }
        if (entity instanceof EntityPlayer && ModManager.getModByName("Teams").isEnabled() && Teams.isEnemy((EntityPlayer)entity)) {
            return false;
        }
        if (entity.isInvisible()) {
            return false;
        }
        if (ClientUtil.isBlockBetween(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), new BlockPos(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ))) {
            return false;
        }
        return true;
    }
}

