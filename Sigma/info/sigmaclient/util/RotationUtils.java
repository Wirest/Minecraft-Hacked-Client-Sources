package info.sigmaclient.util;

import info.sigmaclient.util.misc.ChatUtil;

import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
	static Minecraft mc = Minecraft.getMinecraft();
    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0D;
        double posY = 0.0D;
        double posZ = 0.0D;
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0D;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();

        return new float[]{getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1]};
    }
    public static float getStraitYaw(){
    	float YAW = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
    	if(YAW < 45 && YAW > -45){
			YAW = 0;
		}else if(YAW > 45 && YAW < 135){
			YAW = 90f;
		}else if(YAW > 135 || YAW < -135){
			YAW = 180;
		}else{
			YAW = -90f;
		}	 
    	return YAW;
    }
    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        final double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        final double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
		double d1 = MathHelper.sqrt_double(x * x + z * z);
        final float pitch =  (float) - (Math.atan2(y, d1) * 180.0D / Math.PI) + (float)dist*0.11f;
        
        return new float[]{yaw, -pitch};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006F;
        float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0F * d1 * (velocity * velocity));
        return (float) Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }

    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
        	if(deltaZ != 0)
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(yaw- (float) yawToEntity));
    }

    public static float getPitchChange(float pitch, Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
    }


    public static float getNewAngle(float angle) {
        angle %= 360.0F;
        if (angle >= 180.0F) {
            angle -= 360.0F;
        }
        if (angle < -180.0F) {
            angle += 360.0F;
        }
        return angle;
    }

    public static boolean canEntityBeSeen(Entity e){
    	Vec3 vec1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),mc.thePlayer.posZ);

    	AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight()/1.32F),e.posZ);	
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY) ;
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean see =  mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	    vec2 = new Vec3(maxx, maxy,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,minz);	
	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,maxz - 0.1);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx, maxy,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	
    	return false;
    }
    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0F;
        if (angle > 180.0F) {
            angle = 360.0F - angle;
        }
        return angle;
    }
}

