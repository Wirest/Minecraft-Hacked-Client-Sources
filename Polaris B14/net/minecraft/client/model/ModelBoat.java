/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelBoat extends ModelBase
/*    */ {
/*  7 */   public ModelRenderer[] boatSides = new ModelRenderer[5];
/*    */   
/*    */   public ModelBoat()
/*    */   {
/* 11 */     this.boatSides[0] = new ModelRenderer(this, 0, 8);
/* 12 */     this.boatSides[1] = new ModelRenderer(this, 0, 0);
/* 13 */     this.boatSides[2] = new ModelRenderer(this, 0, 0);
/* 14 */     this.boatSides[3] = new ModelRenderer(this, 0, 0);
/* 15 */     this.boatSides[4] = new ModelRenderer(this, 0, 0);
/* 16 */     int i = 24;
/* 17 */     int j = 6;
/* 18 */     int k = 20;
/* 19 */     int l = 4;
/* 20 */     this.boatSides[0].addBox(-i / 2, -k / 2 + 2, -3.0F, i, k - 4, 4, 0.0F);
/* 21 */     this.boatSides[0].setRotationPoint(0.0F, l, 0.0F);
/* 22 */     this.boatSides[1].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 23 */     this.boatSides[1].setRotationPoint(-i / 2 + 1, l, 0.0F);
/* 24 */     this.boatSides[2].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 25 */     this.boatSides[2].setRotationPoint(i / 2 - 1, l, 0.0F);
/* 26 */     this.boatSides[3].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 27 */     this.boatSides[3].setRotationPoint(0.0F, l, -k / 2 + 1);
/* 28 */     this.boatSides[4].addBox(-i / 2 + 2, -j - 1, -1.0F, i - 4, j, 2, 0.0F);
/* 29 */     this.boatSides[4].setRotationPoint(0.0F, l, k / 2 - 1);
/* 30 */     this.boatSides[0].rotateAngleX = 1.5707964F;
/* 31 */     this.boatSides[1].rotateAngleY = 4.712389F;
/* 32 */     this.boatSides[2].rotateAngleY = 1.5707964F;
/* 33 */     this.boatSides[3].rotateAngleY = 3.1415927F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 41 */     for (int i = 0; i < 5; i++)
/*    */     {
/* 43 */       this.boatSides[i].render(scale);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */