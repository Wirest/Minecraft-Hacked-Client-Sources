// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySkeleton;

public class RenderSkeleton extends RenderBiped<EntitySkeleton>
{
    private static final ResourceLocation skeletonTextures;
    private static final ResourceLocation witherSkeletonTextures;
    
    static {
        skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
        witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    }
    
    public RenderSkeleton(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSkeleton(), 0.5f);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.field_177189_c = new ModelSkeleton(0.5f, true);
                this.field_177186_d = new ModelSkeleton(1.0f, true);
            }
        });
    }
    
    @Override
    protected void preRenderCallback(final EntitySkeleton entitylivingbaseIn, final float partialTickTime) {
        if (entitylivingbaseIn.getSkeletonType() == 1) {
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
        }
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375f, 0.1875f, 0.0f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySkeleton entity) {
        return (entity.getSkeletonType() == 1) ? RenderSkeleton.witherSkeletonTextures : RenderSkeleton.skeletonTextures;
    }
}
