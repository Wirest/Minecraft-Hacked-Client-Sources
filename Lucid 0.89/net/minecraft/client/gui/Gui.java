package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;

    /**
     * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
     */
    protected void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int var5 = startX;
            startX = endX;
            endX = var5;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    protected void drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int var5 = startY;
            startY = endY;
            endY = var5;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        int var5;

        if (left < right)
        {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom)
        {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        float var11 = (color >> 24 & 255) / 255.0F;
        float var6 = (color >> 16 & 255) / 255.0F;
        float var7 = (color >> 8 & 255) / 255.0F;
        float var8 = (color & 255) / 255.0F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        var10.startDrawingQuads();
        var10.addVertex(left, bottom, 0.0D);
        var10.addVertex(right, bottom, 0.0D);
        var10.addVertex(right, top, 0.0D);
        var10.addVertex(left, top, 0.0D);
        var9.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float var7 = (startColor >> 24 & 255) / 255.0F;
        float var8 = (startColor >> 16 & 255) / 255.0F;
        float var9 = (startColor >> 8 & 255) / 255.0F;
        float var10 = (startColor & 255) / 255.0F;
        float var11 = (endColor >> 24 & 255) / 255.0F;
        float var12 = (endColor >> 16 & 255) / 255.0F;
        float var13 = (endColor >> 8 & 255) / 255.0F;
        float var14 = (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.setColorRGBA_F(var8, var9, var10, var7);
        var16.addVertex(right, top, this.zLevel);
        var16.addVertex(left, top, this.zLevel);
        var16.setColorRGBA_F(var12, var13, var14, var11);
        var16.addVertex(left, bottom, this.zLevel);
        var16.addVertex(right, bottom, this.zLevel);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0, y + height, this.zLevel, (textureX + 0) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, this.zLevel, (textureX + width) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0, this.zLevel, (textureX + width) * var7, (textureY + 0) * var8);
        var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (textureX + 0) * var7, (textureY + 0) * var8);
        var9.draw();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(xCoord + 0.0F, yCoord + maxV, this.zLevel, (minU + 0) * var7, (minV + maxV) * var8);
        var10.addVertexWithUV(xCoord + maxU, yCoord + maxV, this.zLevel, (minU + maxU) * var7, (minV + maxV) * var8);
        var10.addVertexWithUV(xCoord + maxU, yCoord + 0.0F, this.zLevel, (minU + maxU) * var7, (minV + 0) * var8);
        var10.addVertexWithUV(xCoord + 0.0F, yCoord + 0.0F, this.zLevel, (minU + 0) * var7, (minV + 0) * var8);
        var9.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn)
    {
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV(xCoord + 0, yCoord + heightIn, this.zLevel, textureSprite.getMinU(), textureSprite.getMaxV());
        var7.addVertexWithUV(xCoord + widthIn, yCoord + heightIn, this.zLevel, textureSprite.getMaxU(), textureSprite.getMaxV());
        var7.addVertexWithUV(xCoord + widthIn, yCoord + 0, this.zLevel, textureSprite.getMaxU(), textureSprite.getMinV());
        var7.addVertexWithUV(xCoord + 0, yCoord + 0, this.zLevel, textureSprite.getMinU(), textureSprite.getMinV());
        var6.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float var8 = 1.0F / textureWidth;
        float var9 = 1.0F / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0D, u * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0D, (u + width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0D, u * var8, v * var9);
        var10.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float var10 = 1.0F / tileWidth;
        float var11 = 1.0F / tileHeight;
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        var13.startDrawingQuads();
        var13.addVertexWithUV(x, y + height, 0.0D, u * var10, (v + vHeight) * var11);
        var13.addVertexWithUV(x + width, y + height, 0.0D, (u + uWidth) * var10, (v + vHeight) * var11);
        var13.addVertexWithUV(x + width, y, 0.0D, (u + uWidth) * var10, v * var11);
        var13.addVertexWithUV(x, y, 0.0D, u * var10, v * var11);
        var12.draw();
    }
}
