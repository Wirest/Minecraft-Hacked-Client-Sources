// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal extends ModelBase
{
    private ModelRenderer cube;
    private ModelRenderer glass;
    private ModelRenderer base;
    
    public ModelEnderCrystal(final float p_i1170_1_, final boolean p_i1170_2_) {
        this.glass = new ModelRenderer(this, "glass");
        this.glass.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        if (p_i1170_2_) {
            this.base = new ModelRenderer(this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0f, 0.0f, -6.0f, 12, 4, 12);
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.translate(0.0f, -0.5f, 0.0f);
        if (this.base != null) {
            this.base.render(scale);
        }
        GlStateManager.rotate(p_78088_3_, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.8f + p_78088_4_, 0.0f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        this.glass.render(scale);
        final float f = 0.875f;
        GlStateManager.scale(f, f, f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(p_78088_3_, 0.0f, 1.0f, 0.0f);
        this.glass.render(scale);
        GlStateManager.scale(f, f, f);
        GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
        GlStateManager.rotate(p_78088_3_, 0.0f, 1.0f, 0.0f);
        this.cube.render(scale);
        GlStateManager.popMatrix();
    }
}
