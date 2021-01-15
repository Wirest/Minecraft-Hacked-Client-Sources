/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ 
/*    */ public class EntityAIRestrictSun extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntity;
/*    */   
/*    */   public EntityAIRestrictSun(EntityCreature p_i1652_1_)
/*    */   {
/* 12 */     this.theEntity = p_i1652_1_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 20 */     return this.theEntity.worldObj.isDaytime();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 28 */     ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void resetTask()
/*    */   {
/* 36 */     ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIRestrictSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */