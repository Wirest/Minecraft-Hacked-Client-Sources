package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStandArmor extends ModelBiped
{
    private static final String __OBFID = "CL_00002632";

    public ModelArmorStandArmor()
    {
        this(0.0F);
    }

    public ModelArmorStandArmor(float p_i46307_1_)
    {
        this(p_i46307_1_, 64, 32);
    }

    protected ModelArmorStandArmor(float p_i46308_1_, int p_i46308_2_, int p_i46308_3_)
    {
        super(p_i46308_1_, 0.0F, p_i46308_2_, p_i46308_3_);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        if (p_78087_7_ instanceof EntityArmorStand)
        {
            EntityArmorStand var8 = (EntityArmorStand)p_78087_7_;
            this.bipedHead.rotateAngleX = 0.017453292F * var8.getHeadRotation().func_179415_b();
            this.bipedHead.rotateAngleY = 0.017453292F * var8.getHeadRotation().func_179416_c();
            this.bipedHead.rotateAngleZ = 0.017453292F * var8.getHeadRotation().func_179413_d();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * var8.getBodyRotation().func_179415_b();
            this.bipedBody.rotateAngleY = 0.017453292F * var8.getBodyRotation().func_179416_c();
            this.bipedBody.rotateAngleZ = 0.017453292F * var8.getBodyRotation().func_179413_d();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * var8.getLeftArmRotation().func_179415_b();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * var8.getLeftArmRotation().func_179416_c();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * var8.getLeftArmRotation().func_179413_d();
            this.bipedRightArm.rotateAngleX = 0.017453292F * var8.getRightArmRotation().func_179415_b();
            this.bipedRightArm.rotateAngleY = 0.017453292F * var8.getRightArmRotation().func_179416_c();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * var8.getRightArmRotation().func_179413_d();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * var8.getLeftLegRotation().func_179415_b();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * var8.getLeftLegRotation().func_179416_c();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * var8.getLeftLegRotation().func_179413_d();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * var8.getRightLegRotation().func_179415_b();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * var8.getRightLegRotation().func_179416_c();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * var8.getRightLegRotation().func_179413_d();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            func_178685_a(this.bipedHead, this.bipedHeadwear);
        }
    }
}
