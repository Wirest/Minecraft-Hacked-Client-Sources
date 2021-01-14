package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStand extends ModelArmorStandArmor {
    public ModelRenderer standRightSide;
    public ModelRenderer standLeftSide;
    public ModelRenderer standWaist;
    public ModelRenderer standBase;
    private static final String __OBFID = "CL_00002631";

    public ModelArmorStand() {
        this(0.0F);
    }

    public ModelArmorStand(float p_i46306_1_) {
        super(p_i46306_1_, 64, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, p_i46306_1_);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 26);
        this.bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, p_i46306_1_);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 24, 0);
        this.bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 32, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 8, 0);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.standRightSide = new ModelRenderer(this, 16, 0);
        this.standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
        this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standRightSide.showModel = true;
        this.standLeftSide = new ModelRenderer(this, 48, 16);
        this.standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
        this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standWaist = new ModelRenderer(this, 0, 48);
        this.standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, p_i46306_1_);
        this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standBase = new ModelRenderer(this, 0, 32);
        this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, p_i46306_1_);
        this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);

        if (p_78087_7_ instanceof EntityArmorStand) {
            EntityArmorStand var8 = (EntityArmorStand) p_78087_7_;
            this.bipedLeftArm.showModel = var8.getShowArms();
            this.bipedRightArm.showModel = var8.getShowArms();
            this.standBase.showModel = !var8.hasNoBasePlate();
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
            this.standRightSide.rotateAngleX = 0.017453292F * var8.getBodyRotation().func_179415_b();
            this.standRightSide.rotateAngleY = 0.017453292F * var8.getBodyRotation().func_179416_c();
            this.standRightSide.rotateAngleZ = 0.017453292F * var8.getBodyRotation().func_179413_d();
            this.standLeftSide.rotateAngleX = 0.017453292F * var8.getBodyRotation().func_179415_b();
            this.standLeftSide.rotateAngleY = 0.017453292F * var8.getBodyRotation().func_179416_c();
            this.standLeftSide.rotateAngleZ = 0.017453292F * var8.getBodyRotation().func_179413_d();
            this.standWaist.rotateAngleX = 0.017453292F * var8.getBodyRotation().func_179415_b();
            this.standWaist.rotateAngleY = 0.017453292F * var8.getBodyRotation().func_179416_c();
            this.standWaist.rotateAngleZ = 0.017453292F * var8.getBodyRotation().func_179413_d();
            float var9 = (var8.getLeftLegRotation().func_179415_b() + var8.getRightLegRotation().func_179415_b()) / 2.0F;
            float var10 = (var8.getLeftLegRotation().func_179416_c() + var8.getRightLegRotation().func_179416_c()) / 2.0F;
            float var11 = (var8.getLeftLegRotation().func_179413_d() + var8.getRightLegRotation().func_179413_d()) / 2.0F;
            this.standBase.rotateAngleX = 0.0F;
            this.standBase.rotateAngleY = 0.017453292F * -p_78087_7_.rotationYaw;
            this.standBase.rotateAngleZ = 0.0F;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        GlStateManager.pushMatrix();

        if (this.isChild) {
            float var8 = 2.0F;
            GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.standRightSide.render(p_78088_7_);
            this.standLeftSide.render(p_78088_7_);
            this.standWaist.render(p_78088_7_);
            this.standBase.render(p_78088_7_);
        } else {
            if (p_78088_1_.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.standRightSide.render(p_78088_7_);
            this.standLeftSide.render(p_78088_7_);
            this.standWaist.render(p_78088_7_);
            this.standBase.render(p_78088_7_);
        }

        GlStateManager.popMatrix();
    }

    public void postRenderHiddenArm(float p_178718_1_) {
        boolean var2 = this.bipedRightArm.showModel;
        this.bipedRightArm.showModel = true;
        super.postRenderHiddenArm(p_178718_1_);
        this.bipedRightArm.showModel = var2;
    }
}
