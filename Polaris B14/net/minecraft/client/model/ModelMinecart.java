/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelMinecart extends ModelBase
/*    */ {
/*  7 */   public ModelRenderer[] sideModels = new ModelRenderer[7];
/*    */   
/*    */   public ModelMinecart()
/*    */   {
/* 11 */     this.sideModels[0] = new ModelRenderer(this, 0, 10);
/* 12 */     this.sideModels[1] = new ModelRenderer(this, 0, 0);
/* 13 */     this.sideModels[2] = new ModelRenderer(this, 0, 0);
/* 14 */     this.sideModels[3] = new ModelRenderer(this, 0, 0);
/* 15 */     this.sideModels[4] = new ModelRenderer(this, 0, 0);
/* 16 */     this.sideModels[5] = new ModelRenderer(this, 44, 10);
/* 17 */     int i = 20;
/* 18 */     int j = 8;
/* 19 */     int k = 16;
/* 20 */     int l = 4;
/* 21 */     this.sideModels[0].addBox(-i / 2, -k / 2, -1.0F, i, k, 2, 0.0F);
/* 22 */     this.sideModels[0].setRotationPoint(0.0F, l, 0.0F);
/* 23 */     this.sideModels[5].addBox(-i / 2 + 1, -k / 2 + 1, -1.0F, i - 2, k - 2, 1, 0.0F);
/* 24 */     this.sideModels[5].setRotationPoint(0.0F, l, 0.0F);
/* 25 */     this.sideModels[1].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 26 */     this.sideModels[1].setRotationPoint(-i / 2 + 1, l, 0.0F);
/* 27 */     this.sideModels[2].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 28 */     this.sideModels[2].setRotationPoint(i / 2 - 1, l, 0.0F);
/* 29 */     this.sideModels[3].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 30 */     this.sideModels[3].setRotationPoint(0.0F, l, -k / 2 + 1);
/* 31 */     this.sideModels[4].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 32 */     this.sideModels[4].setRotationPoint(0.0F, l, k / 2 - 1);
/* 33 */     this.sideModels[0].rotateAngleX = 1.5707964F;
/* 34 */     this.sideModels[1].rotateAngleY = 4.712389F;
/* 35 */     this.sideModels[2].rotateAngleY = 1.5707964F;
/* 36 */     this.sideModels[3].rotateAngleY = 3.1415927F;
/* 37 */     this.sideModels[5].rotateAngleX = -1.5707964F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 45 */     this.sideModels[5].rotationPointY = (4.0F - p_78088_4_);
/*    */     
/* 47 */     for (int i = 0; i < 6; i++)
/*    */     {
/* 49 */       this.sideModels[i].render(scale);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */