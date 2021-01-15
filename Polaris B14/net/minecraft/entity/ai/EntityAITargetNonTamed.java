/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ 
/*    */ public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget
/*    */ {
/*    */   private EntityTameable theTameable;
/*    */   
/*    */   public EntityAITargetNonTamed(EntityTameable entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector)
/*    */   {
/* 13 */     super(entityIn, classTarget, 10, checkSight, false, targetSelector);
/* 14 */     this.theTameable = entityIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 22 */     return (!this.theTameable.isTamed()) && (super.shouldExecute());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAITargetNonTamed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */