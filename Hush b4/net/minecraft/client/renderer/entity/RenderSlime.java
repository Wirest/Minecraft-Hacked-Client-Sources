// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySlime;

public class RenderSlime extends RenderLiving<EntitySlime>
{
    private static final ResourceLocation slimeTextures;
    
    static {
        slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
    }
    
    public RenderSlime(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerSlimeGel(this));
    }
    
    @Override
    public void doRender(final EntitySlime entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.shadowSize = 0.25f * entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(final EntitySlime entitylivingbaseIn, final float partialTickTime) {
        final float f = (float)entitylivingbaseIn.getSlimeSize();
        final float f2 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f * 0.5f + 1.0f);
        final float f3 = 1.0f / (f2 + 1.0f);
        GlStateManager.scale(f3 * f, 1.0f / f3 * f, f3 * f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySlime entity) {
        return RenderSlime.slimeTextures;
    }
}
