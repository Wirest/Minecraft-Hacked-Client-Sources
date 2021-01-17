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
    private static final String __OBFID = "CL_00000983";

    public RenderChicken(RenderManager p_i46188_1_, ModelBase p_i46188_2_, float p_i46188_3_)
    {
        super(p_i46188_1_, p_i46188_2_, p_i46188_3_);
    }

    protected ResourceLocation func_180568_a(EntityChicken p_180568_1_)
    {
        return chickenTextures;
    }

    protected float func_180569_a(EntityChicken p_180569_1_, float p_180569_2_)
    {
        float var3 = p_180569_1_.field_70888_h + (p_180569_1_.field_70886_e - p_180569_1_.field_70888_h) * p_180569_2_;
        float var4 = p_180569_1_.field_70884_g + (p_180569_1_.destPos - p_180569_1_.field_70884_g) * p_180569_2_;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_)
    {
        return this.func_180569_a((EntityChicken)p_77044_1_, p_77044_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.func_180568_a((EntityChicken)p_110775_1_);
    }
}
