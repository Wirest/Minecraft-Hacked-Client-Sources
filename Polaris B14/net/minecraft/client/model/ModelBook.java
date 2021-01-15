/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelBook
/*    */   extends ModelBase
/*    */ {
/*  9 */   public ModelRenderer coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6, 10, 0);
/*    */   
/*    */ 
/* 12 */   public ModelRenderer coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0F, -5.0F, 0.0F, 6, 10, 0);
/*    */   
/*    */ 
/* 15 */   public ModelRenderer pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0F, -4.0F, -0.99F, 5, 8, 1);
/*    */   
/*    */ 
/* 18 */   public ModelRenderer pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0F, -4.0F, -0.01F, 5, 8, 1);
/*    */   
/*    */ 
/* 21 */   public ModelRenderer flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
/*    */   
/*    */ 
/* 24 */   public ModelRenderer flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
/*    */   
/*    */ 
/* 27 */   public ModelRenderer bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2, 10, 0);
/*    */   
/*    */   public ModelBook()
/*    */   {
/* 31 */     this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
/* 32 */     this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
/* 33 */     this.bookSpine.rotateAngleY = 1.5707964F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 41 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 42 */     this.coverRight.render(scale);
/* 43 */     this.coverLeft.render(scale);
/* 44 */     this.bookSpine.render(scale);
/* 45 */     this.pagesRight.render(scale);
/* 46 */     this.pagesLeft.render(scale);
/* 47 */     this.flippingPageRight.render(scale);
/* 48 */     this.flippingPageLeft.render(scale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 58 */     float f = (MathHelper.sin(p_78087_1_ * 0.02F) * 0.1F + 1.25F) * p_78087_4_;
/* 59 */     this.coverRight.rotateAngleY = (3.1415927F + f);
/* 60 */     this.coverLeft.rotateAngleY = (-f);
/* 61 */     this.pagesRight.rotateAngleY = f;
/* 62 */     this.pagesLeft.rotateAngleY = (-f);
/* 63 */     this.flippingPageRight.rotateAngleY = (f - f * 2.0F * p_78087_2_);
/* 64 */     this.flippingPageLeft.rotateAngleY = (f - f * 2.0F * p_78087_3_);
/* 65 */     this.pagesRight.rotationPointX = MathHelper.sin(f);
/* 66 */     this.pagesLeft.rotationPointX = MathHelper.sin(f);
/* 67 */     this.flippingPageRight.rotationPointX = MathHelper.sin(f);
/* 68 */     this.flippingPageLeft.rotationPointX = MathHelper.sin(f);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */