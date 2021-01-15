/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntityBat;
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
/*     */ public class ModelBat
/*     */   extends ModelBase
/*     */ {
/*     */   private ModelRenderer batHead;
/*     */   private ModelRenderer batBody;
/*     */   private ModelRenderer batRightWing;
/*     */   private ModelRenderer batLeftWing;
/*     */   private ModelRenderer batOuterRightWing;
/*     */   private ModelRenderer batOuterLeftWing;
/*     */   
/*     */   public ModelBat()
/*     */   {
/*  28 */     this.textureWidth = 64;
/*  29 */     this.textureHeight = 64;
/*  30 */     this.batHead = new ModelRenderer(this, 0, 0);
/*  31 */     this.batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
/*  32 */     ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
/*  33 */     modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
/*  34 */     this.batHead.addChild(modelrenderer);
/*  35 */     ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
/*  36 */     modelrenderer1.mirror = true;
/*  37 */     modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
/*  38 */     this.batHead.addChild(modelrenderer1);
/*  39 */     this.batBody = new ModelRenderer(this, 0, 16);
/*  40 */     this.batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
/*  41 */     this.batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
/*  42 */     this.batRightWing = new ModelRenderer(this, 42, 0);
/*  43 */     this.batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
/*  44 */     this.batOuterRightWing = new ModelRenderer(this, 24, 16);
/*  45 */     this.batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
/*  46 */     this.batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
/*  47 */     this.batLeftWing = new ModelRenderer(this, 42, 0);
/*  48 */     this.batLeftWing.mirror = true;
/*  49 */     this.batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
/*  50 */     this.batOuterLeftWing = new ModelRenderer(this, 24, 16);
/*  51 */     this.batOuterLeftWing.mirror = true;
/*  52 */     this.batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
/*  53 */     this.batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
/*  54 */     this.batBody.addChild(this.batRightWing);
/*  55 */     this.batBody.addChild(this.batLeftWing);
/*  56 */     this.batRightWing.addChild(this.batOuterRightWing);
/*  57 */     this.batLeftWing.addChild(this.batOuterLeftWing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  65 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  66 */     this.batHead.render(scale);
/*  67 */     this.batBody.render(scale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/*  77 */     if (((EntityBat)entityIn).getIsBatHanging())
/*     */     {
/*  79 */       float f = 57.295776F;
/*  80 */       this.batHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/*  81 */       this.batHead.rotateAngleY = (3.1415927F - p_78087_4_ / 57.295776F);
/*  82 */       this.batHead.rotateAngleZ = 3.1415927F;
/*  83 */       this.batHead.setRotationPoint(0.0F, -2.0F, 0.0F);
/*  84 */       this.batRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
/*  85 */       this.batLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
/*  86 */       this.batBody.rotateAngleX = 3.1415927F;
/*  87 */       this.batRightWing.rotateAngleX = -0.15707964F;
/*  88 */       this.batRightWing.rotateAngleY = -1.2566371F;
/*  89 */       this.batOuterRightWing.rotateAngleY = -1.7278761F;
/*  90 */       this.batLeftWing.rotateAngleX = this.batRightWing.rotateAngleX;
/*  91 */       this.batLeftWing.rotateAngleY = (-this.batRightWing.rotateAngleY);
/*  92 */       this.batOuterLeftWing.rotateAngleY = (-this.batOuterRightWing.rotateAngleY);
/*     */     }
/*     */     else
/*     */     {
/*  96 */       float f1 = 57.295776F;
/*  97 */       this.batHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/*  98 */       this.batHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/*  99 */       this.batHead.rotateAngleZ = 0.0F;
/* 100 */       this.batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 101 */       this.batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 102 */       this.batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 103 */       this.batBody.rotateAngleX = (0.7853982F + MathHelper.cos(p_78087_3_ * 0.1F) * 0.15F);
/* 104 */       this.batBody.rotateAngleY = 0.0F;
/* 105 */       this.batRightWing.rotateAngleY = (MathHelper.cos(p_78087_3_ * 1.3F) * 3.1415927F * 0.25F);
/* 106 */       this.batLeftWing.rotateAngleY = (-this.batRightWing.rotateAngleY);
/* 107 */       this.batOuterRightWing.rotateAngleY = (this.batRightWing.rotateAngleY * 0.5F);
/* 108 */       this.batOuterLeftWing.rotateAngleY = (-this.batRightWing.rotateAngleY * 0.5F);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */