package net.minecraft.client.gui;

import cedo.util.font.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    // TODO: fix optionsBackground resolutions
    //public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    //public static final ResourceLocation optionsBackground = new ResourceLocation("Fan/options_background_new.jpg");
    public static final ResourceLocation optionsBackground = new ResourceLocation("Fan/infinite_sky.jpg");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected static float zLevel;

    /**
     * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
     */
    protected void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int i = startX;
            startX = endX;
            endX = i;
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
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0D).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0D).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0D).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0D).func_181675_d();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect2(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldrenderer.func_181662_b(right, top, zLevel).func_181666_a(f1, f2, f3, f).func_181675_d();
        worldrenderer.func_181662_b(left, top, zLevel).func_181666_a(f1, f2, f3, f).func_181675_d();
        worldrenderer.func_181662_b(left, bottom, zLevel).func_181666_a(f5, f6, f7, f4).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, zLevel).func_181666_a(f5, f6, f7, f4).func_181675_d();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public void drawCenteredString(String text, int x, int y, int color)
    {
        FontUtil.clean.drawStringWithShadow(text, (float)(x - FontUtil.clean.getStringWidth(text) / 2), (float)y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(String text, int x, int y, int color)
    {
        FontUtil.clean.drawStringWithShadow(text, (float)x, (float)y, color);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(x, y + height, zLevel).func_181673_a((float)(textureX) * f, (float)(textureY + height) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y + height, zLevel).func_181673_a((float)(textureX + width) * f, (float)(textureY + height) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y, zLevel).func_181673_a((float)(textureX + width) * f, (float)(textureY) * f1).func_181675_d();
        worldrenderer.func_181662_b(x, y, zLevel).func_181673_a((float)(textureX) * f, (float)(textureY) * f1).func_181675_d();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(xCoord + 0.0F, yCoord + (float)maxV, zLevel).func_181673_a((float)(minU) * f, (float)(minV + maxV) * f1).func_181675_d();
        worldrenderer.func_181662_b(xCoord + (float)maxU, yCoord + (float)maxV, zLevel).func_181673_a((float)(minU + maxU) * f, (float)(minV + maxV) * f1).func_181675_d();
        worldrenderer.func_181662_b(xCoord + (float)maxU, yCoord + 0.0F, zLevel).func_181673_a((float)(minU + maxU) * f, (float)(minV) * f1).func_181675_d();
        worldrenderer.func_181662_b(xCoord + 0.0F, yCoord + 0.0F, zLevel).func_181673_a((float)(minU) * f, (float)(minV) * f1).func_181675_d();
        tessellator.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(xCoord, yCoord + heightIn, zLevel).func_181673_a(textureSprite.getMinU(), textureSprite.getMaxV()).func_181675_d();
        worldrenderer.func_181662_b(xCoord + widthIn, yCoord + heightIn, zLevel).func_181673_a(textureSprite.getMaxU(), textureSprite.getMaxV()).func_181675_d();
        worldrenderer.func_181662_b(xCoord + widthIn, yCoord, zLevel).func_181673_a(textureSprite.getMaxU(), textureSprite.getMinV()).func_181675_d();
        worldrenderer.func_181662_b(xCoord, yCoord, zLevel).func_181673_a(textureSprite.getMinU(), textureSprite.getMinV()).func_181675_d();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(x, y + height, 0.0D).func_181673_a(u * f, (v + height) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y + height, 0.0D).func_181673_a((u + width) * f, (v + height) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y, 0.0D).func_181673_a((u + width) * f, v * f1).func_181675_d();
        worldrenderer.func_181662_b(x, y, 0.0D).func_181673_a(u * f, v * f1).func_181675_d();
        tessellator.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(x, y + height, 0.0D).func_181673_a(u * f, (v + (float)vHeight) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y + height, 0.0D).func_181673_a((u + (float)uWidth) * f, (v + (float)vHeight) * f1).func_181675_d();
        worldrenderer.func_181662_b(x + width, y, 0.0D).func_181673_a((u + (float)uWidth) * f, v * f1).func_181675_d();
        worldrenderer.func_181662_b(x, y, 0.0D).func_181673_a(u * f, v * f1).func_181675_d();
        tessellator.draw();
    }
}
