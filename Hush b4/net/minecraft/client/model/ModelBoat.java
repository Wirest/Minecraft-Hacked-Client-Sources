// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelBoat extends ModelBase
{
    public ModelRenderer[] boatSides;
    
    public ModelBoat() {
        (this.boatSides = new ModelRenderer[5])[0] = new ModelRenderer(this, 0, 8);
        this.boatSides[1] = new ModelRenderer(this, 0, 0);
        this.boatSides[2] = new ModelRenderer(this, 0, 0);
        this.boatSides[3] = new ModelRenderer(this, 0, 0);
        this.boatSides[4] = new ModelRenderer(this, 0, 0);
        final int i = 24;
        final int j = 6;
        final int k = 20;
        final int l = 4;
        this.boatSides[0].addBox((float)(-i / 2), (float)(-k / 2 + 2), -3.0f, i, k - 4, 4, 0.0f);
        this.boatSides[0].setRotationPoint(0.0f, (float)l, 0.0f);
        this.boatSides[1].addBox((float)(-i / 2 + 2), (float)(-j - 1), -1.0f, i - 4, j, 2, 0.0f);
        this.boatSides[1].setRotationPoint((float)(-i / 2 + 1), (float)l, 0.0f);
        this.boatSides[2].addBox((float)(-i / 2 + 2), (float)(-j - 1), -1.0f, i - 4, j, 2, 0.0f);
        this.boatSides[2].setRotationPoint((float)(i / 2 - 1), (float)l, 0.0f);
        this.boatSides[3].addBox((float)(-i / 2 + 2), (float)(-j - 1), -1.0f, i - 4, j, 2, 0.0f);
        this.boatSides[3].setRotationPoint(0.0f, (float)l, (float)(-k / 2 + 1));
        this.boatSides[4].addBox((float)(-i / 2 + 2), (float)(-j - 1), -1.0f, i - 4, j, 2, 0.0f);
        this.boatSides[4].setRotationPoint(0.0f, (float)l, (float)(k / 2 - 1));
        this.boatSides[0].rotateAngleX = 1.5707964f;
        this.boatSides[1].rotateAngleY = 4.712389f;
        this.boatSides[2].rotateAngleY = 1.5707964f;
        this.boatSides[3].rotateAngleY = 3.1415927f;
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }
    }
}
