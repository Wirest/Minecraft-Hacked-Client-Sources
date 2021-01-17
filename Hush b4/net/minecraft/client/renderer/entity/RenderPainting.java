// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityPainting;

public class RenderPainting extends Render<EntityPainting>
{
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE;
    
    static {
        KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    }
    
    public RenderPainting(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityPainting entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        final EntityPainting.EnumArt entitypainting$enumart = entity.art;
        final float f = 0.0625f;
        GlStateManager.scale(f, f, f);
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPainting entity) {
        return RenderPainting.KRISTOFFER_PAINTING_TEXTURE;
    }
    
    private void renderPainting(final EntityPainting painting, final int width, final int height, final int textureU, final int textureV) {
        final float f = -width / 2.0f;
        final float f2 = -height / 2.0f;
        final float f3 = 0.5f;
        final float f4 = 0.75f;
        final float f5 = 0.8125f;
        final float f6 = 0.0f;
        final float f7 = 0.0625f;
        final float f8 = 0.75f;
        final float f9 = 0.8125f;
        final float f10 = 0.001953125f;
        final float f11 = 0.001953125f;
        final float f12 = 0.7519531f;
        final float f13 = 0.7519531f;
        final float f14 = 0.0f;
        final float f15 = 0.0625f;
        for (int i = 0; i < width / 16; ++i) {
            for (int j = 0; j < height / 16; ++j) {
                final float f16 = f + (i + 1) * 16;
                final float f17 = f + i * 16;
                final float f18 = f2 + (j + 1) * 16;
                final float f19 = f2 + j * 16;
                this.setLightmap(painting, (f16 + f17) / 2.0f, (f18 + f19) / 2.0f);
                final float f20 = (textureU + width - i * 16) / 256.0f;
                final float f21 = (textureU + width - (i + 1) * 16) / 256.0f;
                final float f22 = (textureV + height - j * 16) / 256.0f;
                final float f23 = (textureV + height - (j + 1) * 16) / 256.0f;
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldrenderer.pos(f16, f19, -f3).tex(f21, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f17, f19, -f3).tex(f20, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f17, f18, -f3).tex(f20, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f16, f18, -f3).tex(f21, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f16, f18, f3).tex(f4, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f17, f18, f3).tex(f5, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f17, f19, f3).tex(f5, f7).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f16, f19, f3).tex(f4, f7).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f16, f18, -f3).tex(f8, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f18, -f3).tex(f9, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f18, f3).tex(f9, f11).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, f3).tex(f8, f11).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f19, f3).tex(f8, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f19, f3).tex(f9, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f19, -f3).tex(f9, f11).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f19, -f3).tex(f8, f11).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, f3).tex(f13, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f19, f3).tex(f13, f15).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f19, -f3).tex(f12, f15).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, -f3).tex(f12, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f18, -f3).tex(f13, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f19, -f3).tex(f13, f15).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f19, f3).tex(f12, f15).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f17, f18, f3).tex(f12, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
            }
        }
    }
    
    private void setLightmap(final EntityPainting painting, final float p_77008_2_, final float p_77008_3_) {
        int i = MathHelper.floor_double(painting.posX);
        final int j = MathHelper.floor_double(painting.posY + p_77008_3_ / 16.0f);
        int k = MathHelper.floor_double(painting.posZ);
        final EnumFacing enumfacing = painting.facingDirection;
        if (enumfacing == EnumFacing.NORTH) {
            i = MathHelper.floor_double(painting.posX + p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.WEST) {
            k = MathHelper.floor_double(painting.posZ - p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.SOUTH) {
            i = MathHelper.floor_double(painting.posX - p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.EAST) {
            k = MathHelper.floor_double(painting.posZ + p_77008_2_ / 16.0f);
        }
        final int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
        final int i2 = l % 65536;
        final int j2 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i2, (float)j2);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
}
