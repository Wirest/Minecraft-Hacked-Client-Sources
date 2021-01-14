package de.iotacb.cu.core.mc.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {

	public static final RotationUtil INSTANCE = new RotationUtil();
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	/**
	 * Will return rotations to look at the given entity
	 * Index 0: Yaw
	 * Index 1: Pitch
	 * @param entity
	 * @return
	 */
	public final float[] getRotations(Entity entity) {
		final Vec3 playerPos = new Vec3(MC.thePlayer.posX, MC.thePlayer.posY + (entity instanceof EntityLivingBase ? MC.thePlayer.getEyeHeight() : 0), MC.thePlayer.posZ);
		final Vec3 entityPos = new Vec3(entity.posX, entity.posY, entity.posZ);
		
		final double diffX = entityPos.xCoord - playerPos.xCoord;
		final double diffY = (entity instanceof EntityLivingBase ? entityPos.yCoord + ((EntityLivingBase) entity).getEyeHeight() - playerPos.yCoord : entityPos.yCoord - playerPos.yCoord);
		final double diffZ = entityPos.zCoord - playerPos.zCoord;
		
		final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
		
		double yaw = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
		double pitch = -Math.toDegrees(Math.atan2(diffY, dist));
		
        yaw = MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(yaw - MC.thePlayer.rotationYaw);
        pitch = MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(pitch - MC.thePlayer.rotationPitch);
		
		return new float[] {(float) yaw, (float) pitch};
	}
	
	/**
	 * Will return rotations to look at the given position
	 * Index 0: Yaw
	 * Index 1: Pitch
	 * @param pos
	 * @return
	 */
    public final float[] getRotations(Vec3 pos) {
    	final Vec3 playerPos = new Vec3(MC.thePlayer.posX , MC.thePlayer.posY, MC.thePlayer.posZ);
		
    	final double diffX = pos.xCoord + 0.5 - playerPos.xCoord;
    	final double diffY = pos.yCoord + 0.5 - (playerPos.yCoord + MC.thePlayer.getEyeHeight());
    	final double diffZ = pos.zCoord + 0.5 - playerPos.zCoord;
        
    	final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
    	
        double yaw = Math.toDegrees (Math.atan2(diffZ, diffX)) - 90.0F;
        double pitch = -Math.toDegrees(Math.atan2(diffY, dist));
        
        yaw = MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(yaw - MC.thePlayer.rotationYaw);
        pitch = MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(pitch - MC.thePlayer.rotationPitch);
        return new float[] { (float) yaw, (float) pitch };
    }
    
    /**
     * Author: Mojang
     * Returns a vector based on yaw and rotation
     * @param yaw
     * @param pitch
     * @return
     */
    public final Vec3 getVectorForRotation(float yaw, float pitch)
    {
    	final double f = Math.cos(Math.toRadians(-yaw) - Math.PI);
    	final double f1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
    	final double f2 = -Math.cos(Math.toRadians(-pitch));
    	final double f3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
	
}
