/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelCreeper extends ModelBase
/*    */ {
/*    */   public ModelRenderer head;
/*    */   public ModelRenderer creeperArmor;
/*    */   public ModelRenderer body;
/*    */   public ModelRenderer leg1;
/*    */   public ModelRenderer leg2;
/*    */   public ModelRenderer leg3;
/*    */   public ModelRenderer leg4;
/*    */   
/*    */   public ModelCreeper()
/*    */   {
/* 18 */     this(0.0F);
/*    */   }
/*    */   
/*    */   public ModelCreeper(float p_i46366_1_)
/*    */   {
/* 23 */     int i = 6;
/* 24 */     this.head = new ModelRenderer(this, 0, 0);
/* 25 */     this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46366_1_);
/* 26 */     this.head.setRotationPoint(0.0F, i, 0.0F);
/* 27 */     this.creeperArmor = new ModelRenderer(this, 32, 0);
/* 28 */     this.creeperArmor.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46366_1_ + 0.5F);
/* 29 */     this.creeperArmor.setRotationPoint(0.0F, i, 0.0F);
/* 30 */     this.body = new ModelRenderer(this, 16, 16);
/* 31 */     this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46366_1_);
/* 32 */     this.body.setRotationPoint(0.0F, i, 0.0F);
/* 33 */     this.leg1 = new ModelRenderer(this, 0, 16);
/* 34 */     this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
/* 35 */     this.leg1.setRotationPoint(-2.0F, 12 + i, 4.0F);
/* 36 */     this.leg2 = new ModelRenderer(this, 0, 16);
/* 37 */     this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
/* 38 */     this.leg2.setRotationPoint(2.0F, 12 + i, 4.0F);
/* 39 */     this.leg3 = new ModelRenderer(this, 0, 16);
/* 40 */     this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
/* 41 */     this.leg3.setRotationPoint(-2.0F, 12 + i, -4.0F);
/* 42 */     this.leg4 = new ModelRenderer(this, 0, 16);
/* 43 */     this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
/* 44 */     this.leg4.setRotationPoint(2.0F, 12 + i, -4.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 52 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 53 */     this.head.render(scale);
/* 54 */     this.body.render(scale);
/* 55 */     this.leg1.render(scale);
/* 56 */     this.leg2.render(scale);
/* 57 */     this.leg3.render(scale);
/* 58 */     this.leg4.render(scale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 68 */     this.head.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 69 */     this.head.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 70 */     this.leg1.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/* 71 */     this.leg2.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 72 */     this.leg3.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 73 */     this.leg4.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */