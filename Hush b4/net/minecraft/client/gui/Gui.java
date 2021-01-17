// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation optionsBackground;
    public static final ResourceLocation statIcons;
    public static final ResourceLocation icons;
    protected float zLevel;
    
    static {
        optionsBackground = new ResourceLocation("textures/gui/options_background.png");
        statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
        icons = new ResourceLocation("textures/gui/icons.png");
    }
    
    protected void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    protected void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, this.zLevel).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, top, this.zLevel).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, bottom, this.zLevel).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos(right, bottom, this.zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public void drawCenteredString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
    
    public void drawString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0, y + height, this.zLevel).tex((textureX + 0) * f, (textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevel).tex((textureX + width) * f, (textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + 0, this.zLevel).tex((textureX + width) * f, (textureY + 0) * f2).endVertex();
        worldrenderer.pos(x + 0, y + 0, this.zLevel).tex((textureX + 0) * f, (textureY + 0) * f2).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0.0f, yCoord + maxV, this.zLevel).tex((minU + 0) * f, (minV + maxV) * f2).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + maxV, this.zLevel).tex((minU + maxU) * f, (minV + maxV) * f2).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + 0.0f, this.zLevel).tex((minU + maxU) * f, (minV + 0) * f2).endVertex();
        worldrenderer.pos(xCoord + 0.0f, yCoord + 0.0f, this.zLevel).tex((minU + 0) * f, (minV + 0) * f2).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0, yCoord + heightIn, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + heightIn, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + 0, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        worldrenderer.pos(xCoord + 0, yCoord + 0, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final int x, final int y, final float u, final float v, final int width, final int height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
}
