// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityCow;

public class RenderCow extends RenderLiving<EntityCow>
{
    private static final ResourceLocation cowTextures;
    
    static {
        cowTextures = new ResourceLocation("textures/entity/cow/cow.png");
    }
    
    public RenderCow(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityCow entity) {
        return RenderCow.cowTextures;
    }
}
