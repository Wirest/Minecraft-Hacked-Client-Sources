/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ 
/*    */ public class EntityAILookAtVillager extends EntityAIBase
/*    */ {
/*    */   private EntityIronGolem theGolem;
/*    */   private EntityVillager theVillager;
/*    */   private int lookTime;
/*    */   
/*    */   public EntityAILookAtVillager(EntityIronGolem theGolemIn)
/*    */   {
/* 14 */     this.theGolem = theGolemIn;
/* 15 */     setMutexBits(3);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 23 */     if (!this.theGolem.worldObj.isDaytime())
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if (this.theGolem.getRNG().nextInt(8000) != 0)
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 33 */     this.theVillager = ((EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D), this.theGolem));
/* 34 */     return this.theVillager != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 43 */     return this.lookTime > 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 51 */     this.lookTime = 400;
/* 52 */     this.theGolem.setHoldingRose(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 60 */     this.theGolem.setHoldingRose(false);
/* 61 */     this.theVillager = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 69 */     this.theGolem.getLookHelper().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
/* 70 */     this.lookTime -= 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAILookAtVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */