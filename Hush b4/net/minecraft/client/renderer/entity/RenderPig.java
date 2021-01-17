// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityPig;

public class RenderPig extends RenderLiving<EntityPig>
{
    private static final ResourceLocation pigTextures;
    
    static {
        pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
    }
    
    public RenderPig(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerSaddle(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPig entity) {
        return RenderPig.pigTextures;
    }
}
