/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.pathfinding.PathNavigate;
/*    */ 
/*    */ public class EntityAIOcelotAttack extends EntityAIBase
/*    */ {
/*    */   net.minecraft.world.World theWorld;
/*    */   EntityLiving theEntity;
/*    */   EntityLivingBase theVictim;
/*    */   int attackCountdown;
/*    */   
/*    */   public EntityAIOcelotAttack(EntityLiving theEntityIn)
/*    */   {
/* 16 */     this.theEntity = theEntityIn;
/* 17 */     this.theWorld = theEntityIn.worldObj;
/* 18 */     setMutexBits(3);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 26 */     EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/*    */     
/* 28 */     if (entitylivingbase == null)
/*    */     {
/* 30 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 34 */     this.theVictim = entitylivingbase;
/* 35 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean continueExecuting()
/*    */   {
/* 44 */     return this.theVictim.isEntityAlive();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 52 */     this.theVictim = null;
/* 53 */     this.theEntity.getNavigator().clearPathEntity();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 61 */     this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0F, 30.0F);
/* 62 */     double d0 = this.theEntity.width * 2.0F * this.theEntity.width * 2.0F;
/* 63 */     double d1 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY, this.theVictim.posZ);
/* 64 */     double d2 = 0.8D;
/*    */     
/* 66 */     if ((d1 > d0) && (d1 < 16.0D))
/*    */     {
/* 68 */       d2 = 1.33D;
/*    */     }
/* 70 */     else if (d1 < 225.0D)
/*    */     {
/* 72 */       d2 = 0.6D;
/*    */     }
/*    */     
/* 75 */     this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, d2);
/* 76 */     this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
/*    */     
/* 78 */     if (d1 <= d0)
/*    */     {
/* 80 */       if (this.attackCountdown <= 0)
/*    */       {
/* 82 */         this.attackCountdown = 20;
/* 83 */         this.theEntity.attackEntityAsMob(this.theVictim);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIOcelotAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */