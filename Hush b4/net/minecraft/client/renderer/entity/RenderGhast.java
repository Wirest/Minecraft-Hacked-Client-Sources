// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGhast;

public class RenderGhast extends RenderLiving<EntityGhast>
{
    private static final ResourceLocation ghastTextures;
    private static final ResourceLocation ghastShootingTextures;
    
    static {
        ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
        ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
    }
    
    public RenderGhast(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGhast(), 0.5f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGhast entity) {
        return entity.isAttacking() ? RenderGhast.ghastShootingTextures : RenderGhast.ghastTextures;
    }
    
    @Override
    protected void preRenderCallback(final EntityGhast entitylivingbaseIn, final float partialTickTime) {
        final float f = 1.0f;
        final float f2 = (8.0f + f) / 2.0f;
        final float f3 = (8.0f + 1.0f / f) / 2.0f;
        GlStateManager.scale(f3, f2, f3);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
