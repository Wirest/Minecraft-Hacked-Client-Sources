// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityEndermite;

public class RenderEndermite extends RenderLiving<EntityEndermite>
{
    private static final ResourceLocation ENDERMITE_TEXTURES;
    
    static {
        ENDERMITE_TEXTURES = new ResourceLocation("textures/entity/endermite.png");
    }
    
    public RenderEndermite(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelEnderMite(), 0.3f);
    }
    
    @Override
    protected float getDeathMaxRotation(final EntityEndermite entityLivingBaseIn) {
        return 180.0f;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityEndermite entity) {
        return RenderEndermite.ENDERMITE_TEXTURES;
    }
}
