package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerSpiderEyes implements LayerRenderer {
    private static final ResourceLocation field_177150_a = new ResourceLocation("textures/entity/spider_eyes.png");
    private final RenderSpider field_177149_b;
    private static final String __OBFID = "CL_00002410";

    public LayerSpiderEyes(RenderSpider p_i46109_1_) {
        this.field_177149_b = p_i46109_1_;
    }

    public void func_177148_a(EntitySpider p_177148_1_, float p_177148_2_, float p_177148_3_, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float p_177148_8_) {
        this.field_177149_b.bindTexture(field_177150_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);

        if (p_177148_1_.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }

        char var9 = 61680;
        int var10 = var9 % 65536;
        int var11 = var9 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var10 / 1.0F, (float) var11 / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }

        this.field_177149_b.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
        int var12 = p_177148_1_.getBrightnessForRender(p_177148_4_);
        var10 = var12 % 65536;
        var11 = var12 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var10 / 1.0F, (float) var11 / 1.0F);
        this.field_177149_b.func_177105_a(p_177148_1_, p_177148_4_);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.func_177148_a((EntitySpider) p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
