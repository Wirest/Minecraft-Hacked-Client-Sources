// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import shadersmod.client.Shaders;
import optifine.Config;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.util.ResourceLocation;

public class LayerEndermanEyes implements LayerRenderer
{
    private static final ResourceLocation field_177203_a;
    private final RenderEnderman endermanRenderer;
    private static final String __OBFID = "CL_00002418";
    
    static {
        field_177203_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    }
    
    public LayerEndermanEyes(final RenderEnderman endermanRendererIn) {
        this.endermanRenderer = endermanRendererIn;
    }
    
    public void doRenderLayer(final EntityEnderman entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.endermanRenderer.bindTexture(LayerEndermanEyes.field_177203_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        final char c0 = '\uf0f0';
        final int i = c0 % 65536;
        final int j = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0f, j / 1.0f);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }
        this.endermanRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        this.endermanRenderer.func_177105_a(entitylivingbaseIn, partialTicks);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.doRenderLayer((EntityEnderman)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}
