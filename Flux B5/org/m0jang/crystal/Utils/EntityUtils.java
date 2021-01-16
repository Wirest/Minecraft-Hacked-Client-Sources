package org.m0jang.crystal.Utils;

import java.util.Iterator;

import org.m0jang.crystal.Crystal;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class EntityUtils {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static double[] interpolate(Entity entity) {
      double partialTicks = (double)Minecraft.getMinecraft().timer.renderPartialTicks;
      double[] pos = new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
      return pos;
   }

   public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
      double curX = startPos[0];
      double curY = startPos[1];
      double curZ = startPos[2];
      double endX = (double)endPos.getX();
      double endY = (double)endPos.getY();
      double endZ = (double)endPos.getZ();
      double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

      for(int count = 0; distance > slack; ++count) {
         distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
         if (count > 120) {
            break;
         }

         boolean next = false;
         double diffX = curX - endX;
         double diffY = curY - endY;
         double diffZ = curZ - endZ;
         double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
         if (diffX < 0.0D) {
            if (Math.abs(diffX) > offset) {
               curX += offset;
            } else {
               curX += Math.abs(diffX);
            }
         }

         if (diffX > 0.0D) {
            if (Math.abs(diffX) > offset) {
               curX -= offset;
            } else {
               curX -= Math.abs(diffX);
            }
         }

         if (diffY < 0.0D) {
            if (Math.abs(diffY) > 0.25D) {
               curY += 0.25D;
            } else {
               curY += Math.abs(diffY);
            }
         }

         if (diffY > 0.0D) {
            if (Math.abs(diffY) > 0.25D) {
               curY -= 0.25D;
            } else {
               curY -= Math.abs(diffY);
            }
         }

         if (diffZ < 0.0D) {
            if (Math.abs(diffZ) > offset) {
               curZ += offset;
            } else {
               curZ += Math.abs(diffZ);
            }
         }

         if (diffZ > 0.0D) {
            if (Math.abs(diffZ) > offset) {
               curZ -= offset;
            } else {
               curZ -= Math.abs(diffZ);
            }
         }

         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
      }

   }

   public static float[] getEntityRotations(Entity target) {
      double var4 = target.posX - Minecraft.thePlayer.posX;
      double var5 = target.posZ - Minecraft.thePlayer.posZ;
      double var6 = target.posY + (double)target.getEyeHeight() / 1.3D - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var7 = (double)MathHelper.sqrt_double(var4 * var4 + var5 * var5);
      float yaw = (float)(Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(var6, var7) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   public static void damagePlayer(int damage) {
      if (damage < 1) {
         damage = 1;
      }

      Minecraft.getMinecraft();
      if (damage > MathHelper.floor_double((double)Minecraft.thePlayer.getMaxHealth())) {
         Minecraft.getMinecraft();
         damage = MathHelper.floor_double((double)Minecraft.thePlayer.getMaxHealth());
      }

      Minecraft.getMinecraft();
      if (Minecraft.thePlayer != null && Minecraft.getMinecraft().getNetHandler() != null) {
         Minecraft.getMinecraft();
         if (Minecraft.thePlayer.onGround) {
            for(int i = 0; (double)i <= (double)(3 + damage) / 0.0625D; ++i) {
               NetworkManager var10000 = Minecraft.thePlayer.sendQueue.getNetworkManager();
               Minecraft.getMinecraft();
               double var10003 = Minecraft.thePlayer.posX;
               Minecraft.getMinecraft();
               double var10004 = Minecraft.thePlayer.posY + 0.0625D;
               Minecraft.getMinecraft();
               var10000.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(var10003, var10004, Minecraft.thePlayer.posZ, false));
               var10000 = Minecraft.thePlayer.sendQueue.getNetworkManager();
               Minecraft.getMinecraft();
               var10003 = Minecraft.thePlayer.posX;
               Minecraft.getMinecraft();
               var10004 = Minecraft.thePlayer.posY;
               Minecraft.getMinecraft();
               var10000.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(var10003, var10004, Minecraft.thePlayer.posZ, (double)i == (double)(3 + damage) / 0.0625D));
            }
         }
      }

   }

   public static EntityLivingBase findClosestEntity() {
      double distance = Double.MAX_VALUE;
      EntityLivingBase entity = null;
      Iterator var4 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Object object = var4.next();
         if (object instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)object;
            if ((double)e.getDistanceToEntity(Minecraft.thePlayer) < distance && isValid(e)) {
               entity = e;
               distance = (double)e.getDistanceToEntity(Minecraft.thePlayer);
            }
         }
      }

      return entity;
   }

   public static final float limitAngleChange(float current, float intended, float maxChange) {
      float change = intended - current;
      if (change > maxChange) {
         change = maxChange;
      } else if (change < -maxChange) {
         change = -maxChange;
      }

      return current + change;
   }

   public static float[] getRotationsNeeded(Entity entity) {
      if (entity == null) {
         return null;
      } else {
         double var10000 = entity.posX;
         Minecraft.getMinecraft();
         double diffX = var10000 - Minecraft.thePlayer.posX;
         double var10001;
         double diffY;
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            var10000 = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() * 0.9D;
            Minecraft.getMinecraft();
            var10001 = Minecraft.thePlayer.posY;
            Minecraft.getMinecraft();
            diffY = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         } else {
            var10000 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D;
            Minecraft.getMinecraft();
            var10001 = Minecraft.thePlayer.posY;
            Minecraft.getMinecraft();
            diffY = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         }

         var10000 = entity.posZ;
         Minecraft.getMinecraft();
         double diffZ = var10000 - Minecraft.thePlayer.posZ;
         double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         float[] var12 = new float[2];
         Minecraft.getMinecraft();
         float var10003 = Minecraft.thePlayer.rotationYaw;
         Minecraft.getMinecraft();
         var12[0] = var10003 + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
         Minecraft.getMinecraft();
         var10003 = Minecraft.thePlayer.rotationPitch;
         Minecraft.getMinecraft();
         var12[1] = var10003 + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
         return var12;
      }
   }

   public static float[] getRotationsNeeded(Entity entity, float Iyaw, float Ipitch) {
      if (entity == null) {
         return null;
      } else {
         double var10000 = entity.posX;
         Minecraft.getMinecraft();
         double diffX = var10000 - Minecraft.thePlayer.posX;
         double var10001;
         double diffY;
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            var10000 = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() * 0.9D;
            Minecraft.getMinecraft();
            var10001 = Minecraft.thePlayer.posY;
            Minecraft.getMinecraft();
            diffY = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         } else {
            var10000 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D;
            Minecraft.getMinecraft();
            var10001 = Minecraft.thePlayer.posY;
            Minecraft.getMinecraft();
            diffY = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         }

         var10000 = entity.posZ;
         Minecraft.getMinecraft();
         double diffZ = var10000 - Minecraft.thePlayer.posZ;
         double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         return new float[]{Iyaw + MathHelper.wrapAngleTo180_float(yaw - Iyaw), Ipitch + MathHelper.wrapAngleTo180_float(pitch - Ipitch)};
      }
   }

   public static boolean isValid(EntityLivingBase entity) {
      return entity != null && entity != Minecraft.thePlayer && entity.getDistanceToEntity(Minecraft.thePlayer) <= 10000.0F && entity.isEntityAlive() && (!entity.isInvisible() || entity.getTotalArmorValue() != 0) && !Crystal.INSTANCE.friendManager.isFriend(entity.getName());
   }
}
