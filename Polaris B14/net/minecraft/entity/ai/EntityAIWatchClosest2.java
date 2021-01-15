/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ public class EntityAIWatchClosest2 extends EntityAIWatchClosest
/*    */ {
/*    */   public EntityAIWatchClosest2(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn)
/*    */   {
/* 10 */     super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
/* 11 */     setMutexBits(3);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIWatchClosest2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */