// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEndPortal;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
{
    private static final ResourceLocation END_SKY_TEXTURE;
    private static final ResourceLocation END_PORTAL_TEXTURE;
    private static final Random field_147527_e;
    FloatBuffer field_147528_b;
    
    static {
        END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
        END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
        field_147527_e = new Random(31100L);
    }
    
    public TileEntityEndPortalRenderer() {
        this.field_147528_b = GLAllocation.createDirectFloatBuffer(16);
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityEndPortal te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final float f = (float)this.rendererDispatcher.entityX;
        final float f2 = (float)this.rendererDispatcher.entityY;
        final float f3 = (float)this.rendererDispatcher.entityZ;
        GlStateManager.disableLighting();
        TileEntityEndPortalRenderer.field_147527_e.setSeed(31100L);
        final float f4 = 0.75f;
        for (int i = 0; i < 16; ++i) {
            GlStateManager.pushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625f;
            float f7 = 1.0f / (f5 + 1.0f);
            if (i == 0) {
                this.bindTexture(TileEntityEndPortalRenderer.END_SKY_TEXTURE);
                f7 = 0.1f;
                f5 = 65.0f;
                f6 = 0.125f;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
            }
            if (i >= 1) {
                this.bindTexture(TileEntityEndPortalRenderer.END_PORTAL_TEXTURE);
            }
            if (i == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                f6 = 0.5f;
            }
            final float f8 = (float)(-(y + f4));
            float f9 = f8 + (float)ActiveRenderInfo.getPosition().yCoord;
            final float f10 = f8 + f5 + (float)ActiveRenderInfo.getPosition().yCoord;
            float f11 = f9 / f10;
            f11 += (float)(y + f4);
            GlStateManager.translate(f, f11, f3);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0f, 0.0f, 0.0f, 0.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0f, 0.0f, 1.0f, 0.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0f, 0.0f, 0.0f, 1.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0f, 1.0f, 0.0f, 0.0f));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, Minecraft.getSystemTime() % 700000L / 700000.0f, 0.0f);
            GlStateManager.scale(f6, f6, f6);
            GlStateManager.translate(0.5f, 0.5f, 0.0f);
            GlStateManager.rotate((i * i * 4321 + i * 9) * 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.0f);
            GlStateManager.translate(-f, -f3, -f2);
            f9 = f8 + (float)ActiveRenderInfo.getPosition().yCoord;
            GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f5 / f9, (float)ActiveRenderInfo.getPosition().zCoord * f5 / f9, -f2);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f12 = (TileEntityEndPortalRenderer.field_147527_e.nextFloat() * 0.5f + 0.1f) * f7;
            float f13 = (TileEntityEndPortalRenderer.field_147527_e.nextFloat() * 0.5f + 0.4f) * f7;
            float f14 = (TileEntityEndPortalRenderer.field_147527_e.nextFloat() * 0.5f + 0.5f) * f7;
            if (i == 0) {
                f13 = (f12 = (f14 = 1.0f * f7));
            }
            worldrenderer.pos(x, y + f4, z).color(f12, f13, f14, 1.0f).endVertex();
            worldrenderer.pos(x, y + f4, z + 1.0).color(f12, f13, f14, 1.0f).endVertex();
            worldrenderer.pos(x + 1.0, y + f4, z + 1.0).color(f12, f13, f14, 1.0f).endVertex();
            worldrenderer.pos(x + 1.0, y + f4, z).color(f12, f13, f14, 1.0f).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(TileEntityEndPortalRenderer.END_SKY_TEXTURE);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }
    
    private FloatBuffer func_147525_a(final float p_147525_1_, final float p_147525_2_, final float p_147525_3_, final float p_147525_4_) {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }
}
