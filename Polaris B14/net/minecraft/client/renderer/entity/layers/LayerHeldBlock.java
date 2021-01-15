/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.entity.RenderEnderman;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.entity.monster.EntityEnderman;
/*    */ 
/*    */ public class LayerHeldBlock implements LayerRenderer<EntityEnderman>
/*    */ {
/*    */   private final RenderEnderman endermanRenderer;
/*    */   
/*    */   public LayerHeldBlock(RenderEnderman endermanRendererIn)
/*    */   {
/* 19 */     this.endermanRenderer = endermanRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityEnderman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 24 */     IBlockState iblockstate = entitylivingbaseIn.getHeldBlockState();
/*    */     
/* 26 */     if (iblockstate.getBlock().getMaterial() != net.minecraft.block.material.Material.air)
/*    */     {
/* 28 */       BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 29 */       GlStateManager.enableRescaleNormal();
/* 30 */       GlStateManager.pushMatrix();
/* 31 */       GlStateManager.translate(0.0F, 0.6875F, -0.75F);
/* 32 */       GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
/* 33 */       GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 34 */       GlStateManager.translate(0.25F, 0.1875F, 0.25F);
/* 35 */       float f = 0.5F;
/* 36 */       GlStateManager.scale(-f, -f, f);
/* 37 */       int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
/* 38 */       int j = i % 65536;
/* 39 */       int k = i / 65536;
/* 40 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 41 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 42 */       this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
/* 43 */       blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
/* 44 */       GlStateManager.popMatrix();
/* 45 */       GlStateManager.disableRescaleNormal();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerHeldBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */