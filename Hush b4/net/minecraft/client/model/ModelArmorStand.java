// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;

public class ModelArmorStand extends ModelArmorStandArmor
{
    public ModelRenderer standRightSide;
    public ModelRenderer standLeftSide;
    public ModelRenderer standWaist;
    public ModelRenderer standBase;
    
    public ModelArmorStand() {
        this(0.0f);
    }
    
    public ModelArmorStand(final float p_i46306_1_) {
        super(p_i46306_1_, 64, 64);
        (this.bipedHead = new ModelRenderer(this, 0, 0)).addBox(-1.0f, -7.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.bipedBody = new ModelRenderer(this, 0, 26)).addBox(-6.0f, 0.0f, -1.5f, 12, 3, 3, p_i46306_1_);
        this.bipedBody.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.bipedRightArm = new ModelRenderer(this, 24, 0)).addBox(-2.0f, -2.0f, -1.0f, 2, 12, 2, p_i46306_1_);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 32, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(0.0f, -2.0f, -1.0f, 2, 12, 2, p_i46306_1_);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
        (this.bipedRightLeg = new ModelRenderer(this, 8, 0)).addBox(-1.0f, 0.0f, -1.0f, 2, 11, 2, p_i46306_1_);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 11, 2, p_i46306_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.standRightSide = new ModelRenderer(this, 16, 0)).addBox(-3.0f, 3.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.standRightSide.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.standRightSide.showModel = true;
        (this.standLeftSide = new ModelRenderer(this, 48, 16)).addBox(1.0f, 3.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.standLeftSide.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.standWaist = new ModelRenderer(this, 0, 48)).addBox(-4.0f, 10.0f, -1.0f, 8, 2, 2, p_i46306_1_);
        this.standWaist.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.standBase = new ModelRenderer(this, 0, 32)).addBox(-6.0f, 11.0f, -6.0f, 12, 1, 12, p_i46306_1_);
        this.standBase.setRotationPoint(0.0f, 12.0f, 0.0f);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        if (entityIn instanceof EntityArmorStand) {
            final EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
            this.bipedLeftArm.showModel = entityarmorstand.getShowArms();
            this.bipedRightArm.showModel = entityarmorstand.getShowArms();
            this.standBase.showModel = !entityarmorstand.hasNoBasePlate();
            this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
            this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f);
            this.standRightSide.rotateAngleX = 0.017453292f * entityarmorstand.getBodyRotation().getX();
            this.standRightSide.rotateAngleY = 0.017453292f * entityarmorstand.getBodyRotation().getY();
            this.standRightSide.rotateAngleZ = 0.017453292f * entityarmorstand.getBodyRotation().getZ();
            this.standLeftSide.rotateAngleX = 0.017453292f * entityarmorstand.getBodyRotation().getX();
            this.standLeftSide.rotateAngleY = 0.017453292f * entityarmorstand.getBodyRotation().getY();
            this.standLeftSide.rotateAngleZ = 0.017453292f * entityarmorstand.getBodyRotation().getZ();
            this.standWaist.rotateAngleX = 0.017453292f * entityarmorstand.getBodyRotation().getX();
            this.standWaist.rotateAngleY = 0.017453292f * entityarmorstand.getBodyRotation().getY();
            this.standWaist.rotateAngleZ = 0.017453292f * entityarmorstand.getBodyRotation().getZ();
            final float f = (entityarmorstand.getLeftLegRotation().getX() + entityarmorstand.getRightLegRotation().getX()) / 2.0f;
            final float f2 = (entityarmorstand.getLeftLegRotation().getY() + entityarmorstand.getRightLegRotation().getY()) / 2.0f;
            final float f3 = (entityarmorstand.getLeftLegRotation().getZ() + entityarmorstand.getRightLegRotation().getZ()) / 2.0f;
            this.standBase.rotateAngleX = 0.0f;
            this.standBase.rotateAngleY = 0.017453292f * -entityIn.rotationYaw;
            this.standBase.rotateAngleZ = 0.0f;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.standRightSide.render(scale);
            this.standLeftSide.render(scale);
            this.standWaist.render(scale);
            this.standBase.render(scale);
        }
        else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.standRightSide.render(scale);
            this.standLeftSide.render(scale);
            this.standWaist.render(scale);
            this.standBase.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void postRenderArm(final float scale) {
        final boolean flag = this.bipedRightArm.showModel;
        this.bipedRightArm.showModel = true;
        super.postRenderArm(scale);
        this.bipedRightArm.showModel = flag;
    }
}
