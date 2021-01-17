// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelBook extends ModelBase
{
    public ModelRenderer coverRight;
    public ModelRenderer coverLeft;
    public ModelRenderer pagesRight;
    public ModelRenderer pagesLeft;
    public ModelRenderer flippingPageRight;
    public ModelRenderer flippingPageLeft;
    public ModelRenderer bookSpine;
    
    public ModelBook() {
        this.coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0f, -5.0f, 0.0f, 6, 10, 0);
        this.coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0f, -5.0f, 0.0f, 6, 10, 0);
        this.pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0f, -4.0f, -0.99f, 5, 8, 1);
        this.pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0f, -4.0f, -0.01f, 5, 8, 1);
        this.flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0f, -5.0f, 0.0f, 2, 10, 0);
        this.coverRight.setRotationPoint(0.0f, 0.0f, -1.0f);
        this.coverLeft.setRotationPoint(0.0f, 0.0f, 1.0f);
        this.bookSpine.rotateAngleY = 1.5707964f;
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.coverRight.render(scale);
        this.coverLeft.render(scale);
        this.bookSpine.render(scale);
        this.pagesRight.render(scale);
        this.pagesLeft.render(scale);
        this.flippingPageRight.render(scale);
        this.flippingPageLeft.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        final float f = (MathHelper.sin(p_78087_1_ * 0.02f) * 0.1f + 1.25f) * p_78087_4_;
        this.coverRight.rotateAngleY = 3.1415927f + f;
        this.coverLeft.rotateAngleY = -f;
        this.pagesRight.rotateAngleY = f;
        this.pagesLeft.rotateAngleY = -f;
        this.flippingPageRight.rotateAngleY = f - f * 2.0f * p_78087_2_;
        this.flippingPageLeft.rotateAngleY = f - f * 2.0f * p_78087_3_;
        this.pagesRight.rotationPointX = MathHelper.sin(f);
        this.pagesLeft.rotationPointX = MathHelper.sin(f);
        this.flippingPageRight.rotationPointX = MathHelper.sin(f);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(f);
    }
}
