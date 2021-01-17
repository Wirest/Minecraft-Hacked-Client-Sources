// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer extends ModelBiped
{
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private ModelRenderer bipedCape;
    private ModelRenderer bipedDeadmau5Head;
    private boolean smallArms;
    private static final String __OBFID = "CL_00002626";
    
    public ModelPlayer(final float p_i46304_1_, final boolean p_i46304_2_) {
        super(p_i46304_1_, 0.0f, 64, 64);
        this.smallArms = p_i46304_2_;
        (this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0)).addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, p_i46304_1_);
        (this.bipedCape = new ModelRenderer(this, 0, 0)).setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, p_i46304_1_);
        if (p_i46304_2_) {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.5f, 10.0f);
        }
        else {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        (this.bipedLeftLeg = new ModelRenderer(this, 16, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedLeftLegwear = new ModelRenderer(this, 0, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedRightLegwear = new ModelRenderer(this, 0, 32)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.bipedBodyWear = new ModelRenderer(this, 16, 32)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    public void renderDeadmau5Head(final float p_178727_1_) {
        ModelBase.copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0f;
        this.bipedDeadmau5Head.rotationPointY = 0.0f;
        this.bipedDeadmau5Head.render(p_178727_1_);
    }
    
    public void renderCape(final float p_178728_1_) {
        this.bipedCape.render(p_178728_1_);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        ModelBase.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        ModelBase.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        ModelBase.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        ModelBase.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        ModelBase.copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }
    
    public void renderRightArm() {
        this.bipedRightArm.render(0.0625f);
        this.bipedRightArmwear.render(0.0625f);
    }
    
    public void renderLeftArm() {
        this.bipedLeftArm.render(0.0625f);
        this.bipedLeftArmwear.render(0.0625f);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }
    
    @Override
    public void postRenderArm(final float scale) {
        if (this.smallArms) {
            final ModelRenderer bipedRightArm = this.bipedRightArm;
            ++bipedRightArm.rotationPointX;
            this.bipedRightArm.postRender(scale);
            final ModelRenderer bipedRightArm2 = this.bipedRightArm;
            --bipedRightArm2.rotationPointX;
        }
        else {
            this.bipedRightArm.postRender(scale);
        }
    }
}
