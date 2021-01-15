/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelSkeletonHead extends ModelBase
/*    */ {
/*    */   public ModelRenderer skeletonHead;
/*    */   
/*    */   public ModelSkeletonHead()
/*    */   {
/* 11 */     this(0, 35, 64, 64);
/*    */   }
/*    */   
/*    */   public ModelSkeletonHead(int p_i1155_1_, int p_i1155_2_, int p_i1155_3_, int p_i1155_4_)
/*    */   {
/* 16 */     this.textureWidth = p_i1155_3_;
/* 17 */     this.textureHeight = p_i1155_4_;
/* 18 */     this.skeletonHead = new ModelRenderer(this, p_i1155_1_, p_i1155_2_);
/* 19 */     this.skeletonHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
/* 20 */     this.skeletonHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 28 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 29 */     this.skeletonHead.render(scale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 39 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 40 */     this.skeletonHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 41 */     this.skeletonHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelSkeletonHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */