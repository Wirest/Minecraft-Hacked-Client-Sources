package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube extends RenderLiving {
    private static final ResourceLocation magmaCubeTextures = new ResourceLocation("textures/entity/slime/magmacube.png");
    private static final String __OBFID = "CL_00001009";

    public RenderMagmaCube(RenderManager p_i46159_1_) {
        super(p_i46159_1_, new ModelMagmaCube(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMagmaCube p_110775_1_) {
        return magmaCubeTextures;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityMagmaCube p_77041_1_, float p_77041_2_) {
        int var3 = p_77041_1_.getSlimeSize();
        float var4 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_) / ((float) var3 * 0.5F + 1.0F);
        float var5 = 1.0F / (var4 + 1.0F);
        float var6 = (float) var3;
        GlStateManager.scale(var5 * var6, 1.0F / var5 * var6, var5 * var6);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.preRenderCallback((EntityMagmaCube) p_77041_1_, p_77041_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityMagmaCube) p_110775_1_);
    }
}
