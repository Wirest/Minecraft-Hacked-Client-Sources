// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelSpider extends ModelBase
{
    public ModelRenderer spiderHead;
    public ModelRenderer spiderNeck;
    public ModelRenderer spiderBody;
    public ModelRenderer spiderLeg1;
    public ModelRenderer spiderLeg2;
    public ModelRenderer spiderLeg3;
    public ModelRenderer spiderLeg4;
    public ModelRenderer spiderLeg5;
    public ModelRenderer spiderLeg6;
    public ModelRenderer spiderLeg7;
    public ModelRenderer spiderLeg8;
    
    public ModelSpider() {
        final float f = 0.0f;
        final int i = 15;
        (this.spiderHead = new ModelRenderer(this, 32, 4)).addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, f);
        this.spiderHead.setRotationPoint(0.0f, (float)i, -3.0f);
        (this.spiderNeck = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6, f);
        this.spiderNeck.setRotationPoint(0.0f, (float)i, 0.0f);
        (this.spiderBody = new ModelRenderer(this, 0, 12)).addBox(-5.0f, -4.0f, -6.0f, 10, 8, 12, f);
        this.spiderBody.setRotationPoint(0.0f, (float)i, 9.0f);
        (this.spiderLeg1 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg1.setRotationPoint(-4.0f, (float)i, 2.0f);
        (this.spiderLeg2 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg2.setRotationPoint(4.0f, (float)i, 2.0f);
        (this.spiderLeg3 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg3.setRotationPoint(-4.0f, (float)i, 1.0f);
        (this.spiderLeg4 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg4.setRotationPoint(4.0f, (float)i, 1.0f);
        (this.spiderLeg5 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg5.setRotationPoint(-4.0f, (float)i, 0.0f);
        (this.spiderLeg6 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg6.setRotationPoint(4.0f, (float)i, 0.0f);
        (this.spiderLeg7 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg7.setRotationPoint(-4.0f, (float)i, -1.0f);
        (this.spiderLeg8 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg8.setRotationPoint(4.0f, (float)i, -1.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.spiderHead.render(scale);
        this.spiderNeck.render(scale);
        this.spiderBody.render(scale);
        this.spiderLeg1.render(scale);
        this.spiderLeg2.render(scale);
        this.spiderLeg3.render(scale);
        this.spiderLeg4.render(scale);
        this.spiderLeg5.render(scale);
        this.spiderLeg6.render(scale);
        this.spiderLeg7.render(scale);
        this.spiderLeg8.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        this.spiderHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.spiderHead.rotateAngleX = p_78087_5_ / 57.295776f;
        final float f = 0.7853982f;
        this.spiderLeg1.rotateAngleZ = -f;
        this.spiderLeg2.rotateAngleZ = f;
        this.spiderLeg3.rotateAngleZ = -f * 0.74f;
        this.spiderLeg4.rotateAngleZ = f * 0.74f;
        this.spiderLeg5.rotateAngleZ = -f * 0.74f;
        this.spiderLeg6.rotateAngleZ = f * 0.74f;
        this.spiderLeg7.rotateAngleZ = -f;
        this.spiderLeg8.rotateAngleZ = f;
        final float f2 = -0.0f;
        final float f3 = 0.3926991f;
        this.spiderLeg1.rotateAngleY = f3 * 2.0f + f2;
        this.spiderLeg2.rotateAngleY = -f3 * 2.0f - f2;
        this.spiderLeg3.rotateAngleY = f3 * 1.0f + f2;
        this.spiderLeg4.rotateAngleY = -f3 * 1.0f - f2;
        this.spiderLeg5.rotateAngleY = -f3 * 1.0f + f2;
        this.spiderLeg6.rotateAngleY = f3 * 1.0f - f2;
        this.spiderLeg7.rotateAngleY = -f3 * 2.0f + f2;
        this.spiderLeg8.rotateAngleY = f3 * 2.0f - f2;
        final float f4 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 0.0f) * 0.4f) * p_78087_2_;
        final float f5 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 3.1415927f) * 0.4f) * p_78087_2_;
        final float f6 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 1.5707964f) * 0.4f) * p_78087_2_;
        final float f7 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 4.712389f) * 0.4f) * p_78087_2_;
        final float f8 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 0.0f) * 0.4f) * p_78087_2_;
        final float f9 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 3.1415927f) * 0.4f) * p_78087_2_;
        final float f10 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 1.5707964f) * 0.4f) * p_78087_2_;
        final float f11 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 4.712389f) * 0.4f) * p_78087_2_;
        final ModelRenderer spiderLeg1 = this.spiderLeg1;
        spiderLeg1.rotateAngleY += f4;
        final ModelRenderer spiderLeg2 = this.spiderLeg2;
        spiderLeg2.rotateAngleY += -f4;
        final ModelRenderer spiderLeg3 = this.spiderLeg3;
        spiderLeg3.rotateAngleY += f5;
        final ModelRenderer spiderLeg4 = this.spiderLeg4;
        spiderLeg4.rotateAngleY += -f5;
        final ModelRenderer spiderLeg5 = this.spiderLeg5;
        spiderLeg5.rotateAngleY += f6;
        final ModelRenderer spiderLeg6 = this.spiderLeg6;
        spiderLeg6.rotateAngleY += -f6;
        final ModelRenderer spiderLeg7 = this.spiderLeg7;
        spiderLeg7.rotateAngleY += f7;
        final ModelRenderer spiderLeg8 = this.spiderLeg8;
        spiderLeg8.rotateAngleY += -f7;
        final ModelRenderer spiderLeg9 = this.spiderLeg1;
        spiderLeg9.rotateAngleZ += f8;
        final ModelRenderer spiderLeg10 = this.spiderLeg2;
        spiderLeg10.rotateAngleZ += -f8;
        final ModelRenderer spiderLeg11 = this.spiderLeg3;
        spiderLeg11.rotateAngleZ += f9;
        final ModelRenderer spiderLeg12 = this.spiderLeg4;
        spiderLeg12.rotateAngleZ += -f9;
        final ModelRenderer spiderLeg13 = this.spiderLeg5;
        spiderLeg13.rotateAngleZ += f10;
        final ModelRenderer spiderLeg14 = this.spiderLeg6;
        spiderLeg14.rotateAngleZ += -f10;
        final ModelRenderer spiderLeg15 = this.spiderLeg7;
        spiderLeg15.rotateAngleZ += f11;
        final ModelRenderer spiderLeg16 = this.spiderLeg8;
        spiderLeg16.rotateAngleZ += -f11;
    }
}
