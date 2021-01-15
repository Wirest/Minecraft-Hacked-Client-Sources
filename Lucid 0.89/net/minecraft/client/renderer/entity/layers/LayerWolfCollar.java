package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerWolfCollar implements LayerRenderer
{
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf wolfRendererIn)
    {
        this.wolfRenderer = wolfRendererIn;
    }

    public void func_177145_a(EntityWolf wolf, float p_177145_2_, float p_177145_3_, float p_177145_4_, float p_177145_5_, float p_177145_6_, float p_177145_7_, float p_177145_8_)
    {
        if (wolf.isTamed() && !wolf.isInvisible())
        {
            this.wolfRenderer.bindTexture(WOLF_COLLAR);
            EnumDyeColor var9 = EnumDyeColor.byMetadata(wolf.getCollarColor().getMetadata());
            float[] var10 = EntitySheep.func_175513_a(var9);
            GlStateManager.color(var10[0], var10[1], var10[2]);
            this.wolfRenderer.getMainModel().render(wolf, p_177145_2_, p_177145_3_, p_177145_5_, p_177145_6_, p_177145_7_, p_177145_8_);
        }
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return true;
    }

    @Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177145_a((EntityWolf)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
