/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.block.BlockBush;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.ModelQuadruped;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderMooshroom;
/*    */ import net.minecraft.entity.passive.EntityMooshroom;
/*    */ import net.minecraft.init.Blocks;
/*    */ 
/*    */ public class LayerMooshroomMushroom implements LayerRenderer<EntityMooshroom>
/*    */ {
/*    */   private final RenderMooshroom mooshroomRenderer;
/*    */   
/*    */   public LayerMooshroomMushroom(RenderMooshroom mooshroomRendererIn)
/*    */   {
/* 18 */     this.mooshroomRenderer = mooshroomRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityMooshroom entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 23 */     if ((!entitylivingbaseIn.isChild()) && (!entitylivingbaseIn.isInvisible()))
/*    */     {
/* 25 */       BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 26 */       this.mooshroomRenderer.bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture);
/* 27 */       GlStateManager.enableCull();
/* 28 */       GlStateManager.cullFace(1028);
/* 29 */       GlStateManager.pushMatrix();
/* 30 */       GlStateManager.scale(1.0F, -1.0F, 1.0F);
/* 31 */       GlStateManager.translate(0.2F, 0.35F, 0.5F);
/* 32 */       GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
/* 33 */       GlStateManager.pushMatrix();
/* 34 */       GlStateManager.translate(-0.5F, -0.5F, 0.5F);
/* 35 */       blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
/* 36 */       GlStateManager.popMatrix();
/* 37 */       GlStateManager.pushMatrix();
/* 38 */       GlStateManager.translate(0.1F, 0.0F, -0.6F);
/* 39 */       GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
/* 40 */       GlStateManager.translate(-0.5F, -0.5F, 0.5F);
/* 41 */       blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
/* 42 */       GlStateManager.popMatrix();
/* 43 */       GlStateManager.popMatrix();
/* 44 */       GlStateManager.pushMatrix();
/* 45 */       ((ModelQuadruped)this.mooshroomRenderer.getMainModel()).head.postRender(0.0625F);
/* 46 */       GlStateManager.scale(1.0F, -1.0F, 1.0F);
/* 47 */       GlStateManager.translate(0.0F, 0.7F, -0.2F);
/* 48 */       GlStateManager.rotate(12.0F, 0.0F, 1.0F, 0.0F);
/* 49 */       GlStateManager.translate(-0.5F, -0.5F, 0.5F);
/* 50 */       blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0F);
/* 51 */       GlStateManager.popMatrix();
/* 52 */       GlStateManager.cullFace(1029);
/* 53 */       GlStateManager.disableCull();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 59 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerMooshroomMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */