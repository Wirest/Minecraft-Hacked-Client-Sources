/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.material.MaterialTransparent;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import rip.jutting.polaris.friend.FriendManager;
/*     */ 
/*     */ public class EntityUtils
/*     */ {
/*     */   public static boolean lookChanged;
/*     */   public static float yaw;
/*     */   public static float pitch;
/*  24 */   public static Minecraft mc = ;
/*     */   
/*     */   public static synchronized void faceEntityClient(EntityLivingBase entity)
/*     */   {
/*  28 */     float[] rotations = getRotationsNeeded(entity);
/*  29 */     if (rotations != null)
/*     */     {
/*  31 */       Minecraft.getMinecraft().thePlayer.rotationYaw = limitAngleChange(Minecraft.getMinecraft().thePlayer.prevRotationYaw, rotations[0], 55.0F);
/*  32 */       Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
/*     */     }
/*     */   }
/*     */   
/*     */   public static void attackEntity(EntityLivingBase entity)
/*     */   {
/*  38 */     Minecraft.getMinecraft().thePlayer.swingItem();
/*     */     
/*  40 */     Minecraft.getMinecraft().playerController.attackEntity(mc.thePlayer, entity);
/*     */   }
/*     */   
/*     */   public static void doCritical()
/*     */   {
/*  45 */     if ((!Minecraft.getMinecraft().thePlayer.isInWater()) && (!Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.lava)) && (Minecraft.getMinecraft().thePlayer.onGround))
/*     */     {
/*  47 */       Minecraft.getMinecraft().thePlayer.motionY = 0.10000000149011612D;
/*  48 */       Minecraft.getMinecraft().thePlayer.fallDistance = 0.1F;
/*  49 */       Minecraft.getMinecraft().thePlayer.onGround = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized float getDistanceToEntity(EntityLivingBase entity)
/*     */   {
/*  55 */     return mc.thePlayer.getDistanceToEntity(entity);
/*     */   }
/*     */   
/*     */   public static synchronized void faceEntityPacket(EntityLivingBase entity)
/*     */   {
/*  60 */     float[] rotations = getRotationsNeeded(entity);
/*  61 */     if (rotations != null)
/*     */     {
/*  63 */       yaw = limitAngleChange(Minecraft.getMinecraft().thePlayer.prevRotationYaw, rotations[0], 55.0F);
/*  64 */       pitch = rotations[1];
/*  65 */       lookChanged = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static float[] getRotationsNeeded(Entity entity)
/*     */   {
/*  71 */     if (entity == null) {
/*  72 */       return null;
/*     */     }
/*  74 */     double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
/*     */     double diffY;
/*     */     double diffY;
/*  77 */     if ((entity instanceof EntityLivingBase))
/*     */     {
/*  79 */       EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
/*  80 */       diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
/*     */     }
/*     */     else
/*     */     {
/*  84 */       diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
/*     */     }
/*  86 */     double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
/*  87 */     double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
/*  88 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
/*  89 */     float pitch = (float)(-Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
/*  90 */     return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
/*     */   }
/*     */   
/*     */   private static final float limitAngleChange(float current, float intended, float maxChange)
/*     */   {
/*  95 */     float change = intended - current;
/*  96 */     if (change > maxChange) {
/*  97 */       change = maxChange;
/*  98 */     } else if (change < -maxChange) {
/*  99 */       change = -maxChange;
/*     */     }
/* 101 */     return current + change;
/*     */   }
/*     */   
/*     */   public static ArrayList<BlockPos> func1(Entity en) {
/* 105 */     BlockPos pos1 = new BlockPos(en.boundingBox.minX, en.boundingBox.minY - 0.01D, en.boundingBox.minZ);
/* 106 */     BlockPos pos2 = new BlockPos(en.boundingBox.maxX, en.boundingBox.minY - 0.01D, en.boundingBox.maxZ);
/* 107 */     Iterable<BlockPos> collisionBlocks = BlockPos.getAllInBoxMutable(pos1, pos2);
/* 108 */     ArrayList<BlockPos> returnList = new ArrayList();
/* 109 */     for (BlockPos pos3 : collisionBlocks) {
/* 110 */       returnList.add(pos3);
/*     */     }
/* 112 */     return returnList;
/*     */   }
/*     */   
/*     */   public static boolean func2(Entity en) {
/* 116 */     ArrayList<BlockPos> poses = func1(en);
/* 117 */     for (BlockPos pos : poses) {
/* 118 */       Block block = mc.theWorld.getBlockState(pos).getBlock();
/* 119 */       if ((!(block.getMaterial() instanceof MaterialTransparent)) && (block.getMaterial() != Material.air) && (!(block instanceof BlockLiquid)) && (block.isFullCube())) {
/* 120 */         return true;
/*     */       }
/*     */     }
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   public static int getDistanceFromMouse(Entity entity)
/*     */   {
/* 128 */     float[] neededRotations = getRotationsNeeded(entity);
/* 129 */     if (neededRotations != null)
/*     */     {
/* 131 */       float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - neededRotations[0];
/* 132 */       float neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch - neededRotations[1];
/* 133 */       float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
/* 134 */       return (int)distanceFromMouse;
/*     */     }
/* 136 */     return -1;
/*     */   }
/*     */   
/*     */   private static boolean checkName(String name)
/*     */   {
/* 141 */     String[] colors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
/* 142 */     boolean unknownColor = true;
/* 143 */     int i2 = 0;
/* 144 */     while (i2 < 16)
/*     */     {
/* 146 */       if (name.contains("?" + colors[i2])) {
/* 147 */         return true;
/*     */       }
/* 149 */       unknownColor = false;
/* 150 */       i2++;
/*     */     }
/* 152 */     if ((!unknownColor) && (name.contains("?"))) {
/* 153 */       return false;
/*     */     }
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase getClosestEntity(boolean ignoreFriends, boolean useFOV)
/*     */   {
/* 160 */     EntityLivingBase closestEntity = null;
/* 161 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
/* 162 */       if (((o instanceof EntityLivingBase)) && 
/* 163 */         (getDistanceFromMouse((Entity)o) <= 180))
/*     */       {
/* 165 */         EntityLivingBase en = (EntityLivingBase)o;
/* 166 */         if ((!(o instanceof EntityPlayerSP)) && (!FriendManager.isFriend(en.getName())) && (!en.isDead) && (en.getHealth() > 0.0F) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)) && (!en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) && ((closestEntity == null) || (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEntity)))) {
/* 167 */           closestEntity = en;
/*     */         }
/*     */       }
/*     */     }
/* 171 */     return closestEntity;
/*     */   }
/*     */   
/*     */   public static ArrayList<EntityLivingBase> getCloseEntities(float range)
/*     */   {
/* 176 */     ArrayList<EntityLivingBase> closeEntities = new ArrayList();
/* 177 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 179 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 180 */       if ((!(o instanceof EntityPlayerSP)) && (!en2.isDead) && (en2.getHealth() > 0.0F) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en2)) && (!en2.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) && (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en2) <= range)) {
/* 181 */         closeEntities.add(en2);
/*     */       }
/*     */     }
/* 184 */     return closeEntities;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase getClosestEntityRaw()
/*     */   {
/* 189 */     EntityLivingBase closestEntity = null;
/* 190 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 192 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 193 */       if ((!(o instanceof EntityPlayerSP)) && (!en2.isDead) && (en2.getHealth() > 0.0F) && ((closestEntity == null) || (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en2) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEntity)))) {
/* 194 */         closestEntity = en2;
/*     */       }
/*     */     }
/* 197 */     return closestEntity;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase getClosestEnemy(EntityLivingBase friend)
/*     */   {
/* 202 */     EntityLivingBase closestEnemy = null;
/* 203 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 205 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 206 */       if ((!(o instanceof EntityPlayerSP)) && (o != friend) && (!en2.isDead) && (en2.getHealth() > 0.0F) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en2)) && ((closestEnemy == null) || (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en2) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEnemy)))) {
/* 207 */         closestEnemy = en2;
/*     */       }
/*     */     }
/* 210 */     return closestEnemy;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase searchEntityByIdRaw(UUID ID)
/*     */   {
/* 215 */     EntityLivingBase newEntity = null;
/* 216 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 218 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 219 */       if ((!(o instanceof EntityPlayerSP)) && (!en2.isDead) && (newEntity == null) && (en2.getUniqueID().equals(ID))) {
/* 220 */         newEntity = en2;
/*     */       }
/*     */     }
/* 223 */     return newEntity;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase searchEntityByName(String name)
/*     */   {
/* 228 */     EntityLivingBase newEntity = null;
/* 229 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 231 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 232 */       if ((!(o instanceof EntityPlayerSP)) && (!en2.isDead) && (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en2)) && (newEntity == null) && (en2.getName().equals(name))) {
/* 233 */         newEntity = en2;
/*     */       }
/*     */     }
/* 236 */     return newEntity;
/*     */   }
/*     */   
/*     */   public static EntityLivingBase searchEntityByNameRaw(String name)
/*     */   {
/* 241 */     EntityLivingBase newEntity = null;
/* 242 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
/*     */     {
/* 244 */       EntityLivingBase en2 = (EntityLivingBase)o;
/* 245 */       if ((!(o instanceof EntityPlayerSP)) && (!en2.isDead) && (newEntity == null) && (en2.getName().equals(name))) {
/* 246 */         newEntity = en2;
/*     */       }
/*     */     }
/* 249 */     return newEntity;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\EntityUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */