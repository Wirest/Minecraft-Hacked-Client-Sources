package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerEndermanEyes implements LayerRenderer
{
    private static final ResourceLocation field_177203_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;
    private static final String __OBFID = "CL_00002418";

    public LayerEndermanEyes(RenderEnderman endermanRendererIn)
    {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEnderman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        this.endermanRenderer.bindTexture(field_177203_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        char c0 = 61680;
        int i = c0 % 65536;
        int j = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i / 1.0F, (float)j / 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (Config.isShaders())
        {
            Shaders.beginSpiderEyes();
        }

        this.endermanRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        this.endermanRenderer.func_177105_a(entitylivingbaseIn, partialTicks);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        this.doRenderLayer((EntityEnderman)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}
