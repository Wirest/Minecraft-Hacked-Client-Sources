/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ public class ModelCow extends ModelQuadruped
/*    */ {
/*    */   public ModelCow()
/*    */   {
/*  7 */     super(12, 0.0F);
/*  8 */     this.head = new ModelRenderer(this, 0, 0);
/*  9 */     this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
/* 10 */     this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
/* 11 */     this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
/* 12 */     this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
/* 13 */     this.body = new ModelRenderer(this, 18, 4);
/* 14 */     this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
/* 15 */     this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
/* 16 */     this.body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4, 6, 1);
/* 17 */     this.leg1.rotationPointX -= 1.0F;
/* 18 */     this.leg2.rotationPointX += 1.0F;
/* 19 */     this.leg1.rotationPointZ += 0.0F;
/* 20 */     this.leg2.rotationPointZ += 0.0F;
/* 21 */     this.leg3.rotationPointX -= 1.0F;
/* 22 */     this.leg4.rotationPointX += 1.0F;
/* 23 */     this.leg3.rotationPointZ -= 1.0F;
/* 24 */     this.leg4.rotationPointZ -= 1.0F;
/* 25 */     this.childZOffset += 2.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */