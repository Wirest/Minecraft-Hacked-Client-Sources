package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb extends Render
{
    private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        int var10 = entity.getTextureByXP();
        float var11 = (var10 % 4 * 16 + 0) / 64.0F;
        float var12 = (var10 % 4 * 16 + 16) / 64.0F;
        float var13 = (var10 / 4 * 16 + 0) / 64.0F;
        float var14 = (var10 / 4 * 16 + 16) / 64.0F;
        float var15 = 1.0F;
        float var16 = 0.5F;
        float var17 = 0.25F;
        int var18 = entity.getBrightnessForRender(partialTicks);
        int var19 = var18 % 65536;
        int var20 = var18 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var19 / 1.0F, var20 / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float var27 = 255.0F;
        float var28 = (entity.xpColor + partialTicks) / 2.0F;
        var20 = (int)((MathHelper.sin(var28 + 0.0F) + 1.0F) * 0.5F * var27);
        int var21 = (int)var27;
        int var22 = (int)((MathHelper.sin(var28 + 4.1887903F) + 1.0F) * 0.1F * var27);
        int var23 = var20 << 16 | var21 << 8 | var22;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float var24 = 0.3F;
        GlStateManager.scale(var24, var24, var24);
        Tessellator var25 = Tessellator.getInstance();
        WorldRenderer var26 = var25.getWorldRenderer();
        var26.startDrawingQuads();
        var26.setColorRGBA_I(var23, 128);
        var26.setNormal(0.0F, 1.0F, 0.0F);
        var26.addVertexWithUV(0.0F - var16, 0.0F - var17, 0.0D, var11, var14);
        var26.addVertexWithUV(var15 - var16, 0.0F - var17, 0.0D, var12, var14);
        var26.addVertexWithUV(var15 - var16, 1.0F - var17, 0.0D, var12, var13);
        var26.addVertexWithUV(0.0F - var16, 1.0F - var17, 0.0D, var11, var13);
        var25.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getTexture(EntityXPOrb xpOrb)
    {
        return experienceOrbTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getTexture((EntityXPOrb)entity);
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
        this.doRender((EntityXPOrb)entity, x, y, z, entityYaw, partialTicks);
    }
}
