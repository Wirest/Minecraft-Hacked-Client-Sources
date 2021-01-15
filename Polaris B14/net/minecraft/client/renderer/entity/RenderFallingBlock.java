/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockModelRenderer;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.item.EntityFallingBlock;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class RenderFallingBlock extends Render<EntityFallingBlock>
/*    */ {
/*    */   public RenderFallingBlock(RenderManager renderManagerIn)
/*    */   {
/* 22 */     super(renderManagerIn);
/* 23 */     this.shadowSize = 0.5F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 34 */     if (entity.getBlock() != null)
/*    */     {
/* 36 */       bindTexture(TextureMap.locationBlocksTexture);
/* 37 */       IBlockState iblockstate = entity.getBlock();
/* 38 */       Block block = iblockstate.getBlock();
/* 39 */       BlockPos blockpos = new BlockPos(entity);
/* 40 */       World world = entity.getWorldObj();
/*    */       
/* 42 */       if ((iblockstate != world.getBlockState(blockpos)) && (block.getRenderType() != -1))
/*    */       {
/* 44 */         if (block.getRenderType() == 3)
/*    */         {
/* 46 */           GlStateManager.pushMatrix();
/* 47 */           GlStateManager.translate((float)x, (float)y, (float)z);
/* 48 */           GlStateManager.disableLighting();
/* 49 */           Tessellator tessellator = Tessellator.getInstance();
/* 50 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 51 */           worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
/* 52 */           int i = blockpos.getX();
/* 53 */           int j = blockpos.getY();
/* 54 */           int k = blockpos.getZ();
/* 55 */           worldrenderer.setTranslation(-i - 0.5F, -j, -k - 0.5F);
/* 56 */           BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 57 */           net.minecraft.client.resources.model.IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, null);
/* 58 */           blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
/* 59 */           worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 60 */           tessellator.draw();
/* 61 */           GlStateManager.enableLighting();
/* 62 */           GlStateManager.popMatrix();
/* 63 */           super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityFallingBlock entity)
/*    */   {
/* 74 */     return TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */