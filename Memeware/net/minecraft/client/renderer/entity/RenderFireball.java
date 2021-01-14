package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends Render {
    private float scale;
    private static final String __OBFID = "CL_00000995";

    public RenderFireball(RenderManager p_i46176_1_, float p_i46176_2_) {
        super(p_i46176_1_);
        this.scale = p_i46176_2_;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityFireball p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(p_76986_1_);
        GlStateManager.translate((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        GlStateManager.enableRescaleNormal();
        float var10 = this.scale;
        GlStateManager.scale(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
        TextureAtlasSprite var11 = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        float var14 = var11.getMinU();
        float var15 = var11.getMaxU();
        float var16 = var11.getMinV();
        float var17 = var11.getMaxV();
        float var18 = 1.0F;
        float var19 = 0.5F;
        float var20 = 0.25F;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var13.startDrawingQuads();
        var13.func_178980_d(0.0F, 1.0F, 0.0F);
        var13.addVertexWithUV((double) (0.0F - var19), (double) (0.0F - var20), 0.0D, (double) var14, (double) var17);
        var13.addVertexWithUV((double) (var18 - var19), (double) (0.0F - var20), 0.0D, (double) var15, (double) var17);
        var13.addVertexWithUV((double) (var18 - var19), (double) (1.0F - var20), 0.0D, (double) var15, (double) var16);
        var13.addVertexWithUV((double) (0.0F - var19), (double) (1.0F - var20), 0.0D, (double) var14, (double) var16);
        var12.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected ResourceLocation func_180556_a(EntityFireball p_180556_1_) {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180556_a((EntityFireball) p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityFireball) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
