package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool implements LayerRenderer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderSheep sheepRenderer;
    private final ModelSheep1 sheepModel = new ModelSheep1();

    public LayerSheepWool(RenderSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
    }

    public void doRenderLayer(EntitySheep sheep, float p_177162_2_, float p_177162_3_, float p_177162_4_, float p_177162_5_, float p_177162_6_, float p_177162_7_, float p_177162_8_)
    {
        if (!sheep.getSheared() && !sheep.isInvisible())
        {
            this.sheepRenderer.bindTexture(TEXTURE);

            if (sheep.hasCustomName() && "jeb_".equals(sheep.getCustomNameTag()))
            {
                int var10 = sheep.ticksExisted / 25 + sheep.getEntityId();
                int var11 = EnumDyeColor.values().length;
                int var12 = var10 % var11;
                int var13 = (var10 + 1) % var11;
                float var14 = (sheep.ticksExisted % 25 + p_177162_4_) / 25.0F;
                float[] var15 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(var12));
                float[] var16 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(var13));
                GlStateManager.color(var15[0] * (1.0F - var14) + var16[0] * var14, var15[1] * (1.0F - var14) + var16[1] * var14, var15[2] * (1.0F - var14) + var16[2] * var14);
            }
            else
            {
                float[] var9 = EntitySheep.func_175513_a(sheep.getFleeceColor());
                GlStateManager.color(var9[0], var9[1], var9[2]);
            }

            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(sheep, p_177162_2_, p_177162_3_, p_177162_4_);
            this.sheepModel.render(sheep, p_177162_2_, p_177162_3_, p_177162_5_, p_177162_6_, p_177162_7_, p_177162_8_);
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
        this.doRenderLayer((EntitySheep)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
