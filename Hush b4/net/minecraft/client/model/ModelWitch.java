// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelWitch extends ModelVillager
{
    public boolean field_82900_g;
    private ModelRenderer field_82901_h;
    private ModelRenderer witchHat;
    
    public ModelWitch(final float p_i46361_1_) {
        super(p_i46361_1_, 0.0f, 64, 128);
        (this.field_82901_h = new ModelRenderer(this).setTextureSize(64, 128)).setRotationPoint(0.0f, -2.0f, 0.0f);
        this.field_82901_h.setTextureOffset(0, 0).addBox(0.0f, 3.0f, -6.75f, 1, 1, 1, -0.25f);
        this.villagerNose.addChild(this.field_82901_h);
        (this.witchHat = new ModelRenderer(this).setTextureSize(64, 128)).setRotationPoint(-5.0f, -10.03125f, -5.0f);
        this.witchHat.setTextureOffset(0, 64).addBox(0.0f, 0.0f, 0.0f, 10, 2, 10);
        this.villagerHead.addChild(this.witchHat);
        final ModelRenderer modelrenderer = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer.setTextureOffset(0, 76).addBox(0.0f, 0.0f, 0.0f, 7, 4, 7);
        modelrenderer.rotateAngleX = -0.05235988f;
        modelrenderer.rotateAngleZ = 0.02617994f;
        this.witchHat.addChild(modelrenderer);
        final ModelRenderer modelrenderer2 = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer2.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer2.setTextureOffset(0, 87).addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
        modelrenderer2.rotateAngleX = -0.10471976f;
        modelrenderer2.rotateAngleZ = 0.05235988f;
        modelrenderer.addChild(modelrenderer2);
        final ModelRenderer modelrenderer3 = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer3.setRotationPoint(1.75f, -2.0f, 2.0f);
        modelrenderer3.setTextureOffset(0, 95).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.25f);
        modelrenderer3.rotateAngleX = -0.20943952f;
        modelrenderer3.rotateAngleZ = 0.10471976f;
        modelrenderer2.addChild(modelrenderer3);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        final ModelRenderer villagerNose = this.villagerNose;
        final ModelRenderer villagerNose2 = this.villagerNose;
        final ModelRenderer villagerNose3 = this.villagerNose;
        final float offsetX = 0.0f;
        villagerNose3.offsetZ = offsetX;
        villagerNose2.offsetY = offsetX;
        villagerNose.offsetX = offsetX;
        final float f = 0.01f * (entityIn.getEntityId() % 10);
        this.villagerNose.rotateAngleX = MathHelper.sin(entityIn.ticksExisted * f) * 4.5f * 3.1415927f / 180.0f;
        this.villagerNose.rotateAngleY = 0.0f;
        this.villagerNose.rotateAngleZ = MathHelper.cos(entityIn.ticksExisted * f) * 2.5f * 3.1415927f / 180.0f;
        if (this.field_82900_g) {
            this.villagerNose.rotateAngleX = -0.9f;
            this.villagerNose.offsetZ = -0.09375f;
            this.villagerNose.offsetY = 0.1875f;
        }
    }
}
