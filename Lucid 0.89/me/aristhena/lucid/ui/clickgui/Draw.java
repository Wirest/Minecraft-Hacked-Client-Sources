/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.newdawn.slick.Color
 *  org.newdawn.slick.TrueTypeFont
 *  org.newdawn.slick.opengl.TextureImpl
 */
package me.aristhena.lucid.ui.clickgui;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class Draw {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static TrueTypeFont font;

    public static void load() {
        Font awtFont = new Font("Calibri", 0, 18);
        font = new TrueTypeFont(awtFont, true);
    }

    public static void startClip(float x1, float y1, float x2, float y2) {
        if (y1 > y2) {
            float temp = y2;
            y2 = y1;
            y1 = temp;
        }
        GL11.glEnable((int)3089);
        GL11.glScissor((int)((int)x1), (int)((int)((float)Display.getHeight() - y2)), (int)((int)(x2 - x1)), (int)((int)(y2 - y1)));
    }

    public static void endClip() {
        GL11.glDisable((int)3089);
    }

    public static void rect(double x1, double y1, double x2, double y2, int color) {
        float r = (float)(color >> 16 & 255) / 255.0f;
        float g = (float)(color >> 8 & 255) / 255.0f;
        float b = (float)(color & 255) / 255.0f;
        float a = (float)(color >> 24 & 255) / 255.0f;
        Tessellator tes = Tessellator.getInstance();
        WorldRenderer t = tes.getWorldRenderer();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
        t.startDrawingQuads();
        t.addVertex(x1, y2, 0.0);
        t.addVertex(x2, y2, 0.0);
        t.addVertex(x2, y1, 0.0);
        t.addVertex(x1, y1, 0.0);
        tes.draw();
        GL11.glEnable((int)3553);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
    }

    public static void rectOutline(float x1, float y1, float x2, float y2, int color, float width) {
        Draw.rect(x1, y1, x2, y1 + width, color);
        Draw.rect(x1, y2 - width, x2, y2, color);
        Draw.rect(x1, y1 + width, x1 + width, y2 - width, color);
        Draw.rect(x2 - width, y1 + width, x2, y2 - width, color);
    }

    public static void rectBordered(float x1, float y1, float x2, float y2, int fill, int outline, float width) {
        Draw.rectOutline(x1, y1, x2, y2, outline, width);
        Draw.rect(x1 + width, y1 + width, x2 - width, y2 - width, fill);
    }

    public static void rectGradient(float x1, float y1, float x2, float y2, int[] color) {
        float[] r = new float[color.length];
        float[] g = new float[color.length];
        float[] b = new float[color.length];
        float[] a = new float[color.length];
        int i = 0;
        while (i < color.length) {
            r[i] = (float)(color[i] >> 16 & 255) / 255.0f;
            g[i] = (float)(color[i] >> 8 & 255) / 255.0f;
            b[i] = (float)(color[i] & 255) / 255.0f;
            a[i] = (float)(color[i] >> 24 & 255) / 255.0f;
            ++i;
        }
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        Tessellator tes = Tessellator.getInstance();
        WorldRenderer t = tes.getWorldRenderer();
        t.startDrawingQuads();
        t.setColorRGBA_F(r[0], g[0], b[0], a[0]);
        t.addVertex((double)x2, (double)y1, 0.0);
        t.setColorRGBA_F(r[1], g[1], b[1], a[1]);
        t.addVertex((double)x1, (double)y1, 0.0);
        t.setColorRGBA_F(r[2], g[2], b[2], a[2]);
        t.addVertex((double)x1, (double)y2, 0.0);
        t.setColorRGBA_F(r[3], g[3], b[3], a[3]);
        t.addVertex((double)x2, (double)y2, 0.0);
        tes.draw();
        GL11.glShadeModel((int)7424);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
    }

    public static void rectGradientOutline(float x1, float y1, float x2, float y2, int[] color, float width) {
        Draw.rectGradient(x1, y1, x2, y1 + width, new int[]{color[0], color[1], color[0], color[1]});
        Draw.rectGradient(x1, y2 - width, x2, y2, new int[]{color[2], color[3], color[2], color[3]});
        Draw.rectGradient(x1, y1 + width, x1 + width, y2 - width, color);
        Draw.rectGradient(x2 - width, y1 + width, x2, y2 - width, color);
    }

    public static void rectGradientBordered(float x1, float y1, float x2, float y2, int[] fill, int[] outline, float width) {
        Draw.rectGradientOutline(x1, y1, x2, y2, outline, width);
        Draw.rectGradient(x1 + width, y1 + width, x2 - width, y2 - width, fill);
    }

    public static void string(String text, float x, float y, int color) {
        TextureImpl.bindNone();
        GlStateManager.enableBlend();
        if (text != null) {
            font.drawString(x, y, text, new Color(color));
        }
        GlStateManager.disableBlend();
    }

    public static void string(String text, float x, float y, int color, int xAlign, int yAlign) {
        Draw.string(text, x - (float)(font.getWidth(text) / 2) + (float)(font.getWidth(text) * xAlign / 2), y - (float)(font.getHeight(text) / 2) + (float)(font.getHeight(text) * yAlign / 2), color);
    }
}

