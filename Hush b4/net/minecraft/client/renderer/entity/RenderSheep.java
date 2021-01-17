// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntitySheep;

public class RenderSheep extends RenderLiving<EntitySheep>
{
    private static final ResourceLocation shearedSheepTextures;
    
    static {
        shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
    }
    
    public RenderSheep(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerSheepWool(this));
    }
    
    private void addLayer(final LayerSheepWool layerSheepWool) {
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySheep entity) {
        return RenderSheep.shearedSheepTextures;
    }
}
