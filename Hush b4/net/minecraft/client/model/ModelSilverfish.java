// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelSilverfish extends ModelBase
{
    private ModelRenderer[] silverfishBodyParts;
    private ModelRenderer[] silverfishWings;
    private float[] field_78170_c;
    private static final int[][] silverfishBoxLength;
    private static final int[][] silverfishTexturePositions;
    
    static {
        silverfishBoxLength = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
        silverfishTexturePositions = new int[][] { new int[2], { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11, 0 }, { 13, 4 } };
    }
    
    public ModelSilverfish() {
        this.silverfishBodyParts = new ModelRenderer[7];
        this.field_78170_c = new float[7];
        float f = -3.5f;
        for (int i = 0; i < this.silverfishBodyParts.length; ++i) {
            (this.silverfishBodyParts[i] = new ModelRenderer(this, ModelSilverfish.silverfishTexturePositions[i][0], ModelSilverfish.silverfishTexturePositions[i][1])).addBox(ModelSilverfish.silverfishBoxLength[i][0] * -0.5f, 0.0f, ModelSilverfish.silverfishBoxLength[i][2] * -0.5f, ModelSilverfish.silverfishBoxLength[i][0], ModelSilverfish.silverfishBoxLength[i][1], ModelSilverfish.silverfishBoxLength[i][2]);
            this.silverfishBodyParts[i].setRotationPoint(0.0f, (float)(24 - ModelSilverfish.silverfishBoxLength[i][1]), f);
            this.field_78170_c[i] = f;
            if (i < this.silverfishBodyParts.length - 1) {
                f += (ModelSilverfish.silverfishBoxLength[i][2] + ModelSilverfish.silverfishBoxLength[i + 1][2]) * 0.5f;
            }
        }
        this.silverfishWings = new ModelRenderer[3];
        (this.silverfishWings[0] = new ModelRenderer(this, 20, 0)).addBox(-5.0f, 0.0f, ModelSilverfish.silverfishBoxLength[2][2] * -0.5f, 10, 8, ModelSilverfish.silverfishBoxLength[2][2]);
        this.silverfishWings[0].setRotationPoint(0.0f, 16.0f, this.field_78170_c[2]);
        (this.silverfishWings[1] = new ModelRenderer(this, 20, 11)).addBox(-3.0f, 0.0f, ModelSilverfish.silverfishBoxLength[4][2] * -0.5f, 6, 4, ModelSilverfish.silverfishBoxLength[4][2]);
        this.silverfishWings[1].setRotationPoint(0.0f, 20.0f, this.field_78170_c[4]);
        (this.silverfishWings[2] = new ModelRenderer(this, 20, 18)).addBox(-3.0f, 0.0f, ModelSilverfish.silverfishBoxLength[4][2] * -0.5f, 6, 5, ModelSilverfish.silverfishBoxLength[1][2]);
        this.silverfishWings[2].setRotationPoint(0.0f, 19.0f, this.field_78170_c[1]);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        for (int i = 0; i < this.silverfishBodyParts.length; ++i) {
            this.silverfishBodyParts[i].render(scale);
        }
        for (int j = 0; j < this.silverfishWings.length; ++j) {
            this.silverfishWings[j].render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        for (int i = 0; i < this.silverfishBodyParts.length; ++i) {
            this.silverfishBodyParts[i].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.05f * (1 + Math.abs(i - 2));
            this.silverfishBodyParts[i].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.2f * Math.abs(i - 2);
        }
        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}
