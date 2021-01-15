/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelEnderman
/*     */   extends ModelBiped
/*     */ {
/*     */   public boolean isCarrying;
/*     */   public boolean isAttacking;
/*     */   
/*     */   public ModelEnderman(float p_i46305_1_)
/*     */   {
/*  15 */     super(0.0F, -14.0F, 64, 32);
/*  16 */     float f = -14.0F;
/*  17 */     this.bipedHeadwear = new ModelRenderer(this, 0, 16);
/*  18 */     this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46305_1_ - 0.5F);
/*  19 */     this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
/*  20 */     this.bipedBody = new ModelRenderer(this, 32, 16);
/*  21 */     this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46305_1_);
/*  22 */     this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
/*  23 */     this.bipedRightArm = new ModelRenderer(this, 56, 0);
/*  24 */     this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  25 */     this.bipedRightArm.setRotationPoint(-3.0F, 2.0F + f, 0.0F);
/*  26 */     this.bipedLeftArm = new ModelRenderer(this, 56, 0);
/*  27 */     this.bipedLeftArm.mirror = true;
/*  28 */     this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  29 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
/*  30 */     this.bipedRightLeg = new ModelRenderer(this, 56, 0);
/*  31 */     this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  32 */     this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
/*  33 */     this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
/*  34 */     this.bipedLeftLeg.mirror = true;
/*  35 */     this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  36 */     this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/*  46 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/*  47 */     this.bipedHead.showModel = true;
/*  48 */     float f = -14.0F;
/*  49 */     this.bipedBody.rotateAngleX = 0.0F;
/*  50 */     this.bipedBody.rotationPointY = f;
/*  51 */     this.bipedBody.rotationPointZ = -0.0F;
/*  52 */     this.bipedRightLeg.rotateAngleX -= 0.0F;
/*  53 */     this.bipedLeftLeg.rotateAngleX -= 0.0F;
/*  54 */     this.bipedRightArm.rotateAngleX = ((float)(this.bipedRightArm.rotateAngleX * 0.5D));
/*  55 */     this.bipedLeftArm.rotateAngleX = ((float)(this.bipedLeftArm.rotateAngleX * 0.5D));
/*  56 */     this.bipedRightLeg.rotateAngleX = ((float)(this.bipedRightLeg.rotateAngleX * 0.5D));
/*  57 */     this.bipedLeftLeg.rotateAngleX = ((float)(this.bipedLeftLeg.rotateAngleX * 0.5D));
/*  58 */     float f1 = 0.4F;
/*     */     
/*  60 */     if (this.bipedRightArm.rotateAngleX > f1)
/*     */     {
/*  62 */       this.bipedRightArm.rotateAngleX = f1;
/*     */     }
/*     */     
/*  65 */     if (this.bipedLeftArm.rotateAngleX > f1)
/*     */     {
/*  67 */       this.bipedLeftArm.rotateAngleX = f1;
/*     */     }
/*     */     
/*  70 */     if (this.bipedRightArm.rotateAngleX < -f1)
/*     */     {
/*  72 */       this.bipedRightArm.rotateAngleX = (-f1);
/*     */     }
/*     */     
/*  75 */     if (this.bipedLeftArm.rotateAngleX < -f1)
/*     */     {
/*  77 */       this.bipedLeftArm.rotateAngleX = (-f1);
/*     */     }
/*     */     
/*  80 */     if (this.bipedRightLeg.rotateAngleX > f1)
/*     */     {
/*  82 */       this.bipedRightLeg.rotateAngleX = f1;
/*     */     }
/*     */     
/*  85 */     if (this.bipedLeftLeg.rotateAngleX > f1)
/*     */     {
/*  87 */       this.bipedLeftLeg.rotateAngleX = f1;
/*     */     }
/*     */     
/*  90 */     if (this.bipedRightLeg.rotateAngleX < -f1)
/*     */     {
/*  92 */       this.bipedRightLeg.rotateAngleX = (-f1);
/*     */     }
/*     */     
/*  95 */     if (this.bipedLeftLeg.rotateAngleX < -f1)
/*     */     {
/*  97 */       this.bipedLeftLeg.rotateAngleX = (-f1);
/*     */     }
/*     */     
/* 100 */     if (this.isCarrying)
/*     */     {
/* 102 */       this.bipedRightArm.rotateAngleX = -0.5F;
/* 103 */       this.bipedLeftArm.rotateAngleX = -0.5F;
/* 104 */       this.bipedRightArm.rotateAngleZ = 0.05F;
/* 105 */       this.bipedLeftArm.rotateAngleZ = -0.05F;
/*     */     }
/*     */     
/* 108 */     this.bipedRightArm.rotationPointZ = 0.0F;
/* 109 */     this.bipedLeftArm.rotationPointZ = 0.0F;
/* 110 */     this.bipedRightLeg.rotationPointZ = 0.0F;
/* 111 */     this.bipedLeftLeg.rotationPointZ = 0.0F;
/* 112 */     this.bipedRightLeg.rotationPointY = (9.0F + f);
/* 113 */     this.bipedLeftLeg.rotationPointY = (9.0F + f);
/* 114 */     this.bipedHead.rotationPointZ = -0.0F;
/* 115 */     this.bipedHead.rotationPointY = (f + 1.0F);
/* 116 */     this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
/* 117 */     this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
/* 118 */     this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
/* 119 */     this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
/* 120 */     this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
/* 121 */     this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
/*     */     
/* 123 */     if (this.isAttacking)
/*     */     {
/* 125 */       float f2 = 1.0F;
/* 126 */       this.bipedHead.rotationPointY -= f2 * 5.0F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */