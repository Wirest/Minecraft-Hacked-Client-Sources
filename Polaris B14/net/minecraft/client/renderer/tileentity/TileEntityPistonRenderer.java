/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockPistonBase;
/*    */ import net.minecraft.block.BlockPistonExtension;
/*    */ import net.minecraft.block.BlockPistonExtension.EnumPistonType;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockModelRenderer;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderHelper;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntityPiston;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
/*    */ {
/* 23 */   private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
/*    */   
/*    */   public void renderTileEntityAt(TileEntityPiston te, double x, double y, double z, float partialTicks, int destroyStage)
/*    */   {
/* 27 */     BlockPos blockpos = te.getPos();
/* 28 */     IBlockState iblockstate = te.getPistonState();
/* 29 */     Block block = iblockstate.getBlock();
/*    */     
/* 31 */     if ((block.getMaterial() != net.minecraft.block.material.Material.air) && (te.getProgress(partialTicks) < 1.0F))
/*    */     {
/* 33 */       Tessellator tessellator = Tessellator.getInstance();
/* 34 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 35 */       bindTexture(TextureMap.locationBlocksTexture);
/* 36 */       RenderHelper.disableStandardItemLighting();
/* 37 */       GlStateManager.blendFunc(770, 771);
/* 38 */       GlStateManager.enableBlend();
/* 39 */       GlStateManager.disableCull();
/*    */       
/* 41 */       if (Minecraft.isAmbientOcclusionEnabled())
/*    */       {
/* 43 */         GlStateManager.shadeModel(7425);
/*    */       }
/*    */       else
/*    */       {
/* 47 */         GlStateManager.shadeModel(7424);
/*    */       }
/*    */       
/* 50 */       worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
/* 51 */       worldrenderer.setTranslation((float)x - blockpos.getX() + te.getOffsetX(partialTicks), (float)y - blockpos.getY() + te.getOffsetY(partialTicks), (float)z - blockpos.getZ() + te.getOffsetZ(partialTicks));
/* 52 */       net.minecraft.world.World world = getWorld();
/*    */       
/* 54 */       if ((block == Blocks.piston_head) && (te.getProgress(partialTicks) < 0.5F))
/*    */       {
/* 56 */         iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
/* 57 */         this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
/*    */       }
/* 59 */       else if ((te.shouldPistonHeadBeRendered()) && (!te.isExtending()))
/*    */       {
/* 61 */         BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
/* 62 */         IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, (net.minecraft.util.EnumFacing)iblockstate.getValue(BlockPistonBase.FACING));
/* 63 */         iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(te.getProgress(partialTicks) >= 0.5F));
/* 64 */         this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate1, world, blockpos), iblockstate1, blockpos, worldrenderer, true);
/* 65 */         worldrenderer.setTranslation((float)x - blockpos.getX(), (float)y - blockpos.getY(), (float)z - blockpos.getZ());
/* 66 */         iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
/* 67 */         this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
/*    */       }
/*    */       else
/*    */       {
/* 71 */         this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, false);
/*    */       }
/*    */       
/* 74 */       worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 75 */       tessellator.draw();
/* 76 */       RenderHelper.enableStandardItemLighting();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityPistonRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */