// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityIronGolem;

public class RenderIronGolem extends RenderLiving<EntityIronGolem>
{
    private static final ResourceLocation ironGolemTextures;
    
    static {
        ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");
    }
    
    public RenderIronGolem(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelIronGolem(), 0.5f);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerIronGolemFlower(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityIronGolem entity) {
        return RenderIronGolem.ironGolemTextures;
    }
    
    @Override
    protected void rotateCorpse(final EntityIronGolem bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
        if (bat.limbSwingAmount >= 0.01) {
            final float f = 13.0f;
            final float f2 = bat.limbSwing - bat.limbSwingAmount * (1.0f - partialTicks) + 6.0f;
            final float f3 = (Math.abs(f2 % f - f * 0.5f) - f * 0.25f) / (f * 0.25f);
            GlStateManager.rotate(6.5f * f3, 0.0f, 0.0f, 1.0f);
        }
    }
}
