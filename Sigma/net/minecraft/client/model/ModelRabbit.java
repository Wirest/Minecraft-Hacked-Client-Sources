package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.MathHelper;

public class ModelRabbit extends ModelBase {
    /**
     * The Rabbit's Left Foot
     */
    ModelRenderer rabbitLeftFoot;

    /**
     * The Rabbit's Right Foot
     */
    ModelRenderer rabbitRightFoot;

    /**
     * The Rabbit's Left Thigh
     */
    ModelRenderer rabbitLeftThigh;

    /**
     * The Rabbit's Right Thigh
     */
    ModelRenderer rabbitRightThigh;

    /**
     * The Rabbit's Body
     */
    ModelRenderer rabbitBody;

    /**
     * The Rabbit's Left Arm
     */
    ModelRenderer rabbitLeftArm;

    /**
     * The Rabbit's Right Arm
     */
    ModelRenderer rabbitRightArm;

    /**
     * The Rabbit's Head
     */
    ModelRenderer rabbitHead;

    /**
     * The Rabbit's Right Ear
     */
    ModelRenderer rabbitRightEar;

    /**
     * The Rabbit's Left Ear
     */
    ModelRenderer rabbitLeftEar;

    /**
     * The Rabbit's Tail
     */
    ModelRenderer rabbitTail;

    /**
     * The Rabbit's Nose
     */
    ModelRenderer rabbitNose;
    private float field_178701_m = 0.0F;
    private float field_178699_n = 0.0F;
    private static final String __OBFID = "CL_00002625";

    public ModelRabbit() {
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
        this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftFoot.mirror = true;
        this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
        this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
        this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightFoot.mirror = true;
        this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
        this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
        this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftThigh.mirror = true;
        this.setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
        this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
        this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightThigh.mirror = true;
        this.setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
        this.rabbitBody = new ModelRenderer(this, 0, 0);
        this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.rabbitBody.mirror = true;
        this.setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
        this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
        this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
        this.rabbitLeftArm.mirror = true;
        this.setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
        this.rabbitRightArm = new ModelRenderer(this, 0, 15);
        this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
        this.rabbitRightArm.mirror = true;
        this.setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
        this.rabbitHead = new ModelRenderer(this, 32, 0);
        this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitHead.mirror = true;
        this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
        this.rabbitRightEar = new ModelRenderer(this, 52, 0);
        this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitRightEar.mirror = true;
        this.setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
        this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
        this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitLeftEar.mirror = true;
        this.setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
        this.rabbitTail = new ModelRenderer(this, 52, 6);
        this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
        this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
        this.rabbitTail.mirror = true;
        this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
        this.rabbitNose = new ModelRenderer(this, 32, 9);
        this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
        this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitNose.mirror = true;
        this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
    }

    private void setRotationOffset(ModelRenderer p_178691_1_, float p_178691_2_, float p_178691_3_, float p_178691_4_) {
        p_178691_1_.rotateAngleX = p_178691_2_;
        p_178691_1_.rotateAngleY = p_178691_3_;
        p_178691_1_.rotateAngleZ = p_178691_4_;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.isChild) {
            float var8 = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
            this.rabbitHead.render(p_78088_7_);
            this.rabbitLeftEar.render(p_78088_7_);
            this.rabbitRightEar.render(p_78088_7_);
            this.rabbitNose.render(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.rabbitLeftFoot.render(p_78088_7_);
            this.rabbitRightFoot.render(p_78088_7_);
            this.rabbitLeftThigh.render(p_78088_7_);
            this.rabbitRightThigh.render(p_78088_7_);
            this.rabbitBody.render(p_78088_7_);
            this.rabbitLeftArm.render(p_78088_7_);
            this.rabbitRightArm.render(p_78088_7_);
            this.rabbitTail.render(p_78088_7_);
            GlStateManager.popMatrix();
        } else {
            this.rabbitLeftFoot.render(p_78088_7_);
            this.rabbitRightFoot.render(p_78088_7_);
            this.rabbitLeftThigh.render(p_78088_7_);
            this.rabbitRightThigh.render(p_78088_7_);
            this.rabbitBody.render(p_78088_7_);
            this.rabbitLeftArm.render(p_78088_7_);
            this.rabbitRightArm.render(p_78088_7_);
            this.rabbitHead.render(p_78088_7_);
            this.rabbitRightEar.render(p_78088_7_);
            this.rabbitLeftEar.render(p_78088_7_);
            this.rabbitTail.render(p_78088_7_);
            this.rabbitNose.render(p_78088_7_);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        float var8 = p_78087_3_ - (float) p_78087_7_.ticksExisted;
        EntityRabbit var9 = (EntityRabbit) p_78087_7_;
        this.rabbitNose.rotateAngleX = this.rabbitHead.rotateAngleX = this.rabbitRightEar.rotateAngleX = this.rabbitLeftEar.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.rabbitNose.rotateAngleY = this.rabbitHead.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
        this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
        this.field_178701_m = MathHelper.sin(var9.func_175521_o(var8) * (float) Math.PI);
        this.rabbitLeftThigh.rotateAngleX = this.rabbitRightThigh.rotateAngleX = (this.field_178701_m * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitLeftFoot.rotateAngleX = this.rabbitRightFoot.rotateAngleX = this.field_178701_m * 50.0F * 0.017453292F;
        this.rabbitLeftArm.rotateAngleX = this.rabbitRightArm.rotateAngleX = (this.field_178701_m * -40.0F - 11.0F) * 0.017453292F;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
    }
}
