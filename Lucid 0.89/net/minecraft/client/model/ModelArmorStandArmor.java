package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStandArmor extends ModelBiped
{

    public ModelArmorStandArmor()
    {
        this(0.0F);
    }

    public ModelArmorStandArmor(float modelSize)
    {
        this(modelSize, 64, 32);
    }

    protected ModelArmorStandArmor(float modelSize, int textureWidthIn, int textureHeightIn)
    {
        super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        if (p_78087_7_ instanceof EntityArmorStand)
        {
            EntityArmorStand var8 = (EntityArmorStand)p_78087_7_;
            this.bipedHead.rotateAngleX = 0.017453292F * var8.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * var8.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * var8.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * var8.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * var8.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * var8.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * var8.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * var8.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * var8.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * var8.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * var8.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * var8.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * var8.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * var8.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * var8.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * var8.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * var8.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * var8.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        }
    }
}
