/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.monster.EntityGuardian;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class ModelGuardian extends ModelBase
/*     */ {
/*     */   private ModelRenderer guardianBody;
/*     */   private ModelRenderer guardianEye;
/*     */   private ModelRenderer[] guardianSpines;
/*     */   private ModelRenderer[] guardianTail;
/*     */   
/*     */   public ModelGuardian()
/*     */   {
/*  18 */     this.textureWidth = 64;
/*  19 */     this.textureHeight = 64;
/*  20 */     this.guardianSpines = new ModelRenderer[12];
/*  21 */     this.guardianBody = new ModelRenderer(this);
/*  22 */     this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
/*  23 */     this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
/*  24 */     this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
/*  25 */     this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
/*  26 */     this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);
/*     */     
/*  28 */     for (int i = 0; i < this.guardianSpines.length; i++)
/*     */     {
/*  30 */       this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
/*  31 */       this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
/*  32 */       this.guardianBody.addChild(this.guardianSpines[i]);
/*     */     }
/*     */     
/*  35 */     this.guardianEye = new ModelRenderer(this, 8, 0);
/*  36 */     this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
/*  37 */     this.guardianBody.addChild(this.guardianEye);
/*  38 */     this.guardianTail = new ModelRenderer[3];
/*  39 */     this.guardianTail[0] = new ModelRenderer(this, 40, 0);
/*  40 */     this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
/*  41 */     this.guardianTail[1] = new ModelRenderer(this, 0, 54);
/*  42 */     this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
/*  43 */     this.guardianTail[2] = new ModelRenderer(this);
/*  44 */     this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
/*  45 */     this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
/*  46 */     this.guardianBody.addChild(this.guardianTail[0]);
/*  47 */     this.guardianTail[0].addChild(this.guardianTail[1]);
/*  48 */     this.guardianTail[1].addChild(this.guardianTail[2]);
/*     */   }
/*     */   
/*     */   public int func_178706_a()
/*     */   {
/*  53 */     return 54;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  61 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  62 */     this.guardianBody.render(scale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/*  72 */     EntityGuardian entityguardian = (EntityGuardian)entityIn;
/*  73 */     float f = p_78087_3_ - entityguardian.ticksExisted;
/*  74 */     this.guardianBody.rotateAngleY = (p_78087_4_ / 57.295776F);
/*  75 */     this.guardianBody.rotateAngleX = (p_78087_5_ / 57.295776F);
/*  76 */     float[] afloat = { 1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F };
/*  77 */     float[] afloat1 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F };
/*  78 */     float[] afloat2 = { 0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F };
/*  79 */     float[] afloat3 = { 0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F };
/*  80 */     float[] afloat4 = { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F };
/*  81 */     float[] afloat5 = { 8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F };
/*  82 */     float f1 = (1.0F - entityguardian.func_175469_o(f)) * 0.55F;
/*     */     
/*  84 */     for (int i = 0; i < 12; i++)
/*     */     {
/*  86 */       this.guardianSpines[i].rotateAngleX = (3.1415927F * afloat[i]);
/*  87 */       this.guardianSpines[i].rotateAngleY = (3.1415927F * afloat1[i]);
/*  88 */       this.guardianSpines[i].rotateAngleZ = (3.1415927F * afloat2[i]);
/*  89 */       this.guardianSpines[i].rotationPointX = (afloat3[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + i) * 0.01F - f1));
/*  90 */       this.guardianSpines[i].rotationPointY = (16.0F + afloat4[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + i) * 0.01F - f1));
/*  91 */       this.guardianSpines[i].rotationPointZ = (afloat5[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + i) * 0.01F - f1));
/*     */     }
/*     */     
/*  94 */     this.guardianEye.rotationPointZ = -8.25F;
/*  95 */     Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
/*     */     
/*  97 */     if (entityguardian.hasTargetedEntity())
/*     */     {
/*  99 */       entity = entityguardian.getTargetedEntity();
/*     */     }
/*     */     
/* 102 */     if (entity != null)
/*     */     {
/* 104 */       Vec3 vec3 = entity.getPositionEyes(0.0F);
/* 105 */       Vec3 vec31 = entityIn.getPositionEyes(0.0F);
/* 106 */       double d0 = vec3.yCoord - vec31.yCoord;
/*     */       
/* 108 */       if (d0 > 0.0D)
/*     */       {
/* 110 */         this.guardianEye.rotationPointY = 0.0F;
/*     */       }
/*     */       else
/*     */       {
/* 114 */         this.guardianEye.rotationPointY = 1.0F;
/*     */       }
/*     */       
/* 117 */       Vec3 vec32 = entityIn.getLook(0.0F);
/* 118 */       vec32 = new Vec3(vec32.xCoord, 0.0D, vec32.zCoord);
/* 119 */       Vec3 vec33 = new Vec3(vec31.xCoord - vec3.xCoord, 0.0D, vec31.zCoord - vec3.zCoord).normalize().rotateYaw(1.5707964F);
/* 120 */       double d1 = vec32.dotProduct(vec33);
/* 121 */       this.guardianEye.rotationPointX = (MathHelper.sqrt_float((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1));
/*     */     }
/*     */     
/* 124 */     this.guardianEye.showModel = true;
/* 125 */     float f2 = entityguardian.func_175471_a(f);
/* 126 */     this.guardianTail[0].rotateAngleY = (MathHelper.sin(f2) * 3.1415927F * 0.05F);
/* 127 */     this.guardianTail[1].rotateAngleY = (MathHelper.sin(f2) * 3.1415927F * 0.1F);
/* 128 */     this.guardianTail[1].rotationPointX = -1.5F;
/* 129 */     this.guardianTail[1].rotationPointY = 0.5F;
/* 130 */     this.guardianTail[1].rotationPointZ = 14.0F;
/* 131 */     this.guardianTail[2].rotateAngleY = (MathHelper.sin(f2) * 3.1415927F * 0.15F);
/* 132 */     this.guardianTail[2].rotationPointX = 0.5F;
/* 133 */     this.guardianTail[2].rotationPointY = 0.5F;
/* 134 */     this.guardianTail[2].rotationPointZ = 6.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */