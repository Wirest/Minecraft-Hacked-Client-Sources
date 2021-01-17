// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityPigZombie;

public class RenderPigZombie extends RenderBiped<EntityPigZombie>
{
    private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE;
    
    static {
        ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");
    }
    
    public RenderPigZombie(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5f, 1.0f);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.field_177189_c = new ModelZombie(0.5f, true);
                this.field_177186_d = new ModelZombie(1.0f, true);
            }
        });
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPigZombie entity) {
        return RenderPigZombie.ZOMBIE_PIGMAN_TEXTURE;
    }
}
