/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.razerboy420.weepcraft.util;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderUtils2D {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static ScaledResolution newScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public static void drawTriangle(float x, float y, float width, float height, int color) {
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)x);
        GL11.glVertex2d((double)(x + width), (double)(x + width));
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(int cx, int cy, double r, int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f = (float)(c >> 24 & 255) / 255.0f;
        float f1 = (float)(c >> 16 & 255) / 255.0f;
        float f2 = (float)(c >> 8 & 255) / 255.0f;
        float f3 = (float)(c & 255) / 255.0f;
        RenderUtils2D.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)6);
        int i = 0;
        while (i <= 360) {
            double x = Math.sin((double)i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double)i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils2D.disableGL2D();
    }

    public static void drawFillCircle(int cx, int cy, double r, int c, int startpoint, int arc) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f = (float)(c >> 24 & 255) / 255.0f;
        float f1 = (float)(c >> 16 & 255) / 255.0f;
        float f2 = (float)(c >> 8 & 255) / 255.0f;
        float f3 = (float)(c & 255) / 255.0f;
        RenderUtils2D.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        int i = startpoint;
        while (i <= arc) {
            double x = Math.sin((double)i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double)i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils2D.disableGL2D();
    }

    public static void drawArc(float cx, float cy, double r, int c, int startpoint, double arc, int linewidth) {
        r *= 2.0;
        cx *= 2.0f;
        cy *= 2.0f;
        float f = (float)(c >> 24 & 255) / 255.0f;
        float f1 = (float)(c >> 16 & 255) / 255.0f;
        float f2 = (float)(c >> 8 & 255) / 255.0f;
        float f3 = (float)(c & 255) / 255.0f;
        RenderUtils2D.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)linewidth);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)3);
        int i = startpoint;
        while ((double)i <= arc) {
            double x = Math.sin((double)i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double)i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils2D.disableGL2D();
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtils2D.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderUtils2D.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        RenderUtils2D.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        RenderUtils2D.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderUtils2D.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        RenderUtils2D.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        RenderUtils2D.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderUtils2D.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        Gui.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtils2D.drawVLine(x *= 2.0f, y *= 2.0f, (y1 *= 2.0f) - 1.0f, borderC);
        RenderUtils2D.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
        RenderUtils2D.drawHLine(x, x1 - 1.0f, y, borderC);
        RenderUtils2D.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        RenderUtils2D.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static boolean stringListContains(List<String> list, String needle) {
        for (String s : list) {
            if (!s.trim().equalsIgnoreCase(needle.trim())) continue;
            return true;
        }
        return false;
    }

    public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
        RenderUtils2D.drawRect((float)x, (float)y, (float)x2, (float)y2, col2);
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawHLine(float par1, float par2, float par3, int par4) {
        if (par2 < par1) {
            float var5 = par1;
            par1 = par2;
            par2 = var5;
        }
        Gui.drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
    }

    public static void drawVLine(float par1, float par2, float par3, int par4) {
        if (par3 < par2) {
            float var5 = par2;
            par2 = par3;
            par3 = var5;
        }
        Gui.drawRect(par1, par2 + 1.0f, par1 + 1.0f, par3, par4);
    }

    public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int paramColor) {
        float alpha = (float)(paramColor >> 24 & 255) / 255.0f;
        float red = (float)(paramColor >> 16 & 255) / 255.0f;
        float green = (float)(paramColor >> 8 & 255) / 255.0f;
        float blue = (float)(paramColor & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)paramXEnd, (double)paramYStart);
        GL11.glVertex2d((double)paramXStart, (double)paramYStart);
        GL11.glVertex2d((double)paramXStart, (double)paramYEnd);
        GL11.glVertex2d((double)paramXEnd, (double)paramYEnd);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        float f4 = (float)(col2 >> 24 & 255) / 255.0f;
        float f5 = (float)(col2 >> 16 & 255) / 255.0f;
        float f6 = (float)(col2 >> 8 & 255) / 255.0f;
        float f7 = (float)(col2 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtils2D.drawGradientRect(x, y, x2, y2, col2, col3);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
    }

    public static void drawCheck(int x, int y) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)0.6f, (float)0.6f, (float)0.6f, (float)1.0f);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GlStateManager.scale(2.0f, 2.0f, 1.0f);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)(x /= 2), (float)(y /= 2));
        GL11.glVertex2f((float)(x + 5), (float)y);
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)(y + 1));
        GL11.glVertex2f((float)(x + 5), (float)((float)y + 1.0f));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)x, (float)(y + 2));
        GL11.glVertex2f((float)(x + 5), (float)((float)y + 2.0f));
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
        int i;
        float xc;
        float yc;
        float a;
        GL11.glPushMatrix();
        float f1 = (float)(color >> 24 & 255) / 255.0f;
        float f2 = (float)(color >> 16 & 255) / 255.0f;
        float f3 = (float)(color >> 8 & 255) / 255.0f;
        float f4 = (float)(color & 255) / 255.0f;
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f1);
        GL11.glLineWidth((float)width);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3008);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glEnable((int)32925);
        if (angle > 0.0) {
            GL11.glBegin((int)3);
            i = 0;
            while ((double)i < angle) {
                a = (float)((double)i * (angle * 3.141592653589793 / (double)points));
                xc = (float)(Math.cos(a) * (double)radius);
                yc = (float)(Math.sin(a) * (double)radius);
                GL11.glVertex2f((float)xc, (float)yc);
                ++i;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin((int)3);
            i = 0;
            while ((double)i > angle) {
                a = (float)((double)i * (angle * 3.141592653589793 / (double)points));
                xc = (float)(Math.cos(a) * (double)(- radius));
                yc = (float)(Math.sin(a) * (double)(- radius));
                GL11.glVertex2f((float)xc, (float)yc);
                --i;
            }
            GL11.glEnd();
        }
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)32925);
        GL11.glDisable((int)3479);
        GL11.glPopMatrix();
    }
}

