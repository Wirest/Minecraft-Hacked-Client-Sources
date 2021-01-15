/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelEnderCrystal
/*    */   extends ModelBase
/*    */ {
/*    */   private ModelRenderer cube;
/* 12 */   private ModelRenderer glass = new ModelRenderer(this, "glass");
/*    */   
/*    */   private ModelRenderer base;
/*    */   
/*    */ 
/*    */   public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_)
/*    */   {
/* 19 */     this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/* 20 */     this.cube = new ModelRenderer(this, "cube");
/* 21 */     this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/*    */     
/* 23 */     if (p_i1170_2_)
/*    */     {
/* 25 */       this.base = new ModelRenderer(this, "base");
/* 26 */       this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 35 */     GlStateManager.pushMatrix();
/* 36 */     GlStateManager.scale(2.0F, 2.0F, 2.0F);
/* 37 */     GlStateManager.translate(0.0F, -0.5F, 0.0F);
/*    */     
/* 39 */     if (this.base != null)
/*    */     {
/* 41 */       this.base.render(scale);
/*    */     }
/*    */     
/* 44 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 45 */     GlStateManager.translate(0.0F, 0.8F + p_78088_4_, 0.0F);
/* 46 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 47 */     this.glass.render(scale);
/* 48 */     float f = 0.875F;
/* 49 */     GlStateManager.scale(f, f, f);
/* 50 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 51 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 52 */     this.glass.render(scale);
/* 53 */     GlStateManager.scale(f, f, f);
/* 54 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 55 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 56 */     this.cube.render(scale);
/* 57 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */