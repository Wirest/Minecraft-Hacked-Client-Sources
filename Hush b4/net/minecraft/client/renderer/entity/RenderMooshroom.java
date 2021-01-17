// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityMooshroom;

public class RenderMooshroom extends RenderLiving<EntityMooshroom>
{
    private static final ResourceLocation mooshroomTextures;
    
    static {
        mooshroomTextures = new ResourceLocation("textures/entity/cow/mooshroom.png");
    }
    
    public RenderMooshroom(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerMooshroomMushroom(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMooshroom entity) {
        return RenderMooshroom.mooshroomTextures;
    }
}
