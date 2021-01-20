package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class LayerWolfCollar implements LayerRenderer
{
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;
    private static final String __OBFID = "CL_00002405";

    public LayerWolfCollar(RenderWolf wolfRendererIn)
    {
        this.wolfRenderer = wolfRendererIn;
    }

    public void doRenderLayer(EntityWolf entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible())
        {
            this.wolfRenderer.bindTexture(WOLF_COLLAR);
            EnumDyeColor enumdyecolor = EnumDyeColor.byMetadata(entitylivingbaseIn.getCollarColor().getMetadata());
            float[] afloat = EntitySheep.func_175513_a(enumdyecolor);

            if (Config.isCustomColors())
            {
                afloat = CustomColors.getWolfCollarColors(enumdyecolor, afloat);
            }

            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.wolfRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        this.doRenderLayer((EntityWolf)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}
