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
    private static final String __OBFID = "CL_00000985";

    public RenderCreeper(RenderManager p_i46186_1_)
    {
        super(p_i46186_1_, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerCreeperCharge(this));
    }

    protected void func_180570_a(EntityCreeper p_180570_1_, float p_180570_2_)
    {
        float var3 = p_180570_1_.getCreeperFlashIntensity(p_180570_2_);
        float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;
        var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
        var3 *= var3;
        var3 *= var3;
        float var5 = (1.0F + var3 * 0.4F) * var4;
        float var6 = (1.0F + var3 * 0.1F) / var4;
        GlStateManager.scale(var5, var6, var5);
    }

    protected int func_180571_a(EntityCreeper p_180571_1_, float p_180571_2_, float p_180571_3_)
    {
        float var4 = p_180571_1_.getCreeperFlashIntensity(p_180571_3_);

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
    protected ResourceLocation getEntityTexture(EntityCreeper p_110775_1_)
    {
        return creeperTextures;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.func_180570_a((EntityCreeper)p_77041_1_, p_77041_2_);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_)
    {
        return this.func_180571_a((EntityCreeper)p_77030_1_, p_77030_2_, p_77030_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityCreeper)p_110775_1_);
    }
}
