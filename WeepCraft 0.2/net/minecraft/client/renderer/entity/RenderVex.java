package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVex;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.util.ResourceLocation;

public class RenderVex extends RenderBiped<EntityVex>
{
    private static final ResourceLocation field_191343_a = new ResourceLocation("textures/entity/illager/vex.png");
    private static final ResourceLocation field_191344_j = new ResourceLocation("textures/entity/illager/vex_charging.png");
    private int field_191345_k;

    public RenderVex(RenderManager p_i47190_1_)
    {
        super(p_i47190_1_, new ModelVex(), 0.3F);
        this.field_191345_k = ((ModelVex)this.mainModel).func_191228_a();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityVex entity)
    {
        return entity.func_190647_dj() ? field_191344_j : field_191343_a;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityVex entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        int i = ((ModelVex)this.mainModel).func_191228_a();

        if (i != this.field_191345_k)
        {
            this.mainModel = new ModelVex();
            this.field_191345_k = i;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityVex entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }
}
