package me.xatzdevelopments.xatz.client.modules.scaffoldevents;


import me.xatzdevelopments.xatz.client.main.Xatz;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public float serverYaw, serverPitch;
	
	public final float[] getRotations(Entity entity, boolean predict, double predictionFactor) {
		final Vec3 playerPos = new Vec3(MC.thePlayer.posX + (predict ? MC.thePlayer.motionX * predictionFactor : 0), MC.thePlayer.posY + (entity instanceof EntityLivingBase ? MC.thePlayer.getEyeHeight() : 0) + (predict ? MC.thePlayer.motionY * predictionFactor : 0), MC.thePlayer.posZ + (predict ? MC.thePlayer.motionZ * predictionFactor : 0));
		final Vec3 entityPos = new Vec3(entity.posX + (predict ? (entity.posX - entity.prevPosX) * predictionFactor : 0), entity.posY + (predict ? (entity.posY - entity.prevPosY) * predictionFactor : 0), entity.posZ + (predict ? (entity.posZ - entity.prevPosZ) * predictionFactor : 0));
		
		final double diffX = entityPos.xCoord - playerPos.xCoord;
		final double diffY = (entity instanceof EntityLivingBase ? entityPos.yCoord + ((EntityLivingBase) entity).getEyeHeight() - playerPos.yCoord : entityPos.yCoord - playerPos.yCoord);
		final double diffZ = entityPos.zCoord - playerPos.zCoord;
		
		final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
		
		final double yaw = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
		final double pitch = -Math.toDegrees(Math.atan2(diffY, dist));
		
		return new float[] {(float) yaw, (float) pitch};
	}
	
    public final float[] getRotations(Vec3 pos, boolean predict, double predictionFactor) {
    	final Vec3 playerPos = new Vec3(MC.thePlayer.posX + (predict ? MC.thePlayer.motionX * predictionFactor : 0), MC.thePlayer.posY+ (predict ? MC.thePlayer.motionY * predictionFactor : 0), MC.thePlayer.posZ + (predict ? MC.thePlayer.motionZ * predictionFactor : 0));
		
    	final double diffX = pos.xCoord + 0.5 - playerPos.xCoord;
    	final double diffY = pos.yCoord + 0.5 - (playerPos.yCoord + MC.thePlayer.getEyeHeight());
    	final double diffZ = pos.zCoord + 0.5 - playerPos.zCoord;
        
    	final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        double yaw = Math.toDegrees (Math.atan2(diffZ, diffX)) - 90.0f;
        double pitch = -Math.toDegrees(Math.atan2(diffY, dist));
        yaw = MC.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(yaw - MC.thePlayer.rotationYaw);
        pitch = MC.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(pitch - MC.thePlayer.rotationPitch);
        return new float[] { (float) yaw, (float) pitch };
    }
    
    public final Vec3 getVectorForRotation(float yaw, float pitch)
    {
    	final double f = Math.cos(Math.toRadians(-yaw) - Math.PI);
    	final double f1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
    	final double f2 = -Math.cos(Math.toRadians(-pitch));
    	final double f3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
    
    public final float getDifference(float a, float b) {
        float r = (float) ((a - b) % 360.0);
        
        if (r < -180.0) {
        	r += 360.0;
        }
        
        if (r >= 180.0) {
        	r -= 360.0;
        }
        
        return r;
    }
    
    public final double getRotationDifference(float[] clientRotations, float[] serverRotations) {
    	return Math.hypot(getDifference(clientRotations[0], serverRotations[0]), clientRotations[1] - serverRotations[1]);
    }
    
    public final double getRotationDifference(Entity entity) {
    	final float[] rotations = getRotations(entity, false, 1);
    	return getRotationDifference(rotations, new float[] {MC.thePlayer.rotationYaw, MC.thePlayer.rotationPitch});
    }
    
    public final float[] smoothRotation(float[] currentRotations, float[] neededRotations, float rotationSpeed) {
    	final float yawDiff = getDifference(neededRotations[0], currentRotations[0]);
    	final float pitchDiff = getDifference(neededRotations[1], currentRotations[1]);
    	
    	float rotationSpeedYaw = rotationSpeed;
    	
    	if (yawDiff > rotationSpeed) {
    		rotationSpeedYaw = rotationSpeed;
    	} else {
    		rotationSpeedYaw = Math.max(yawDiff, -rotationSpeed);
    	}
    	
    	float rotationSpeedPitch = rotationSpeed;
    	
    	if (pitchDiff > rotationSpeed) {
    		rotationSpeedPitch = rotationSpeed;
    	} else {
    		rotationSpeedPitch = Math.max(pitchDiff, -rotationSpeed);
    	}
    	
    	final float newYaw = currentRotations[0] + rotationSpeedYaw;
    	final float newPitch = currentRotations[1] + rotationSpeedPitch;
    	
    	return new float[] { newYaw, newPitch };
    }
    
    public final boolean isFaced(double range) {
    	return Xatz.RAYCAST_UTIL.raycastEntity(range, new float[] {serverYaw, serverPitch}) != null;
    }
    
    public final void setRotations(float yaw, float pitch) {
    	serverYaw = yaw;
    	serverPitch = pitch;
    }
    
    public final void setRotations(float[] rotations) {
    	setRotations(rotations[0], rotations[1]);
    }
    
    public final float[] getServerRotations() {
    	return new float[] {serverYaw, serverPitch};
    }
    
}
