package nivia.utils.utils;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import nivia.managers.FriendManager;
import nivia.utils.Helper;

public class EntityUtils {
	
	public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.player().posX;
        double pY = Helper.player().posY + (Helper.player().getEyeHeight());
        double pZ = Helper.player().posZ;

        double eX = entity.posX;
        double eY = entity.posY + (entity.height/2);
        double eZ = entity.posZ;

        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2));

        double yaw = (Math.toDegrees(Math.atan2(dZ, dX)) + 90);
        double pitch = (Math.toDegrees(Math.atan2(dH, dY)));

        return new double[]{yaw, 90 - pitch};
    }
	public double getDistanceToFall(Entity e){
		double distance = 0;
		for(double i = e.posY; i > 0; i -= 0.1){
			if(i < 0)
				break;
			Block block = Helper.blockUtils().getBlock(new BlockPos(e.posX, i, e.posZ));
			if(block.getMaterial() != Material.air  && (block.isCollidable()) && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs || block instanceof BlockGlass || block instanceof BlockStainedGlass)){
				if(block instanceof BlockSlab)
					i -= 0.5;
				distance = i;
				break;
			}
		}
		return (e.posY - distance);
    }
	
	
	 public Entity findClosest(double r) {
		    Entity e = null;
		    for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
		      Entity ent = (Entity)o;
		      if (ent != Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent) <= r && ent instanceof EntityPlayer && !FriendManager.isFriend(ent.getName())) {
		        r = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent);
		        e = ent;
		      }
		    }
		    return e;
		  }
	 
	 
	 public Entity findClosestToCross(double range) {
		    Entity e = null;
		  
		 	double best = 360;
		 	 
		    for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
		      Entity ent = (Entity) o;
		      
		      if (!(ent instanceof EntityPlayer)) continue;
		      
		      	 	 double diffX = ent.posX - Minecraft.getMinecraft().thePlayer.posX;
			 	 double diffZ = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;
			 	 float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90);
			 	 double difference = Math.abs(Helper.combatUtils().angleDifference(newYaw, Minecraft.getMinecraft().thePlayer.rotationYaw));
			 	 
			 	  if (ent != Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent) <= range && ent instanceof EntityPlayer && !FriendManager.isFriend(ent.getName())) {
			 		 if (difference < best) {
				 		 best = difference;
				 		e = ent;
				 	 }
			      }
		    	}
		    return e;
		 }
	 
	 
	public static float[] getEntityRotations(Entity target) {
        final double var4 = target.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double var6 = target.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double var8 = target.posY + target.getEyeHeight() / 1.3 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
        final float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }
	
	public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal;
    }
	
    public static boolean isEntityWithinView(Entity entityLiving, float scope) {
  	  final double diffX = entityLiving.posX - Minecraft.getMinecraft().thePlayer.posX;
  	  final double diffZ = entityLiving.posZ - Minecraft.getMinecraft().thePlayer.posZ;
  	  final float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90);
  	  final double difference = Helper.combatUtils().angleDifference(newYaw, Minecraft.getMinecraft().thePlayer.rotationYaw);
  	  
  	  return difference <= scope;
  	}
    
    public static boolean isMonster(Entity entity) {
        return  entity instanceof IMob ||
                entity instanceof EntityDragon ||
                entity instanceof EntityGolem;
    }

    public static boolean isNeutral(Entity entity) {
        return  entity instanceof EntityBat ||
                entity instanceof EntitySquid ||
                entity instanceof EntityVillager;
    }
    public boolean isCloser(Entity now, Entity first, float error){
        if(first.getClass().isAssignableFrom(now.getClass()))
            return getDistanceToEntity(Helper.player(), now) < getDistanceToEntity(Helper.player(), first);     
        	return getDistanceToEntity(Helper.player(), now) < getDistanceToEntity(Helper.player(), first)+error;
    }
    public float getDistanceToEntity(Entity from, Entity to){
        return from.getDistanceToEntity(to);
    }

    public boolean isWithingFOV(Entity en, float angle) {
        angle *= 0.5;
        double angleDifference = Helper.combatUtils().angleDifference(Helper.player().rotationYaw , Helper.entityUtils().getRotationToEntity(en)[0]);
        return (angleDifference > 0 && angleDifference < angle) || (-angle < angleDifference && angleDifference < 0);
    }
}
