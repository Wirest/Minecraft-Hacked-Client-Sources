/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomPositionGenerator
/*     */ {
/*  15 */   private static Vec3 staticVector = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Vec3 findRandomTarget(EntityCreature entitycreatureIn, int xz, int y)
/*     */   {
/*  22 */     return findRandomTargetBlock(entitycreatureIn, xz, y, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Vec3 findRandomTargetBlockTowards(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
/*     */   {
/*  30 */     staticVector = targetVec3.subtract(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ);
/*  31 */     return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
/*     */   {
/*  39 */     staticVector = new Vec3(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ).subtract(targetVec3);
/*  40 */     return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Vec3 findRandomTargetBlock(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
/*     */   {
/*  49 */     Random random = entitycreatureIn.getRNG();
/*  50 */     boolean flag = false;
/*  51 */     int i = 0;
/*  52 */     int j = 0;
/*  53 */     int k = 0;
/*  54 */     float f = -99999.0F;
/*     */     boolean flag1;
/*     */     boolean flag1;
/*  57 */     if (entitycreatureIn.hasHome())
/*     */     {
/*  59 */       double d0 = entitycreatureIn.getHomePosition().distanceSq(MathHelper.floor_double(entitycreatureIn.posX), MathHelper.floor_double(entitycreatureIn.posY), MathHelper.floor_double(entitycreatureIn.posZ)) + 4.0D;
/*  60 */       double d1 = entitycreatureIn.getMaximumHomeDistance() + xz;
/*  61 */       flag1 = d0 < d1 * d1;
/*     */     }
/*     */     else
/*     */     {
/*  65 */       flag1 = false;
/*     */     }
/*     */     
/*  68 */     for (int j1 = 0; j1 < 10; j1++)
/*     */     {
/*  70 */       int l = random.nextInt(2 * xz + 1) - xz;
/*  71 */       int k1 = random.nextInt(2 * y + 1) - y;
/*  72 */       int i1 = random.nextInt(2 * xz + 1) - xz;
/*     */       
/*  74 */       if ((targetVec3 == null) || (l * targetVec3.xCoord + i1 * targetVec3.zCoord >= 0.0D))
/*     */       {
/*  76 */         if ((entitycreatureIn.hasHome()) && (xz > 1))
/*     */         {
/*  78 */           BlockPos blockpos = entitycreatureIn.getHomePosition();
/*     */           
/*  80 */           if (entitycreatureIn.posX > blockpos.getX())
/*     */           {
/*  82 */             l -= random.nextInt(xz / 2);
/*     */           }
/*     */           else
/*     */           {
/*  86 */             l += random.nextInt(xz / 2);
/*     */           }
/*     */           
/*  89 */           if (entitycreatureIn.posZ > blockpos.getZ())
/*     */           {
/*  91 */             i1 -= random.nextInt(xz / 2);
/*     */           }
/*     */           else
/*     */           {
/*  95 */             i1 += random.nextInt(xz / 2);
/*     */           }
/*     */         }
/*     */         
/*  99 */         l += MathHelper.floor_double(entitycreatureIn.posX);
/* 100 */         k1 += MathHelper.floor_double(entitycreatureIn.posY);
/* 101 */         i1 += MathHelper.floor_double(entitycreatureIn.posZ);
/* 102 */         BlockPos blockpos1 = new BlockPos(l, k1, i1);
/*     */         
/* 104 */         if ((!flag1) || (entitycreatureIn.isWithinHomeDistanceFromPosition(blockpos1)))
/*     */         {
/* 106 */           float f1 = entitycreatureIn.getBlockPathWeight(blockpos1);
/*     */           
/* 108 */           if (f1 > f)
/*     */           {
/* 110 */             f = f1;
/* 111 */             i = l;
/* 112 */             j = k1;
/* 113 */             k = i1;
/* 114 */             flag = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 120 */     if (flag)
/*     */     {
/* 122 */       return new Vec3(i, j, k);
/*     */     }
/*     */     
/*     */ 
/* 126 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\RandomPositionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */