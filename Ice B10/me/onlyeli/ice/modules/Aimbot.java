package me.onlyeli.ice.modules;

import java.util.List;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

public class Aimbot extends Module {
	public Aimbot() {
		super("Aimbot", Keyboard.KEY_B, Category.COMBAT);
	}

	public void onUpdate() { 
		if (!this.isToggled()) {
			return;
		}

		List list = Wrapper.mc.theWorld.playerEntities;

		for (int k = 0; k < list.size(); k++) {
			if (((EntityPlayer) list.get(k)).getName() == Wrapper.mc.thePlayer.getName()) {
				continue;
			}

			EntityPlayer entityplayer = (EntityPlayer) list.get(1);

			if (Wrapper.mc.thePlayer.getDistanceToEntity(entityplayer) > Wrapper.mc.thePlayer.getDistanceToEntity((Entity) list.get(k))) {
				entityplayer = (EntityPlayer) list.get(k);
			}

			float f = Wrapper.mc.thePlayer.getDistanceToEntity(entityplayer);

			if (f < 8F && Wrapper.mc.thePlayer.canEntityBeSeen(entityplayer)) {
				this.faceEntity(entityplayer);
			}
		}
	}

	public static synchronized void faceEntity(EntityLivingBase entity) {
		final float[] rotations = getRotationsNeeded(entity);

		if (rotations != null) {
			Wrapper.mc.thePlayer.rotationYaw = rotations[0];
			Wrapper.mc.thePlayer.rotationPitch = rotations[1] + 1.0F;// 14
		}
	}

	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) {
			return null;
		}

		final double diffX = entity.posX - Wrapper.mc.thePlayer.posX;
		final double diffZ = entity.posZ - Wrapper.mc.thePlayer.posZ;
		double diffY;

		if (entity instanceof EntityLivingBase) {
			final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight());
		}

		final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { Wrapper.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Wrapper.mc.thePlayer.rotationYaw), Wrapper.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Wrapper.mc.thePlayer.rotationPitch) };
	}
}
