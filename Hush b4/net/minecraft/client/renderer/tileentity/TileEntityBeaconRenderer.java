// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityBeacon;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer<TileEntityBeacon>
{
    private static final ResourceLocation beaconBeam;
    
    static {
        beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityBeacon te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final float f = te.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1f);
        if (f > 0.0f) {
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableFog();
            final List<TileEntityBeacon.BeamSegment> list = te.getBeamSegments();
            int i = 0;
            for (int j = 0; j < list.size(); ++j) {
                final TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = list.get(j);
                final int k = i + tileentitybeacon$beamsegment.getHeight();
                this.bindTexture(TileEntityBeaconRenderer.beaconBeam);
                GL11.glTexParameterf(3553, 10242, 10497.0f);
                GL11.glTexParameterf(3553, 10243, 10497.0f);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                final double d0 = te.getWorld().getTotalWorldTime() + (double)partialTicks;
                final double d2 = MathHelper.func_181162_h(-d0 * 0.2 - MathHelper.floor_double(-d0 * 0.1));
                final float f2 = tileentitybeacon$beamsegment.getColors()[0];
                final float f3 = tileentitybeacon$beamsegment.getColors()[1];
                final float f4 = tileentitybeacon$beamsegment.getColors()[2];
                double d3 = d0 * 0.025 * -1.5;
                double d4 = 0.2;
                double d5 = 0.5 + Math.cos(d3 + 2.356194490192345) * 0.2;
                double d6 = 0.5 + Math.sin(d3 + 2.356194490192345) * 0.2;
                double d7 = 0.5 + Math.cos(d3 + 0.7853981633974483) * 0.2;
                double d8 = 0.5 + Math.sin(d3 + 0.7853981633974483) * 0.2;
                double d9 = 0.5 + Math.cos(d3 + 3.9269908169872414) * 0.2;
                double d10 = 0.5 + Math.sin(d3 + 3.9269908169872414) * 0.2;
                double d11 = 0.5 + Math.cos(d3 + 5.497787143782138) * 0.2;
                double d12 = 0.5 + Math.sin(d3 + 5.497787143782138) * 0.2;
                double d13 = 0.0;
                double d14 = 1.0;
                final double d15 = -1.0 + d2;
                final double d16 = tileentitybeacon$beamsegment.getHeight() * f * 2.5 + d15;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(x + d5, y + k, z + d6).tex(1.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d5, y + i, z + d6).tex(1.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d7, y + i, z + d8).tex(0.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d7, y + k, z + d8).tex(0.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d11, y + k, z + d12).tex(1.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d11, y + i, z + d12).tex(1.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d9, y + i, z + d10).tex(0.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d9, y + k, z + d10).tex(0.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d7, y + k, z + d8).tex(1.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d7, y + i, z + d8).tex(1.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d11, y + i, z + d12).tex(0.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d11, y + k, z + d12).tex(0.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d9, y + k, z + d10).tex(1.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d9, y + i, z + d10).tex(1.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d5, y + i, z + d6).tex(0.0, d15).color(f2, f3, f4, 1.0f).endVertex();
                worldrenderer.pos(x + d5, y + k, z + d6).tex(0.0, d16).color(f2, f3, f4, 1.0f).endVertex();
                tessellator.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                d3 = 0.2;
                d4 = 0.2;
                d5 = 0.8;
                d6 = 0.2;
                d7 = 0.2;
                d8 = 0.8;
                d9 = 0.8;
                d10 = 0.8;
                d11 = 0.0;
                d12 = 1.0;
                d13 = -1.0 + d2;
                d14 = tileentitybeacon$beamsegment.getHeight() * f + d13;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(x + 0.2, y + k, z + 0.2).tex(1.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + i, z + 0.2).tex(1.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + i, z + 0.2).tex(0.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + k, z + 0.2).tex(0.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + k, z + 0.8).tex(1.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + i, z + 0.8).tex(1.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + i, z + 0.8).tex(0.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + k, z + 0.8).tex(0.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + k, z + 0.2).tex(1.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + i, z + 0.2).tex(1.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + i, z + 0.8).tex(0.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.8, y + k, z + 0.8).tex(0.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + k, z + 0.8).tex(1.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + i, z + 0.8).tex(1.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + i, z + 0.2).tex(0.0, d13).color(f2, f3, f4, 0.125f).endVertex();
                worldrenderer.pos(x + 0.2, y + k, z + 0.2).tex(0.0, d14).color(f2, f3, f4, 0.125f).endVertex();
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                i = k;
            }
            GlStateManager.enableFog();
        }
    }
    
    @Override
    public boolean func_181055_a() {
        return true;
    }
}
