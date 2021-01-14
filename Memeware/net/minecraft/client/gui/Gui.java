package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Gui {
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;
    private static final String __OBFID = "CL_00000662";

    /**
     * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
     */
  protected static void drawHorizontalLine(int startX, int endX, int y, int color) {
		if (endX < startX) {
			int var5 = startX;
			startX = endX;
			endX = var5;
		}

		drawRect(startX, y, endX + 1, y + 1, color);
	}

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
   	protected static void drawVerticalLine(int x, int startY, int endY, int color) {
		if (endY < startY) {
			int var5 = startY;
			startY = endY;
			endY = var5;
		}

		drawRect(x, startY + 1, x + 1, endY, color);
	}

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(double x, double y, double d, double e, int color) {
        double var5;

        if (x < d) {
            var5 = x;
            x = d;
            d = var5;
        }

        if (y < e) {
            var5 = y;
            y = e;
            e = var5;
        }

        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        var10.startDrawingQuads();
        var10.addVertex((double) x, (double) e, 0.0D);
        var10.addVertex((double) d, (double) e, 0.0D);
        var10.addVertex((double) d, (double) y, 0.0D);
        var10.addVertex((double) x, (double) y, 0.0D);
        var9.draw();
        GlStateManager.func_179098_w();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float var7 = (float) (startColor >> 24 & 255) / 255.0F;
        float var8 = (float) (startColor >> 16 & 255) / 255.0F;
        float var9 = (float) (startColor >> 8 & 255) / 255.0F;
        float var10 = (float) (startColor & 255) / 255.0F;
        float var11 = (float) (endColor >> 24 & 255) / 255.0F;
        float var12 = (float) (endColor >> 16 & 255) / 255.0F;
        float var13 = (float) (endColor >> 8 & 255) / 255.0F;
        float var14 = (float) (endColor & 255) / 255.0F;
        GlStateManager.func_179090_x();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex((double) right, (double) top, (double) this.zLevel);
        var16.addVertex((double) left, (double) top, (double) this.zLevel);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex((double) left, (double) bottom, (double) this.zLevel);
        var16.addVertex((double) right, (double) bottom, (double) this.zLevel);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.func_179098_w();
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.func_175063_a(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.func_175063_a(text, (float) x, (float) y, color);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     * @param x
     * @param y
     * @param textureX
     * @param textureY
     * @param width
     * @param height
     */
    public void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV((double) (x + 0), (double) (y + height), (double) this.zLevel, (double) ((float) (textureX + 0) * var7), (double) ((float) (textureY + height) * var8));
        var10.addVertexWithUV((double) (x + width), (double) (y + height), (double) this.zLevel, (double) ((float) (textureX + width) * var7), (double) ((float) (textureY + height) * var8));
        var10.addVertexWithUV((double) (x + width), (double) (y + 0), (double) this.zLevel, (double) ((float) (textureX + width) * var7), (double) ((float) (textureY + 0) * var8));
        var10.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel, (double) ((float) (textureX + 0) * var7), (double) ((float) (textureY + 0) * var8));
        var9.draw();
    }

    public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_, int p_175174_6_) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV((double) (p_175174_1_ + 0.0F), (double) (p_175174_2_ + (float) p_175174_6_), (double) this.zLevel, (double) ((float) (p_175174_3_ + 0) * var7), (double) ((float) (p_175174_4_ + p_175174_6_) * var8));
        var10.addVertexWithUV((double) (p_175174_1_ + (float) p_175174_5_), (double) (p_175174_2_ + (float) p_175174_6_), (double) this.zLevel, (double) ((float) (p_175174_3_ + p_175174_5_) * var7), (double) ((float) (p_175174_4_ + p_175174_6_) * var8));
        var10.addVertexWithUV((double) (p_175174_1_ + (float) p_175174_5_), (double) (p_175174_2_ + 0.0F), (double) this.zLevel, (double) ((float) (p_175174_3_ + p_175174_5_) * var7), (double) ((float) (p_175174_4_ + 0) * var8));
        var10.addVertexWithUV((double) (p_175174_1_ + 0.0F), (double) (p_175174_2_ + 0.0F), (double) this.zLevel, (double) ((float) (p_175174_3_ + 0) * var7), (double) ((float) (p_175174_4_ + 0) * var8));
        var9.draw();
    }

    public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_, int p_175175_5_) {
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV((double) (p_175175_1_ + 0), (double) (p_175175_2_ + p_175175_5_), (double) this.zLevel, (double) p_175175_3_.getMinU(), (double) p_175175_3_.getMaxV());
        var7.addVertexWithUV((double) (p_175175_1_ + p_175175_4_), (double) (p_175175_2_ + p_175175_5_), (double) this.zLevel, (double) p_175175_3_.getMaxU(), (double) p_175175_3_.getMaxV());
        var7.addVertexWithUV((double) (p_175175_1_ + p_175175_4_), (double) (p_175175_2_ + 0), (double) this.zLevel, (double) p_175175_3_.getMaxU(), (double) p_175175_3_.getMinV());
        var7.addVertexWithUV((double) (p_175175_1_ + 0), (double) (p_175175_2_ + 0), (double) this.zLevel, (double) p_175175_3_.getMinU(), (double) p_175175_3_.getMinV());
        var6.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float var8 = 1.0F / textureWidth;
        float var9 = 1.0F / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * var8), (double) ((v + (float) height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + (float) width) * var8), (double) ((v + (float) height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + (float) width) * var8), (double) (v * var9));
        var11.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * var8), (double) (v * var9));
        var10.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float var10 = 1.0F / tileWidth;
        float var11 = 1.0F / tileHeight;
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        var13.startDrawingQuads();
        var13.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * var10), (double) ((v + (float) vHeight) * var11));
        var13.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + (float) uWidth) * var10), (double) ((v + (float) vHeight) * var11));
        var13.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + (float) uWidth) * var10), (double) (v * var11));
        var13.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * var10), (double) (v * var11));
        var12.draw();
    }
}
