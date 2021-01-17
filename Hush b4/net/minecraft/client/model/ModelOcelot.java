// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelOcelot extends ModelBase
{
    ModelRenderer ocelotBackLeftLeg;
    ModelRenderer ocelotBackRightLeg;
    ModelRenderer ocelotFrontLeftLeg;
    ModelRenderer ocelotFrontRightLeg;
    ModelRenderer ocelotTail;
    ModelRenderer ocelotTail2;
    ModelRenderer ocelotHead;
    ModelRenderer ocelotBody;
    int field_78163_i;
    
    public ModelOcelot() {
        this.field_78163_i = 1;
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        (this.ocelotHead = new ModelRenderer(this, "head")).addBox("main", -2.5f, -2.0f, -3.0f, 5, 4, 5);
        this.ocelotHead.addBox("nose", -1.5f, 0.0f, -4.0f, 3, 2, 2);
        this.ocelotHead.addBox("ear1", -2.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.addBox("ear2", 1.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.setRotationPoint(0.0f, 15.0f, -9.0f);
        (this.ocelotBody = new ModelRenderer(this, 20, 0)).addBox(-2.0f, 3.0f, -8.0f, 4, 16, 6, 0.0f);
        this.ocelotBody.setRotationPoint(0.0f, 12.0f, -10.0f);
        (this.ocelotTail = new ModelRenderer(this, 0, 15)).addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail.rotateAngleX = 0.9f;
        this.ocelotTail.setRotationPoint(0.0f, 15.0f, 8.0f);
        (this.ocelotTail2 = new ModelRenderer(this, 4, 15)).addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail2.setRotationPoint(0.0f, 20.0f, 14.0f);
        (this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackLeftLeg.setRotationPoint(1.1f, 18.0f, 5.0f);
        (this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackRightLeg.setRotationPoint(-1.1f, 18.0f, 5.0f);
        (this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontLeftLeg.setRotationPoint(1.2f, 13.8f, -5.0f);
        (this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontRightLeg.setRotationPoint(-1.2f, 13.8f, -5.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f / f, 1.5f / f, 1.5f / f);
            GlStateManager.translate(0.0f, 10.0f * scale, 4.0f * scale);
            this.ocelotHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.ocelotBody.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.ocelotHead.render(scale);
            this.ocelotBody.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        this.ocelotHead.rotateAngleX = p_78087_5_ / 57.295776f;
        this.ocelotHead.rotateAngleY = p_78087_4_ / 57.295776f;
        if (this.field_78163_i != 3) {
            this.ocelotBody.rotateAngleX = 1.5707964f;
            if (this.field_78163_i == 2) {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.0f * p_78087_2_;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 0.3f) * 1.0f * p_78087_2_;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f + 0.3f) * 1.0f * p_78087_2_;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f) * 1.0f * p_78087_2_;
                this.ocelotTail2.rotateAngleX = 1.7278761f + 0.31415927f * MathHelper.cos(p_78087_1_) * p_78087_2_;
            }
            else {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.0f * p_78087_2_;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f) * 1.0f * p_78087_2_;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f) * 1.0f * p_78087_2_;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.0f * p_78087_2_;
                if (this.field_78163_i == 1) {
                    this.ocelotTail2.rotateAngleX = 1.7278761f + 0.7853982f * MathHelper.cos(p_78087_1_) * p_78087_2_;
                }
                else {
                    this.ocelotTail2.rotateAngleX = 1.7278761f + 0.47123894f * MathHelper.cos(p_78087_1_) * p_78087_2_;
                }
            }
        }
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float p_78086_2_, final float p_78086_3_, final float partialTickTime) {
        final EntityOcelot entityocelot = (EntityOcelot)entitylivingbaseIn;
        this.ocelotBody.rotationPointY = 12.0f;
        this.ocelotBody.rotationPointZ = -10.0f;
        this.ocelotHead.rotationPointY = 15.0f;
        this.ocelotHead.rotationPointZ = -9.0f;
        this.ocelotTail.rotationPointY = 15.0f;
        this.ocelotTail.rotationPointZ = 8.0f;
        this.ocelotTail2.rotationPointY = 20.0f;
        this.ocelotTail2.rotationPointZ = 14.0f;
        final ModelRenderer ocelotFrontLeftLeg = this.ocelotFrontLeftLeg;
        final ModelRenderer ocelotFrontRightLeg = this.ocelotFrontRightLeg;
        final float n = 13.8f;
        ocelotFrontRightLeg.rotationPointY = n;
        ocelotFrontLeftLeg.rotationPointY = n;
        final ModelRenderer ocelotFrontLeftLeg2 = this.ocelotFrontLeftLeg;
        final ModelRenderer ocelotFrontRightLeg2 = this.ocelotFrontRightLeg;
        final float n2 = -5.0f;
        ocelotFrontRightLeg2.rotationPointZ = n2;
        ocelotFrontLeftLeg2.rotationPointZ = n2;
        final ModelRenderer ocelotBackLeftLeg = this.ocelotBackLeftLeg;
        final ModelRenderer ocelotBackRightLeg = this.ocelotBackRightLeg;
        final float n3 = 18.0f;
        ocelotBackRightLeg.rotationPointY = n3;
        ocelotBackLeftLeg.rotationPointY = n3;
        final ModelRenderer ocelotBackLeftLeg2 = this.ocelotBackLeftLeg;
        final ModelRenderer ocelotBackRightLeg2 = this.ocelotBackRightLeg;
        final float n4 = 5.0f;
        ocelotBackRightLeg2.rotationPointZ = n4;
        ocelotBackLeftLeg2.rotationPointZ = n4;
        this.ocelotTail.rotateAngleX = 0.9f;
        if (entityocelot.isSneaking()) {
            final ModelRenderer ocelotBody = this.ocelotBody;
            ++ocelotBody.rotationPointY;
            final ModelRenderer ocelotHead = this.ocelotHead;
            ocelotHead.rotationPointY += 2.0f;
            final ModelRenderer ocelotTail = this.ocelotTail;
            ++ocelotTail.rotationPointY;
            final ModelRenderer ocelotTail2 = this.ocelotTail2;
            ocelotTail2.rotationPointY -= 4.0f;
            final ModelRenderer ocelotTail3 = this.ocelotTail2;
            ocelotTail3.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 0;
        }
        else if (entityocelot.isSprinting()) {
            this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
            final ModelRenderer ocelotTail4 = this.ocelotTail2;
            ocelotTail4.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 2;
        }
        else if (entityocelot.isSitting()) {
            this.ocelotBody.rotateAngleX = 0.7853982f;
            final ModelRenderer ocelotBody2 = this.ocelotBody;
            ocelotBody2.rotationPointY -= 4.0f;
            final ModelRenderer ocelotBody3 = this.ocelotBody;
            ocelotBody3.rotationPointZ += 5.0f;
            final ModelRenderer ocelotHead2 = this.ocelotHead;
            ocelotHead2.rotationPointY -= 3.3f;
            final ModelRenderer ocelotHead3 = this.ocelotHead;
            ++ocelotHead3.rotationPointZ;
            final ModelRenderer ocelotTail5 = this.ocelotTail;
            ocelotTail5.rotationPointY += 8.0f;
            final ModelRenderer ocelotTail6 = this.ocelotTail;
            ocelotTail6.rotationPointZ -= 2.0f;
            final ModelRenderer ocelotTail7 = this.ocelotTail2;
            ocelotTail7.rotationPointY += 2.0f;
            final ModelRenderer ocelotTail8 = this.ocelotTail2;
            ocelotTail8.rotationPointZ -= 0.8f;
            this.ocelotTail.rotateAngleX = 1.7278761f;
            this.ocelotTail2.rotateAngleX = 2.670354f;
            final ModelRenderer ocelotFrontLeftLeg3 = this.ocelotFrontLeftLeg;
            final ModelRenderer ocelotFrontRightLeg3 = this.ocelotFrontRightLeg;
            final float n5 = -0.15707964f;
            ocelotFrontRightLeg3.rotateAngleX = n5;
            ocelotFrontLeftLeg3.rotateAngleX = n5;
            final ModelRenderer ocelotFrontLeftLeg4 = this.ocelotFrontLeftLeg;
            final ModelRenderer ocelotFrontRightLeg4 = this.ocelotFrontRightLeg;
            final float n6 = 15.8f;
            ocelotFrontRightLeg4.rotationPointY = n6;
            ocelotFrontLeftLeg4.rotationPointY = n6;
            final ModelRenderer ocelotFrontLeftLeg5 = this.ocelotFrontLeftLeg;
            final ModelRenderer ocelotFrontRightLeg5 = this.ocelotFrontRightLeg;
            final float n7 = -7.0f;
            ocelotFrontRightLeg5.rotationPointZ = n7;
            ocelotFrontLeftLeg5.rotationPointZ = n7;
            final ModelRenderer ocelotBackLeftLeg3 = this.ocelotBackLeftLeg;
            final ModelRenderer ocelotBackRightLeg3 = this.ocelotBackRightLeg;
            final float n8 = -1.5707964f;
            ocelotBackRightLeg3.rotateAngleX = n8;
            ocelotBackLeftLeg3.rotateAngleX = n8;
            final ModelRenderer ocelotBackLeftLeg4 = this.ocelotBackLeftLeg;
            final ModelRenderer ocelotBackRightLeg4 = this.ocelotBackRightLeg;
            final float n9 = 21.0f;
            ocelotBackRightLeg4.rotationPointY = n9;
            ocelotBackLeftLeg4.rotationPointY = n9;
            final ModelRenderer ocelotBackLeftLeg5 = this.ocelotBackLeftLeg;
            final ModelRenderer ocelotBackRightLeg5 = this.ocelotBackRightLeg;
            final float n10 = 1.0f;
            ocelotBackRightLeg5.rotationPointZ = n10;
            ocelotBackLeftLeg5.rotationPointZ = n10;
            this.field_78163_i = 3;
        }
        else {
            this.field_78163_i = 1;
        }
    }
}
