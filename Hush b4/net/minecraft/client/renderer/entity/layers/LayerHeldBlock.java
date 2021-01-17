// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.monster.EntityEnderman;

public class LayerHeldBlock implements LayerRenderer<EntityEnderman>
{
    private final RenderEnderman endermanRenderer;
    
    public LayerHeldBlock(final RenderEnderman endermanRendererIn) {
        this.endermanRenderer = endermanRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityEnderman entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        final IBlockState iblockstate = entitylivingbaseIn.getHeldBlockState();
        if (iblockstate.getBlock().getMaterial() != Material.air) {
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.6875f, -0.75f);
            GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.25f, 0.1875f, 0.25f);
            final float f = 0.5f;
            GlStateManager.scale(-f, -f, f);
            final int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            final int j = i % 65536;
            final int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
