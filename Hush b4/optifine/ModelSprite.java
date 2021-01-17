// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.model.ModelRenderer;

public class ModelSprite
{
    private ModelRenderer modelRenderer;
    private int textureOffsetX;
    private int textureOffsetY;
    private float posX;
    private float posY;
    private float posZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private float sizeAdd;
    private float minU;
    private float minV;
    private float maxU;
    private float maxV;
    
    public ModelSprite(final ModelRenderer p_i67_1_, final int p_i67_2_, final int p_i67_3_, final float p_i67_4_, final float p_i67_5_, final float p_i67_6_, final int p_i67_7_, final int p_i67_8_, final int p_i67_9_, final float p_i67_10_) {
        this.modelRenderer = null;
        this.textureOffsetX = 0;
        this.textureOffsetY = 0;
        this.posX = 0.0f;
        this.posY = 0.0f;
        this.posZ = 0.0f;
        this.sizeX = 0;
        this.sizeY = 0;
        this.sizeZ = 0;
        this.sizeAdd = 0.0f;
        this.minU = 0.0f;
        this.minV = 0.0f;
        this.maxU = 0.0f;
        this.maxV = 0.0f;
        this.modelRenderer = p_i67_1_;
        this.textureOffsetX = p_i67_2_;
        this.textureOffsetY = p_i67_3_;
        this.posX = p_i67_4_;
        this.posY = p_i67_5_;
        this.posZ = p_i67_6_;
        this.sizeX = p_i67_7_;
        this.sizeY = p_i67_8_;
        this.sizeZ = p_i67_9_;
        this.sizeAdd = p_i67_10_;
        this.minU = p_i67_2_ / p_i67_1_.textureWidth;
        this.minV = p_i67_3_ / p_i67_1_.textureHeight;
        this.maxU = (p_i67_2_ + p_i67_7_) / p_i67_1_.textureWidth;
        this.maxV = (p_i67_3_ + p_i67_8_) / p_i67_1_.textureHeight;
    }
    
    public void render(final Tessellator p_render_1_, final float p_render_2_) {
        GlStateManager.translate(this.posX * p_render_2_, this.posY * p_render_2_, this.posZ * p_render_2_);
        float f = this.minU;
        float f2 = this.maxU;
        float f3 = this.minV;
        float f4 = this.maxV;
        if (this.modelRenderer.mirror) {
            f = this.maxU;
            f2 = this.minU;
        }
        if (this.modelRenderer.mirrorV) {
            f3 = this.maxV;
            f4 = this.minV;
        }
        renderItemIn2D(p_render_1_, f, f3, f2, f4, this.sizeX, this.sizeY, p_render_2_ * this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
        GlStateManager.translate(-this.posX * p_render_2_, -this.posY * p_render_2_, -this.posZ * p_render_2_);
    }
    
    public static void renderItemIn2D(final Tessellator p_renderItemIn2D_0_, final float p_renderItemIn2D_1_, final float p_renderItemIn2D_2_, final float p_renderItemIn2D_3_, final float p_renderItemIn2D_4_, final int p_renderItemIn2D_5_, final int p_renderItemIn2D_6_, float p_renderItemIn2D_7_, final float p_renderItemIn2D_8_, final float p_renderItemIn2D_9_) {
        if (p_renderItemIn2D_7_ < 6.25E-4f) {
            p_renderItemIn2D_7_ = 6.25E-4f;
        }
        final float f = p_renderItemIn2D_3_ - p_renderItemIn2D_1_;
        final float f2 = p_renderItemIn2D_4_ - p_renderItemIn2D_2_;
        final double d0 = MathHelper.abs(f) * (p_renderItemIn2D_8_ / 16.0f);
        final double d2 = MathHelper.abs(f2) * (p_renderItemIn2D_9_ / 16.0f);
        final WorldRenderer worldrenderer = p_renderItemIn2D_0_.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, d2, 0.0).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(d0, d2, 0.0).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(d0, 0.0, 0.0).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, 0.0, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(d0, 0.0, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(d0, d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(0.0, d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        p_renderItemIn2D_0_.draw();
        final float f3 = 0.5f * f / p_renderItemIn2D_5_;
        final float f4 = 0.5f * f2 / p_renderItemIn2D_6_;
        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int i = 0; i < p_renderItemIn2D_5_; ++i) {
            final float f5 = i / (float)p_renderItemIn2D_5_;
            final float f6 = p_renderItemIn2D_1_ + f * f5 + f3;
            worldrenderer.pos(f5 * d0, d2, p_renderItemIn2D_7_).tex(f6, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos(f5 * d0, d2, 0.0).tex(f6, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos(f5 * d0, 0.0, 0.0).tex(f6, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos(f5 * d0, 0.0, p_renderItemIn2D_7_).tex(f6, p_renderItemIn2D_2_).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int j = 0; j < p_renderItemIn2D_5_; ++j) {
            final float f7 = j / (float)p_renderItemIn2D_5_;
            final float f8 = p_renderItemIn2D_1_ + f * f7 + f3;
            final float f9 = f7 + 1.0f / p_renderItemIn2D_5_;
            worldrenderer.pos(f9 * d0, 0.0, p_renderItemIn2D_7_).tex(f8, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos(f9 * d0, 0.0, 0.0).tex(f8, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos(f9 * d0, d2, 0.0).tex(f8, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos(f9 * d0, d2, p_renderItemIn2D_7_).tex(f8, p_renderItemIn2D_4_).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int k = 0; k < p_renderItemIn2D_6_; ++k) {
            final float f10 = k / (float)p_renderItemIn2D_6_;
            final float f11 = p_renderItemIn2D_2_ + f2 * f10 + f4;
            final float f12 = f10 + 1.0f / p_renderItemIn2D_6_;
            worldrenderer.pos(0.0, f12 * d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f11).endVertex();
            worldrenderer.pos(d0, f12 * d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f11).endVertex();
            worldrenderer.pos(d0, f12 * d2, 0.0).tex(p_renderItemIn2D_3_, f11).endVertex();
            worldrenderer.pos(0.0, f12 * d2, 0.0).tex(p_renderItemIn2D_1_, f11).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int l = 0; l < p_renderItemIn2D_6_; ++l) {
            final float f13 = l / (float)p_renderItemIn2D_6_;
            final float f14 = p_renderItemIn2D_2_ + f2 * f13 + f4;
            worldrenderer.pos(d0, f13 * d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f14).endVertex();
            worldrenderer.pos(0.0, f13 * d2, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f14).endVertex();
            worldrenderer.pos(0.0, f13 * d2, 0.0).tex(p_renderItemIn2D_1_, f14).endVertex();
            worldrenderer.pos(d0, f13 * d2, 0.0).tex(p_renderItemIn2D_3_, f14).endVertex();
        }
        p_renderItemIn2D_0_.draw();
    }
}
