package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem extends RenderLiving
{
    private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderIronGolem(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelIronGolem(), 0.5F);
        this.addLayer(new LayerIronGolemFlower(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityIronGolem entity)
    {
        return ironGolemTextures;
    }

    protected void func_180588_a(EntityIronGolem ironGolem, float p_180588_2_, float p_180588_3_, float p_180588_4_)
    {
        super.rotateCorpse(ironGolem, p_180588_2_, p_180588_3_, p_180588_4_);

        if (ironGolem.limbSwingAmount >= 0.01D)
        {
            float var5 = 13.0F;
            float var6 = ironGolem.limbSwing - ironGolem.limbSwingAmount * (1.0F - p_180588_4_) + 6.0F;
            float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
            GlStateManager.rotate(6.5F * var7, 0.0F, 0.0F, 1.0F);
        }
    }

    @Override
	protected void rotateCorpse(EntityLivingBase bat, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.func_180588_a((EntityIronGolem)bat, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityIronGolem)entity);
    }
}
