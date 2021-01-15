package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderChicken extends RenderLiving
{
    private static final ResourceLocation chickenTextures = new ResourceLocation("textures/entity/chicken.png");

    public RenderChicken(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected ResourceLocation func_180568_a(EntityChicken chicken)
    {
        return chickenTextures;
    }

    protected float func_180569_a(EntityChicken chicken, float p_180569_2_)
    {
        float var3 = chicken.field_70888_h + (chicken.field_70886_e - chicken.field_70888_h) * p_180569_2_;
        float var4 = chicken.field_70884_g + (chicken.destPos - chicken.field_70884_g) * p_180569_2_;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
	protected float handleRotationFloat(EntityLivingBase livingBase, float p_77044_2_)
    {
        return this.func_180569_a((EntityChicken)livingBase, p_77044_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180568_a((EntityChicken)entity);
    }
}
