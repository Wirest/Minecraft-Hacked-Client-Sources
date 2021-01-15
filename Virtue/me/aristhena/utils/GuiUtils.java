package me.aristhena.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GuiUtils {
    public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
      private static ScaledResolution scaledResolution;

    /**
     * Enables 2D GL constants for 2D rendering.
     */
    public static void enableGL2D() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    /**
     * Disables 2D GL constants for 2D rendering.
     */
    public static void disableGL2D() {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }
    public static void drawRect(Rectangle rectangle, int color) {
        drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    /**
     * Draws a rectangle at the coordinates specified with the hexadecimal color.
     */
    public static void drawRect(float x, float y, float x1, float y1, int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }



    /**
     * Draws a bordered rectangle at the coordinates specified with the hexadecimal color.
     */
    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
        enableGL2D();
        glColor(internalColor);
        drawRect(x + width, y + width, x1 - width, y1 - width);
        glColor(borderColor);
        drawRect(x + width, y, x1 - width, y + width);
        drawRect(x, y, x + width, y1);
        drawRect(x1 - width, y, x1, y1);
        drawRect(x + width, y1 - width, x1 - width, y1);
        disableGL2D();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        enableGL2D();
        x *= 2; x1 *= 2; y *= 2; y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y, y1 , borderC);
        drawVLine(x1 - 1, y , y1, borderC);
        drawHLine(x, x1 - 1, y, borderC);
        drawHLine(x, x1 - 2, y1 -1, borderC);
        drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_STRIP);
        glVertex2f(x, y);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        disableGL2D();
    }

    public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        enableGL2D();
        drawGradientRect(x, y, x1, y1, top, bottom);
        glColor(border);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_STRIP);
        glVertex2f(x, y);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        disableGL2D();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        enableGL2D();
        x *= 2; y *= 2; x1 *= 2; y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y + 1, y1 -2, borderC);
        drawVLine(x1 - 1, y + 1, y1 - 2, borderC);
        drawHLine(x + 2, x1 - 3, y, borderC);
        drawHLine(x + 2, x1 - 3, y1 -1, borderC);
        drawHLine(x + 1, x + 1, y + 1, borderC);
        drawHLine(x1 - 2, x1 - 2, y + 1, borderC);
        drawHLine(x1 - 2, x1 - 2, y1 - 2, borderC);
        drawHLine(x + 1, x + 1, y1 - 2, borderC);
        drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    /**
     * Draws a bordered rectangle at the coordinates specified with the hexadecimal color.
     */
    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x = rectangle.x;
        float y = rectangle.y;
        float x1 = rectangle.x + rectangle.width;
        float y1 = rectangle.y + rectangle.height;
        enableGL2D();
        glColor(internalColor);
        drawRect(x + width, y + width, x1 - width, y1 - width);
        glColor(borderColor);
        drawRect(x + 1, y, x1 - 1, y + width);
        drawRect(x, y, x + width, y1);
        drawRect(x1 - width, y, x1, y1);
        drawRect(x + 1, y1 - width, x1 - 1, y1);
        disableGL2D();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        enableGL2D();
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        glColor(topColor);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glColor(bottomColor);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
        glShadeModel(GL_FLAT);
        disableGL2D();
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        enableGL2D();
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        glColor(topColor);
        glVertex2f(x, y);
        glVertex2f(x, y1);
        glColor(bottomColor);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glEnd();
        glShadeModel(GL_FLAT);
        disableGL2D();
    }

    public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glPushMatrix();
        glBegin(GL_QUADS);
        glColor(col1);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glColor(col2);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glShadeModel(GL_FLAT);
    }

    public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
        enableGL2D();
        glPushMatrix();
        glColor(col1);
        glLineWidth(1F);
        glBegin(GL_LINES);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x2, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glPopMatrix();
        drawGradientRect(x, y, x2, y2, col2, col3);
        disableGL2D();
    }
    public static void glColor(Color color) {
        glColor4f((color.getRed() / 255F), (color.getGreen() / 255F), (color.getBlue() / 255F), (color.getAlpha() / 255F));
    }

    /**
     *
     * */
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 255) / 255.0F;
        float red = (hex >> 16 & 255) / 255.0F;
        float green = (hex >> 8 & 255) / 255.0F;
        float blue = (hex & 255) / 255.0F;
        glColor4f(red, green, blue, alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = (1 / 255.0F) * redRGB;
        float green = (1 / 255.0F) * greenRGB;
        float blue = (1 / 255.0F) * blueRGB;
        glColor4f(red, green, blue, alpha);
    }

    public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
        float f1 = (color >> 24 & 255) / 255.0F;
        float f2 = (color >> 16 & 255) / 255.0F;
        float f3 = (color >> 8 & 255) / 255.0F;
        float f4 = (color & 255) / 255.0F;
        glPushMatrix();
        glTranslated(x, y, 0);
        glColor4f(f2, f3, f4, f1);
        glLineWidth(width);

        if (angle > 0) {
            glBegin(GL_LINE_STRIP);

            for (int i = 0; i < angle; i++)
            {
                float a = (float)(i * (angle * Math.PI / points));
                float xc = (float)(Math.cos(a) * radius);
                float yc = (float)(Math.sin(a) * radius);
                glVertex2f(xc, yc);
            }

            glEnd();
        }

        if (angle < 0) {
            glBegin(GL_LINE_STRIP);

            for (int i = 0; i > angle; i--) {
                float a = (float)(i * (angle * Math.PI / points));
                float xc = (float)(Math.cos(a) * -radius);
                float yc = (float)(Math.sin(a) * -radius);
                glVertex2f(xc, yc);
            }

            glEnd();
        }

        disableGL2D();
        glDisable(GL_MAP1_VERTEX_3);
        glPopMatrix();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }

        drawRect(x, x1, y + 1, x1 + 1, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }

        drawRect(x, y + 1, x + 1, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }

        drawGradientRect(x, x1, y + 1, x1 + 1, y1, y2);
    }

    /**
     * Draws a rectangle at the coordinates specified.
     */
    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
        enableGL2D();
        glColor4f(r, g, b, a);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        // glRectd(x, y, x1, y1);
        glBegin(GL_QUADS);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        r *= 2;
        cx *= 2;
        cy *= 2;
        float f = (c >> 24 & 0xff) / 255F;
        float f1 = (c >> 16 & 0xff) / 255F;
        float f2 = (c >> 8 & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float p = (float) Math.cos(theta);//calculate the sine and cosine
        float s = (float) Math.sin(theta);
        float t;
        float x = r;
        float y = 0;//start at angle = 0
        enableGL2D();
        glScalef(0.5F, 0.5F, 0.5F);
        glColor4f(f1, f2, f3, f);
        glBegin(GL_LINE_LOOP);

        for(int ii = 0; ii < num_segments; ii++) {
            glVertex2f(x + cx, y + cy);//final vertex vertex

            //rotate the stuff
            t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }

        glEnd();
        glScalef(2F, 2F, 2F);
        disableGL2D();
    }

    public static void drawFullCircle(int cx, int cy, double r, int c) {
        r *= 2;
        cx *= 2;
        cy *= 2;
        float f = (c >> 24 & 0xff) / 255F;
        float f1 = (c >> 16 & 0xff) / 255F;
        float f2 = (c >> 8 & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        enableGL2D();
        glScalef(0.5F, 0.5F, 0.5F);
        glColor4f(f1, f2, f3, f);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++) {
            double x = Math.sin((i * Math.PI / 180)) * r;
            double y = Math.cos((i * Math.PI / 180)) * r;
            glVertex2d(cx + x, cy + y);
        }

        glEnd();
        glScalef(2F, 2F, 2F);
        disableGL2D();
    }
    public static void drawSmallString(String s, int x, int y, int color)
    {
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().fontRendererObj.drawString(s, x * 2, y * 2, color);
        GL11.glPopMatrix();
    }
    public static void drawLargeString(String text, int x, int y,int color)
    {
    x *= 2;
        GL11.glPushMatrix();
    GL11.glScalef(1.5F, 1.5F, 1.5F);
    Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color);
        GL11.glPopMatrix();
   }
    public static ScaledResolution getScaledResolution()
     {
        return scaledResolution;
       }


}

