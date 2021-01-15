package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderCreeper extends RenderLiving
{
    private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderCreeper(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerCreeperCharge(this));
    }

    /**
     * Updates creeper scale in prerender callback
     */
    protected void preRenderCallback(EntityCreeper entityCreeperIn, float renderTick)
    {
        float var3 = entityCreeperIn.getCreeperFlashIntensity(renderTick);
        float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;
        var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
        var3 *= var3;
        var3 *= var3;
        float var5 = (1.0F + var3 * 0.4F) * var4;
        float var6 = (1.0F + var3 * 0.1F) / var4;
        GlStateManager.scale(var5, var6, var5);
    }

    /**
     * Returns an ARGB int color back. Args: entityCreeper, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityCreeper entityCreeperIn, float lightBrightness, float partialTickTime)
    {
        float var4 = entityCreeperIn.getCreeperFlashIntensity(partialTickTime);

        if ((int)(var4 * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int var5 = (int)(var4 * 0.2F * 255.0F);
            var5 = MathHelper.clamp_int(var5, 0, 255);
            return var5 << 24 | 16777215;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCreeper entity)
    {
        return creeperTextures;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
	protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
        this.preRenderCallback((EntityCreeper)entitylivingbaseIn, partialTickTime);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    @Override
	protected int getColorMultiplier(EntityLivingBase entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        return this.getColorMultiplier((EntityCreeper)entitylivingbaseIn, lightBrightness, partialTickTime);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityCreeper)entity);
    }
}
