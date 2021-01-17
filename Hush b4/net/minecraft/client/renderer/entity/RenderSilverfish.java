// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySilverfish;

public class RenderSilverfish extends RenderLiving<EntitySilverfish>
{
    private static final ResourceLocation silverfishTextures;
    
    static {
        silverfishTextures = new ResourceLocation("textures/entity/silverfish.png");
    }
    
    public RenderSilverfish(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSilverfish(), 0.3f);
    }
    
    @Override
    protected float getDeathMaxRotation(final EntitySilverfish entityLivingBaseIn) {
        return 180.0f;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySilverfish entity) {
        return RenderSilverfish.silverfishTextures;
    }
}
