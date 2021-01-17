// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityArrow;

public class RenderArrow extends Render<EntityArrow>
{
    private static final ResourceLocation arrowTextures;
    
    static {
        arrowTextures = new ResourceLocation("textures/entity/arrow.png");
    }
    
    public RenderArrow(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityArrow entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0f, 0.0f, 1.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final int i = 0;
        final float f = 0.0f;
        final float f2 = 0.5f;
        final float f3 = (0 + i * 10) / 32.0f;
        final float f4 = (5 + i * 10) / 32.0f;
        final float f5 = 0.0f;
        final float f6 = 0.15625f;
        final float f7 = (5 + i * 10) / 32.0f;
        final float f8 = (10 + i * 10) / 32.0f;
        final float f9 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        final float f10 = entity.arrowShake - partialTicks;
        if (f10 > 0.0f) {
            final float f11 = -MathHelper.sin(f10 * 3.0f) * f10;
            GlStateManager.rotate(f11, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(f9, f9, f9);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);
        GL11.glNormal3f(f9, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, -2.0, -2.0).tex(f5, f7).endVertex();
        worldrenderer.pos(-7.0, -2.0, 2.0).tex(f6, f7).endVertex();
        worldrenderer.pos(-7.0, 2.0, 2.0).tex(f6, f8).endVertex();
        worldrenderer.pos(-7.0, 2.0, -2.0).tex(f5, f8).endVertex();
        tessellator.draw();
        GL11.glNormal3f(-f9, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 2.0, -2.0).tex(f5, f7).endVertex();
        worldrenderer.pos(-7.0, 2.0, 2.0).tex(f6, f7).endVertex();
        worldrenderer.pos(-7.0, -2.0, 2.0).tex(f6, f8).endVertex();
        worldrenderer.pos(-7.0, -2.0, -2.0).tex(f5, f8).endVertex();
        tessellator.draw();
        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glNormal3f(0.0f, 0.0f, f9);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-8.0, -2.0, 0.0).tex(f, f3).endVertex();
            worldrenderer.pos(8.0, -2.0, 0.0).tex(f2, f3).endVertex();
            worldrenderer.pos(8.0, 2.0, 0.0).tex(f2, f4).endVertex();
            worldrenderer.pos(-8.0, 2.0, 0.0).tex(f, f4).endVertex();
            tessellator.draw();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityArrow entity) {
        return RenderArrow.arrowTextures;
    }
}
