package store.shadowclient.client.utils.render;

import java.awt.Color;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class R2DUtils {
    public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
    private static ScaledResolution scaledResolution;

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawRect(Rectangle rectangle, int color) {
        R2DUtils.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, int color) {
        R2DUtils.enableGL2D();
        R2DUtils.glColor(color);
        R2DUtils.drawRect(x2, y2, x1, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x1, float y1, float width, int internalColor, int borderColor) {
        R2DUtils.enableGL2D();
        R2DUtils.glColor(internalColor);
        R2DUtils.drawRect(x2 + width, y2 + width, x1 - width, y1 - width);
        R2DUtils.glColor(borderColor);
        R2DUtils.drawRect(x2 + width, y2, x1 - width, y2 + width);
        R2DUtils.drawRect(x2, y2, x2 + width, y1);
        R2DUtils.drawRect(x1 - width, y2, x1, y1);
        R2DUtils.drawRect(x2 + width, y1 - width, x1 - width, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x2, float y2, float x1, float y1, int insideC, int borderC) {
        R2DUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        R2DUtils.drawVLine(x2 *= 2.0f, y2 *= 2.0f, y1 *= 2.0f, borderC);
        R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y2, y1, borderC);
        R2DUtils.drawHLine(x2, x1 - 1.0f, y2, borderC);
        R2DUtils.drawHLine(x2, x1 - 2.0f, y1 - 1.0f, borderC);
        R2DUtils.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int inside, int border) {
        R2DUtils.enableGL2D();
        R2DUtils.drawRect(x2, y2, x1, y1, inside);
        R2DUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        R2DUtils.disableGL2D();
    }

    public static void drawGradientBorderedRectReliant(float x2, float y2, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        R2DUtils.enableGL2D();
        R2DUtils.drawGradientRect(x2, y2, x1, y1, top, bottom);
        R2DUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        R2DUtils.disableGL2D();
    }

    public static void drawRoundedRect(float x2, float y2, float x1, float y1, int borderC, int insideC) {
        R2DUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        R2DUtils.drawVLine(x2 *= 2.0f, (y2 *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y2 + 1.0f, y1 - 2.0f, borderC);
        R2DUtils.drawHLine(x2 + 2.0f, x1 - 3.0f, y2, borderC);
        R2DUtils.drawHLine(x2 + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        R2DUtils.drawHLine(x2 + 1.0f, x2 + 1.0f, y2 + 1.0f, borderC);
        R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y2 + 1.0f, borderC);
        R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        R2DUtils.drawHLine(x2 + 1.0f, x2 + 1.0f, y1 - 2.0f, borderC);
        R2DUtils.drawRect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x2 = rectangle.x;
        float y2 = rectangle.y;
        float x22 = rectangle.x + rectangle.width;
        float y22 = rectangle.y + rectangle.height;
        R2DUtils.enableGL2D();
        R2DUtils.glColor(internalColor);
        R2DUtils.drawRect(x2 + width, y2 + width, x22 - width, y22 - width);
        R2DUtils.glColor(borderColor);
        R2DUtils.drawRect(x2 + 1.0f, y2, x22 - 1.0f, y2 + width);
        R2DUtils.drawRect(x2, y2, x2 + width, y22);
        R2DUtils.drawRect(x22 - width, y2, x22, y22);
        R2DUtils.drawRect(x2 + 1.0f, y22 - width, x22 - 1.0f, y22);
        R2DUtils.disableGL2D();
    }

    public static void drawGradientRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        R2DUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        R2DUtils.glColor(topColor);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        R2DUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        R2DUtils.disableGL2D();
    }

    public static void drawGradientHRect(float x2, float y2, float x1, float y1, int topColor, int bottomColor) {
        R2DUtils.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        R2DUtils.glColor(topColor);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        R2DUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        R2DUtils.disableGL2D();
    }

    public static void drawGradientRect(double x2, double y2, double x22, double y22, int col1, int col2) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        R2DUtils.glColor(col1);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        R2DUtils.glColor(col2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradientBorderedRect(double x2, double y2, double x22, double y22, float l1, int col1, int col2, int col3) {
        R2DUtils.enableGL2D();
        GL11.glPushMatrix();
        R2DUtils.glColor(col1);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        R2DUtils.drawGradientRect(x2, y2, x22, y22, col2, col3);
        R2DUtils.disableGL2D();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float)redRGB;
        float green = 0.003921569f * (float)greenRGB;
        float blue = 0.003921569f * (float)blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawStrip(int x2, int y2, float width, double angle, float points, float radius, int color) {
        float a2;
        float xc2;
        float yc2;
        int i2;
        float f1 = (float)(color >> 24 & 255) / 255.0f;
        float f2 = (float)(color >> 16 & 255) / 255.0f;
        float f3 = (float)(color >> 8 & 255) / 255.0f;
        float f4 = (float)(color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated(x2, y2, 0.0);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glLineWidth(width);
        if (angle > 0.0) {
            GL11.glBegin(3);
            i2 = 0;
            while ((double)i2 < angle) {
                a2 = (float)((double)i2 * (angle * 3.141592653589793 / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)radius);
                yc2 = (float)(Math.sin(a2) * (double)radius);
                GL11.glVertex2f(xc2, yc2);
                ++i2;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin(3);
            i2 = 0;
            while ((double)i2 > angle) {
                a2 = (float)((double)i2 * (angle * 3.141592653589793 / (double)points));
                xc2 = (float)(Math.cos(a2) * (double)(- radius));
                yc2 = (float)(Math.sin(a2) * (double)(- radius));
                GL11.glVertex2f(xc2, yc2);
                --i2;
            }
            GL11.glEnd();
        }
        R2DUtils.disableGL2D();
        GL11.glDisable(3479);
        GL11.glPopMatrix();
    }

    public static void drawHLine(float x2, float y2, float x1, int y1) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        R2DUtils.drawRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x2, float y2, float x1, int y1) {
        if (x1 < y2) {
            float var5 = y2;
            y2 = x1;
            x1 = var5;
        }
        R2DUtils.drawRect(x2, y2 + 1.0f, x2 + 1.0f, x1, y1);
    }

    public static void drawHLine(float x2, float y2, float x1, int y1, int y22) {
        if (y2 < x2) {
            float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        R2DUtils.drawGradientRect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1, y22);
    }

    public static void drawRect(float x2, float y2, float x1, float y1, float r2, float g2, float b2, float a2) {
        R2DUtils.enableGL2D();
        GL11.glColor4f(r2, g2, b2, a2);
        R2DUtils.drawRect(x2, y2, x1, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawRect(float x2, float y2, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public static void drawCircle(float cx, float cy2, float r2, int num_segments, int c2) {
        cx *= 2.0f;
        cy2 *= 2.0f;
        float f2 = (float)(c2 >> 24 & 255) / 255.0f;
        float f22 = (float)(c2 >> 16 & 255) / 255.0f;
        float f3 = (float)(c2 >> 8 & 255) / 255.0f;
        float f4 = (float)(c2 & 255) / 255.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p2 = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x2 = r2 *= 2.0f;
        float y2 = 0.0f;
        R2DUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(2);
        int ii2 = 0;
        while (ii2 < num_segments) {
            GL11.glVertex2f(x2 + cx, y2 + cy2);
            float t = x2;
            x2 = p2 * x2 - s * y2;
            y2 = s * t + p2 * y2;
            ++ii2;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        R2DUtils.disableGL2D();
    }

    public static void drawFullCircle(int cx, int cy2, double r2, int c2) {
        r2 *= 2.0;
        cx *= 2;
        cy2 *= 2;
        float f2 = (float)(c2 >> 24 & 255) / 255.0f;
        float f22 = (float)(c2 >> 16 & 255) / 255.0f;
        float f3 = (float)(c2 >> 8 & 255) / 255.0f;
        float f4 = (float)(c2 & 255) / 255.0f;
        R2DUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(6);
        int i2 = 0;
        while (i2 <= 360) {
            double x2 = Math.sin((double)i2 * 3.141592653589793 / 180.0) * r2;
            double y2 = Math.cos((double)i2 * 3.141592653589793 / 180.0) * r2;
            GL11.glVertex2d((double)cx + x2, (double)cy2 + y2);
            ++i2;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        R2DUtils.disableGL2D();
    }

    public static void drawSmallString(String s, int x2, int y2, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s, x2 * 2, y2 * 2, color);
        GL11.glPopMatrix();
    }

    public static void drawLargeString(String text, int x2, int y2, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x2 *= 2, y2, color);
        GL11.glPopMatrix();
    }

    public static ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

	public static void drawIconNew(String location, int x, int y, int s, int s2){
    	GL11.glPushMatrix();
    	GL11.glTranslatef(x, y, 0);
    	GL11.glEnable(GL11.GL_BLEND);
    	TextureUtil.bindTexture(location, s, s2);
    	GL11.glPopMatrix();
    }
}

