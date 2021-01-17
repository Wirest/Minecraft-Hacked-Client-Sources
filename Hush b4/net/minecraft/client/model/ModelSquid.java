// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase
{
    ModelRenderer squidBody;
    ModelRenderer[] squidTentacles;
    
    public ModelSquid() {
        this.squidTentacles = new ModelRenderer[8];
        final int i = -16;
        (this.squidBody = new ModelRenderer(this, 0, 0)).addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        final ModelRenderer squidBody = this.squidBody;
        squidBody.rotationPointY += 24 + i;
        for (int j = 0; j < this.squidTentacles.length; ++j) {
            this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
            double d0 = j * 3.141592653589793 * 2.0 / this.squidTentacles.length;
            final float f = (float)Math.cos(d0) * 5.0f;
            final float f2 = (float)Math.sin(d0) * 5.0f;
            this.squidTentacles[j].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[j].rotationPointX = f;
            this.squidTentacles[j].rotationPointZ = f2;
            this.squidTentacles[j].rotationPointY = (float)(31 + i);
            d0 = j * 3.141592653589793 * -2.0 / this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[j].rotateAngleY = (float)d0;
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        ModelRenderer[] squidTentacles;
        for (int length = (squidTentacles = this.squidTentacles).length, i = 0; i < length; ++i) {
            final ModelRenderer modelrenderer = squidTentacles[i];
            modelrenderer.rotateAngleX = p_78087_3_;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.squidBody.render(scale);
        for (int i = 0; i < this.squidTentacles.length; ++i) {
            this.squidTentacles[i].render(scale);
        }
    }
}
