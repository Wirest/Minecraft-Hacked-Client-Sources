/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.entity.RenderDragon;
/*    */ import net.minecraft.entity.boss.EntityDragon;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerEnderDragonEyes implements LayerRenderer<EntityDragon>
/*    */ {
/* 11 */   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
/*    */   private final RenderDragon dragonRenderer;
/*    */   
/*    */   public LayerEnderDragonEyes(RenderDragon dragonRendererIn)
/*    */   {
/* 16 */     this.dragonRenderer = dragonRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityDragon entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 21 */     this.dragonRenderer.bindTexture(TEXTURE);
/* 22 */     GlStateManager.enableBlend();
/* 23 */     GlStateManager.disableAlpha();
/* 24 */     GlStateManager.blendFunc(1, 1);
/* 25 */     GlStateManager.disableLighting();
/* 26 */     GlStateManager.depthFunc(514);
/* 27 */     int i = 61680;
/* 28 */     int j = i % 65536;
/* 29 */     int k = i / 65536;
/* 30 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 31 */     GlStateManager.enableLighting();
/* 32 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 33 */     this.dragonRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/* 34 */     this.dragonRenderer.func_177105_a(entitylivingbaseIn, partialTicks);
/* 35 */     GlStateManager.disableBlend();
/* 36 */     GlStateManager.enableAlpha();
/* 37 */     GlStateManager.depthFunc(515);
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 42 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerEnderDragonEyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */