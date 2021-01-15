package nivia.utils.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import nivia.utils.Helper;

import java.util.List;
import java.util.Random;

public class CombatUtils {

	public static double angleDifference(double a, double b) {
		return ((((a - b) % 360D) + 540D) % 360D) - 180D;
	}

	public static float[] faceTarget(Entity target, float p_70625_2_, float p_70625_3_, boolean miss) {
		float yaw, pitch;
		double var4 = target.posX - Helper.player().posX;
		double var8 = target.posZ - Helper.player().posZ;
		double var6;
		if (target instanceof EntityLivingBase) {
			EntityLivingBase var10 = (EntityLivingBase) target;
			var6 = var10.posY + (double) var10.getEyeHeight()
					- (Helper.player().posY + (double) Helper.player().getEyeHeight());
		} else {
			var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D
					- (Helper.player().posY + (double) Helper.player().getEyeHeight());
		}
		Random rnd = new Random();
		double var14 = (double) MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float) (-(Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0), var14)
				* 180.0D / Math.PI));
		pitch = changeRotation(Helper.player().rotationPitch, var13, p_70625_3_);
		yaw = changeRotation(Helper.player().rotationYaw, var12, p_70625_2_);
		return new float[] { yaw, pitch };
	}
	public float[] getRotationsNeeded(Entity entity) {
	        if (entity == null) {
	            return null;
	        }
	        double diffX = entity.posX - Helper.player().posX;
	        double diffY;
	        if (entity instanceof EntityLivingBase) {
	            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (Helper.player().posY + Helper.player().getEyeHeight());
	        } else {
	            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Helper.player().posY + Helper.player().getEyeHeight());
	        }
	        double diffZ = entity.posZ - Helper.player().posZ;
	        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
	        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
	        return new float[] { Helper.player().rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Helper.player().rotationYaw), Helper.player().rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Helper.player().rotationPitch) };
	    }
	    
	    public void faceEntity(Entity cur, double speed2) {
	        float[] rotations = this.getRotationsNeeded(cur);
	        if (rotations != null) {
	            Helper.player().rotationYaw = (float)this.limitAngleChange(Helper.player().prevRotationYaw, rotations[0], speed2);
	        }
	    }
	    
	    public double limitAngleChange(double current, double intended, double speed2) {
	        double change = intended - current;
	        if (change > speed2) {
	            change = speed2;
	        } else if (change < -speed2) {
	            change = -speed2;
	        }
	        return current + change;
	    }
	public static Entity rayTrace(float partialTicks, double distance) {
		Entity pointedEntity = null;
		MovingObjectPosition rayTrace = null;

		if(Minecraft.getMinecraft().field_175622_Z != null) {
			if(Minecraft.getMinecraft().theWorld != null) {
				rayTrace = Minecraft.getMinecraft().field_175622_Z.func_174822_a(distance, partialTicks);
				Vec3 positionVec = Minecraft.getMinecraft().field_175622_Z.func_174824_e(partialTicks);
				double distanceToVec3 = distance;

				if(rayTrace != null) {
					distanceToVec3 = rayTrace.hitVec.distanceTo(positionVec);
				}

				Vec3 lookVec = Minecraft.getMinecraft().field_175622_Z.getLook(partialTicks);
				Vec3 posDistVec = positionVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
				Vec3 tempVec = null;
				double boxExpand = 1.0F;
				List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(Minecraft.getMinecraft().field_175622_Z, Minecraft.getMinecraft().field_175622_Z.boundingBox.addCoord(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance).expand(boxExpand, boxExpand, boxExpand));
				double vecInsideDist = distanceToVec3;

				for(int i = 0; i < entities.size(); i++){
					Entity entity = entities.get(i);


					double borderSize = entity.getCollisionBorderSize();
					AxisAlignedBB expEntityBox = entity.boundingBox.expand(borderSize, borderSize, borderSize);
					MovingObjectPosition calculateInterceptPos = expEntityBox.calculateIntercept(positionVec, posDistVec);

					if(expEntityBox.isVecInside(positionVec)){
						if(0.0D < vecInsideDist || vecInsideDist == 0.0D){
							pointedEntity = entity;
							tempVec = calculateInterceptPos == null ? positionVec : calculateInterceptPos.hitVec;
							vecInsideDist = 0.0D;
						}
					}else if(calculateInterceptPos != null){
						double calcInterceptPosDist = positionVec.distanceTo(calculateInterceptPos.hitVec);

						if(calcInterceptPosDist < vecInsideDist || vecInsideDist == 0.0D){
							if(entity == Minecraft.getMinecraft().field_175622_Z.ridingEntity){
								if(vecInsideDist == 0.0D){
									pointedEntity = entity;
									tempVec = calculateInterceptPos.hitVec;
								}
							}else{
								pointedEntity = entity;
								tempVec = calculateInterceptPos.hitVec;
								vecInsideDist = calcInterceptPosDist;
							}
						}
					}
				}

				if(pointedEntity != null && (vecInsideDist < distanceToVec3 || rayTrace == null)){
					return pointedEntity;
				}
			}
		}
		return null;
	}

	public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
		if (var4 > p_70663_3_)
			var4 = p_70663_3_;
		if (var4 < -p_70663_3_)
			var4 = -p_70663_3_;
		return p_70663_1_ + var4;
	}
}
