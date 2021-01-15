/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelSpider
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer spiderHead;
/*     */   public ModelRenderer spiderNeck;
/*     */   public ModelRenderer spiderBody;
/*     */   public ModelRenderer spiderLeg1;
/*     */   public ModelRenderer spiderLeg2;
/*     */   public ModelRenderer spiderLeg3;
/*     */   public ModelRenderer spiderLeg4;
/*     */   public ModelRenderer spiderLeg5;
/*     */   public ModelRenderer spiderLeg6;
/*     */   public ModelRenderer spiderLeg7;
/*     */   public ModelRenderer spiderLeg8;
/*     */   
/*     */   public ModelSpider()
/*     */   {
/*  43 */     float f = 0.0F;
/*  44 */     int i = 15;
/*  45 */     this.spiderHead = new ModelRenderer(this, 32, 4);
/*  46 */     this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
/*  47 */     this.spiderHead.setRotationPoint(0.0F, i, -3.0F);
/*  48 */     this.spiderNeck = new ModelRenderer(this, 0, 0);
/*  49 */     this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
/*  50 */     this.spiderNeck.setRotationPoint(0.0F, i, 0.0F);
/*  51 */     this.spiderBody = new ModelRenderer(this, 0, 12);
/*  52 */     this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
/*  53 */     this.spiderBody.setRotationPoint(0.0F, i, 9.0F);
/*  54 */     this.spiderLeg1 = new ModelRenderer(this, 18, 0);
/*  55 */     this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  56 */     this.spiderLeg1.setRotationPoint(-4.0F, i, 2.0F);
/*  57 */     this.spiderLeg2 = new ModelRenderer(this, 18, 0);
/*  58 */     this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  59 */     this.spiderLeg2.setRotationPoint(4.0F, i, 2.0F);
/*  60 */     this.spiderLeg3 = new ModelRenderer(this, 18, 0);
/*  61 */     this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  62 */     this.spiderLeg3.setRotationPoint(-4.0F, i, 1.0F);
/*  63 */     this.spiderLeg4 = new ModelRenderer(this, 18, 0);
/*  64 */     this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  65 */     this.spiderLeg4.setRotationPoint(4.0F, i, 1.0F);
/*  66 */     this.spiderLeg5 = new ModelRenderer(this, 18, 0);
/*  67 */     this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  68 */     this.spiderLeg5.setRotationPoint(-4.0F, i, 0.0F);
/*  69 */     this.spiderLeg6 = new ModelRenderer(this, 18, 0);
/*  70 */     this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  71 */     this.spiderLeg6.setRotationPoint(4.0F, i, 0.0F);
/*  72 */     this.spiderLeg7 = new ModelRenderer(this, 18, 0);
/*  73 */     this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  74 */     this.spiderLeg7.setRotationPoint(-4.0F, i, -1.0F);
/*  75 */     this.spiderLeg8 = new ModelRenderer(this, 18, 0);
/*  76 */     this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  77 */     this.spiderLeg8.setRotationPoint(4.0F, i, -1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  85 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  86 */     this.spiderHead.render(scale);
/*  87 */     this.spiderNeck.render(scale);
/*  88 */     this.spiderBody.render(scale);
/*  89 */     this.spiderLeg1.render(scale);
/*  90 */     this.spiderLeg2.render(scale);
/*  91 */     this.spiderLeg3.render(scale);
/*  92 */     this.spiderLeg4.render(scale);
/*  93 */     this.spiderLeg5.render(scale);
/*  94 */     this.spiderLeg6.render(scale);
/*  95 */     this.spiderLeg7.render(scale);
/*  96 */     this.spiderLeg8.render(scale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/* 106 */     this.spiderHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 107 */     this.spiderHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 108 */     float f = 0.7853982F;
/* 109 */     this.spiderLeg1.rotateAngleZ = (-f);
/* 110 */     this.spiderLeg2.rotateAngleZ = f;
/* 111 */     this.spiderLeg3.rotateAngleZ = (-f * 0.74F);
/* 112 */     this.spiderLeg4.rotateAngleZ = (f * 0.74F);
/* 113 */     this.spiderLeg5.rotateAngleZ = (-f * 0.74F);
/* 114 */     this.spiderLeg6.rotateAngleZ = (f * 0.74F);
/* 115 */     this.spiderLeg7.rotateAngleZ = (-f);
/* 116 */     this.spiderLeg8.rotateAngleZ = f;
/* 117 */     float f1 = -0.0F;
/* 118 */     float f2 = 0.3926991F;
/* 119 */     this.spiderLeg1.rotateAngleY = (f2 * 2.0F + f1);
/* 120 */     this.spiderLeg2.rotateAngleY = (-f2 * 2.0F - f1);
/* 121 */     this.spiderLeg3.rotateAngleY = (f2 * 1.0F + f1);
/* 122 */     this.spiderLeg4.rotateAngleY = (-f2 * 1.0F - f1);
/* 123 */     this.spiderLeg5.rotateAngleY = (-f2 * 1.0F + f1);
/* 124 */     this.spiderLeg6.rotateAngleY = (f2 * 1.0F - f1);
/* 125 */     this.spiderLeg7.rotateAngleY = (-f2 * 2.0F + f1);
/* 126 */     this.spiderLeg8.rotateAngleY = (f2 * 2.0F - f1);
/* 127 */     float f3 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_78087_2_;
/* 128 */     float f4 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * p_78087_2_;
/* 129 */     float f5 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * p_78087_2_;
/* 130 */     float f6 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 4.712389F) * 0.4F) * p_78087_2_;
/* 131 */     float f7 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 0.0F) * 0.4F) * p_78087_2_;
/* 132 */     float f8 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 3.1415927F) * 0.4F) * p_78087_2_;
/* 133 */     float f9 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 1.5707964F) * 0.4F) * p_78087_2_;
/* 134 */     float f10 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 4.712389F) * 0.4F) * p_78087_2_;
/* 135 */     this.spiderLeg1.rotateAngleY += f3;
/* 136 */     this.spiderLeg2.rotateAngleY += -f3;
/* 137 */     this.spiderLeg3.rotateAngleY += f4;
/* 138 */     this.spiderLeg4.rotateAngleY += -f4;
/* 139 */     this.spiderLeg5.rotateAngleY += f5;
/* 140 */     this.spiderLeg6.rotateAngleY += -f5;
/* 141 */     this.spiderLeg7.rotateAngleY += f6;
/* 142 */     this.spiderLeg8.rotateAngleY += -f6;
/* 143 */     this.spiderLeg1.rotateAngleZ += f7;
/* 144 */     this.spiderLeg2.rotateAngleZ += -f7;
/* 145 */     this.spiderLeg3.rotateAngleZ += f8;
/* 146 */     this.spiderLeg4.rotateAngleZ += -f8;
/* 147 */     this.spiderLeg5.rotateAngleZ += f9;
/* 148 */     this.spiderLeg6.rotateAngleZ += -f9;
/* 149 */     this.spiderLeg7.rotateAngleZ += f10;
/* 150 */     this.spiderLeg8.rotateAngleZ += -f10;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */