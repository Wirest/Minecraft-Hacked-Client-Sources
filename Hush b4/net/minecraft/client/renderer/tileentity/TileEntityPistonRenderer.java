// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.tileentity.TileEntityPiston;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    private final BlockRendererDispatcher blockRenderer;
    
    public TileEntityPistonRenderer() {
        this.blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityPiston te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final BlockPos blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        final Block block = iblockstate.getBlock();
        if (block.getMaterial() != Material.air && te.getProgress(partialTicks) < 1.0f) {
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            }
            else {
                GlStateManager.shadeModel(7424);
            }
            worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
            worldrenderer.setTranslation((float)x - blockpos.getX() + te.getOffsetX(partialTicks), (float)y - blockpos.getY() + te.getOffsetY(partialTicks), (float)z - blockpos.getZ() + te.getOffsetZ(partialTicks));
            final World world = this.getWorld();
            if (block == Blocks.piston_head && te.getProgress(partialTicks) < 0.5f) {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, true);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending()) {
                final BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = (block == Blocks.sticky_piston) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate2 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockPistonBase.FACING));
                iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, te.getProgress(partialTicks) >= 0.5f);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate2, world, blockpos), iblockstate2, blockpos, worldrenderer, true);
                worldrenderer.setTranslation((float)x - blockpos.getX(), (float)y - blockpos.getY(), (float)z - blockpos.getZ());
                iblockstate.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else {
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, false);
            }
            worldrenderer.setTranslation(0.0, 0.0, 0.0);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
}
