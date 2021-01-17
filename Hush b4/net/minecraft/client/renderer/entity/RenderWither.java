// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWither;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityWither;

public class RenderWither extends RenderLiving<EntityWither>
{
    private static final ResourceLocation invulnerableWitherTextures;
    private static final ResourceLocation witherTextures;
    
    static {
        invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    }
    
    public RenderWither(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelWither(0.0f), 1.0f);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerWitherAura(this));
    }
    
    @Override
    public void doRender(final EntityWither entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        BossStatus.setBossStatus(entity, true);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityWither entity) {
        final int i = entity.getInvulTime();
        return (i > 0 && (i > 80 || i / 5 % 2 != 1)) ? RenderWither.invulnerableWitherTextures : RenderWither.witherTextures;
    }
    
    @Override
    protected void preRenderCallback(final EntityWither entitylivingbaseIn, final float partialTickTime) {
        float f = 2.0f;
        final int i = entitylivingbaseIn.getInvulTime();
        if (i > 0) {
            f -= (i - partialTickTime) / 220.0f * 0.5f;
        }
        GlStateManager.scale(f, f, f);
    }
}
