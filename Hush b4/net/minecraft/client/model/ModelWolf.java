// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelWolf extends ModelBase
{
    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    ModelRenderer wolfTail;
    ModelRenderer wolfMane;
    
    public ModelWolf() {
        final float f = 0.0f;
        final float f2 = 13.5f;
        (this.wolfHeadMain = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -3.0f, -2.0f, 6, 6, 4, f);
        this.wolfHeadMain.setRotationPoint(-1.0f, f2, -7.0f);
        (this.wolfBody = new ModelRenderer(this, 18, 14)).addBox(-4.0f, -2.0f, -3.0f, 6, 9, 6, f);
        this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
        (this.wolfMane = new ModelRenderer(this, 21, 0)).addBox(-4.0f, -3.0f, -3.0f, 8, 6, 7, f);
        this.wolfMane.setRotationPoint(-1.0f, 14.0f, 2.0f);
        (this.wolfLeg1 = new ModelRenderer(this, 0, 18)).addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
        (this.wolfLeg2 = new ModelRenderer(this, 0, 18)).addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
        (this.wolfLeg3 = new ModelRenderer(this, 0, 18)).addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
        (this.wolfLeg4 = new ModelRenderer(this, 0, 18)).addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
        (this.wolfTail = new ModelRenderer(this, 9, 18)).addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0f, -5.0f, 0.0f, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0f, -5.0f, 0.0f, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5f, 0.0f, -5.0f, 3, 3, 4, f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 5.0f * scale, 2.0f * scale);
            this.wolfHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.wolfHeadMain.renderWithRotation(scale);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float p_78086_2_, final float p_78086_3_, final float partialTickTime) {
        final EntityWolf entitywolf = (EntityWolf)entitylivingbaseIn;
        if (entitywolf.isAngry()) {
            this.wolfTail.rotateAngleY = 0.0f;
        }
        else {
            this.wolfTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
        }
        if (entitywolf.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0f, 16.0f, -3.0f);
            this.wolfMane.rotateAngleX = 1.2566371f;
            this.wolfMane.rotateAngleY = 0.0f;
            this.wolfBody.setRotationPoint(0.0f, 18.0f, 0.0f);
            this.wolfBody.rotateAngleX = 0.7853982f;
            this.wolfTail.setRotationPoint(-1.0f, 21.0f, 6.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 22.0f, 2.0f);
            this.wolfLeg1.rotateAngleX = 4.712389f;
            this.wolfLeg2.setRotationPoint(0.5f, 22.0f, 2.0f);
            this.wolfLeg2.rotateAngleX = 4.712389f;
            this.wolfLeg3.rotateAngleX = 5.811947f;
            this.wolfLeg3.setRotationPoint(-2.49f, 17.0f, -4.0f);
            this.wolfLeg4.rotateAngleX = 5.811947f;
            this.wolfLeg4.setRotationPoint(0.51f, 17.0f, -4.0f);
        }
        else {
            this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
            this.wolfBody.rotateAngleX = 1.5707964f;
            this.wolfMane.setRotationPoint(-1.0f, 14.0f, -3.0f);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
            this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
            this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
            this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
            this.wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
            this.wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f + 3.1415927f) * 1.4f * p_78086_3_;
            this.wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f + 3.1415927f) * 1.4f * p_78086_3_;
            this.wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
        }
        this.wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0f);
        this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.08f);
        this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16f);
        this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.2f);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        this.wolfHeadMain.rotateAngleX = p_78087_5_ / 57.295776f;
        this.wolfHeadMain.rotateAngleY = p_78087_4_ / 57.295776f;
        this.wolfTail.rotateAngleX = p_78087_3_;
    }
}
