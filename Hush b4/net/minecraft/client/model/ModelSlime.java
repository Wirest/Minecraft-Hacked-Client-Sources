// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase
{
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;
    
    public ModelSlime(final int p_i1157_1_) {
        (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        if (p_i1157_1_ > 0) {
            (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            (this.slimeRightEye = new ModelRenderer(this, 32, 0)).addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeLeftEye = new ModelRenderer(this, 32, 4)).addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
            (this.slimeMouth = new ModelRenderer(this, 32, 8)).addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.slimeBodies.render(scale);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(scale);
            this.slimeLeftEye.render(scale);
            this.slimeMouth.render(scale);
        }
    }
}
