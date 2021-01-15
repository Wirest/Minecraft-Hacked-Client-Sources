/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelZombie extends ModelBiped
/*    */ {
/*    */   public ModelZombie()
/*    */   {
/* 10 */     this(0.0F, false);
/*    */   }
/*    */   
/*    */   protected ModelZombie(float modelSize, float p_i1167_2_, int textureWidthIn, int textureHeightIn)
/*    */   {
/* 15 */     super(modelSize, p_i1167_2_, textureWidthIn, textureHeightIn);
/*    */   }
/*    */   
/*    */   public ModelZombie(float modelSize, boolean p_i1168_2_)
/*    */   {
/* 20 */     super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 30 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 31 */     float f = MathHelper.sin(this.swingProgress * 3.1415927F);
/* 32 */     float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
/* 33 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/* 34 */     this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 35 */     this.bipedRightArm.rotateAngleY = (-(0.1F - f * 0.6F));
/* 36 */     this.bipedLeftArm.rotateAngleY = (0.1F - f * 0.6F);
/* 37 */     this.bipedRightArm.rotateAngleX = -1.5707964F;
/* 38 */     this.bipedLeftArm.rotateAngleX = -1.5707964F;
/* 39 */     this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 40 */     this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 41 */     this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 42 */     this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 43 */     this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/* 44 */     this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */