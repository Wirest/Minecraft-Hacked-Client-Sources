/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ 
/*     */ public class EntityAIFollowParent extends EntityAIBase
/*     */ {
/*     */   EntityAnimal childAnimal;
/*     */   EntityAnimal parentAnimal;
/*     */   double moveSpeed;
/*     */   private int delayCounter;
/*     */   
/*     */   public EntityAIFollowParent(EntityAnimal animal, double speed)
/*     */   {
/*  16 */     this.childAnimal = animal;
/*  17 */     this.moveSpeed = speed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  25 */     if (this.childAnimal.getGrowingAge() >= 0)
/*     */     {
/*  27 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  31 */     java.util.List<EntityAnimal> list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
/*  32 */     EntityAnimal entityanimal = null;
/*  33 */     double d0 = Double.MAX_VALUE;
/*     */     
/*  35 */     for (EntityAnimal entityanimal1 : list)
/*     */     {
/*  37 */       if (entityanimal1.getGrowingAge() >= 0)
/*     */       {
/*  39 */         double d1 = this.childAnimal.getDistanceSqToEntity(entityanimal1);
/*     */         
/*  41 */         if (d1 <= d0)
/*     */         {
/*  43 */           d0 = d1;
/*  44 */           entityanimal = entityanimal1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  49 */     if (entityanimal == null)
/*     */     {
/*  51 */       return false;
/*     */     }
/*  53 */     if (d0 < 9.0D)
/*     */     {
/*  55 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  59 */     this.parentAnimal = entityanimal;
/*  60 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  70 */     if (this.childAnimal.getGrowingAge() >= 0)
/*     */     {
/*  72 */       return false;
/*     */     }
/*  74 */     if (!this.parentAnimal.isEntityAlive())
/*     */     {
/*  76 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  80 */     double d0 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
/*  81 */     return (d0 >= 9.0D) && (d0 <= 256.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExecuting()
/*     */   {
/*  90 */     this.delayCounter = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  98 */     this.parentAnimal = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/* 106 */     if (--this.delayCounter <= 0)
/*     */     {
/* 108 */       this.delayCounter = 10;
/* 109 */       this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIFollowParent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */