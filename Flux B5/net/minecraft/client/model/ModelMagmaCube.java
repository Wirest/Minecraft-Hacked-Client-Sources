package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

public class ModelMagmaCube extends ModelBase
{
    ModelRenderer[] segments = new ModelRenderer[8];
    ModelRenderer core;
    private static final String __OBFID = "CL_00000842";

    public ModelMagmaCube()
    {
        for (int var1 = 0; var1 < this.segments.length; ++var1)
        {
            byte var2 = 0;
            int var3 = var1;

            if (var1 == 2)
            {
                var2 = 24;
                var3 = 10;
            }
            else if (var1 == 3)
            {
                var2 = 24;
                var3 = 19;
            }

            this.segments[var1] = new ModelRenderer(this, var2, var3);
            this.segments[var1].addBox(-4.0F, (float)(16 + var1), -4.0F, 8, 1, 8);
        }

        this.core = new ModelRenderer(this, 0, 16);
        this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        EntityMagmaCube var5 = (EntityMagmaCube)p_78086_1_;
        float var6 = var5.prevSquishFactor + (var5.squishFactor - var5.prevSquishFactor) * p_78086_4_;

        if (var6 < 0.0F)
        {
            var6 = 0.0F;
        }

        for (int var7 = 0; var7 < this.segments.length; ++var7)
        {
            this.segments[var7].rotationPointY = (float)(-(4 - var7)) * var6 * 1.7F;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.core.render(p_78088_7_);

        for (int var8 = 0; var8 < this.segments.length; ++var8)
        {
            this.segments[var8].render(p_78088_7_);
        }
    }
}
