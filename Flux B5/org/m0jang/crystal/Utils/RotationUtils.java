package org.m0jang.crystal.Utils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.m0jang.crystal.Wrapper;

public class RotationUtils {
   public static double[] interpolate(Entity entity) {
      double partialTicks = (double)Minecraft.getMinecraft().timer.renderPartialTicks;
      double[] pos = new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
      return pos;
   }

   public static boolean isWithingFOV(Entity en, float angle) {
      return false;
   }

   public static float[] getRotationsForAura(Entity entity, double maxRange) {
      if (entity == null) {
         System.out.println("Fuck you ! Entity is nul!");
         return null;
      } else {
         double diffX = entity.posX - Wrapper.getPlayer().posX;
         double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
         Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
         Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Wrapper.getPlayer().getEyeHeight(), Minecraft.thePlayer.posZ);

         double diffY;
         for(diffY = entity.boundingBox.minY + 0.7D; diffY < entity.boundingBox.maxY - 0.1D; diffY += 0.1D) {
            if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) < myEyePos.distanceTo(BestPos)) {
               BestPos = new Location(entity.posX, diffY, entity.posZ);
            }
         }

         if (myEyePos.distanceTo(BestPos) > maxRange) {
            return null;
         } else {
            diffY = BestPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
            double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            return new float[]{yaw, pitch};
         }
      }
   }

   public static float[] getRotations(Entity entity) {
      if (entity == null) {
         return null;
      } else {
         double diffX = entity.posX - Wrapper.getPlayer().posX;
         double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
         double diffY;
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + (double)elb.getEyeHeight() - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
         } else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
         }

         double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         return new float[]{yaw, pitch};
      }
   }

   public static float[] getRotations(EntityLivingBase ent, double Ix, double Iy, double Iz) {
      double x = ent.posX;
      double z = ent.posZ;
      double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
      double xDiff = x - Ix;
      double zDiff = z - Iz;
      double yDiff = y - Iy - 0.6D;
      double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   public static float[] getAverageRotations(List targetList) {
      double posX = 0.0D;
      double posY = 0.0D;
      double posZ = 0.0D;

      Entity ent;
      for(Iterator var8 = targetList.iterator(); var8.hasNext(); posZ += ent.posZ) {
         ent = (Entity)var8.next();
         posX += ent.posX;
         posY += ent.boundingBox.maxY - 2.0D;
      }

      posX /= (double)targetList.size();
      posY /= (double)targetList.size();
      posZ /= (double)targetList.size();
      return new float[]{getRotationFromPosition(posX, posY, posZ)[0], getRotationFromPosition(posX, posY, posZ)[1]};
   }

   public static float[] getRotationFromPosition(double x, double y, double z) {
      Minecraft.getMinecraft();
      double xDiff = x - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double zDiff = z - Minecraft.thePlayer.posZ;
      Minecraft.getMinecraft();
      double yDiff = y - Minecraft.thePlayer.posY - 1.3D;
      double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
      float g = 0.006F;
      float sqrt = velocity * velocity * velocity * velocity - 0.006F * (0.006F * d3 * d3 + 2.0F * d1 * velocity * velocity);
      return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)sqrt)) / (double)(0.006F * d3)));
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

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
      float angle3 = Math.abs(angle1 - angle2) % 360.0F;
      if (angle3 > 180.0F) {
         angle3 = 360.0F - angle3;
      }

      return angle3;
   }
}
