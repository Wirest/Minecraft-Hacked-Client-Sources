// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntitySquid;

public class RenderSquid extends RenderLiving<EntitySquid>
{
    private static final ResourceLocation squidTextures;
    
    static {
        squidTextures = new ResourceLocation("textures/entity/squid.png");
    }
    
    public RenderSquid(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySquid entity) {
        return RenderSquid.squidTextures;
    }
    
    @Override
    protected void rotateCorpse(final EntitySquid bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        final float f = bat.prevSquidPitch + (bat.squidPitch - bat.prevSquidPitch) * partialTicks;
        final float f2 = bat.prevSquidYaw + (bat.squidYaw - bat.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0f, 0.5f, 0.0f);
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.2f, 0.0f);
    }
    
    @Override
    protected float handleRotationFloat(final EntitySquid livingBase, final float partialTicks) {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}
