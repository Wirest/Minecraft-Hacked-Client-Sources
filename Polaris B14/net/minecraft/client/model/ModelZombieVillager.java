/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelZombieVillager extends ModelBiped
/*    */ {
/*    */   public ModelZombieVillager()
/*    */   {
/* 10 */     this(0.0F, 0.0F, false);
/*    */   }
/*    */   
/*    */   public ModelZombieVillager(float p_i1165_1_, float p_i1165_2_, boolean p_i1165_3_)
/*    */   {
/* 15 */     super(p_i1165_1_, 0.0F, 64, p_i1165_3_ ? 32 : 64);
/*    */     
/* 17 */     if (p_i1165_3_)
/*    */     {
/* 19 */       this.bipedHead = new ModelRenderer(this, 0, 0);
/* 20 */       this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, p_i1165_1_);
/* 21 */       this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1165_2_, 0.0F);
/*    */     }
/*    */     else
/*    */     {
/* 25 */       this.bipedHead = new ModelRenderer(this);
/* 26 */       this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1165_2_, 0.0F);
/* 27 */       this.bipedHead.setTextureOffset(0, 32).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, p_i1165_1_);
/* 28 */       this.bipedHead.setTextureOffset(24, 32).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, p_i1165_1_);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 39 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 40 */     float f = MathHelper.sin(this.swingProgress * 3.1415927F);
/* 41 */     float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
/* 42 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/* 43 */     this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 44 */     this.bipedRightArm.rotateAngleY = (-(0.1F - f * 0.6F));
/* 45 */     this.bipedLeftArm.rotateAngleY = (0.1F - f * 0.6F);
/* 46 */     this.bipedRightArm.rotateAngleX = -1.5707964F;
/* 47 */     this.bipedLeftArm.rotateAngleX = -1.5707964F;
/* 48 */     this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 49 */     this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 50 */     this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 51 */     this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 52 */     this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/* 53 */     this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelZombieVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */