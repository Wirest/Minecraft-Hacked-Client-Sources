/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
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
/*     */ public class ModelIronGolem
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer ironGolemHead;
/*     */   public ModelRenderer ironGolemBody;
/*     */   public ModelRenderer ironGolemRightArm;
/*     */   public ModelRenderer ironGolemLeftArm;
/*     */   public ModelRenderer ironGolemLeftLeg;
/*     */   public ModelRenderer ironGolemRightLeg;
/*     */   
/*     */   public ModelIronGolem()
/*     */   {
/*  29 */     this(0.0F);
/*     */   }
/*     */   
/*     */   public ModelIronGolem(float p_i1161_1_)
/*     */   {
/*  34 */     this(p_i1161_1_, -7.0F);
/*     */   }
/*     */   
/*     */   public ModelIronGolem(float p_i46362_1_, float p_i46362_2_)
/*     */   {
/*  39 */     int i = 128;
/*  40 */     int j = 128;
/*  41 */     this.ironGolemHead = new ModelRenderer(this).setTextureSize(i, j);
/*  42 */     this.ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i46362_2_, -2.0F);
/*  43 */     this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, p_i46362_1_);
/*  44 */     this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, p_i46362_1_);
/*  45 */     this.ironGolemBody = new ModelRenderer(this).setTextureSize(i, j);
/*  46 */     this.ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i46362_2_, 0.0F);
/*  47 */     this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i46362_1_);
/*  48 */     this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i46362_1_ + 0.5F);
/*  49 */     this.ironGolemRightArm = new ModelRenderer(this).setTextureSize(i, j);
/*  50 */     this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
/*  51 */     this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
/*  52 */     this.ironGolemLeftArm = new ModelRenderer(this).setTextureSize(i, j);
/*  53 */     this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
/*  54 */     this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
/*  55 */     this.ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(i, j);
/*  56 */     this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i46362_2_, 0.0F);
/*  57 */     this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
/*  58 */     this.ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(i, j);
/*  59 */     this.ironGolemRightLeg.mirror = true;
/*  60 */     this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i46362_2_, 0.0F);
/*  61 */     this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  69 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  70 */     this.ironGolemHead.render(scale);
/*  71 */     this.ironGolemBody.render(scale);
/*  72 */     this.ironGolemLeftLeg.render(scale);
/*  73 */     this.ironGolemRightLeg.render(scale);
/*  74 */     this.ironGolemRightArm.render(scale);
/*  75 */     this.ironGolemLeftArm.render(scale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/*  85 */     this.ironGolemHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/*  86 */     this.ironGolemHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/*  87 */     this.ironGolemLeftLeg.rotateAngleX = (-1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_);
/*  88 */     this.ironGolemRightLeg.rotateAngleX = (1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_);
/*  89 */     this.ironGolemLeftLeg.rotateAngleY = 0.0F;
/*  90 */     this.ironGolemRightLeg.rotateAngleY = 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*     */   {
/*  99 */     EntityIronGolem entityirongolem = (EntityIronGolem)entitylivingbaseIn;
/* 100 */     int i = entityirongolem.getAttackTimer();
/*     */     
/* 102 */     if (i > 0)
/*     */     {
/* 104 */       this.ironGolemRightArm.rotateAngleX = (-2.0F + 1.5F * func_78172_a(i - partialTickTime, 10.0F));
/* 105 */       this.ironGolemLeftArm.rotateAngleX = (-2.0F + 1.5F * func_78172_a(i - partialTickTime, 10.0F));
/*     */     }
/*     */     else
/*     */     {
/* 109 */       int j = entityirongolem.getHoldRoseTick();
/*     */       
/* 111 */       if (j > 0)
/*     */       {
/* 113 */         this.ironGolemRightArm.rotateAngleX = (-0.8F + 0.025F * func_78172_a(j, 70.0F));
/* 114 */         this.ironGolemLeftArm.rotateAngleX = 0.0F;
/*     */       }
/*     */       else
/*     */       {
/* 118 */         this.ironGolemRightArm.rotateAngleX = ((-0.2F + 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_);
/* 119 */         this.ironGolemLeftArm.rotateAngleX = ((-0.2F - 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private float func_78172_a(float p_78172_1_, float p_78172_2_)
/*     */   {
/* 126 */     return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */