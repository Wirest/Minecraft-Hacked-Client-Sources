// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityOcelot;

public class RenderOcelot extends RenderLiving<EntityOcelot>
{
    private static final ResourceLocation blackOcelotTextures;
    private static final ResourceLocation ocelotTextures;
    private static final ResourceLocation redOcelotTextures;
    private static final ResourceLocation siameseOcelotTextures;
    
    static {
        blackOcelotTextures = new ResourceLocation("textures/entity/cat/black.png");
        ocelotTextures = new ResourceLocation("textures/entity/cat/ocelot.png");
        redOcelotTextures = new ResourceLocation("textures/entity/cat/red.png");
        siameseOcelotTextures = new ResourceLocation("textures/entity/cat/siamese.png");
    }
    
    public RenderOcelot(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityOcelot entity) {
        switch (entity.getTameSkin()) {
            default: {
                return RenderOcelot.ocelotTextures;
            }
            case 1: {
                return RenderOcelot.blackOcelotTextures;
            }
            case 2: {
                return RenderOcelot.redOcelotTextures;
            }
            case 3: {
                return RenderOcelot.siameseOcelotTextures;
            }
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityOcelot entitylivingbaseIn, final float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        if (entitylivingbaseIn.isTamed()) {
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
        }
    }
}
