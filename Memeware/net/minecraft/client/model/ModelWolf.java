package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;

public class ModelWolf extends ModelBase {
    /**
     * main box for the wolf head
     */
    public ModelRenderer wolfHeadMain;

    /**
     * The wolf's body
     */
    public ModelRenderer wolfBody;

    /**
     * Wolf'se first leg
     */
    public ModelRenderer wolfLeg1;

    /**
     * Wolf's second leg
     */
    public ModelRenderer wolfLeg2;

    /**
     * Wolf's third leg
     */
    public ModelRenderer wolfLeg3;

    /**
     * Wolf's fourth leg
     */
    public ModelRenderer wolfLeg4;

    /**
     * The wolf's tail
     */
    ModelRenderer wolfTail;

    /**
     * The wolf's mane
     */
    ModelRenderer wolfMane;
    private static final String __OBFID = "CL_00000868";

    public ModelWolf() {
        float var1 = 0.0F;
        float var2 = 13.5F;
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, var1);
        this.wolfHeadMain.setRotationPoint(-1.0F, var2, -7.0F);
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, var1);
        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.wolfMane = new ModelRenderer(this, 21, 0);
        this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, var1);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.wolfTail = new ModelRenderer(this, 9, 18);
        this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, var1);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, var1);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, var1);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.isChild) {
            float var8 = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
            this.wolfHeadMain.renderWithRotation(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.wolfBody.render(p_78088_7_);
            this.wolfLeg1.render(p_78088_7_);
            this.wolfLeg2.render(p_78088_7_);
            this.wolfLeg3.render(p_78088_7_);
            this.wolfLeg4.render(p_78088_7_);
            this.wolfTail.renderWithRotation(p_78088_7_);
            this.wolfMane.render(p_78088_7_);
            GlStateManager.popMatrix();
        } else {
            this.wolfHeadMain.renderWithRotation(p_78088_7_);
            this.wolfBody.render(p_78088_7_);
            this.wolfLeg1.render(p_78088_7_);
            this.wolfLeg2.render(p_78088_7_);
            this.wolfLeg3.render(p_78088_7_);
            this.wolfLeg4.render(p_78088_7_);
            this.wolfTail.renderWithRotation(p_78088_7_);
            this.wolfMane.render(p_78088_7_);
        }
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        EntityWolf var5 = (EntityWolf) p_78086_1_;

        if (var5.isAngry()) {
            this.wolfTail.rotateAngleY = 0.0F;
        } else {
            this.wolfTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        if (var5.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.wolfMane.rotateAngleX = ((float) Math.PI * 2F / 5F);
            this.wolfMane.rotateAngleY = 0.0F;
            this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBody.rotateAngleX = ((float) Math.PI / 4F);
            this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.wolfLeg1.rotateAngleX = ((float) Math.PI * 3F / 2F);
            this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.wolfLeg2.rotateAngleX = ((float) Math.PI * 3F / 2F);
            this.wolfLeg3.rotateAngleX = 5.811947F;
            this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.wolfLeg4.rotateAngleX = 5.811947F;
            this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        } else {
            this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBody.rotateAngleX = ((float) Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
            this.wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
            this.wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
            this.wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        this.wolfHeadMain.rotateAngleZ = var5.getInterestedAngle(p_78086_4_) + var5.getShakeAngle(p_78086_4_, 0.0F);
        this.wolfMane.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.08F);
        this.wolfBody.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.16F);
        this.wolfTail.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.2F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.wolfHeadMain.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
        this.wolfHeadMain.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
        this.wolfTail.rotateAngleX = p_78087_3_;
    }
}
