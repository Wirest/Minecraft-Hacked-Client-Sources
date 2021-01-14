package store.shadowclient.client.module.combat;

import java.util.List;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class Aimbot extends Module{
	public Aimbot() {
		super("Aimbot", 0, Category.COMBAT);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
	List list = mc.theWorld.playerEntities;

	for (int k = 0; k < list.size(); k++) {
		if (((EntityPlayer) list.get(k)).getName() == mc.thePlayer.getName()) {
			continue;
		}

		EntityPlayer entityplayer = (EntityPlayer) list.get(1);

		if (mc.thePlayer.getDistanceToEntity(entityplayer) > mc.thePlayer.getDistanceToEntity((Entity) list.get(k))) {
			entityplayer = (EntityPlayer) list.get(k);
		}

		float f = mc.thePlayer.getDistanceToEntity(entityplayer);

		if (f < 8F && mc.thePlayer.canEntityBeSeen(entityplayer)) {
			this.faceEntity(entityplayer);
		}
	}
}

public static synchronized void faceEntity(EntityLivingBase entity) {
	final float[] rotations = getRotationsNeeded(entity);

	if (rotations != null) {
		mc.thePlayer.rotationYaw = rotations[0];
		mc.thePlayer.rotationPitch = rotations[1] + 1.0F;// 14
	}
}

public static float[] getRotationsNeeded(Entity entity) {
	if (entity == null) {
		return null;
	}

	final double diffX = entity.posX - mc.thePlayer.posX;
	final double diffZ = entity.posZ - mc.thePlayer.posZ;
	double diffY;

	if (entity instanceof EntityLivingBase) {
		final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
		diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
	} else {
		diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
	}

	final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
	}

@Override
public void onEnable() {
	super.onEnable();
}

@Override
public void onDisable() {
	super.onDisable();
}
}
