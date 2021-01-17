// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.entity.monster.EntityIronGolem;

public class LayerIronGolemFlower implements LayerRenderer<EntityIronGolem>
{
    private final RenderIronGolem ironGolemRenderer;
    
    public LayerIronGolemFlower(final RenderIronGolem ironGolemRendererIn) {
        this.ironGolemRenderer = ironGolemRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityIronGolem entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.getHoldRoseTick() != 0) {
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(5.0f + 180.0f * ((ModelIronGolem)this.ironGolemRenderer.getMainModel()).ironGolemRightArm.rotateAngleX / 3.1415927f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(-0.9375f, -0.625f, -0.9375f);
            final float f = 0.5f;
            GlStateManager.scale(f, -f, f);
            final int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            final int j = i % 65536;
            final int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.ironGolemRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
