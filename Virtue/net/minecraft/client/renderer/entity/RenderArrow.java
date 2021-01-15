package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArrow extends Render
{
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
    private static final String __OBFID = "CL_00000978";

    public RenderArrow(RenderManager p_i46193_1_)
    {
        super(p_i46193_1_);
    }

    public void doRender(EntityArrow p_180551_1_, double p_180551_2_, double p_180551_4_, double p_180551_6_, float p_180551_8_, float p_180551_9_)
    {
        this.bindEntityTexture(p_180551_1_);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180551_2_, (float)p_180551_4_, (float)p_180551_6_);
        GlStateManager.rotate(p_180551_1_.prevRotationYaw + (p_180551_1_.rotationYaw - p_180551_1_.prevRotationYaw) * p_180551_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(p_180551_1_.prevRotationPitch + (p_180551_1_.rotationPitch - p_180551_1_.prevRotationPitch) * p_180551_9_, 0.0F, 0.0F, 1.0F);
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        byte var12 = 0;
        float var13 = 0.0F;
        float var14 = 0.5F;
        float var15 = (float)(0 + var12 * 10) / 32.0F;
        float var16 = (float)(5 + var12 * 10) / 32.0F;
        float var17 = 0.0F;
        float var18 = 0.15625F;
        float var19 = (float)(5 + var12 * 10) / 32.0F;
        float var20 = (float)(10 + var12 * 10) / 32.0F;
        float var21 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float var22 = (float)p_180551_1_.arrowShake - p_180551_9_;

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
        var11.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)var17, (double)var19);
        var11.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)var18, (double)var19);
        var11.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)var18, (double)var20);
        var11.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)var17, (double)var20);
        var10.draw();
        GL11.glNormal3f(-var21, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)var17, (double)var19);
        var11.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)var18, (double)var19);
        var11.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)var18, (double)var20);
        var11.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)var17, (double)var20);
        var10.draw();

        for (int var24 = 0; var24 < 4; ++var24)
        {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, var21);
            var11.startDrawingQuads();
            var11.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)var13, (double)var15);
            var11.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)var14, (double)var15);
            var11.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)var14, (double)var16);
            var11.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)var13, (double)var16);
            var10.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(p_180551_1_, p_180551_2_, p_180551_4_, p_180551_6_, p_180551_8_, p_180551_9_);
    }

    protected ResourceLocation getEntityTexture(EntityArrow p_180550_1_)
    {
        return arrowTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityArrow)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityArrow)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
