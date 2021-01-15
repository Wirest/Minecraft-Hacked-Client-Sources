/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.entity.RenderSpider;
/*    */ import net.minecraft.entity.monster.EntitySpider;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerSpiderEyes implements LayerRenderer<EntitySpider>
/*    */ {
/* 11 */   private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
/*    */   private final RenderSpider spiderRenderer;
/*    */   
/*    */   public LayerSpiderEyes(RenderSpider spiderRendererIn)
/*    */   {
/* 16 */     this.spiderRenderer = spiderRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntitySpider entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 21 */     this.spiderRenderer.bindTexture(SPIDER_EYES);
/* 22 */     GlStateManager.enableBlend();
/* 23 */     GlStateManager.disableAlpha();
/* 24 */     GlStateManager.blendFunc(1, 1);
/*    */     
/* 26 */     if (entitylivingbaseIn.isInvisible())
/*    */     {
/* 28 */       GlStateManager.depthMask(false);
/*    */     }
/*    */     else
/*    */     {
/* 32 */       GlStateManager.depthMask(true);
/*    */     }
/*    */     
/* 35 */     int i = 61680;
/* 36 */     int j = i % 65536;
/* 37 */     int k = i / 65536;
/* 38 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 39 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 40 */     this.spiderRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/* 41 */     i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
/* 42 */     j = i % 65536;
/* 43 */     k = i / 65536;
/* 44 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 45 */     this.spiderRenderer.func_177105_a(entitylivingbaseIn, partialTicks);
/* 46 */     GlStateManager.disableBlend();
/* 47 */     GlStateManager.enableAlpha();
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerSpiderEyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */