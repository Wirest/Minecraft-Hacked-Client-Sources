// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelDragon extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer spine;
    private ModelRenderer jaw;
    private ModelRenderer body;
    private ModelRenderer rearLeg;
    private ModelRenderer frontLeg;
    private ModelRenderer rearLegTip;
    private ModelRenderer frontLegTip;
    private ModelRenderer rearFoot;
    private ModelRenderer frontFoot;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private float partialTicks;
    
    public ModelDragon(final float p_i46360_1_) {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.setTextureOffset("body.body", 0, 0);
        this.setTextureOffset("wing.skin", -56, 88);
        this.setTextureOffset("wingtip.skin", -56, 144);
        this.setTextureOffset("rearleg.main", 0, 0);
        this.setTextureOffset("rearfoot.main", 112, 0);
        this.setTextureOffset("rearlegtip.main", 196, 0);
        this.setTextureOffset("head.upperhead", 112, 30);
        this.setTextureOffset("wing.bone", 112, 88);
        this.setTextureOffset("head.upperlip", 176, 44);
        this.setTextureOffset("jaw.jaw", 176, 65);
        this.setTextureOffset("frontleg.main", 112, 104);
        this.setTextureOffset("wingtip.bone", 112, 136);
        this.setTextureOffset("frontfoot.main", 144, 104);
        this.setTextureOffset("neck.box", 192, 104);
        this.setTextureOffset("frontlegtip.main", 226, 138);
        this.setTextureOffset("body.scale", 220, 53);
        this.setTextureOffset("head.scale", 0, 0);
        this.setTextureOffset("neck.scale", 48, 0);
        this.setTextureOffset("head.nostril", 112, 0);
        final float f = -16.0f;
        (this.head = new ModelRenderer(this, "head")).addBox("upperlip", -6.0f, -1.0f, -8.0f + f, 12, 5, 16);
        this.head.addBox("upperhead", -8.0f, -8.0f, 6.0f + f, 16, 16, 16);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0f, -12.0f, 12.0f + f, 2, 4, 6);
        this.head.addBox("nostril", -5.0f, -3.0f, -6.0f + f, 2, 2, 4);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0f, -12.0f, 12.0f + f, 2, 4, 6);
        this.head.addBox("nostril", 3.0f, -3.0f, -6.0f + f, 2, 2, 4);
        (this.jaw = new ModelRenderer(this, "jaw")).setRotationPoint(0.0f, 4.0f, 8.0f + f);
        this.jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16);
        this.head.addChild(this.jaw);
        (this.spine = new ModelRenderer(this, "neck")).addBox("box", -5.0f, -5.0f, -5.0f, 10, 10, 10);
        this.spine.addBox("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6);
        (this.body = new ModelRenderer(this, "body")).setRotationPoint(0.0f, 4.0f, 8.0f);
        this.body.addBox("body", -12.0f, 0.0f, -16.0f, 24, 24, 64);
        this.body.addBox("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12);
        this.body.addBox("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12);
        this.body.addBox("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12);
        (this.wing = new ModelRenderer(this, "wing")).setRotationPoint(-12.0f, 5.0f, 2.0f);
        this.wing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8);
        this.wing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        (this.wingTip = new ModelRenderer(this, "wingtip")).setRotationPoint(-56.0f, 0.0f, 0.0f);
        this.wingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4);
        this.wingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        this.wing.addChild(this.wingTip);
        (this.frontLeg = new ModelRenderer(this, "frontleg")).setRotationPoint(-12.0f, 20.0f, 2.0f);
        this.frontLeg.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8);
        (this.frontLegTip = new ModelRenderer(this, "frontlegtip")).setRotationPoint(0.0f, 20.0f, -1.0f);
        this.frontLegTip.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6);
        this.frontLeg.addChild(this.frontLegTip);
        (this.frontFoot = new ModelRenderer(this, "frontfoot")).setRotationPoint(0.0f, 23.0f, 0.0f);
        this.frontFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16);
        this.frontLegTip.addChild(this.frontFoot);
        (this.rearLeg = new ModelRenderer(this, "rearleg")).setRotationPoint(-16.0f, 16.0f, 42.0f);
        this.rearLeg.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16);
        (this.rearLegTip = new ModelRenderer(this, "rearlegtip")).setRotationPoint(0.0f, 32.0f, -4.0f);
        this.rearLegTip.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12);
        this.rearLeg.addChild(this.rearLegTip);
        (this.rearFoot = new ModelRenderer(this, "rearfoot")).setRotationPoint(0.0f, 31.0f, 4.0f);
        this.rearFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24);
        this.rearLegTip.addChild(this.rearFoot);
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float p_78086_2_, final float p_78086_3_, final float partialTickTime) {
        this.partialTicks = partialTickTime;
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        GlStateManager.pushMatrix();
        final EntityDragon entitydragon = (EntityDragon)entityIn;
        final float f = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * this.partialTicks;
        this.jaw.rotateAngleX = (float)(Math.sin(f * 3.1415927f * 2.0f) + 1.0) * 0.2f;
        float f2 = (float)(Math.sin(f * 3.1415927f * 2.0f - 1.0f) + 1.0);
        f2 = (f2 * f2 * 1.0f + f2 * 2.0f) * 0.05f;
        GlStateManager.translate(0.0f, f2 - 2.0f, -3.0f);
        GlStateManager.rotate(f2 * 2.0f, 1.0f, 0.0f, 0.0f);
        float f3 = -30.0f;
        float f4 = 0.0f;
        final float f5 = 1.5f;
        double[] adouble = entitydragon.getMovementOffsets(6, this.partialTicks);
        final float f6 = this.updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] - entitydragon.getMovementOffsets(10, this.partialTicks)[0]);
        final float f7 = this.updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] + f6 / 2.0f);
        f3 += 2.0f;
        float f8 = f * 3.1415927f * 2.0f;
        f3 = 20.0f;
        float f9 = -12.0f;
        for (int i = 0; i < 5; ++i) {
            final double[] adouble2 = entitydragon.getMovementOffsets(5 - i, this.partialTicks);
            final float f10 = (float)Math.cos(i * 0.45f + f8) * 0.15f;
            this.spine.rotateAngleY = this.updateRotations(adouble2[0] - adouble[0]) * 3.1415927f / 180.0f * f5;
            this.spine.rotateAngleX = f10 + (float)(adouble2[1] - adouble[1]) * 3.1415927f / 180.0f * f5 * 5.0f;
            this.spine.rotateAngleZ = -this.updateRotations(adouble2[0] - f7) * 3.1415927f / 180.0f * f5;
            this.spine.rotationPointY = f3;
            this.spine.rotationPointZ = f9;
            this.spine.rotationPointX = f4;
            f3 += (float)(Math.sin(this.spine.rotateAngleX) * 10.0);
            f9 -= (float)(Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            f4 -= (float)(Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(scale);
        }
        this.head.rotationPointY = f3;
        this.head.rotationPointZ = f9;
        this.head.rotationPointX = f4;
        double[] adouble3 = entitydragon.getMovementOffsets(0, this.partialTicks);
        this.head.rotateAngleY = this.updateRotations(adouble3[0] - adouble[0]) * 3.1415927f / 180.0f * 1.0f;
        this.head.rotateAngleZ = -this.updateRotations(adouble3[0] - f7) * 3.1415927f / 180.0f * 1.0f;
        this.head.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f6 * f5 * 1.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        this.body.rotateAngleZ = 0.0f;
        this.body.render(scale);
        for (int j = 0; j < 2; ++j) {
            GlStateManager.enableCull();
            final float f11 = f * 3.1415927f * 2.0f;
            this.wing.rotateAngleX = 0.125f - (float)Math.cos(f11) * 0.2f;
            this.wing.rotateAngleY = 0.25f;
            this.wing.rotateAngleZ = (float)(Math.sin(f11) + 0.125) * 0.8f;
            this.wingTip.rotateAngleZ = -(float)(Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
            this.rearLeg.rotateAngleX = 1.0f + f2 * 0.1f;
            this.rearLegTip.rotateAngleX = 0.5f + f2 * 0.1f;
            this.rearFoot.rotateAngleX = 0.75f + f2 * 0.1f;
            this.frontLeg.rotateAngleX = 1.3f + f2 * 0.1f;
            this.frontLegTip.rotateAngleX = -0.5f - f2 * 0.1f;
            this.frontFoot.rotateAngleX = 0.75f + f2 * 0.1f;
            this.wing.render(scale);
            this.frontLeg.render(scale);
            this.rearLeg.render(scale);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (j == 0) {
                GlStateManager.cullFace(1028);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.cullFace(1029);
        GlStateManager.disableCull();
        float f12 = -(float)Math.sin(f * 3.1415927f * 2.0f) * 0.0f;
        f8 = f * 3.1415927f * 2.0f;
        f3 = 10.0f;
        f9 = 60.0f;
        f4 = 0.0f;
        adouble = entitydragon.getMovementOffsets(11, this.partialTicks);
        for (int k = 0; k < 12; ++k) {
            adouble3 = entitydragon.getMovementOffsets(12 + k, this.partialTicks);
            f12 += (float)(Math.sin(k * 0.45f + f8) * 0.05000000074505806);
            this.spine.rotateAngleY = (this.updateRotations(adouble3[0] - adouble[0]) * f5 + 180.0f) * 3.1415927f / 180.0f;
            this.spine.rotateAngleX = f12 + (float)(adouble3[1] - adouble[1]) * 3.1415927f / 180.0f * f5 * 5.0f;
            this.spine.rotateAngleZ = this.updateRotations(adouble3[0] - f7) * 3.1415927f / 180.0f * f5;
            this.spine.rotationPointY = f3;
            this.spine.rotationPointZ = f9;
            this.spine.rotationPointX = f4;
            f3 += (float)(Math.sin(this.spine.rotateAngleX) * 10.0);
            f9 -= (float)(Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            f4 -= (float)(Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    private float updateRotations(double p_78214_1_) {
        while (p_78214_1_ >= 180.0) {
            p_78214_1_ -= 360.0;
        }
        while (p_78214_1_ < -180.0) {
            p_78214_1_ += 360.0;
        }
        return (float)p_78214_1_;
    }
}
