package net.minecraft.client.renderer.entity;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderHorse extends RenderLiving
{
    private static final Map field_110852_a = Maps.newHashMap();
    private static final ResourceLocation whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
    private static final ResourceLocation muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
    private static final ResourceLocation donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
    private static final ResourceLocation zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
    private static final ResourceLocation skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");

    public RenderHorse(RenderManager rendermanagerIn, ModelHorse model, float shadowSizeIn)
    {
        super(rendermanagerIn, model, shadowSizeIn);
    }

    protected void preRenderCallback(EntityHorse entityhorseIn, float partialTicks)
    {
        float var3 = 1.0F;
        int var4 = entityhorseIn.getHorseType();

        if (var4 == 1)
        {
            var3 *= 0.87F;
        }
        else if (var4 == 2)
        {
            var3 *= 0.92F;
        }

        GlStateManager.scale(var3, var3, var3);
        super.preRenderCallback(entityhorseIn, partialTicks);
    }

    protected ResourceLocation getHorseTexture(EntityHorse entityhorseIn)
    {
        if (!entityhorseIn.func_110239_cn())
        {
            switch (entityhorseIn.getHorseType())
            {
                case 0:
                default:
                    return whiteHorseTextures;

                case 1:
                    return donkeyTextures;

                case 2:
                    return muleTextures;

                case 3:
                    return zombieHorseTextures;

                case 4:
                    return skeletonHorseTextures;
            }
        }
        else
        {
            return this.func_110848_b(entityhorseIn);
        }
    }

    private ResourceLocation func_110848_b(EntityHorse horse)
    {
        String var2 = horse.getHorseTexture();

        if (!horse.func_175507_cI())
        {
            return null;
        }
        else
        {
            ResourceLocation var3 = (ResourceLocation)field_110852_a.get(var2);

            if (var3 == null)
            {
                var3 = new ResourceLocation(var2);
                Minecraft.getMinecraft().getTextureManager().loadTexture(var3, new LayeredTexture(horse.getVariantTexturePaths()));
                field_110852_a.put(var2, var3);
            }

            return var3;
        }
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
	protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
        this.preRenderCallback((EntityHorse)entitylivingbaseIn, partialTickTime);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getHorseTexture((EntityHorse)entity);
    }
}
