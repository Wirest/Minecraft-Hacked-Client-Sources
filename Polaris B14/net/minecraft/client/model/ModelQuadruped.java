/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelQuadruped extends ModelBase
/*    */ {
/*  9 */   public ModelRenderer head = new ModelRenderer(this, 0, 0);
/*    */   public ModelRenderer body;
/*    */   public ModelRenderer leg1;
/*    */   public ModelRenderer leg2;
/*    */   public ModelRenderer leg3;
/*    */   public ModelRenderer leg4;
/* 15 */   protected float childYOffset = 8.0F;
/* 16 */   protected float childZOffset = 4.0F;
/*    */   
/*    */   public ModelQuadruped(int p_i1154_1_, float p_i1154_2_)
/*    */   {
/* 20 */     this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, p_i1154_2_);
/* 21 */     this.head.setRotationPoint(0.0F, 18 - p_i1154_1_, -6.0F);
/* 22 */     this.body = new ModelRenderer(this, 28, 8);
/* 23 */     this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, p_i1154_2_);
/* 24 */     this.body.setRotationPoint(0.0F, 17 - p_i1154_1_, 2.0F);
/* 25 */     this.leg1 = new ModelRenderer(this, 0, 16);
/* 26 */     this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 27 */     this.leg1.setRotationPoint(-3.0F, 24 - p_i1154_1_, 7.0F);
/* 28 */     this.leg2 = new ModelRenderer(this, 0, 16);
/* 29 */     this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 30 */     this.leg2.setRotationPoint(3.0F, 24 - p_i1154_1_, 7.0F);
/* 31 */     this.leg3 = new ModelRenderer(this, 0, 16);
/* 32 */     this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 33 */     this.leg3.setRotationPoint(-3.0F, 24 - p_i1154_1_, -5.0F);
/* 34 */     this.leg4 = new ModelRenderer(this, 0, 16);
/* 35 */     this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 36 */     this.leg4.setRotationPoint(3.0F, 24 - p_i1154_1_, -5.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 44 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 46 */     if (this.isChild)
/*    */     {
/* 48 */       float f = 2.0F;
/* 49 */       GlStateManager.pushMatrix();
/* 50 */       GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
/* 51 */       this.head.render(scale);
/* 52 */       GlStateManager.popMatrix();
/* 53 */       GlStateManager.pushMatrix();
/* 54 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 55 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 56 */       this.body.render(scale);
/* 57 */       this.leg1.render(scale);
/* 58 */       this.leg2.render(scale);
/* 59 */       this.leg3.render(scale);
/* 60 */       this.leg4.render(scale);
/* 61 */       GlStateManager.popMatrix();
/*    */     }
/*    */     else
/*    */     {
/* 65 */       this.head.render(scale);
/* 66 */       this.body.render(scale);
/* 67 */       this.leg1.render(scale);
/* 68 */       this.leg2.render(scale);
/* 69 */       this.leg3.render(scale);
/* 70 */       this.leg4.render(scale);
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
/* 81 */     float f = 57.295776F;
/* 82 */     this.head.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 83 */     this.head.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 84 */     this.body.rotateAngleX = 1.5707964F;
/* 85 */     this.leg1.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/* 86 */     this.leg2.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 87 */     this.leg3.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 88 */     this.leg4.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelQuadruped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */