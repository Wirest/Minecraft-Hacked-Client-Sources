/*    */ package net.minecraft.entity;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityBodyHelper
/*    */ {
/*    */   private EntityLivingBase theLiving;
/*    */   private int rotationTickCounter;
/*    */   private float prevRenderYawHead;
/*    */   
/*    */   public EntityBodyHelper(EntityLivingBase p_i1611_1_)
/*    */   {
/* 18 */     this.theLiving = p_i1611_1_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateRenderAngles()
/*    */   {
/* 26 */     double d0 = this.theLiving.posX - this.theLiving.prevPosX;
/* 27 */     double d1 = this.theLiving.posZ - this.theLiving.prevPosZ;
/*    */     
/* 29 */     if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D)
/*    */     {
/* 31 */       this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
/* 32 */       this.theLiving.rotationYawHead = computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0F);
/* 33 */       this.prevRenderYawHead = this.theLiving.rotationYawHead;
/* 34 */       this.rotationTickCounter = 0;
/*    */     }
/*    */     else
/*    */     {
/* 38 */       float f = 75.0F;
/*    */       
/* 40 */       if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0F)
/*    */       {
/* 42 */         this.rotationTickCounter = 0;
/* 43 */         this.prevRenderYawHead = this.theLiving.rotationYawHead;
/*    */       }
/*    */       else
/*    */       {
/* 47 */         this.rotationTickCounter += 1;
/* 48 */         int i = 10;
/*    */         
/* 50 */         if (this.rotationTickCounter > 10)
/*    */         {
/* 52 */           f = Math.max(1.0F - (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
/*    */         }
/*    */       }
/*    */       
/* 56 */       this.theLiving.renderYawOffset = computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private float computeAngleWithBound(float p_75665_1_, float p_75665_2_, float p_75665_3_)
/*    */   {
/* 66 */     float f = MathHelper.wrapAngleTo180_float(p_75665_1_ - p_75665_2_);
/*    */     
/* 68 */     if (f < -p_75665_3_)
/*    */     {
/* 70 */       f = -p_75665_3_;
/*    */     }
/*    */     
/* 73 */     if (f >= p_75665_3_)
/*    */     {
/* 75 */       f = p_75665_3_;
/*    */     }
/*    */     
/* 78 */     return p_75665_1_ - f;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityBodyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */