/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelSilverfish
/*    */   extends ModelBase
/*    */ {
/*  9 */   private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
/*    */   
/*    */   private ModelRenderer[] silverfishWings;
/*    */   
/* 13 */   private float[] field_78170_c = new float[7];
/*    */   
/*    */ 
/* 16 */   private static final int[][] silverfishBoxLength = { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
/*    */   
/*    */ 
/* 19 */   private static final int[][] silverfishTexturePositions = { new int[2], { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11 }, { 13, 4 } };
/*    */   
/*    */   public ModelSilverfish()
/*    */   {
/* 23 */     float f = -3.5F;
/*    */     
/* 25 */     for (int i = 0; i < this.silverfishBodyParts.length; i++)
/*    */     {
/* 27 */       this.silverfishBodyParts[i] = new ModelRenderer(this, silverfishTexturePositions[i][0], silverfishTexturePositions[i][1]);
/* 28 */       this.silverfishBodyParts[i].addBox(silverfishBoxLength[i][0] * -0.5F, 0.0F, silverfishBoxLength[i][2] * -0.5F, silverfishBoxLength[i][0], silverfishBoxLength[i][1], silverfishBoxLength[i][2]);
/* 29 */       this.silverfishBodyParts[i].setRotationPoint(0.0F, 24 - silverfishBoxLength[i][1], f);
/* 30 */       this.field_78170_c[i] = f;
/*    */       
/* 32 */       if (i < this.silverfishBodyParts.length - 1)
/*    */       {
/* 34 */         f += (silverfishBoxLength[i][2] + silverfishBoxLength[(i + 1)][2]) * 0.5F;
/*    */       }
/*    */     }
/*    */     
/* 38 */     this.silverfishWings = new ModelRenderer[3];
/* 39 */     this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
/* 40 */     this.silverfishWings[0].addBox(-5.0F, 0.0F, silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
/* 41 */     this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.field_78170_c[2]);
/* 42 */     this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
/* 43 */     this.silverfishWings[1].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
/* 44 */     this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.field_78170_c[4]);
/* 45 */     this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
/* 46 */     this.silverfishWings[2].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
/* 47 */     this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.field_78170_c[1]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 55 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 57 */     for (int i = 0; i < this.silverfishBodyParts.length; i++)
/*    */     {
/* 59 */       this.silverfishBodyParts[i].render(scale);
/*    */     }
/*    */     
/* 62 */     for (int j = 0; j < this.silverfishWings.length; j++)
/*    */     {
/* 64 */       this.silverfishWings[j].render(scale);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 75 */     for (int i = 0; i < this.silverfishBodyParts.length; i++)
/*    */     {
/* 77 */       this.silverfishBodyParts[i].rotateAngleY = (MathHelper.cos(p_78087_3_ * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (1 + Math.abs(i - 2)));
/* 78 */       this.silverfishBodyParts[i].rotationPointX = (MathHelper.sin(p_78087_3_ * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * Math.abs(i - 2));
/*    */     }
/*    */     
/* 81 */     this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
/* 82 */     this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
/* 83 */     this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
/* 84 */     this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
/* 85 */     this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */