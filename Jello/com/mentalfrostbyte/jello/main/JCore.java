package com.mentalfrostbyte.jello.main;

import com.mentalfrostbyte.jello.modules.AirWalk;
import com.mentalfrostbyte.jello.modules.KillAura;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class JCore {

	public static Minecraft mc = Minecraft.getMinecraft();
	
	public EntityPlayerSP player(){
		return mc.thePlayer;
	}
	
	public boolean isValidHypixel(EntityLivingBase entity) {
        return entity != null && entity.isEntityAlive()  && entity != mc.thePlayer && (!entity.isInvisible()) && (entity.ticksExisted > 10) /*&& (!FriendManager.isFriend(entity.getName())) && (Team.isEnabled ? !(colorcode != "" && colorcode.equalsIgnoreCase(ecolorcode)):true*/;
    }
	public float normalise(double value, double start, double end)
	  {
	    double width = end - start;
	    double offsetValue = value - start;
	    
	    return (float)(offsetValue - Math.floor(offsetValue / width) * width + start);
	  }
	public void attack(EntityLivingBase entity) {
        boolean blocking = this.player().isBlocking();
        if (blocking) {
        //	this.player().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            //this.player().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.fromAngle(-255.0)));
        }
        this.player().swingItem();
        this.player().sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
       
        
        if (blocking) {
        //	mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            //this.player().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, this.player().inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
        }
        /*if (this.crack) {
            int i = 0;
            while (i < this.crackSize.getValue()) {
                this.p.onCriticalHit(entity);
                ++i;
            }
        }*/
    }
	public void legitAttack(EntityLivingBase entity) {
	mc.playerController.attackEntity(mc.thePlayer, entity);
	}
	/*public boolean isEntityInFov(EntityLivingBase entity, double angle) {
        double angleDifference = MathUtil.getAngleDifference(this.p.rotationYaw, AimUtil.getRotations(entity)[0]);
        if (!(angleDifference > 0.0 && angleDifference < angle || - (angle *= 0.5) < angleDifference && angleDifference < 0.0)) {
            return false;
        }
        return true;
    }*/
	public EntityLivingBase findClosestEntity() {
        double distance = Double.MAX_VALUE;
        EntityLivingBase entity = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            EntityLivingBase e;
            if (!(object instanceof EntityLivingBase) || (double)(e = (EntityLivingBase)object).getDistanceToEntity(this.player()) >= distance || !this.isValidHypixel(e)) continue;
            entity = e;
            distance = e.getDistanceToEntity(this.player());
        }
        return entity;
    }
	public double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }
	public boolean canCrit() {
        return !this.mc.thePlayer.isInWater() && this.mc.thePlayer.onGround;
    }
	public WorldClient world(){
		return mc.theWorld;
	}
	
}
