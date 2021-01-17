// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelRabbit extends ModelBase
{
    ModelRenderer rabbitLeftFoot;
    ModelRenderer rabbitRightFoot;
    ModelRenderer rabbitLeftThigh;
    ModelRenderer rabbitRightThigh;
    ModelRenderer rabbitBody;
    ModelRenderer rabbitLeftArm;
    ModelRenderer rabbitRightArm;
    ModelRenderer rabbitHead;
    ModelRenderer rabbitRightEar;
    ModelRenderer rabbitLeftEar;
    ModelRenderer rabbitTail;
    ModelRenderer rabbitNose;
    private float field_178701_m;
    private float field_178699_n;
    
    public ModelRabbit() {
        this.field_178701_m = 0.0f;
        this.field_178699_n = 0.0f;
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        (this.rabbitLeftFoot = new ModelRenderer(this, 26, 24)).addBox(-1.0f, 5.5f, -3.7f, 2, 1, 7);
        this.rabbitLeftFoot.setRotationPoint(3.0f, 17.5f, 3.7f);
        this.rabbitLeftFoot.mirror = true;
        this.setRotationOffset(this.rabbitLeftFoot, 0.0f, 0.0f, 0.0f);
        (this.rabbitRightFoot = new ModelRenderer(this, 8, 24)).addBox(-1.0f, 5.5f, -3.7f, 2, 1, 7);
        this.rabbitRightFoot.setRotationPoint(-3.0f, 17.5f, 3.7f);
        this.rabbitRightFoot.mirror = true;
        this.setRotationOffset(this.rabbitRightFoot, 0.0f, 0.0f, 0.0f);
        (this.rabbitLeftThigh = new ModelRenderer(this, 30, 15)).addBox(-1.0f, 0.0f, 0.0f, 2, 4, 5);
        this.rabbitLeftThigh.setRotationPoint(3.0f, 17.5f, 3.7f);
        this.rabbitLeftThigh.mirror = true;
        this.setRotationOffset(this.rabbitLeftThigh, -0.34906584f, 0.0f, 0.0f);
        (this.rabbitRightThigh = new ModelRenderer(this, 16, 15)).addBox(-1.0f, 0.0f, 0.0f, 2, 4, 5);
        this.rabbitRightThigh.setRotationPoint(-3.0f, 17.5f, 3.7f);
        this.rabbitRightThigh.mirror = true;
        this.setRotationOffset(this.rabbitRightThigh, -0.34906584f, 0.0f, 0.0f);
        (this.rabbitBody = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -2.0f, -10.0f, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0f, 19.0f, 8.0f);
        this.rabbitBody.mirror = true;
        this.setRotationOffset(this.rabbitBody, -0.34906584f, 0.0f, 0.0f);
        (this.rabbitLeftArm = new ModelRenderer(this, 8, 15)).addBox(-1.0f, 0.0f, -1.0f, 2, 7, 2);
        this.rabbitLeftArm.setRotationPoint(3.0f, 17.0f, -1.0f);
        this.rabbitLeftArm.mirror = true;
        this.setRotationOffset(this.rabbitLeftArm, -0.17453292f, 0.0f, 0.0f);
        (this.rabbitRightArm = new ModelRenderer(this, 0, 15)).addBox(-1.0f, 0.0f, -1.0f, 2, 7, 2);
        this.rabbitRightArm.setRotationPoint(-3.0f, 17.0f, -1.0f);
        this.rabbitRightArm.mirror = true;
        this.setRotationOffset(this.rabbitRightArm, -0.17453292f, 0.0f, 0.0f);
        (this.rabbitHead = new ModelRenderer(this, 32, 0)).addBox(-2.5f, -4.0f, -5.0f, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0f, 16.0f, -1.0f);
        this.rabbitHead.mirror = true;
        this.setRotationOffset(this.rabbitHead, 0.0f, 0.0f, 0.0f);
        (this.rabbitRightEar = new ModelRenderer(this, 52, 0)).addBox(-2.5f, -9.0f, -1.0f, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0f, 16.0f, -1.0f);
        this.rabbitRightEar.mirror = true;
        this.setRotationOffset(this.rabbitRightEar, 0.0f, -0.2617994f, 0.0f);
        (this.rabbitLeftEar = new ModelRenderer(this, 58, 0)).addBox(0.5f, -9.0f, -1.0f, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0f, 16.0f, -1.0f);
        this.rabbitLeftEar.mirror = true;
        this.setRotationOffset(this.rabbitLeftEar, 0.0f, 0.2617994f, 0.0f);
        (this.rabbitTail = new ModelRenderer(this, 52, 6)).addBox(-1.5f, -1.5f, 0.0f, 3, 3, 2);
        this.rabbitTail.setRotationPoint(0.0f, 20.0f, 7.0f);
        this.rabbitTail.mirror = true;
        this.setRotationOffset(this.rabbitTail, -0.3490659f, 0.0f, 0.0f);
        (this.rabbitNose = new ModelRenderer(this, 32, 9)).addBox(-0.5f, -2.5f, -5.5f, 1, 1, 1);
        this.rabbitNose.setRotationPoint(0.0f, 16.0f, -1.0f);
        this.rabbitNose.mirror = true;
        this.setRotationOffset(this.rabbitNose, 0.0f, 0.0f, 0.0f);
    }
    
    private void setRotationOffset(final ModelRenderer p_178691_1_, final float p_178691_2_, final float p_178691_3_, final float p_178691_4_) {
        p_178691_1_.rotateAngleX = p_178691_2_;
        p_178691_1_.rotateAngleY = p_178691_3_;
        p_178691_1_.rotateAngleZ = p_178691_4_;
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 5.0f * scale, 2.0f * scale);
            this.rabbitHead.render(scale);
            this.rabbitLeftEar.render(scale);
            this.rabbitRightEar.render(scale);
            this.rabbitNose.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.rabbitLeftFoot.render(scale);
            this.rabbitRightFoot.render(scale);
            this.rabbitLeftThigh.render(scale);
            this.rabbitRightThigh.render(scale);
            this.rabbitBody.render(scale);
            this.rabbitLeftArm.render(scale);
            this.rabbitRightArm.render(scale);
            this.rabbitTail.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.rabbitLeftFoot.render(scale);
            this.rabbitRightFoot.render(scale);
            this.rabbitLeftThigh.render(scale);
            this.rabbitRightThigh.render(scale);
            this.rabbitBody.render(scale);
            this.rabbitLeftArm.render(scale);
            this.rabbitRightArm.render(scale);
            this.rabbitHead.render(scale);
            this.rabbitRightEar.render(scale);
            this.rabbitLeftEar.render(scale);
            this.rabbitTail.render(scale);
            this.rabbitNose.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        final float f = p_78087_3_ - entityIn.ticksExisted;
        final EntityRabbit entityrabbit = (EntityRabbit)entityIn;
        final ModelRenderer rabbitNose = this.rabbitNose;
        final ModelRenderer rabbitHead = this.rabbitHead;
        final ModelRenderer rabbitRightEar = this.rabbitRightEar;
        final ModelRenderer rabbitLeftEar = this.rabbitLeftEar;
        final float n = p_78087_5_ * 0.017453292f;
        rabbitLeftEar.rotateAngleX = n;
        rabbitRightEar.rotateAngleX = n;
        rabbitHead.rotateAngleX = n;
        rabbitNose.rotateAngleX = n;
        final ModelRenderer rabbitNose2 = this.rabbitNose;
        final ModelRenderer rabbitHead2 = this.rabbitHead;
        final float n2 = p_78087_4_ * 0.017453292f;
        rabbitHead2.rotateAngleY = n2;
        rabbitNose2.rotateAngleY = n2;
        this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994f;
        this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994f;
        this.field_178701_m = MathHelper.sin(entityrabbit.func_175521_o(f) * 3.1415927f);
        final ModelRenderer rabbitLeftThigh = this.rabbitLeftThigh;
        final ModelRenderer rabbitRightThigh = this.rabbitRightThigh;
        final float n3 = (this.field_178701_m * 50.0f - 21.0f) * 0.017453292f;
        rabbitRightThigh.rotateAngleX = n3;
        rabbitLeftThigh.rotateAngleX = n3;
        final ModelRenderer rabbitLeftFoot = this.rabbitLeftFoot;
        final ModelRenderer rabbitRightFoot = this.rabbitRightFoot;
        final float n4 = this.field_178701_m * 50.0f * 0.017453292f;
        rabbitRightFoot.rotateAngleX = n4;
        rabbitLeftFoot.rotateAngleX = n4;
        final ModelRenderer rabbitLeftArm = this.rabbitLeftArm;
        final ModelRenderer rabbitRightArm = this.rabbitRightArm;
        final float n5 = (this.field_178701_m * -40.0f - 11.0f) * 0.017453292f;
        rabbitRightArm.rotateAngleX = n5;
        rabbitLeftArm.rotateAngleX = n5;
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float p_78086_2_, final float p_78086_3_, final float partialTickTime) {
    }
}
