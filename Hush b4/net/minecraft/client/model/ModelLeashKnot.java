// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLeashKnot extends ModelBase
{
    public ModelRenderer field_110723_a;
    
    public ModelLeashKnot() {
        this(0, 0, 32, 32);
    }
    
    public ModelLeashKnot(final int p_i46365_1_, final int p_i46365_2_, final int p_i46365_3_, final int p_i46365_4_) {
        this.textureWidth = p_i46365_3_;
        this.textureHeight = p_i46365_4_;
        (this.field_110723_a = new ModelRenderer(this, p_i46365_1_, p_i46365_2_)).addBox(-3.0f, -6.0f, -3.0f, 6, 8, 6, 0.0f);
        this.field_110723_a.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.field_110723_a.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        this.field_110723_a.rotateAngleY = p_78087_4_ / 57.295776f;
        this.field_110723_a.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}
