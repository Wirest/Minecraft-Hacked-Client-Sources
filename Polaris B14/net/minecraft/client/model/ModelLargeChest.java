/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ public class ModelLargeChest extends ModelChest
/*    */ {
/*    */   public ModelLargeChest()
/*    */   {
/*  7 */     this.chestLid = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
/*  8 */     this.chestLid.addBox(0.0F, -5.0F, -14.0F, 30, 5, 14, 0.0F);
/*  9 */     this.chestLid.rotationPointX = 1.0F;
/* 10 */     this.chestLid.rotationPointY = 7.0F;
/* 11 */     this.chestLid.rotationPointZ = 15.0F;
/* 12 */     this.chestKnob = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
/* 13 */     this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
/* 14 */     this.chestKnob.rotationPointX = 16.0F;
/* 15 */     this.chestKnob.rotationPointY = 7.0F;
/* 16 */     this.chestKnob.rotationPointZ = 15.0F;
/* 17 */     this.chestBelow = new ModelRenderer(this, 0, 19).setTextureSize(128, 64);
/* 18 */     this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 30, 10, 14, 0.0F);
/* 19 */     this.chestBelow.rotationPointX = 1.0F;
/* 20 */     this.chestBelow.rotationPointY = 6.0F;
/* 21 */     this.chestBelow.rotationPointZ = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelLargeChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */