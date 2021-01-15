/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class EntityAIPlay extends EntityAIBase
/*     */ {
/*     */   private EntityVillager villagerObj;
/*     */   private net.minecraft.entity.EntityLivingBase targetVillager;
/*     */   private double speed;
/*     */   private int playTime;
/*     */   
/*     */   public EntityAIPlay(EntityVillager villagerObjIn, double speedIn)
/*     */   {
/*  17 */     this.villagerObj = villagerObjIn;
/*  18 */     this.speed = speedIn;
/*  19 */     setMutexBits(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  27 */     if (this.villagerObj.getGrowingAge() >= 0)
/*     */     {
/*  29 */       return false;
/*     */     }
/*  31 */     if (this.villagerObj.getRNG().nextInt(400) != 0)
/*     */     {
/*  33 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  37 */     java.util.List<EntityVillager> list = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
/*  38 */     double d0 = Double.MAX_VALUE;
/*     */     
/*  40 */     for (EntityVillager entityvillager : list)
/*     */     {
/*  42 */       if ((entityvillager != this.villagerObj) && (!entityvillager.isPlaying()) && (entityvillager.getGrowingAge() < 0))
/*     */       {
/*  44 */         double d1 = entityvillager.getDistanceSqToEntity(this.villagerObj);
/*     */         
/*  46 */         if (d1 <= d0)
/*     */         {
/*  48 */           d0 = d1;
/*  49 */           this.targetVillager = entityvillager;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  54 */     if (this.targetVillager == null)
/*     */     {
/*  56 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
/*     */       
/*  58 */       if (vec3 == null)
/*     */       {
/*  60 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  64 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  73 */     return this.playTime > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  81 */     if (this.targetVillager != null)
/*     */     {
/*  83 */       this.villagerObj.setPlaying(true);
/*     */     }
/*     */     
/*  86 */     this.playTime = 1000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  94 */     this.villagerObj.setPlaying(false);
/*  95 */     this.targetVillager = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 103 */     this.playTime -= 1;
/*     */     
/* 105 */     if (this.targetVillager != null)
/*     */     {
/* 107 */       if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0D)
/*     */       {
/* 109 */         this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
/*     */       }
/*     */     }
/* 112 */     else if (this.villagerObj.getNavigator().noPath())
/*     */     {
/* 114 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
/*     */       
/* 116 */       if (vec3 == null)
/*     */       {
/* 118 */         return;
/*     */       }
/*     */       
/* 121 */       this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIPlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */