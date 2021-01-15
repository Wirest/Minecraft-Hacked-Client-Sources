/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelChicken extends ModelBase
/*     */ {
/*     */   public ModelRenderer head;
/*     */   public ModelRenderer body;
/*     */   public ModelRenderer rightLeg;
/*     */   public ModelRenderer leftLeg;
/*     */   public ModelRenderer rightWing;
/*     */   public ModelRenderer leftWing;
/*     */   public ModelRenderer bill;
/*     */   public ModelRenderer chin;
/*     */   
/*     */   public ModelChicken()
/*     */   {
/*  20 */     int i = 16;
/*  21 */     this.head = new ModelRenderer(this, 0, 0);
/*  22 */     this.head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
/*  23 */     this.head.setRotationPoint(0.0F, -1 + i, -4.0F);
/*  24 */     this.bill = new ModelRenderer(this, 14, 0);
/*  25 */     this.bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
/*  26 */     this.bill.setRotationPoint(0.0F, -1 + i, -4.0F);
/*  27 */     this.chin = new ModelRenderer(this, 14, 4);
/*  28 */     this.chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
/*  29 */     this.chin.setRotationPoint(0.0F, -1 + i, -4.0F);
/*  30 */     this.body = new ModelRenderer(this, 0, 9);
/*  31 */     this.body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
/*  32 */     this.body.setRotationPoint(0.0F, i, 0.0F);
/*  33 */     this.rightLeg = new ModelRenderer(this, 26, 0);
/*  34 */     this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
/*  35 */     this.rightLeg.setRotationPoint(-2.0F, 3 + i, 1.0F);
/*  36 */     this.leftLeg = new ModelRenderer(this, 26, 0);
/*  37 */     this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
/*  38 */     this.leftLeg.setRotationPoint(1.0F, 3 + i, 1.0F);
/*  39 */     this.rightWing = new ModelRenderer(this, 24, 13);
/*  40 */     this.rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
/*  41 */     this.rightWing.setRotationPoint(-4.0F, -3 + i, 0.0F);
/*  42 */     this.leftWing = new ModelRenderer(this, 24, 13);
/*  43 */     this.leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
/*  44 */     this.leftWing.setRotationPoint(4.0F, -3 + i, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  52 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/*  54 */     if (this.isChild)
/*     */     {
/*  56 */       float f = 2.0F;
/*  57 */       GlStateManager.pushMatrix();
/*  58 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/*  59 */       this.head.render(scale);
/*  60 */       this.bill.render(scale);
/*  61 */       this.chin.render(scale);
/*  62 */       GlStateManager.popMatrix();
/*  63 */       GlStateManager.pushMatrix();
/*  64 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  65 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  66 */       this.body.render(scale);
/*  67 */       this.rightLeg.render(scale);
/*  68 */       this.leftLeg.render(scale);
/*  69 */       this.rightWing.render(scale);
/*  70 */       this.leftWing.render(scale);
/*  71 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else
/*     */     {
/*  75 */       this.head.render(scale);
/*  76 */       this.bill.render(scale);
/*  77 */       this.chin.render(scale);
/*  78 */       this.body.render(scale);
/*  79 */       this.rightLeg.render(scale);
/*  80 */       this.leftLeg.render(scale);
/*  81 */       this.rightWing.render(scale);
/*  82 */       this.leftWing.render(scale);
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
/*  93 */     this.head.rotateAngleX = (p_78087_5_ / 57.295776F);
/*  94 */     this.head.rotateAngleY = (p_78087_4_ / 57.295776F);
/*  95 */     this.bill.rotateAngleX = this.head.rotateAngleX;
/*  96 */     this.bill.rotateAngleY = this.head.rotateAngleY;
/*  97 */     this.chin.rotateAngleX = this.head.rotateAngleX;
/*  98 */     this.chin.rotateAngleY = this.head.rotateAngleY;
/*  99 */     this.body.rotateAngleX = 1.5707964F;
/* 100 */     this.rightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/* 101 */     this.leftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 102 */     this.rightWing.rotateAngleZ = p_78087_3_;
/* 103 */     this.leftWing.rotateAngleZ = (-p_78087_3_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */