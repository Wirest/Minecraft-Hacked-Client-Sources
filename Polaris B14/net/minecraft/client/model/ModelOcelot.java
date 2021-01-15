/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
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
/*     */ public class ModelOcelot
/*     */   extends ModelBase
/*     */ {
/*     */   ModelRenderer ocelotBackLeftLeg;
/*     */   ModelRenderer ocelotBackRightLeg;
/*     */   ModelRenderer ocelotFrontLeftLeg;
/*     */   ModelRenderer ocelotFrontRightLeg;
/*     */   ModelRenderer ocelotTail;
/*     */   ModelRenderer ocelotTail2;
/*     */   ModelRenderer ocelotHead;
/*     */   ModelRenderer ocelotBody;
/*  34 */   int field_78163_i = 1;
/*     */   
/*     */   public ModelOcelot()
/*     */   {
/*  38 */     setTextureOffset("head.main", 0, 0);
/*  39 */     setTextureOffset("head.nose", 0, 24);
/*  40 */     setTextureOffset("head.ear1", 0, 10);
/*  41 */     setTextureOffset("head.ear2", 6, 10);
/*  42 */     this.ocelotHead = new ModelRenderer(this, "head");
/*  43 */     this.ocelotHead.addBox("main", -2.5F, -2.0F, -3.0F, 5, 4, 5);
/*  44 */     this.ocelotHead.addBox("nose", -1.5F, 0.0F, -4.0F, 3, 2, 2);
/*  45 */     this.ocelotHead.addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2);
/*  46 */     this.ocelotHead.addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2);
/*  47 */     this.ocelotHead.setRotationPoint(0.0F, 15.0F, -9.0F);
/*  48 */     this.ocelotBody = new ModelRenderer(this, 20, 0);
/*  49 */     this.ocelotBody.addBox(-2.0F, 3.0F, -8.0F, 4, 16, 6, 0.0F);
/*  50 */     this.ocelotBody.setRotationPoint(0.0F, 12.0F, -10.0F);
/*  51 */     this.ocelotTail = new ModelRenderer(this, 0, 15);
/*  52 */     this.ocelotTail.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
/*  53 */     this.ocelotTail.rotateAngleX = 0.9F;
/*  54 */     this.ocelotTail.setRotationPoint(0.0F, 15.0F, 8.0F);
/*  55 */     this.ocelotTail2 = new ModelRenderer(this, 4, 15);
/*  56 */     this.ocelotTail2.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
/*  57 */     this.ocelotTail2.setRotationPoint(0.0F, 20.0F, 14.0F);
/*  58 */     this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
/*  59 */     this.ocelotBackLeftLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
/*  60 */     this.ocelotBackLeftLeg.setRotationPoint(1.1F, 18.0F, 5.0F);
/*  61 */     this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
/*  62 */     this.ocelotBackRightLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
/*  63 */     this.ocelotBackRightLeg.setRotationPoint(-1.1F, 18.0F, 5.0F);
/*  64 */     this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
/*  65 */     this.ocelotFrontLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
/*  66 */     this.ocelotFrontLeftLeg.setRotationPoint(1.2F, 13.8F, -5.0F);
/*  67 */     this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
/*  68 */     this.ocelotFrontRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
/*  69 */     this.ocelotFrontRightLeg.setRotationPoint(-1.2F, 13.8F, -5.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  77 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/*  79 */     if (this.isChild)
/*     */     {
/*  81 */       float f = 2.0F;
/*  82 */       GlStateManager.pushMatrix();
/*  83 */       GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
/*  84 */       GlStateManager.translate(0.0F, 10.0F * scale, 4.0F * scale);
/*  85 */       this.ocelotHead.render(scale);
/*  86 */       GlStateManager.popMatrix();
/*  87 */       GlStateManager.pushMatrix();
/*  88 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  89 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  90 */       this.ocelotBody.render(scale);
/*  91 */       this.ocelotBackLeftLeg.render(scale);
/*  92 */       this.ocelotBackRightLeg.render(scale);
/*  93 */       this.ocelotFrontLeftLeg.render(scale);
/*  94 */       this.ocelotFrontRightLeg.render(scale);
/*  95 */       this.ocelotTail.render(scale);
/*  96 */       this.ocelotTail2.render(scale);
/*  97 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else
/*     */     {
/* 101 */       this.ocelotHead.render(scale);
/* 102 */       this.ocelotBody.render(scale);
/* 103 */       this.ocelotTail.render(scale);
/* 104 */       this.ocelotTail2.render(scale);
/* 105 */       this.ocelotBackLeftLeg.render(scale);
/* 106 */       this.ocelotBackRightLeg.render(scale);
/* 107 */       this.ocelotFrontLeftLeg.render(scale);
/* 108 */       this.ocelotFrontRightLeg.render(scale);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/* 119 */     this.ocelotHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 120 */     this.ocelotHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/*     */     
/* 122 */     if (this.field_78163_i != 3)
/*     */     {
/* 124 */       this.ocelotBody.rotateAngleX = 1.5707964F;
/*     */       
/* 126 */       if (this.field_78163_i == 2)
/*     */       {
/* 128 */         this.ocelotBackLeftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_);
/* 129 */         this.ocelotBackRightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 0.3F) * 1.0F * p_78087_2_);
/* 130 */         this.ocelotFrontLeftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F + 0.3F) * 1.0F * p_78087_2_);
/* 131 */         this.ocelotFrontRightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_);
/* 132 */         this.ocelotTail2.rotateAngleX = (1.7278761F + 0.31415927F * MathHelper.cos(p_78087_1_) * p_78087_2_);
/*     */       }
/*     */       else
/*     */       {
/* 136 */         this.ocelotBackLeftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_);
/* 137 */         this.ocelotBackRightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_);
/* 138 */         this.ocelotFrontLeftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_);
/* 139 */         this.ocelotFrontRightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_);
/*     */         
/* 141 */         if (this.field_78163_i == 1)
/*     */         {
/* 143 */           this.ocelotTail2.rotateAngleX = (1.7278761F + 0.7853982F * MathHelper.cos(p_78087_1_) * p_78087_2_);
/*     */         }
/*     */         else
/*     */         {
/* 147 */           this.ocelotTail2.rotateAngleX = (1.7278761F + 0.47123894F * MathHelper.cos(p_78087_1_) * p_78087_2_);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*     */   {
/* 159 */     EntityOcelot entityocelot = (EntityOcelot)entitylivingbaseIn;
/* 160 */     this.ocelotBody.rotationPointY = 12.0F;
/* 161 */     this.ocelotBody.rotationPointZ = -10.0F;
/* 162 */     this.ocelotHead.rotationPointY = 15.0F;
/* 163 */     this.ocelotHead.rotationPointZ = -9.0F;
/* 164 */     this.ocelotTail.rotationPointY = 15.0F;
/* 165 */     this.ocelotTail.rotationPointZ = 8.0F;
/* 166 */     this.ocelotTail2.rotationPointY = 20.0F;
/* 167 */     this.ocelotTail2.rotationPointZ = 14.0F;
/* 168 */     this.ocelotFrontLeftLeg.rotationPointY = (this.ocelotFrontRightLeg.rotationPointY = 13.8F);
/* 169 */     this.ocelotFrontLeftLeg.rotationPointZ = (this.ocelotFrontRightLeg.rotationPointZ = -5.0F);
/* 170 */     this.ocelotBackLeftLeg.rotationPointY = (this.ocelotBackRightLeg.rotationPointY = 18.0F);
/* 171 */     this.ocelotBackLeftLeg.rotationPointZ = (this.ocelotBackRightLeg.rotationPointZ = 5.0F);
/* 172 */     this.ocelotTail.rotateAngleX = 0.9F;
/*     */     
/* 174 */     if (entityocelot.isSneaking())
/*     */     {
/* 176 */       this.ocelotBody.rotationPointY += 1.0F;
/* 177 */       this.ocelotHead.rotationPointY += 2.0F;
/* 178 */       this.ocelotTail.rotationPointY += 1.0F;
/* 179 */       this.ocelotTail2.rotationPointY += -4.0F;
/* 180 */       this.ocelotTail2.rotationPointZ += 2.0F;
/* 181 */       this.ocelotTail.rotateAngleX = 1.5707964F;
/* 182 */       this.ocelotTail2.rotateAngleX = 1.5707964F;
/* 183 */       this.field_78163_i = 0;
/*     */     }
/* 185 */     else if (entityocelot.isSprinting())
/*     */     {
/* 187 */       this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
/* 188 */       this.ocelotTail2.rotationPointZ += 2.0F;
/* 189 */       this.ocelotTail.rotateAngleX = 1.5707964F;
/* 190 */       this.ocelotTail2.rotateAngleX = 1.5707964F;
/* 191 */       this.field_78163_i = 2;
/*     */     }
/* 193 */     else if (entityocelot.isSitting())
/*     */     {
/* 195 */       this.ocelotBody.rotateAngleX = 0.7853982F;
/* 196 */       this.ocelotBody.rotationPointY += -4.0F;
/* 197 */       this.ocelotBody.rotationPointZ += 5.0F;
/* 198 */       this.ocelotHead.rotationPointY += -3.3F;
/* 199 */       this.ocelotHead.rotationPointZ += 1.0F;
/* 200 */       this.ocelotTail.rotationPointY += 8.0F;
/* 201 */       this.ocelotTail.rotationPointZ += -2.0F;
/* 202 */       this.ocelotTail2.rotationPointY += 2.0F;
/* 203 */       this.ocelotTail2.rotationPointZ += -0.8F;
/* 204 */       this.ocelotTail.rotateAngleX = 1.7278761F;
/* 205 */       this.ocelotTail2.rotateAngleX = 2.670354F;
/* 206 */       this.ocelotFrontLeftLeg.rotateAngleX = (this.ocelotFrontRightLeg.rotateAngleX = -0.15707964F);
/* 207 */       this.ocelotFrontLeftLeg.rotationPointY = (this.ocelotFrontRightLeg.rotationPointY = 15.8F);
/* 208 */       this.ocelotFrontLeftLeg.rotationPointZ = (this.ocelotFrontRightLeg.rotationPointZ = -7.0F);
/* 209 */       this.ocelotBackLeftLeg.rotateAngleX = (this.ocelotBackRightLeg.rotateAngleX = -1.5707964F);
/* 210 */       this.ocelotBackLeftLeg.rotationPointY = (this.ocelotBackRightLeg.rotationPointY = 21.0F);
/* 211 */       this.ocelotBackLeftLeg.rotationPointZ = (this.ocelotBackRightLeg.rotationPointZ = 1.0F);
/* 212 */       this.field_78163_i = 3;
/*     */     }
/*     */     else
/*     */     {
/* 216 */       this.field_78163_i = 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */