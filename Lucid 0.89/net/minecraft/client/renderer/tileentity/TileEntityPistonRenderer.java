package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer
{
    private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

    public void func_178461_a(TileEntityPiston piston, double p_178461_2_, double p_178461_4_, double p_178461_6_, float p_178461_8_, int p_178461_9_)
    {
        BlockPos var10 = piston.getPos();
        IBlockState var11 = piston.getPistonState();
        Block var12 = var11.getBlock();

        if (var12.getMaterial() != Material.air && piston.func_145860_a(p_178461_8_) < 1.0F)
        {
            Tessellator var13 = Tessellator.getInstance();
            WorldRenderer var14 = var13.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.shadeModel(7425);
            }
            else
            {
                GlStateManager.shadeModel(7424);
            }

            var14.startDrawingQuads();
            var14.setVertexFormat(DefaultVertexFormats.BLOCK);
            var14.setTranslation((float)p_178461_2_ - var10.getX() + piston.func_174929_b(p_178461_8_), (float)p_178461_4_ - var10.getY() + piston.func_174928_c(p_178461_8_), (float)p_178461_6_ - var10.getZ() + piston.func_174926_d(p_178461_8_));
            var14.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            World var15 = this.getWorld();

            if (var12 == Blocks.piston_head && piston.func_145860_a(p_178461_8_) < 0.5F)
            {
                var11 = var11.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, true);
            }
            else if (piston.shouldPistonHeadBeRendered() && !piston.isExtending())
            {
                BlockPistonExtension.EnumPistonType var16 = var12 == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState var17 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, var16).withProperty(BlockPistonExtension.FACING, var11.getValue(BlockPistonBase.FACING));
                var17 = var17.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(piston.func_145860_a(p_178461_8_) >= 0.5F));
                this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var17, var15, var10), var17, var10, var14, true);
                var14.setTranslation((float)p_178461_2_ - var10.getX(), (float)p_178461_4_ - var10.getY(), (float)p_178461_6_ - var10.getZ());
                var11.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, true);
            }
            else
            {
                this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, false);
            }

            var14.setTranslation(0.0D, 0.0D, 0.0D);
            var13.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.func_178461_a((TileEntityPiston)te, x, y, z, partialTicks, destroyStage);
    }
}
