// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelEnderMite extends ModelBase
{
    private static final int[][] field_178716_a;
    private static final int[][] field_178714_b;
    private static final int field_178715_c;
    private final ModelRenderer[] field_178713_d;
    
    static {
        field_178716_a = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
        field_178714_b = new int[][] { new int[2], { 0, 5 }, { 0, 14 }, { 0, 18 } };
        field_178715_c = ModelEnderMite.field_178716_a.length;
    }
    
    public ModelEnderMite() {
        this.field_178713_d = new ModelRenderer[ModelEnderMite.field_178715_c];
        float f = -3.5f;
        for (int i = 0; i < this.field_178713_d.length; ++i) {
            (this.field_178713_d[i] = new ModelRenderer(this, ModelEnderMite.field_178714_b[i][0], ModelEnderMite.field_178714_b[i][1])).addBox(ModelEnderMite.field_178716_a[i][0] * -0.5f, 0.0f, ModelEnderMite.field_178716_a[i][2] * -0.5f, ModelEnderMite.field_178716_a[i][0], ModelEnderMite.field_178716_a[i][1], ModelEnderMite.field_178716_a[i][2]);
            this.field_178713_d[i].setRotationPoint(0.0f, (float)(24 - ModelEnderMite.field_178716_a[i][1]), f);
            if (i < this.field_178713_d.length - 1) {
                f += (ModelEnderMite.field_178716_a[i][2] + ModelEnderMite.field_178716_a[i + 1][2]) * 0.5f;
            }
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        for (int i = 0; i < this.field_178713_d.length; ++i) {
            this.field_178713_d[i].render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        for (int i = 0; i < this.field_178713_d.length; ++i) {
            this.field_178713_d[i].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + Math.abs(i - 2));
            this.field_178713_d[i].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.1f * Math.abs(i - 2);
        }
    }
}
