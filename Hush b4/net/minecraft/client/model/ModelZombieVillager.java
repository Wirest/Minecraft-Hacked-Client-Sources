// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelZombieVillager extends ModelBiped
{
    public ModelZombieVillager() {
        this(0.0f, 0.0f, false);
    }
    
    public ModelZombieVillager(final float p_i1165_1_, final float p_i1165_2_, final boolean p_i1165_3_) {
        super(p_i1165_1_, 0.0f, 64, p_i1165_3_ ? 32 : 64);
        if (p_i1165_3_) {
            (this.bipedHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -10.0f, -4.0f, 8, 8, 8, p_i1165_1_);
            this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
        }
        else {
            (this.bipedHead = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedHead.setTextureOffset(0, 32).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i1165_1_);
            this.bipedHead.setTextureOffset(24, 32).addBox(-1.0f, -3.0f, -6.0f, 2, 4, 2, p_i1165_1_);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        this.bipedRightArm.rotateAngleX = -1.5707964f;
        this.bipedLeftArm.rotateAngleX = -1.5707964f;
        final ModelRenderer bipedRightArm = this.bipedRightArm;
        bipedRightArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
        final ModelRenderer bipedLeftArm = this.bipedLeftArm;
        bipedLeftArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
        final ModelRenderer bipedRightArm2 = this.bipedRightArm;
        bipedRightArm2.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
        bipedLeftArm2.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedRightArm3 = this.bipedRightArm;
        bipedRightArm3.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
        final ModelRenderer bipedLeftArm3 = this.bipedLeftArm;
        bipedLeftArm3.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
    }
}
