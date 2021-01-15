package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.ResourceLocation;

public class RenderEndermite extends RenderLiving
{
    private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation("textures/entity/endermite.png");

    public RenderEndermite(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderMite(), 0.3F);
    }

    protected float func_177107_a(EntityEndermite endermite)
    {
        return 180.0F;
    }

    protected ResourceLocation func_177106_b(EntityEndermite endermite)
    {
        return ENDERMITE_TEXTURES;
    }

    @Override
	protected float getDeathMaxRotation(EntityLivingBase entityLivingBaseIn)
    {
        return this.func_177107_a((EntityEndermite)entityLivingBaseIn);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_177106_b((EntityEndermite)entity);
    }
}
