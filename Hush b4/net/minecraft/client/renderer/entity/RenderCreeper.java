// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityCreeper;

public class RenderCreeper extends RenderLiving<EntityCreeper>
{
    private static final ResourceLocation creeperTextures;
    
    static {
        creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
    }
    
    public RenderCreeper(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCreeper(), 0.5f);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerCreeperCharge(this));
    }
    
    @Override
    protected void preRenderCallback(final EntityCreeper entitylivingbaseIn, final float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        final float f2 = 1.0f + MathHelper.sin(f * 100.0f) * f * 0.01f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        f *= f;
        f *= f;
        final float f3 = (1.0f + f * 0.4f) * f2;
        final float f4 = (1.0f + f * 0.1f) / f2;
        GlStateManager.scale(f3, f4, f3);
    }
    
    @Override
    protected int getColorMultiplier(final EntityCreeper entitylivingbaseIn, final float lightBrightness, final float partialTickTime) {
        final float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        if ((int)(f * 10.0f) % 2 == 0) {
            return 0;
        }
        int i = (int)(f * 0.2f * 255.0f);
        i = MathHelper.clamp_int(i, 0, 255);
        return i << 24 | 0xFFFFFF;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityCreeper entity) {
        return RenderCreeper.creeperTextures;
    }
}
