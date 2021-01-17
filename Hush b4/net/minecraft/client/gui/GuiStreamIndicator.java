// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator
{
    private static final ResourceLocation locationStreamIndicator;
    private final Minecraft mc;
    private float field_152443_c;
    private int field_152444_d;
    
    static {
        locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    }
    
    public GuiStreamIndicator(final Minecraft mcIn) {
        this.field_152443_c = 1.0f;
        this.field_152444_d = 1;
        this.mc = mcIn;
    }
    
    public void render(final int p_152437_1_, final int p_152437_2_) {
        if (this.mc.getTwitchStream().isBroadcasting()) {
            GlStateManager.enableBlend();
            final int i = this.mc.getTwitchStream().func_152920_A();
            if (i > 0) {
                final String s = new StringBuilder().append(i).toString();
                final int j = this.mc.fontRendererObj.getStringWidth(s);
                final int k = 20;
                final int l = p_152437_1_ - j - 1;
                final int i2 = p_152437_2_ + 20 - 1;
                final int j2 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.disableTexture2D();
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.color(0.0f, 0.0f, 0.0f, (0.65f + 0.35000002f * this.field_152443_c) / 2.0f);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                worldrenderer.pos(l, j2, 0.0).endVertex();
                worldrenderer.pos(p_152437_1_, j2, 0.0).endVertex();
                worldrenderer.pos(p_152437_1_, i2, 0.0).endVertex();
                worldrenderer.pos(l, i2, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
                this.mc.fontRendererObj.drawString(s, p_152437_1_ - j, p_152437_2_ + 20, 16777215);
            }
            this.render(p_152437_1_, p_152437_2_, this.func_152440_b(), 0);
            this.render(p_152437_1_, p_152437_2_, this.func_152438_c(), 17);
        }
    }
    
    private void render(final int p_152436_1_, final int p_152436_2_, final int p_152436_3_, final int p_152436_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.65f + 0.35000002f * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(GuiStreamIndicator.locationStreamIndicator);
        final float f = 150.0f;
        final float f2 = 0.0f;
        final float f3 = p_152436_3_ * 0.015625f;
        final float f4 = 1.0f;
        final float f5 = (p_152436_3_ + 16) * 0.015625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, f).tex(f2, f5).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, f).tex(f4, f5).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, f).tex(f4, f3).endVertex();
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0, f).tex(f2, f3).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private int func_152440_b() {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }
    
    private int func_152438_c() {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }
    
    public void func_152439_a() {
        if (this.mc.getTwitchStream().isBroadcasting()) {
            this.field_152443_c += 0.025f * this.field_152444_d;
            if (this.field_152443_c < 0.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 0.0f;
            }
            else if (this.field_152443_c > 1.0f) {
                this.field_152444_d *= -1;
                this.field_152443_c = 1.0f;
            }
        }
        else {
            this.field_152443_c = 1.0f;
            this.field_152444_d = 1;
        }
    }
}
