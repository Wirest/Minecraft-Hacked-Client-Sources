/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ 
/*    */ public class ModelSheep2 extends ModelQuadruped
/*    */ {
/*    */   private float headRotationAngleX;
/*    */   
/*    */   public ModelSheep2()
/*    */   {
/* 13 */     super(12, 0.0F);
/* 14 */     this.head = new ModelRenderer(this, 0, 0);
/* 15 */     this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
/* 16 */     this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
/* 17 */     this.body = new ModelRenderer(this, 28, 8);
/* 18 */     this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
/* 19 */     this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*    */   {
/* 28 */     super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
/* 29 */     this.head.rotationPointY = (6.0F + ((EntitySheep)entitylivingbaseIn).getHeadRotationPointY(partialTickTime) * 9.0F);
/* 30 */     this.headRotationAngleX = ((EntitySheep)entitylivingbaseIn).getHeadRotationAngleX(partialTickTime);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 40 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 41 */     this.head.rotateAngleX = this.headRotationAngleX;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelSheep2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */