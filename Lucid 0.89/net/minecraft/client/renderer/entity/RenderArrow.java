package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderArrow extends Render
{
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    public RenderArrow(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityArrow arrow, double x, double y, double z, float p_180551_8_, float p_180551_9_)
    {
        this.bindEntityTexture(arrow);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(arrow.prevRotationYaw + (arrow.rotationYaw - arrow.prevRotationYaw) * p_180551_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(arrow.prevRotationPitch + (arrow.rotationPitch - arrow.prevRotationPitch) * p_180551_9_, 0.0F, 0.0F, 1.0F);
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        byte var12 = 0;
        float var13 = 0.0F;
        float var14 = 0.5F;
        float var15 = (0 + var12 * 10) / 32.0F;
        float var16 = (5 + var12 * 10) / 32.0F;
        float var17 = 0.0F;
        float var18 = 0.15625F;
        float var19 = (5 + var12 * 10) / 32.0F;
        float var20 = (10 + var12 * 10) / 32.0F;
        float var21 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float var22 = arrow.arrowShake - p_180551_9_;

        if (var22 > 0.0F)
        {
            float var23 = -MathHelper.sin(var22 * 3.0F) * var22;
            GlStateManager.rotate(var23, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(var21, var21, var21);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(var21, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.addVertexWithUV(-7.0D, -2.0D, -2.0D, var17, var19);
        var11.addVertexWithUV(-7.0D, -2.0D, 2.0D, var18, var19);
        var11.addVertexWithUV(-7.0D, 2.0D, 2.0D, var18, var20);
        var11.addVertexWithUV(-7.0D, 2.0D, -2.0D, var17, var20);
        var10.draw();
        GL11.glNormal3f(-var21, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.addVertexWithUV(-7.0D, 2.0D, -2.0D, var17, var19);
        var11.addVertexWithUV(-7.0D, 2.0D, 2.0D, var18, var19);
        var11.addVertexWithUV(-7.0D, -2.0D, 2.0D, var18, var20);
        var11.addVertexWithUV(-7.0D, -2.0D, -2.0D, var17, var20);
        var10.draw();

        for (int var24 = 0; var24 < 4; ++var24)
        {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, var21);
            var11.startDrawingQuads();
            var11.addVertexWithUV(-8.0D, -2.0D, 0.0D, var13, var15);
            var11.addVertexWithUV(8.0D, -2.0D, 0.0D, var14, var15);
            var11.addVertexWithUV(8.0D, 2.0D, 0.0D, var14, var16);
            var11.addVertexWithUV(-8.0D, 2.0D, 0.0D, var13, var16);
            var10.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(arrow, x, y, z, p_180551_8_, p_180551_9_);
    }

    protected ResourceLocation getEntityTexture(EntityArrow p_180550_1_)
    {
        return arrowTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityArrow)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    @Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.doRender((EntityArrow)entity, x, y, z, entityYaw, partialTicks);
    }
}
