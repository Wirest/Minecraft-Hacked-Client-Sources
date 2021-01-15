/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RotationUtils
/*     */ {
/*     */   public static float[] getRotations(Entity ent)
/*     */   {
/*  14 */     double x = ent.posX;
/*  15 */     double z = ent.posZ;
/*  16 */     double y = ent.posY + ent.getEyeHeight() / 2.0F;
/*  17 */     return getRotationFromPosition(x, z, y);
/*     */   }
/*     */   
/*     */   public static float[] getAverageRotations(List targetList) {
/*  21 */     double posX = 0.0D;
/*  22 */     double posY = 0.0D;
/*  23 */     double posZ = 0.0D;
/*     */     
/*     */     Entity ent;
/*  26 */     for (java.util.Iterator var7 = targetList.iterator(); var7.hasNext(); posZ += ent.posZ) {
/*  27 */       ent = (Entity)var7.next();
/*  28 */       posX += ent.posX;
/*  29 */       posY += ent.boundingBox.maxY - 2.0D;
/*     */     }
/*     */     
/*  32 */     posX /= targetList.size();
/*  33 */     posY /= targetList.size();
/*  34 */     posZ /= targetList.size();
/*  35 */     return new float[] { getRotationFromPosition(posX, posZ, posY)[0], 
/*  36 */       getRotationFromPosition(posX, posZ, posY)[1] };
/*     */   }
/*     */   
/*     */   public static float[] getBlockRotations(Vec3 vec) {
/*  40 */     Entity temp = new net.minecraft.entity.projectile.EntitySnowball(Minecraft.getMinecraft().theWorld);
/*  41 */     Vec3 eyesPos = getEyesPos();
/*  42 */     temp.posX = (vec.xCoord - eyesPos.xCoord + 0.5D);
/*  43 */     temp.posY = (vec.yCoord - eyesPos.yCoord + 0.5D);
/*  44 */     temp.posZ = (vec.zCoord - eyesPos.zCoord + 0.5D);
/*  45 */     return getAngles(temp);
/*     */   }
/*     */   
/*     */   public static Vec3 getEyesPos()
/*     */   {
/*  50 */     Minecraft.getMinecraft();Minecraft.getMinecraft();Minecraft.getMinecraft();Minecraft.getMinecraft();return new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
/*     */   }
/*     */   
/*  53 */   public static float[] getAngles(Entity e) { return new float[] { getYawChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationYaw, 
/*  54 */       getPitchChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationPitch };
/*     */   }
/*     */   
/*     */   public static float getPitchChangeToEntity(Entity entity) {
/*  58 */     double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
/*  59 */     double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
/*  60 */     double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4D - Minecraft.getMinecraft().thePlayer.posY;
/*  61 */     double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
/*  62 */     double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
/*  63 */     return -
/*  64 */       MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity);
/*     */   }
/*     */   
/*     */   public static float getYawChangeToEntity(Entity entity) {
/*  68 */     double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
/*  69 */     double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
/*     */     double yawToEntity;
/*  71 */     double yawToEntity; if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
/*  72 */       yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)); } else { double yawToEntity;
/*  73 */       if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
/*  74 */         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
/*     */       } else
/*  76 */         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
/*     */     }
/*  78 */     return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity));
/*     */   }
/*     */   
/*     */   public static float[] getBowAngles(Entity entity) {
/*  82 */     double xDelta = entity.posX - entity.lastTickPosX;
/*  83 */     double zDelta = entity.posZ - entity.lastTickPosZ;
/*  84 */     double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
/*  85 */     d -= d % 0.8D;
/*  86 */     double xMulti = 1.0D;
/*  87 */     double zMulti = 1.0D;
/*  88 */     boolean sprint = entity.isSprinting();
/*  89 */     xMulti = d / 0.8D * xDelta * (sprint ? 1.25D : 1.0D);
/*  90 */     zMulti = d / 0.8D * zDelta * (sprint ? 1.25D : 1.0D);
/*  91 */     double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
/*  92 */     double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
/*  93 */     double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (
/*  94 */       entity.posY + entity.getEyeHeight());
/*  95 */     double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
/*  96 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/*  97 */     float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
/*  98 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static float[] getRotationFromPosition(double x, double z, double y) {
/* 102 */     double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
/* 103 */     double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
/* 104 */     double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2D;
/* 105 */     double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
/* 106 */     float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
/* 107 */     float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
/* 108 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
/* 112 */     float g = 0.006F;
/* 113 */     float sqrt = velocity * velocity * velocity * velocity - g * (g * d3 * d3 + 2.0F * d1 * velocity * velocity);
/* 114 */     return 
/* 115 */       (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
/*     */   }
/*     */   
/*     */   public static float getYawChange(double posX, double posZ) {
/* 119 */     double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
/* 120 */     double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
/*     */     double yawToEntity;
/* 122 */     double yawToEntity; if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
/* 123 */       yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)); } else { double yawToEntity;
/* 124 */       if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
/* 125 */         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
/*     */       } else {
/* 127 */         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
/*     */       }
/*     */     }
/* 130 */     return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity));
/*     */   }
/*     */   
/*     */   public static float getPitchChange(Entity entity, double posY) {
/* 134 */     double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
/* 135 */     double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
/* 136 */     double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
/* 137 */     double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
/* 138 */     double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
/* 139 */     return -
/* 140 */       MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity) - 2.5F;
/*     */   }
/*     */   
/*     */   public static float getNewAngle(float angle) {
/* 144 */     angle %= 360.0F;
/* 145 */     if (angle >= 180.0F) {
/* 146 */       angle -= 360.0F;
/*     */     }
/*     */     
/* 149 */     if (angle < -180.0F) {
/* 150 */       angle += 360.0F;
/*     */     }
/*     */     
/* 153 */     return angle;
/*     */   }
/*     */   
/*     */   public static float getDistanceBetweenAngles(float angle1, float angle2) {
/* 157 */     float angle = Math.abs(angle1 - angle2) % 360.0F;
/* 158 */     if (angle > 180.0F) {
/* 159 */       angle = 360.0F - angle;
/*     */     }
/*     */     
/* 162 */     return angle;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\RotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */