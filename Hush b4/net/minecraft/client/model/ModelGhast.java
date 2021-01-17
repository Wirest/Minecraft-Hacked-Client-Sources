// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import java.util.Random;

public class ModelGhast extends ModelBase
{
    ModelRenderer body;
    ModelRenderer[] tentacles;
    
    public ModelGhast() {
        this.tentacles = new ModelRenderer[9];
        final int i = -16;
        (this.body = new ModelRenderer(this, 0, 0)).addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
        final ModelRenderer body = this.body;
        body.rotationPointY += 24 + i;
        final Random random = new Random(1660L);
        for (int j = 0; j < this.tentacles.length; ++j) {
            this.tentacles[j] = new ModelRenderer(this, 0, 0);
            final float f = ((j % 3 - j / 3 % 2 * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            final float f2 = (j / 3 / 2.0f * 2.0f - 1.0f) * 5.0f;
            final int k = random.nextInt(7) + 8;
            this.tentacles[j].addBox(-1.0f, 0.0f, -1.0f, 2, k, 2);
            this.tentacles[j].rotationPointX = f;
            this.tentacles[j].rotationPointZ = f2;
            this.tentacles[j].rotationPointY = (float)(31 + i);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].rotateAngleX = 0.2f * MathHelper.sin(p_78087_3_ * 0.3f + i) + 0.4f;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.6f, 0.0f);
        this.body.render(scale);
        ModelRenderer[] tentacles;
        for (int length = (tentacles = this.tentacles).length, i = 0; i < length; ++i) {
            final ModelRenderer modelrenderer = tentacles[i];
            modelrenderer.render(scale);
        }
        GlStateManager.popMatrix();
    }
}
