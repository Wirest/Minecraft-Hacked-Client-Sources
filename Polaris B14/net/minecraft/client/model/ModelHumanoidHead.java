/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelHumanoidHead extends ModelSkeletonHead
/*    */ {
/*  7 */   private final ModelRenderer head = new ModelRenderer(this, 32, 0);
/*    */   
/*    */   public ModelHumanoidHead()
/*    */   {
/* 11 */     super(0, 0, 64, 64);
/* 12 */     this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.25F);
/* 13 */     this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 21 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/* 22 */     this.head.render(scale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 32 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 33 */     this.head.rotateAngleY = this.skeletonHead.rotateAngleY;
/* 34 */     this.head.rotateAngleX = this.skeletonHead.rotateAngleX;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelHumanoidHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */