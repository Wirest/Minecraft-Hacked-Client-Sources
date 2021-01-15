/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ public class EntityJumpHelper
/*    */ {
/*    */   private EntityLiving entity;
/*    */   protected boolean isJumping;
/*    */   
/*    */   public EntityJumpHelper(EntityLiving entityIn)
/*    */   {
/* 12 */     this.entity = entityIn;
/*    */   }
/*    */   
/*    */   public void setJumping()
/*    */   {
/* 17 */     this.isJumping = true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doJump()
/*    */   {
/* 25 */     this.entity.setJumping(this.isJumping);
/* 26 */     this.isJumping = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityJumpHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */