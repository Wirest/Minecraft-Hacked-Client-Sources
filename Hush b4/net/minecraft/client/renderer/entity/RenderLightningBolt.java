// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.effect.EntityLightningBolt;

public class RenderLightningBolt extends Render<EntityLightningBolt>
{
    public RenderLightningBolt(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityLightningBolt entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        final double[] adouble = new double[8];
        final double[] adouble2 = new double[8];
        double d0 = 0.0;
        double d2 = 0.0;
        final Random random = new Random(entity.boltVertex);
        for (int i = 7; i >= 0; --i) {
            adouble[i] = d0;
            adouble2[i] = d2;
            d0 += random.nextInt(11) - 5;
            d2 += random.nextInt(11) - 5;
        }
        for (int k1 = 0; k1 < 4; ++k1) {
            final Random random2 = new Random(entity.boltVertex);
            for (int j = 0; j < 3; ++j) {
                int l = 7;
                int m = 0;
                if (j > 0) {
                    l = 7 - j;
                }
                if (j > 0) {
                    m = l - 2;
                }
                double d3 = adouble[l] - d0;
                double d4 = adouble2[l] - d2;
                for (int i2 = l; i2 >= m; --i2) {
                    final double d5 = d3;
                    final double d6 = d4;
                    if (j == 0) {
                        d3 += random2.nextInt(11) - 5;
                        d4 += random2.nextInt(11) - 5;
                    }
                    else {
                        d3 += random2.nextInt(31) - 15;
                        d4 += random2.nextInt(31) - 15;
                    }
                    worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    final float f = 0.5f;
                    final float f2 = 0.45f;
                    final float f3 = 0.45f;
                    final float f4 = 0.5f;
                    double d7 = 0.1 + k1 * 0.2;
                    if (j == 0) {
                        d7 *= i2 * 0.1 + 1.0;
                    }
                    double d8 = 0.1 + k1 * 0.2;
                    if (j == 0) {
                        d8 *= (i2 - 1) * 0.1 + 1.0;
                    }
                    for (int j2 = 0; j2 < 5; ++j2) {
                        double d9 = x + 0.5 - d7;
                        double d10 = z + 0.5 - d7;
                        if (j2 == 1 || j2 == 2) {
                            d9 += d7 * 2.0;
                        }
                        if (j2 == 2 || j2 == 3) {
                            d10 += d7 * 2.0;
                        }
                        double d11 = x + 0.5 - d8;
                        double d12 = z + 0.5 - d8;
                        if (j2 == 1 || j2 == 2) {
                            d11 += d8 * 2.0;
                        }
                        if (j2 == 2 || j2 == 3) {
                            d12 += d8 * 2.0;
                        }
                        worldrenderer.pos(d11 + d3, y + i2 * 16, d12 + d4).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex();
                        worldrenderer.pos(d9 + d5, y + (i2 + 1) * 16, d10 + d6).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex();
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLightningBolt entity) {
        return null;
    }
}
