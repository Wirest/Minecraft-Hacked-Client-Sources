package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBoat extends Render
{
    private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");

    /** instance of ModelBoat for rendering */
    protected ModelBase modelBoat = new ModelBoat();
    private static final String __OBFID = "CL_00000981";

    public RenderBoat(RenderManager p_i46190_1_)
    {
        super(p_i46190_1_);
        this.shadowSize = 0.5F;
    }

    public void func_180552_a(EntityBoat p_180552_1_, double p_180552_2_, double p_180552_4_, double p_180552_6_, float p_180552_8_, float p_180552_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180552_2_, (float)p_180552_4_ + 0.25F, (float)p_180552_6_);
        GlStateManager.rotate(180.0F - p_180552_8_, 0.0F, 1.0F, 0.0F);
        float var10 = (float)p_180552_1_.getTimeSinceHit() - p_180552_9_;
        float var11 = p_180552_1_.getDamageTaken() - p_180552_9_;

        if (var11 < 0.0F)
        {
            var11 = 0.0F;
        }

        if (var10 > 0.0F)
        {
            GlStateManager.rotate(MathHelper.sin(var10) * var10 * var11 / 10.0F * (float)p_180552_1_.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        float var12 = 0.75F;
        GlStateManager.scale(var12, var12, var12);
        GlStateManager.scale(1.0F / var12, 1.0F / var12, 1.0F / var12);
        this.bindEntityTexture(p_180552_1_);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        this.modelBoat.render(p_180552_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(p_180552_1_, p_180552_2_, p_180552_4_, p_180552_6_, p_180552_8_, p_180552_9_);
    }

    protected ResourceLocation func_180553_a(EntityBoat p_180553_1_)
    {
        return boatTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.func_180553_a((EntityBoat)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.func_180552_a((EntityBoat)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
