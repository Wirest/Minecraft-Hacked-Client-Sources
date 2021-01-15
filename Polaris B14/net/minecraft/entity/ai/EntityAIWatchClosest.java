/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIWatchClosest
/*    */   extends EntityAIBase
/*    */ {
/*    */   protected EntityLiving theWatcher;
/*    */   protected Entity closestEntity;
/*    */   protected float maxDistanceForPlayer;
/*    */   private int lookTime;
/*    */   private float chance;
/*    */   protected Class<? extends Entity> watchedClass;
/*    */   
/*    */   public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance)
/*    */   {
/* 22 */     this.theWatcher = entitylivingIn;
/* 23 */     this.watchedClass = watchTargetClass;
/* 24 */     this.maxDistanceForPlayer = maxDistance;
/* 25 */     this.chance = 0.02F;
/* 26 */     setMutexBits(2);
/*    */   }
/*    */   
/*    */   public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn)
/*    */   {
/* 31 */     this.theWatcher = entitylivingIn;
/* 32 */     this.watchedClass = watchTargetClass;
/* 33 */     this.maxDistanceForPlayer = maxDistance;
/* 34 */     this.chance = chanceIn;
/* 35 */     setMutexBits(2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 43 */     if (this.theWatcher.getRNG().nextFloat() >= this.chance)
/*    */     {
/* 45 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 49 */     if (this.theWatcher.getAttackTarget() != null)
/*    */     {
/* 51 */       this.closestEntity = this.theWatcher.getAttackTarget();
/*    */     }
/*    */     
/* 54 */     if (this.watchedClass == EntityPlayer.class)
/*    */     {
/* 56 */       this.closestEntity = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, this.maxDistanceForPlayer);
/*    */     }
/*    */     else
/*    */     {
/* 60 */       this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0D, this.maxDistanceForPlayer), this.theWatcher);
/*    */     }
/*    */     
/* 63 */     return this.closestEntity != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 72 */     return this.closestEntity.isEntityAlive();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 80 */     this.lookTime = (40 + this.theWatcher.getRNG().nextInt(40));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 88 */     this.closestEntity = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 96 */     this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F, this.theWatcher.getVerticalFaceSpeed());
/* 97 */     this.lookTime -= 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIWatchClosest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */