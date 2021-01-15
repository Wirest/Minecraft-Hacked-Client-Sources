/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
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
/*     */ 
/*     */ 
/*     */ public class ModelRabbit
/*     */   extends ModelBase
/*     */ {
/*     */   ModelRenderer rabbitLeftFoot;
/*     */   ModelRenderer rabbitRightFoot;
/*     */   ModelRenderer rabbitLeftThigh;
/*     */   ModelRenderer rabbitRightThigh;
/*     */   ModelRenderer rabbitBody;
/*     */   ModelRenderer rabbitLeftArm;
/*     */   ModelRenderer rabbitRightArm;
/*     */   ModelRenderer rabbitHead;
/*     */   ModelRenderer rabbitRightEar;
/*     */   ModelRenderer rabbitLeftEar;
/*     */   ModelRenderer rabbitTail;
/*     */   ModelRenderer rabbitNose;
/*  46 */   private float field_178701_m = 0.0F;
/*  47 */   private float field_178699_n = 0.0F;
/*     */   
/*     */   public ModelRabbit()
/*     */   {
/*  51 */     setTextureOffset("head.main", 0, 0);
/*  52 */     setTextureOffset("head.nose", 0, 24);
/*  53 */     setTextureOffset("head.ear1", 0, 10);
/*  54 */     setTextureOffset("head.ear2", 6, 10);
/*  55 */     this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
/*  56 */     this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  57 */     this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
/*  58 */     this.rabbitLeftFoot.mirror = true;
/*  59 */     setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
/*  60 */     this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
/*  61 */     this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  62 */     this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
/*  63 */     this.rabbitRightFoot.mirror = true;
/*  64 */     setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
/*  65 */     this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
/*  66 */     this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  67 */     this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
/*  68 */     this.rabbitLeftThigh.mirror = true;
/*  69 */     setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
/*  70 */     this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
/*  71 */     this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  72 */     this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
/*  73 */     this.rabbitRightThigh.mirror = true;
/*  74 */     setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
/*  75 */     this.rabbitBody = new ModelRenderer(this, 0, 0);
/*  76 */     this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
/*  77 */     this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
/*  78 */     this.rabbitBody.mirror = true;
/*  79 */     setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
/*  80 */     this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
/*  81 */     this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  82 */     this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
/*  83 */     this.rabbitLeftArm.mirror = true;
/*  84 */     setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
/*  85 */     this.rabbitRightArm = new ModelRenderer(this, 0, 15);
/*  86 */     this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  87 */     this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
/*  88 */     this.rabbitRightArm.mirror = true;
/*  89 */     setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
/*  90 */     this.rabbitHead = new ModelRenderer(this, 32, 0);
/*  91 */     this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
/*  92 */     this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  93 */     this.rabbitHead.mirror = true;
/*  94 */     setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
/*  95 */     this.rabbitRightEar = new ModelRenderer(this, 52, 0);
/*  96 */     this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
/*  97 */     this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  98 */     this.rabbitRightEar.mirror = true;
/*  99 */     setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
/* 100 */     this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
/* 101 */     this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
/* 102 */     this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 103 */     this.rabbitLeftEar.mirror = true;
/* 104 */     setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
/* 105 */     this.rabbitTail = new ModelRenderer(this, 52, 6);
/* 106 */     this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
/* 107 */     this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
/* 108 */     this.rabbitTail.mirror = true;
/* 109 */     setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
/* 110 */     this.rabbitNose = new ModelRenderer(this, 32, 9);
/* 111 */     this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
/* 112 */     this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
/* 113 */     this.rabbitNose.mirror = true;
/* 114 */     setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private void setRotationOffset(ModelRenderer p_178691_1_, float p_178691_2_, float p_178691_3_, float p_178691_4_)
/*     */   {
/* 119 */     p_178691_1_.rotateAngleX = p_178691_2_;
/* 120 */     p_178691_1_.rotateAngleY = p_178691_3_;
/* 121 */     p_178691_1_.rotateAngleZ = p_178691_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/* 129 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/* 131 */     if (this.isChild)
/*     */     {
/* 133 */       float f = 2.0F;
/* 134 */       GlStateManager.pushMatrix();
/* 135 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/* 136 */       this.rabbitHead.render(scale);
/* 137 */       this.rabbitLeftEar.render(scale);
/* 138 */       this.rabbitRightEar.render(scale);
/* 139 */       this.rabbitNose.render(scale);
/* 140 */       GlStateManager.popMatrix();
/* 141 */       GlStateManager.pushMatrix();
/* 142 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 143 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 144 */       this.rabbitLeftFoot.render(scale);
/* 145 */       this.rabbitRightFoot.render(scale);
/* 146 */       this.rabbitLeftThigh.render(scale);
/* 147 */       this.rabbitRightThigh.render(scale);
/* 148 */       this.rabbitBody.render(scale);
/* 149 */       this.rabbitLeftArm.render(scale);
/* 150 */       this.rabbitRightArm.render(scale);
/* 151 */       this.rabbitTail.render(scale);
/* 152 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else
/*     */     {
/* 156 */       this.rabbitLeftFoot.render(scale);
/* 157 */       this.rabbitRightFoot.render(scale);
/* 158 */       this.rabbitLeftThigh.render(scale);
/* 159 */       this.rabbitRightThigh.render(scale);
/* 160 */       this.rabbitBody.render(scale);
/* 161 */       this.rabbitLeftArm.render(scale);
/* 162 */       this.rabbitRightArm.render(scale);
/* 163 */       this.rabbitHead.render(scale);
/* 164 */       this.rabbitRightEar.render(scale);
/* 165 */       this.rabbitLeftEar.render(scale);
/* 166 */       this.rabbitTail.render(scale);
/* 167 */       this.rabbitNose.render(scale);
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
/* 178 */     float f = p_78087_3_ - entityIn.ticksExisted;
/* 179 */     EntityRabbit entityrabbit = (EntityRabbit)entityIn;
/* 180 */     this.rabbitNose.rotateAngleX = (this.rabbitHead.rotateAngleX = this.rabbitRightEar.rotateAngleX = this.rabbitLeftEar.rotateAngleX = p_78087_5_ * 0.017453292F);
/* 181 */     this.rabbitNose.rotateAngleY = (this.rabbitHead.rotateAngleY = p_78087_4_ * 0.017453292F);
/* 182 */     this.rabbitRightEar.rotateAngleY = (this.rabbitNose.rotateAngleY - 0.2617994F);
/* 183 */     this.rabbitLeftEar.rotateAngleY = (this.rabbitNose.rotateAngleY + 0.2617994F);
/* 184 */     this.field_178701_m = MathHelper.sin(entityrabbit.func_175521_o(f) * 3.1415927F);
/* 185 */     this.rabbitLeftThigh.rotateAngleX = (this.rabbitRightThigh.rotateAngleX = (this.field_178701_m * 50.0F - 21.0F) * 0.017453292F);
/* 186 */     this.rabbitLeftFoot.rotateAngleX = (this.rabbitRightFoot.rotateAngleX = this.field_178701_m * 50.0F * 0.017453292F);
/* 187 */     this.rabbitLeftArm.rotateAngleX = (this.rabbitRightArm.rotateAngleX = (this.field_178701_m * -40.0F - 11.0F) * 0.017453292F);
/*     */   }
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */