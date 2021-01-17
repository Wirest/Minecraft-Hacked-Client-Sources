// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityFallingBlock;

public class RenderFallingBlock extends Render<EntityFallingBlock>
{
    public RenderFallingBlock(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityFallingBlock entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.getBlock() != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            final IBlockState iblockstate = entity.getBlock();
            final Block block = iblockstate.getBlock();
            final BlockPos blockpos = new BlockPos(entity);
            final World world = entity.getWorldObj();
            if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1 && block.getRenderType() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)x, (float)y, (float)z);
                GlStateManager.disableLighting();
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                final int i = blockpos.getX();
                final int j = blockpos.getY();
                final int k = blockpos.getZ();
                worldrenderer.setTranslation(-i - 0.5f, -j, -k - 0.5f);
                final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                final IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, null);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                worldrenderer.setTranslation(0.0, 0.0, 0.0);
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityFallingBlock entity) {
        return TextureMap.locationBlocksTexture;
    }
}
