package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderOcelot extends RenderLiving
{
    private static final ResourceLocation blackOcelotTextures = new ResourceLocation("textures/entity/cat/black.png");
    private static final ResourceLocation ocelotTextures = new ResourceLocation("textures/entity/cat/ocelot.png");
    private static final ResourceLocation redOcelotTextures = new ResourceLocation("textures/entity/cat/red.png");
    private static final ResourceLocation siameseOcelotTextures = new ResourceLocation("textures/entity/cat/siamese.png");
    private static final String __OBFID = "CL_00001017";

    public RenderOcelot(RenderManager p_i46151_1_, ModelBase p_i46151_2_, float p_i46151_3_)
    {
        super(p_i46151_1_, p_i46151_2_, p_i46151_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityOcelot p_110775_1_)
    {
        switch (p_110775_1_.getTameSkin())
        {
            case 0:
            default:
                return ocelotTextures;

            case 1:
                return blackOcelotTextures;

            case 2:
                return redOcelotTextures;

            case 3:
                return siameseOcelotTextures;
        }
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityOcelot p_77041_1_, float p_77041_2_)
    {
        super.preRenderCallback(p_77041_1_, p_77041_2_);

        if (p_77041_1_.isTamed())
        {
            GlStateManager.scale(0.8F, 0.8F, 0.8F);
        }
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((EntityOcelot)p_77041_1_, p_77041_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityOcelot)p_110775_1_);
    }
}
