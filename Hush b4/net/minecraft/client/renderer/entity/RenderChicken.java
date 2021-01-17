// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityChicken;

public class RenderChicken extends RenderLiving<EntityChicken>
{
    private static final ResourceLocation chickenTextures;
    
    static {
        chickenTextures = new ResourceLocation("textures/entity/chicken.png");
    }
    
    public RenderChicken(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityChicken entity) {
        return RenderChicken.chickenTextures;
    }
    
    @Override
    protected float handleRotationFloat(final EntityChicken livingBase, final float partialTicks) {
        final float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
        final float f2 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
        return (MathHelper.sin(f) + 1.0f) * f2;
    }
}
