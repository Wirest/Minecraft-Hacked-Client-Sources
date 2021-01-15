/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntitySkeleton;
/*    */ 
/*    */ public class ModelSkeleton extends ModelZombie
/*    */ {
/*    */   public ModelSkeleton()
/*    */   {
/* 11 */     this(0.0F, false);
/*    */   }
/*    */   
/*    */   public ModelSkeleton(float p_i46303_1_, boolean p_i46303_2_)
/*    */   {
/* 16 */     super(p_i46303_1_, 0.0F, 64, 32);
/*    */     
/* 18 */     if (!p_i46303_2_)
/*    */     {
/* 20 */       this.bipedRightArm = new ModelRenderer(this, 40, 16);
/* 21 */       this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
/* 22 */       this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
/* 23 */       this.bipedLeftArm = new ModelRenderer(this, 40, 16);
/* 24 */       this.bipedLeftArm.mirror = true;
/* 25 */       this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
/* 26 */       this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
/* 27 */       this.bipedRightLeg = new ModelRenderer(this, 0, 16);
/* 28 */       this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
/* 29 */       this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
/* 30 */       this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
/* 31 */       this.bipedLeftLeg.mirror = true;
/* 32 */       this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
/* 33 */       this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*    */   {
/* 43 */     this.aimedBow = (((EntitySkeleton)entitylivingbaseIn).getSkeletonType() == 1);
/* 44 */     super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 54 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */