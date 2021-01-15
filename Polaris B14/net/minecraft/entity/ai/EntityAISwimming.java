/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ 
/*    */ public class EntityAISwimming extends EntityAIBase
/*    */ {
/*    */   private EntityLiving theEntity;
/*    */   
/*    */   public EntityAISwimming(EntityLiving entitylivingIn)
/*    */   {
/* 12 */     this.theEntity = entitylivingIn;
/* 13 */     setMutexBits(4);
/* 14 */     ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 22 */     return (this.theEntity.isInWater()) || (this.theEntity.isInLava());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 30 */     if (this.theEntity.getRNG().nextFloat() < 0.8F)
/*    */     {
/* 32 */       this.theEntity.getJumpHelper().setJumping();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAISwimming.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */