package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArrow extends Render<EntityArrow>
{
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    public RenderArrow(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = (float)(i * 10) / 32.0F;
        float f3 = (float)(5 + i * 10) / 32.0F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = (float)(5 + i * 10) / 32.0F;
        float f7 = (float)(10 + i * 10) / 32.0F;
        float f8 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f9 = (float)entity.arrowShake - partialTicks;

        if (f9 > 0.0F)
        {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f8, f8, f8);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f8, 0.0F, 0.0F);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(-7.0D, -2.0D, -2.0D).func_181673_a(f4, f6).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, -2.0D, 2.0D).func_181673_a(f5, f6).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, 2.0D, 2.0D).func_181673_a(f5, f7).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, 2.0D, -2.0D).func_181673_a(f4, f7).func_181675_d();
        tessellator.draw();
        GL11.glNormal3f(-f8, 0.0F, 0.0F);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(-7.0D, 2.0D, -2.0D).func_181673_a(f4, f6).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, 2.0D, 2.0D).func_181673_a(f5, f6).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, -2.0D, 2.0D).func_181673_a(f5, f7).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, -2.0D, -2.0D).func_181673_a(f4, f7).func_181675_d();
        tessellator.draw();

        for (int j = 0; j < 4; ++j)
        {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f8);
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
            worldrenderer.func_181662_b(-8.0D, -2.0D, 0.0D).func_181673_a(f, f2).func_181675_d();
            worldrenderer.func_181662_b(8.0D, -2.0D, 0.0D).func_181673_a(f1, f2).func_181675_d();
            worldrenderer.func_181662_b(8.0D, 2.0D, 0.0D).func_181673_a(f1, f3).func_181675_d();
            worldrenderer.func_181662_b(-8.0D, 2.0D, 0.0D).func_181673_a(f, f3).func_181675_d();
            tessellator.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityArrow entity)
    {
        return arrowTextures;
    }
}
