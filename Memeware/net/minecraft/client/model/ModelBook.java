package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBook extends ModelBase {
    /**
     * Right cover renderer (when facing the book)
     */
    public ModelRenderer coverRight = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6, 10, 0);

    /**
     * Left cover renderer (when facing the book)
     */
    public ModelRenderer coverLeft = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5.0F, 0.0F, 6, 10, 0);

    /**
     * The right pages renderer (when facing the book)
     */
    public ModelRenderer pagesRight = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4.0F, -0.99F, 5, 8, 1);

    /**
     * The left pages renderer (when facing the book)
     */
    public ModelRenderer pagesLeft = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4.0F, -0.01F, 5, 8, 1);

    /**
     * Right cover renderer (when facing the book)
     */
    public ModelRenderer flippingPageRight = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);

    /**
     * Right cover renderer (when facing the book)
     */
    public ModelRenderer flippingPageLeft = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);

    /**
     * The renderer of spine of the book
     */
    public ModelRenderer bookSpine = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2, 10, 0);
    private static final String __OBFID = "CL_00000833";

    public ModelBook() {
        this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.bookSpine.rotateAngleY = ((float) Math.PI / 2F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.coverRight.render(p_78088_7_);
        this.coverLeft.render(p_78088_7_);
        this.bookSpine.render(p_78088_7_);
        this.pagesRight.render(p_78088_7_);
        this.pagesLeft.render(p_78088_7_);
        this.flippingPageRight.render(p_78088_7_);
        this.flippingPageLeft.render(p_78088_7_);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        float var8 = (MathHelper.sin(p_78087_1_ * 0.02F) * 0.1F + 1.25F) * p_78087_4_;
        this.coverRight.rotateAngleY = (float) Math.PI + var8;
        this.coverLeft.rotateAngleY = -var8;
        this.pagesRight.rotateAngleY = var8;
        this.pagesLeft.rotateAngleY = -var8;
        this.flippingPageRight.rotateAngleY = var8 - var8 * 2.0F * p_78087_2_;
        this.flippingPageLeft.rotateAngleY = var8 - var8 * 2.0F * p_78087_3_;
        this.pagesRight.rotationPointX = MathHelper.sin(var8);
        this.pagesLeft.rotationPointX = MathHelper.sin(var8);
        this.flippingPageRight.rotationPointX = MathHelper.sin(var8);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(var8);
    }
}
