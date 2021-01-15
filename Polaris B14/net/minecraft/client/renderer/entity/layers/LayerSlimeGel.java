/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelSlime;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderSlime;
/*    */ import net.minecraft.entity.monster.EntitySlime;
/*    */ 
/*    */ public class LayerSlimeGel implements LayerRenderer<EntitySlime>
/*    */ {
/*    */   private final RenderSlime slimeRenderer;
/* 12 */   private final ModelBase slimeModel = new ModelSlime(0);
/*    */   
/*    */   public LayerSlimeGel(RenderSlime slimeRendererIn)
/*    */   {
/* 16 */     this.slimeRenderer = slimeRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntitySlime entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 21 */     if (!entitylivingbaseIn.isInvisible())
/*    */     {
/* 23 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 24 */       GlStateManager.enableNormalize();
/* 25 */       GlStateManager.enableBlend();
/* 26 */       GlStateManager.blendFunc(770, 771);
/* 27 */       this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
/* 28 */       this.slimeModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/* 29 */       GlStateManager.disableBlend();
/* 30 */       GlStateManager.disableNormalize();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerSlimeGel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */