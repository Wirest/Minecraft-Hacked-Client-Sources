// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSkeletonHead extends ModelBase
{
    public ModelRenderer skeletonHead;
    
    public ModelSkeletonHead() {
        this(0, 35, 64, 64);
    }
    
    public ModelSkeletonHead(final int p_i1155_1_, final int p_i1155_2_, final int p_i1155_3_, final int p_i1155_4_) {
        this.textureWidth = p_i1155_3_;
        this.textureHeight = p_i1155_4_;
        (this.skeletonHead = new ModelRenderer(this, p_i1155_1_, p_i1155_2_)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.0f);
        this.skeletonHead.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.skeletonHead.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        this.skeletonHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.skeletonHead.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}
