/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*    */ 
/*    */ public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer>
/*    */ {
/*    */   private final RenderPlayer playerRenderer;
/*    */   
/*    */   public LayerDeadmau5Head(RenderPlayer playerRendererIn)
/*    */   {
/* 13 */     this.playerRenderer = playerRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 18 */     if ((entitylivingbaseIn.getName().equals("deadmau5")) && (entitylivingbaseIn.hasSkin()) && (!entitylivingbaseIn.isInvisible()))
/*    */     {
/* 20 */       this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());
/*    */       
/* 22 */       for (int i = 0; i < 2; i++)
/*    */       {
/* 24 */         float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
/* 25 */         float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
/* 26 */         GlStateManager.pushMatrix();
/* 27 */         GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
/* 28 */         GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
/* 29 */         GlStateManager.translate(0.375F * (i * 2 - 1), 0.0F, 0.0F);
/* 30 */         GlStateManager.translate(0.0F, -0.375F, 0.0F);
/* 31 */         GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
/* 32 */         GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
/* 33 */         float f2 = 1.3333334F;
/* 34 */         GlStateManager.scale(f2, f2, f2);
/* 35 */         this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
/* 36 */         GlStateManager.popMatrix();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerDeadmau5Head.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */