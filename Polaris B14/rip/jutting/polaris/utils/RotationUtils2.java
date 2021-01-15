/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RotationUtils2
/*     */ {
/*     */   private static Minecraft mc;
/*     */   
/*     */   public static float[] getRotations(Entity ent)
/*     */   {
/*  18 */     double x = ent.posX;
/*  19 */     double z = ent.posZ;
/*  20 */     double y = ent.boundingBox.maxY - 4.0D;
/*  21 */     return getRotationFromPosition(x, z, y);
/*     */   }
/*     */   
/*     */   public static Vec3 getClientLookVec() {
/*  25 */     float f = (float)Math.cos(-Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292F - 3.1415927F);
/*  26 */     float f1 = (float)Math.sin(-Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292F - 3.1415927F);
/*  27 */     float f2 = (float)-Math.cos(-Minecraft.getMinecraft().thePlayer.rotationPitch * 0.017453292F);
/*  28 */     float f3 = (float)Math.sin(-Minecraft.getMinecraft().thePlayer.rotationPitch * 0.017453292F);
/*  29 */     return new Vec3(f1 * f2, f3, f * f2);
/*     */   }
/*     */   
/*     */   public static float[] getNeededRotations(Vec3 vec) {
/*  33 */     Vec3 eyesPos = getEyesPos();
/*  34 */     double diffX = vec.xCoord - eyesPos.xCoord + 0.5D;
/*  35 */     double diffY = vec.yCoord - eyesPos.yCoord + 0.5D;
/*  36 */     double diffZ = vec.zCoord - eyesPos.zCoord + 0.5D;
/*  37 */     double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
/*  38 */     float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
/*  39 */     float pitch = (float)-(Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D);
/*  40 */     return new float[] { MathHelper.wrapAngleTo180_float(yaw), 
/*  41 */       Minecraft.getMinecraft().gameSettings.keyBindJump.pressed ? 90.0F : 
/*  42 */       MathHelper.wrapAngleTo180_float(pitch) };
/*     */   }
/*     */   
/*     */   public static void faceVectorPacketInstant(Vec3 vec) {
/*  46 */     float[] rotations = getNeededRotations(vec);
/*  47 */     Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook(
/*  48 */       rotations[0], rotations[1], Minecraft.getMinecraft().thePlayer.onGround));
/*     */   }
/*     */   
/*     */   public static Vec3 getEyesPos() {
/*  52 */     return new Vec3(Minecraft.getMinecraft().thePlayer.posX, 
/*  53 */       Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 
/*  54 */       Minecraft.getMinecraft().thePlayer.posZ);
/*     */   }
/*     */   
/*     */   public static float[] getAverageRotations(java.util.List<EntityLivingBase> targetList) {
/*  58 */     double posX = 0.0D;
/*  59 */     double posY = 0.0D;
/*  60 */     double posZ = 0.0D;
/*  61 */     for (Entity ent : targetList) {
/*  62 */       posX += ent.posX;
/*  63 */       posY += ent.boundingBox.maxY - 2.0D;
/*  64 */       posZ += ent.posZ;
/*     */     }
/*  66 */     return new float[] {
/*  67 */       getRotationFromPosition(posX /= targetList.size(), 
/*  68 */       posZ /= targetList.size(), posY /= targetList.size())[0], 
/*  69 */       getRotationFromPosition(posX, posZ, posY)[1] };
/*     */   }
/*     */   
/*     */   public static float[] getRotationFromPosition(double x, double z, double y) {
/*  73 */     double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
/*  74 */     double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
/*  75 */     double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + 
/*  76 */       Minecraft.getMinecraft().thePlayer.getEyeHeight();
/*  77 */     double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
/*  78 */     float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
/*  79 */     float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
/*  80 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
/*  84 */     float g = 0.006F;
/*  85 */     float sqrt = velocity * velocity * velocity * velocity - 
/*  86 */       g * (g * (d3 * d3) + 2.0F * d1 * (velocity * velocity));
/*  87 */     return 
/*  88 */       (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
/*     */   }
/*     */   
/*     */   public static float getNewAngle(float angle) {
/*  92 */     if (angle %= 360.0F >= 180.0F) {
/*  93 */       angle -= 360.0F;
/*     */     }
/*  95 */     if (angle < -180.0F) {
/*  96 */       angle += 360.0F;
/*     */     }
/*  98 */     return angle;
/*     */   }
/*     */   
/*     */   public static float getDistanceBetweenAngles(float angle1, float angle2) {
/* 102 */     float angle = Math.abs(angle1 - angle2) % 360.0F;
/* 103 */     if (angle > 180.0F) {
/* 104 */       angle = 360.0F - angle;
/*     */     }
/* 106 */     return angle;
/*     */   }
/*     */   
/*     */   public static void jitterEffect(Random rand) {
/* 110 */     Minecraft mc1 = Minecraft.getMinecraft();
/* 111 */     if (rand.nextBoolean()) {
/* 112 */       mc1.thePlayer.rotationPitch = (rand.nextBoolean() ? 
/* 113 */         (float)(mc1.thePlayer.rotationPitch - rand.nextFloat() * 0.8D) : 
/* 114 */         (float)(mc1.thePlayer.rotationPitch + rand.nextFloat() * 0.8D));
/*     */     } else {
/* 116 */       mc1.thePlayer.rotationYaw = (rand.nextBoolean() ? 
/* 117 */         (float)(mc1.thePlayer.rotationYaw - rand.nextFloat() * 0.8D) : 
/* 118 */         (float)(mc1.thePlayer.rotationYaw + rand.nextFloat() * 0.8D));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\RotationUtils2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */