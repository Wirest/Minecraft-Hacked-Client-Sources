// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityTNTPrimed;

public class RenderTNTPrimed extends Render<EntityTNTPrimed>
{
    public RenderTNTPrimed(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityTNTPrimed entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.5f, (float)z);
        if (entity.fuse - partialTicks + 1.0f < 10.0f) {
            float f = 1.0f - (entity.fuse - partialTicks + 1.0f) / 10.0f;
            f = MathHelper.clamp_float(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            final float f2 = 1.0f + f * 0.3f;
            GlStateManager.scale(f2, f2, f2);
        }
        final float f3 = (1.0f - (entity.fuse - partialTicks + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5f, -0.5f, 0.5f);
        blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (entity.fuse / 5 % 2 == 0) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f3);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityTNTPrimed entity) {
        return TextureMap.locationBlocksTexture;
    }
}
