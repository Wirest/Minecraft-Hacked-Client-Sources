package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static Gui INSTANCE;
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected static float zLevel;
    

    public Gui(){
    	INSTANCE = this;
    }
    
    /**
     * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
     */
    public static void drawHLine(float startX, float endX, float y, int color){
    	INSTANCE.drawHorizontalLine(startX, endX, y, color);
    }
    public static void drawVLine(float startX, float endX, float y, int color){
    	INSTANCE.drawVerticalLine(startX, endX, y, color);
    }
    protected void drawHorizontalLine(float startX, float endX, float y, int color)
    {
        if (endX < startX)
        {
        	float var5 = startX;
            startX = endX;
            endX = var5;
        }

        drawFloatRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    protected void drawVerticalLine(float x, float startY, float endY, int color)
    {
        if (endY < startY)
        {
        	float var5 = startY;
            startY = endY;
            endY = var5;
        }

        drawFloatRect(x, startY + 1, x + 1, endY, color);
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

        float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        var10.startDrawingQuads();
        var10.addVertex((double)left, (double)bottom, 0.0D);
        var10.addVertex((double)right, (double)bottom, 0.0D);
        var10.addVertex((double)right, (double)top, 0.0D);
        var10.addVertex((double)left, (double)top, 0.0D);
        var9.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    public void drawGradientRect(float g, float h, float i, float f, int startColor, int endColor)
    {
        float var7 = (float)(startColor >> 24 & 255) / 255.0F;
        float var8 = (float)(startColor >> 16 & 255) / 255.0F;
        float var9 = (float)(startColor >> 8 & 255) / 255.0F;
        float var10 = (float)(startColor & 255) / 255.0F;
        float var11 = (float)(endColor >> 24 & 255) / 255.0F;
        float var12 = (float)(endColor >> 16 & 255) / 255.0F;
        float var13 = (float)(endColor >> 8 & 255) / 255.0F;
        float var14 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex((double)i, (double)h, (double)this.zLevel);
        var16.addVertex((double)g, (double)h, (double)this.zLevel);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex((double)g, (double)f, (double)this.zLevel);
        var16.addVertex((double)i, (double)f, (double)this.zLevel);
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
        fontRendererIn.func_175063_a(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.func_175063_a(text, (float)x, (float)y, color);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(float f, float g, float h, float i, float j, float k)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV((double)(f + 0), (double)(g + k), (double)this.zLevel, (double)((float)(h + 0) * var7), (double)((float)(i + k) * var8));
        var10.addVertexWithUV((double)(f + j), (double)(g + k), (double)this.zLevel, (double)((float)(h + j) * var7), (double)((float)(i + k) * var8));
        var10.addVertexWithUV((double)(f + j), (double)(g + 0), (double)this.zLevel, (double)((float)(h + j) * var7), (double)((float)(i + 0) * var8));
        var10.addVertexWithUV((double)(f + 0), (double)(g + 0), (double)this.zLevel, (double)((float)(h + 0) * var7), (double)((float)(i + 0) * var8));
        var9.draw();
    }

    public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_, int p_175174_6_)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV((double)(p_175174_1_ + 0.0F), (double)(p_175174_2_ + (float)p_175174_6_), (double)this.zLevel, (double)((float)(p_175174_3_ + 0) * var7), (double)((float)(p_175174_4_ + p_175174_6_) * var8));
        var10.addVertexWithUV((double)(p_175174_1_ + (float)p_175174_5_), (double)(p_175174_2_ + (float)p_175174_6_), (double)this.zLevel, (double)((float)(p_175174_3_ + p_175174_5_) * var7), (double)((float)(p_175174_4_ + p_175174_6_) * var8));
        var10.addVertexWithUV((double)(p_175174_1_ + (float)p_175174_5_), (double)(p_175174_2_ + 0.0F), (double)this.zLevel, (double)((float)(p_175174_3_ + p_175174_5_) * var7), (double)((float)(p_175174_4_ + 0) * var8));
        var10.addVertexWithUV((double)(p_175174_1_ + 0.0F), (double)(p_175174_2_ + 0.0F), (double)this.zLevel, (double)((float)(p_175174_3_ + 0) * var7), (double)((float)(p_175174_4_ + 0) * var8));
        var9.draw();
    }

    public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_, int p_175175_5_)
    {
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV((double)(p_175175_1_ + 0), (double)(p_175175_2_ + p_175175_5_), (double)this.zLevel, (double)p_175175_3_.getMinU(), (double)p_175175_3_.getMaxV());
        var7.addVertexWithUV((double)(p_175175_1_ + p_175175_4_), (double)(p_175175_2_ + p_175175_5_), (double)this.zLevel, (double)p_175175_3_.getMaxU(), (double)p_175175_3_.getMaxV());
        var7.addVertexWithUV((double)(p_175175_1_ + p_175175_4_), (double)(p_175175_2_ + 0), (double)this.zLevel, (double)p_175175_3_.getMaxU(), (double)p_175175_3_.getMinV());
        var7.addVertexWithUV((double)(p_175175_1_ + 0), (double)(p_175175_2_ + 0), (double)this.zLevel, (double)p_175175_3_.getMinU(), (double)p_175175_3_.getMinV());
        var6.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(float f, double d, float u, float v, double h, double e, double i, double g)
    {
    	double var8 = 1.0F / i;
        double var9 = 1.0F / g;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV((double)f, (double)(d + e), 0.0D, (double)(u * var8), (double)((v + (float)e) * var9));
        var11.addVertexWithUV((double)(f + h), (double)(d + e), 0.0D, (double)((u + (float)h) * var8), (double)((v + (float)e) * var9));
        var11.addVertexWithUV((double)(f + h), (double)d, 0.0D, (double)((u + (float)h) * var8), (double)(v * var9));
        var11.addVertexWithUV((double)f, (double)d, 0.0D, (double)(u * var8), (double)(v * var9));
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
        var13.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)(u * var10), (double)((v + (float)vHeight) * var11));
        var13.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((u + (float)uWidth) * var10), (double)((v + (float)vHeight) * var11));
        var13.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((u + (float)uWidth) * var10), (double)(v * var11));
        var13.addVertexWithUV((double)x, (double)y, 0.0D, (double)(u * var10), (double)(v * var11));
        var12.draw();
    }

    public static void drawFloatRect(double left, double top, double d, double bottom, int color)
    {
    	double var5;

        if (left < d)
        {
            var5 = left;
            left = d;
            d = var5;
        }

        if (top < bottom)
        {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        var10.startDrawingQuads();
        var10.addVertex((double)left, (double)bottom, 0.0D);
        var10.addVertex((double)d, (double)bottom, 0.0D);
        var10.addVertex((double)d, (double)top, 0.0D);
        var10.addVertex((double)left, (double)top, 0.0D);
        var9.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
