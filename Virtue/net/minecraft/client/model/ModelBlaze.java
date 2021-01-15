package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBlaze extends ModelBase
{
    /** The sticks that fly around the Blaze. */
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private ModelRenderer blazeHead;
    private static final String __OBFID = "CL_00000831";

    public ModelBlaze()
    {
        for (int var1 = 0; var1 < this.blazeSticks.length; ++var1)
        {
            this.blazeSticks[var1] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[var1].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }

        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.blazeHead.render(p_78088_7_);

        for (int var8 = 0; var8 < this.blazeSticks.length; ++var8)
        {
            this.blazeSticks[var8].render(p_78088_7_);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        float var8 = p_78087_3_ * (float)Math.PI * -0.1F;
        int var9;

        for (var9 = 0; var9 < 4; ++var9)
        {
            this.blazeSticks[var9].rotationPointY = -2.0F + MathHelper.cos(((float)(var9 * 2) + p_78087_3_) * 0.25F);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 9.0F;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 9.0F;
            ++var8;
        }

        var8 = ((float)Math.PI / 4F) + p_78087_3_ * (float)Math.PI * 0.03F;

        for (var9 = 4; var9 < 8; ++var9)
        {
            this.blazeSticks[var9].rotationPointY = 2.0F + MathHelper.cos(((float)(var9 * 2) + p_78087_3_) * 0.25F);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 7.0F;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 7.0F;
            ++var8;
        }

        var8 = 0.47123894F + p_78087_3_ * (float)Math.PI * -0.05F;

        for (var9 = 8; var9 < 12; ++var9)
        {
            this.blazeSticks[var9].rotationPointY = 11.0F + MathHelper.cos(((float)var9 * 1.5F + p_78087_3_) * 0.5F);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 5.0F;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 5.0F;
            ++var8;
        }

        this.blazeHead.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.blazeHead.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
    }
}
