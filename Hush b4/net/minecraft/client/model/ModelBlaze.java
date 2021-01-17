// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelBlaze extends ModelBase
{
    private ModelRenderer[] blazeSticks;
    private ModelRenderer blazeHead;
    
    public ModelBlaze() {
        this.blazeSticks = new ModelRenderer[12];
        for (int i = 0; i < this.blazeSticks.length; ++i) {
            (this.blazeSticks[i] = new ModelRenderer(this, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        (this.blazeHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.blazeHead.render(scale);
        for (int i = 0; i < this.blazeSticks.length; ++i) {
            this.blazeSticks[i].render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        float f = p_78087_3_ * 3.1415927f * -0.1f;
        for (int i = 0; i < 4; ++i) {
            this.blazeSticks[i].rotationPointY = -2.0f + MathHelper.cos((i * 2 + p_78087_3_) * 0.25f);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f) * 9.0f;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0f;
            ++f;
        }
        f = 0.7853982f + p_78087_3_ * 3.1415927f * 0.03f;
        for (int j = 4; j < 8; ++j) {
            this.blazeSticks[j].rotationPointY = 2.0f + MathHelper.cos((j * 2 + p_78087_3_) * 0.25f);
            this.blazeSticks[j].rotationPointX = MathHelper.cos(f) * 7.0f;
            this.blazeSticks[j].rotationPointZ = MathHelper.sin(f) * 7.0f;
            ++f;
        }
        f = 0.47123894f + p_78087_3_ * 3.1415927f * -0.05f;
        for (int k = 8; k < 12; ++k) {
            this.blazeSticks[k].rotationPointY = 11.0f + MathHelper.cos((k * 1.5f + p_78087_3_) * 0.5f);
            this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0f;
            this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0f;
            ++f;
        }
        this.blazeHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.blazeHead.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}
