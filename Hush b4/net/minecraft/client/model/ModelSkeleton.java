// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.EntityLivingBase;

public class ModelSkeleton extends ModelZombie
{
    public ModelSkeleton() {
        this(0.0f, false);
    }
    
    public ModelSkeleton(final float p_i46303_1_, final boolean p_i46303_2_) {
        super(p_i46303_1_, 0.0f, 64, 32);
        if (!p_i46303_2_) {
            (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
            this.bipedLeftArm = new ModelRenderer(this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedRightLeg = new ModelRenderer(this, 0, 16)).addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.0f);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.0f);
        }
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float p_78086_2_, final float p_78086_3_, final float partialTickTime) {
        this.aimedBow = (((EntitySkeleton)entitylivingbaseIn).getSkeletonType() == 1);
        super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
    }
}
