/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ 
/*    */ public class EntityAIOwnerHurtTarget extends EntityAITarget
/*    */ {
/*    */   EntityTameable theEntityTameable;
/*    */   EntityLivingBase theTarget;
/*    */   private int field_142050_e;
/*    */   
/*    */   public EntityAIOwnerHurtTarget(EntityTameable theEntityTameableIn)
/*    */   {
/* 14 */     super(theEntityTameableIn, false);
/* 15 */     this.theEntityTameable = theEntityTameableIn;
/* 16 */     setMutexBits(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 24 */     if (!this.theEntityTameable.isTamed())
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 30 */     EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
/*    */     
/* 32 */     if (entitylivingbase == null)
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 38 */     this.theTarget = entitylivingbase.getLastAttacker();
/* 39 */     int i = entitylivingbase.getLastAttackerTime();
/* 40 */     return (i != this.field_142050_e) && (isSuitableTarget(this.theTarget, false)) && (this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 50 */     this.taskOwner.setAttackTarget(this.theTarget);
/* 51 */     EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
/*    */     
/* 53 */     if (entitylivingbase != null)
/*    */     {
/* 55 */       this.field_142050_e = entitylivingbase.getLastAttackerTime();
/*    */     }
/*    */     
/* 58 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIOwnerHurtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */