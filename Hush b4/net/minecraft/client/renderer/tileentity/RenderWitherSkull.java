// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.client.renderer.entity.Render;

public class RenderWitherSkull extends Render<EntityWitherSkull>
{
    private static final ResourceLocation invulnerableWitherTextures;
    private static final ResourceLocation witherTextures;
    private final ModelSkeletonHead skeletonHeadModel;
    
    static {
        invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    }
    
    public RenderWitherSkull(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.skeletonHeadModel = new ModelSkeletonHead();
    }
    
    private float func_82400_a(final float p_82400_1_, final float p_82400_2_, final float p_82400_3_) {
        float f;
        for (f = p_82400_2_ - p_82400_1_; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return p_82400_1_ + p_82400_3_ * f;
    }
    
    @Override
    public void doRender(final EntityWitherSkull entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        final float f = this.func_82400_a(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        final float f2 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.translate((float)x, (float)y, (float)z);
        final float f3 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.skeletonHeadModel.render(entity, 0.0f, 0.0f, 0.0f, f, f2, f3);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityWitherSkull entity) {
        return entity.isInvulnerable() ? RenderWitherSkull.invulnerableWitherTextures : RenderWitherSkull.witherTextures;
    }
}
