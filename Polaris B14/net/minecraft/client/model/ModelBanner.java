/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ public class ModelBanner extends ModelBase
/*    */ {
/*    */   public ModelRenderer bannerSlate;
/*    */   public ModelRenderer bannerStand;
/*    */   public ModelRenderer bannerTop;
/*    */   
/*    */   public ModelBanner()
/*    */   {
/* 11 */     this.textureWidth = 64;
/* 12 */     this.textureHeight = 64;
/* 13 */     this.bannerSlate = new ModelRenderer(this, 0, 0);
/* 14 */     this.bannerSlate.addBox(-10.0F, 0.0F, -2.0F, 20, 40, 1, 0.0F);
/* 15 */     this.bannerStand = new ModelRenderer(this, 44, 0);
/* 16 */     this.bannerStand.addBox(-1.0F, -30.0F, -1.0F, 2, 42, 2, 0.0F);
/* 17 */     this.bannerTop = new ModelRenderer(this, 0, 42);
/* 18 */     this.bannerTop.addBox(-10.0F, -32.0F, -1.0F, 20, 2, 2, 0.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderBanner()
/*    */   {
/* 26 */     this.bannerSlate.rotationPointY = -32.0F;
/* 27 */     this.bannerSlate.render(0.0625F);
/* 28 */     this.bannerStand.render(0.0625F);
/* 29 */     this.bannerTop.render(0.0625F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */