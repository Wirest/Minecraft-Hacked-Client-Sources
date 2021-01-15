/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityWolf;
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
/*     */ public class ModelWolf
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer wolfHeadMain;
/*     */   public ModelRenderer wolfBody;
/*     */   public ModelRenderer wolfLeg1;
/*     */   public ModelRenderer wolfLeg2;
/*     */   public ModelRenderer wolfLeg3;
/*     */   public ModelRenderer wolfLeg4;
/*     */   ModelRenderer wolfTail;
/*     */   ModelRenderer wolfMane;
/*     */   
/*     */   public ModelWolf()
/*     */   {
/*  37 */     float f = 0.0F;
/*  38 */     float f1 = 13.5F;
/*  39 */     this.wolfHeadMain = new ModelRenderer(this, 0, 0);
/*  40 */     this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
/*  41 */     this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
/*  42 */     this.wolfBody = new ModelRenderer(this, 18, 14);
/*  43 */     this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
/*  44 */     this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
/*  45 */     this.wolfMane = new ModelRenderer(this, 21, 0);
/*  46 */     this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
/*  47 */     this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
/*  48 */     this.wolfLeg1 = new ModelRenderer(this, 0, 18);
/*  49 */     this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  50 */     this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
/*  51 */     this.wolfLeg2 = new ModelRenderer(this, 0, 18);
/*  52 */     this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  53 */     this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
/*  54 */     this.wolfLeg3 = new ModelRenderer(this, 0, 18);
/*  55 */     this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  56 */     this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
/*  57 */     this.wolfLeg4 = new ModelRenderer(this, 0, 18);
/*  58 */     this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  59 */     this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
/*  60 */     this.wolfTail = new ModelRenderer(this, 9, 18);
/*  61 */     this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  62 */     this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
/*  63 */     this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
/*  64 */     this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
/*  65 */     this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  73 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/*  74 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/*  76 */     if (this.isChild)
/*     */     {
/*  78 */       float f = 2.0F;
/*  79 */       GlStateManager.pushMatrix();
/*  80 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/*  81 */       this.wolfHeadMain.renderWithRotation(scale);
/*  82 */       GlStateManager.popMatrix();
/*  83 */       GlStateManager.pushMatrix();
/*  84 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  85 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  86 */       this.wolfBody.render(scale);
/*  87 */       this.wolfLeg1.render(scale);
/*  88 */       this.wolfLeg2.render(scale);
/*  89 */       this.wolfLeg3.render(scale);
/*  90 */       this.wolfLeg4.render(scale);
/*  91 */       this.wolfTail.renderWithRotation(scale);
/*  92 */       this.wolfMane.render(scale);
/*  93 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else
/*     */     {
/*  97 */       this.wolfHeadMain.renderWithRotation(scale);
/*  98 */       this.wolfBody.render(scale);
/*  99 */       this.wolfLeg1.render(scale);
/* 100 */       this.wolfLeg2.render(scale);
/* 101 */       this.wolfLeg3.render(scale);
/* 102 */       this.wolfLeg4.render(scale);
/* 103 */       this.wolfTail.renderWithRotation(scale);
/* 104 */       this.wolfMane.render(scale);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*     */   {
/* 114 */     EntityWolf entitywolf = (EntityWolf)entitylivingbaseIn;
/*     */     
/* 116 */     if (entitywolf.isAngry())
/*     */     {
/* 118 */       this.wolfTail.rotateAngleY = 0.0F;
/*     */     }
/*     */     else
/*     */     {
/* 122 */       this.wolfTail.rotateAngleY = (MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_);
/*     */     }
/*     */     
/* 125 */     if (entitywolf.isSitting())
/*     */     {
/* 127 */       this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
/* 128 */       this.wolfMane.rotateAngleX = 1.2566371F;
/* 129 */       this.wolfMane.rotateAngleY = 0.0F;
/* 130 */       this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
/* 131 */       this.wolfBody.rotateAngleX = 0.7853982F;
/* 132 */       this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
/* 133 */       this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
/* 134 */       this.wolfLeg1.rotateAngleX = 4.712389F;
/* 135 */       this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
/* 136 */       this.wolfLeg2.rotateAngleX = 4.712389F;
/* 137 */       this.wolfLeg3.rotateAngleX = 5.811947F;
/* 138 */       this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
/* 139 */       this.wolfLeg4.rotateAngleX = 5.811947F;
/* 140 */       this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
/*     */     }
/*     */     else
/*     */     {
/* 144 */       this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
/* 145 */       this.wolfBody.rotateAngleX = 1.5707964F;
/* 146 */       this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
/* 147 */       this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
/* 148 */       this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
/* 149 */       this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
/* 150 */       this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
/* 151 */       this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
/* 152 */       this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
/* 153 */       this.wolfLeg1.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_);
/* 154 */       this.wolfLeg2.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_);
/* 155 */       this.wolfLeg3.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_);
/* 156 */       this.wolfLeg4.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_);
/*     */     }
/*     */     
/* 159 */     this.wolfHeadMain.rotateAngleZ = (entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0F));
/* 160 */     this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.08F);
/* 161 */     this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16F);
/* 162 */     this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.2F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/* 172 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 173 */     this.wolfHeadMain.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 174 */     this.wolfHeadMain.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 175 */     this.wolfTail.rotateAngleX = p_78087_3_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */