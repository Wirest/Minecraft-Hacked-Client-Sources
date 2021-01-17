// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import optifine.CustomColors;
import optifine.Config;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb extends Render
{
    private static final ResourceLocation experienceOrbTextures;
    private static final String __OBFID = "CL_00000993";
    
    static {
        experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");
    }
    
    public RenderXPOrb(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
    
    public void doRender(final EntityXPOrb entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        final int i = entity.getTextureByXP();
        final float f = (i % 4 * 16 + 0) / 64.0f;
        final float f2 = (i % 4 * 16 + 16) / 64.0f;
        final float f3 = (i / 4 * 16 + 0) / 64.0f;
        final float f4 = (i / 4 * 16 + 16) / 64.0f;
        final float f5 = 1.0f;
        final float f6 = 0.5f;
        final float f7 = 0.25f;
        final int j = entity.getBrightnessForRender(partialTicks);
        final int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0f, l / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f8 = 255.0f;
        final float f9 = (entity.xpColor + partialTicks) / 2.0f;
        l = (int)((MathHelper.sin(f9 + 0.0f) + 1.0f) * 0.5f * 255.0f);
        final boolean flag = true;
        final int i2 = (int)((MathHelper.sin(f9 + 4.1887903f) + 1.0f) * 0.1f * 255.0f);
        GlStateManager.rotate(180.0f - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        final float f10 = 0.3f;
        GlStateManager.scale(0.3f, 0.3f, 0.3f);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        int j2 = l;
        int k2 = 255;
        int l2 = i2;
        if (Config.isCustomColors()) {
            final int i3 = CustomColors.getXpOrbColor(f9);
            if (i3 >= 0) {
                j2 = (i3 >> 16 & 0xFF);
                k2 = (i3 >> 8 & 0xFF);
                l2 = (i3 >> 0 & 0xFF);
            }
        }
        worldrenderer.pos(0.0f - f6, 0.0f - f7, 0.0).tex(f, f4).color(j2, k2, l2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(f5 - f6, 0.0f - f7, 0.0).tex(f2, f4).color(j2, k2, l2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(f5 - f6, 1.0f - f7, 0.0).tex(f2, f3).color(j2, k2, l2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.0f - f6, 1.0f - f7, 0.0).tex(f, f3).color(j2, k2, l2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    protected ResourceLocation getEntityTexture(final EntityXPOrb entity) {
        return RenderXPOrb.experienceOrbTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return this.getEntityTexture((EntityXPOrb)entity);
    }
    
    @Override
    public void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.doRender((EntityXPOrb)entity, x, y, z, entityYaw, partialTicks);
    }
}
