// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.entity.passive.EntityHorse;

public class RenderHorse extends RenderLiving<EntityHorse>
{
    private static final Map<String, ResourceLocation> field_110852_a;
    private static final ResourceLocation whiteHorseTextures;
    private static final ResourceLocation muleTextures;
    private static final ResourceLocation donkeyTextures;
    private static final ResourceLocation zombieHorseTextures;
    private static final ResourceLocation skeletonHorseTextures;
    
    static {
        field_110852_a = Maps.newHashMap();
        whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
        muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
        donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
        zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
        skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    }
    
    public RenderHorse(final RenderManager rendermanagerIn, final ModelHorse model, final float shadowSizeIn) {
        super(rendermanagerIn, model, shadowSizeIn);
    }
    
    @Override
    protected void preRenderCallback(final EntityHorse entitylivingbaseIn, final float partialTickTime) {
        float f = 1.0f;
        final int i = entitylivingbaseIn.getHorseType();
        if (i == 1) {
            f *= 0.87f;
        }
        else if (i == 2) {
            f *= 0.92f;
        }
        GlStateManager.scale(f, f, f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityHorse entity) {
        if (entity.func_110239_cn()) {
            return this.func_110848_b(entity);
        }
        switch (entity.getHorseType()) {
            default: {
                return RenderHorse.whiteHorseTextures;
            }
            case 1: {
                return RenderHorse.donkeyTextures;
            }
            case 2: {
                return RenderHorse.muleTextures;
            }
            case 3: {
                return RenderHorse.zombieHorseTextures;
            }
            case 4: {
                return RenderHorse.skeletonHorseTextures;
            }
        }
    }
    
    private ResourceLocation func_110848_b(final EntityHorse horse) {
        final String s = horse.getHorseTexture();
        if (!horse.func_175507_cI()) {
            return null;
        }
        ResourceLocation resourcelocation = RenderHorse.field_110852_a.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(horse.getVariantTexturePaths()));
            RenderHorse.field_110852_a.put(s, resourcelocation);
        }
        return resourcelocation;
    }
}
