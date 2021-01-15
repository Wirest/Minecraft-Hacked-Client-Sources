/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.util.Rotations;
/*     */ 
/*     */ public class ModelArmorStand extends ModelArmorStandArmor
/*     */ {
/*     */   public ModelRenderer standRightSide;
/*     */   public ModelRenderer standLeftSide;
/*     */   public ModelRenderer standWaist;
/*     */   public ModelRenderer standBase;
/*     */   
/*     */   public ModelArmorStand()
/*     */   {
/*  16 */     this(0.0F);
/*     */   }
/*     */   
/*     */   public ModelArmorStand(float p_i46306_1_)
/*     */   {
/*  21 */     super(p_i46306_1_, 64, 64);
/*  22 */     this.bipedHead = new ModelRenderer(this, 0, 0);
/*  23 */     this.bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  24 */     this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  25 */     this.bipedBody = new ModelRenderer(this, 0, 26);
/*  26 */     this.bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, p_i46306_1_);
/*  27 */     this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  28 */     this.bipedRightArm = new ModelRenderer(this, 24, 0);
/*  29 */     this.bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
/*  30 */     this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
/*  31 */     this.bipedLeftArm = new ModelRenderer(this, 32, 16);
/*  32 */     this.bipedLeftArm.mirror = true;
/*  33 */     this.bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
/*  34 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
/*  35 */     this.bipedRightLeg = new ModelRenderer(this, 8, 0);
/*  36 */     this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
/*  37 */     this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
/*  38 */     this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
/*  39 */     this.bipedLeftLeg.mirror = true;
/*  40 */     this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
/*  41 */     this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  42 */     this.standRightSide = new ModelRenderer(this, 16, 0);
/*  43 */     this.standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  44 */     this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  45 */     this.standRightSide.showModel = true;
/*  46 */     this.standLeftSide = new ModelRenderer(this, 48, 16);
/*  47 */     this.standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  48 */     this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  49 */     this.standWaist = new ModelRenderer(this, 0, 48);
/*  50 */     this.standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, p_i46306_1_);
/*  51 */     this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  52 */     this.standBase = new ModelRenderer(this, 0, 32);
/*  53 */     this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, p_i46306_1_);
/*  54 */     this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/*  64 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/*     */     
/*  66 */     if ((entityIn instanceof EntityArmorStand))
/*     */     {
/*  68 */       EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
/*  69 */       this.bipedLeftArm.showModel = entityarmorstand.getShowArms();
/*  70 */       this.bipedRightArm.showModel = entityarmorstand.getShowArms();
/*  71 */       this.standBase.showModel = (!entityarmorstand.hasNoBasePlate());
/*  72 */       this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  73 */       this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
/*  74 */       this.standRightSide.rotateAngleX = (0.017453292F * entityarmorstand.getBodyRotation().getX());
/*  75 */       this.standRightSide.rotateAngleY = (0.017453292F * entityarmorstand.getBodyRotation().getY());
/*  76 */       this.standRightSide.rotateAngleZ = (0.017453292F * entityarmorstand.getBodyRotation().getZ());
/*  77 */       this.standLeftSide.rotateAngleX = (0.017453292F * entityarmorstand.getBodyRotation().getX());
/*  78 */       this.standLeftSide.rotateAngleY = (0.017453292F * entityarmorstand.getBodyRotation().getY());
/*  79 */       this.standLeftSide.rotateAngleZ = (0.017453292F * entityarmorstand.getBodyRotation().getZ());
/*  80 */       this.standWaist.rotateAngleX = (0.017453292F * entityarmorstand.getBodyRotation().getX());
/*  81 */       this.standWaist.rotateAngleY = (0.017453292F * entityarmorstand.getBodyRotation().getY());
/*  82 */       this.standWaist.rotateAngleZ = (0.017453292F * entityarmorstand.getBodyRotation().getZ());
/*  83 */       float f = (entityarmorstand.getLeftLegRotation().getX() + entityarmorstand.getRightLegRotation().getX()) / 2.0F;
/*  84 */       float f1 = (entityarmorstand.getLeftLegRotation().getY() + entityarmorstand.getRightLegRotation().getY()) / 2.0F;
/*  85 */       float f2 = (entityarmorstand.getLeftLegRotation().getZ() + entityarmorstand.getRightLegRotation().getZ()) / 2.0F;
/*  86 */       this.standBase.rotateAngleX = 0.0F;
/*  87 */       this.standBase.rotateAngleY = (0.017453292F * -entityIn.rotationYaw);
/*  88 */       this.standBase.rotateAngleZ = 0.0F;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  97 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/*  98 */     net.minecraft.client.renderer.GlStateManager.pushMatrix();
/*     */     
/* 100 */     if (this.isChild)
/*     */     {
/* 102 */       float f = 2.0F;
/* 103 */       net.minecraft.client.renderer.GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 104 */       net.minecraft.client.renderer.GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 105 */       this.standRightSide.render(scale);
/* 106 */       this.standLeftSide.render(scale);
/* 107 */       this.standWaist.render(scale);
/* 108 */       this.standBase.render(scale);
/*     */     }
/*     */     else
/*     */     {
/* 112 */       if (entityIn.isSneaking())
/*     */       {
/* 114 */         net.minecraft.client.renderer.GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/* 117 */       this.standRightSide.render(scale);
/* 118 */       this.standLeftSide.render(scale);
/* 119 */       this.standWaist.render(scale);
/* 120 */       this.standBase.render(scale);
/*     */     }
/*     */     
/* 123 */     net.minecraft.client.renderer.GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   public void postRenderArm(float scale)
/*     */   {
/* 128 */     boolean flag = this.bipedRightArm.showModel;
/* 129 */     this.bipedRightArm.showModel = true;
/* 130 */     super.postRenderArm(scale);
/* 131 */     this.bipedRightArm.showModel = flag;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */