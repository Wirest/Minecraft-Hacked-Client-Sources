// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityBoat;

public class RenderBoat extends Render<EntityBoat>
{
    private static final ResourceLocation boatTextures;
    protected ModelBase modelBoat;
    
    static {
        boatTextures = new ResourceLocation("textures/entity/boat.png");
    }
    
    public RenderBoat(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelBoat = new ModelBoat();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityBoat entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.25f, (float)z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        final float f = entity.getTimeSinceHit() - partialTicks;
        float f2 = entity.getDamageTaken() - partialTicks;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f) * f * f2 / 10.0f * entity.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        final float f3 = 0.75f;
        GlStateManager.scale(f3, f3, f3);
        GlStateManager.scale(1.0f / f3, 1.0f / f3, 1.0f / f3);
        this.bindEntityTexture(entity);
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBoat entity) {
        return RenderBoat.boatTextures;
    }
}
