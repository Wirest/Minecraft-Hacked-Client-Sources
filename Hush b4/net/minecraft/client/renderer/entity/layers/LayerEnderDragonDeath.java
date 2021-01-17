// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Random;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.EntityDragon;

public class LayerEnderDragonDeath implements LayerRenderer<EntityDragon>
{
    @Override
    public void doRenderLayer(final EntityDragon entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.deathTicks > 0) {
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            final float f = (entitylivingbaseIn.deathTicks + partialTicks) / 200.0f;
            float f2 = 0.0f;
            if (f > 0.8f) {
                f2 = (f - 0.8f) / 0.2f;
            }
            final Random random = new Random(432L);
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 1);
            GlStateManager.disableAlpha();
            GlStateManager.enableCull();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -1.0f, -2.0f);
            for (int i = 0; i < (f + f * f) / 2.0f * 60.0f; ++i) {
                GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f + f * 90.0f, 0.0f, 0.0f, 1.0f);
                final float f3 = random.nextFloat() * 20.0f + 5.0f + f2 * 10.0f;
                final float f4 = random.nextFloat() * 2.0f + 1.0f + f2 * 2.0f;
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(0.0, 0.0, 0.0).color(255, 255, 255, (int)(255.0f * (1.0f - f2))).endVertex();
                worldrenderer.pos(-0.866 * f4, f3, -0.5f * f4).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(0.866 * f4, f3, -0.5f * f4).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(0.0, f3, 1.0f * f4).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(-0.866 * f4, f3, -0.5f * f4).color(255, 0, 255, 0).endVertex();
                tessellator.draw();
            }
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.shadeModel(7424);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
