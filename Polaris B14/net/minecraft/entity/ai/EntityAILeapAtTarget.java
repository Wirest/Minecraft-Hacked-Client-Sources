/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityAILeapAtTarget
/*    */   extends EntityAIBase
/*    */ {
/*    */   EntityLiving leaper;
/*    */   EntityLivingBase leapTarget;
/*    */   float leapMotionY;
/*    */   
/*    */   public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn)
/*    */   {
/* 20 */     this.leaper = leapingEntity;
/* 21 */     this.leapMotionY = leapMotionYIn;
/* 22 */     setMutexBits(5);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 30 */     this.leapTarget = this.leaper.getAttackTarget();
/*    */     
/* 32 */     if (this.leapTarget == null)
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 38 */     double d0 = this.leaper.getDistanceSqToEntity(this.leapTarget);
/* 39 */     return this.leaper.onGround;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 48 */     return !this.leaper.onGround;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 56 */     double d0 = this.leapTarget.posX - this.leaper.posX;
/* 57 */     double d1 = this.leapTarget.posZ - this.leaper.posZ;
/* 58 */     float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
/* 59 */     this.leaper.motionX += d0 / f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
/* 60 */     this.leaper.motionZ += d1 / f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
/* 61 */     this.leaper.motionY = this.leapMotionY;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAILeapAtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */