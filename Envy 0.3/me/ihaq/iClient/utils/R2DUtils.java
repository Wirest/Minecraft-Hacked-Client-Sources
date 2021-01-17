package me.ihaq.iClient.utils;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;

public class R2DUtils {
	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth,
			int border, int bottom, int top) {
		enableGL2D();
		drawGradientRect(x, y, x1, y1, top, bottom);
		glColor(border);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(3);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		disableGL2D();
	}

	public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
		float x = rectangle.x;
		float y = rectangle.y;
		float x1 = rectangle.x + rectangle.width;
		float y1 = rectangle.y + rectangle.height;
		enableGL2D();
		glColor(internalColor);
		drawRect(x + width, y + width, x1 - width, y1 - width);
		glColor(borderColor);
		drawRect(x + 1.0F, y, x1 - 1.0F, y + width);
		drawRect(x, y, x + width, y1);
		drawRect(x1 - width, y, x1, y1);
		drawRect(x + 1.0F, y1 - width, x1 - 1.0F, y1);
		disableGL2D();
	}

	public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		glColor(col1);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		glColor(col2);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2,
			int col3) {
		enableGL2D();
		GL11.glPushMatrix();
		glColor(col1);
		GL11.glLineWidth(1.0F);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		drawGradientRect(x, y, x2, y2, col2, col3);
		disableGL2D();
	}

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

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawRect(float x, float y, float x1, float y1) {
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}

	
	public static void drawBorderedRectAlt(float x, float y, float x1, float y1, float width, int internalColor,
			int borderColor) {
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
	
	
	public static void drawBorderRect(float left, float top, float right, float bottom, int bcolor, int icolor,
            float f) {
        Gui.drawRect((int)left + (int)f, (int)top + (int)f, (int)right - (int)f, (int)bottom - (int)f, (int)icolor);
        Gui.drawRect((int)left,(int) top, (int)left + (int)f,(int) bottom,(int) bcolor);
        Gui.drawRect((int)left + (int)f,(int) top, (int)right, (int)top + (int)f,(int) bcolor);
        Gui.drawRect((int)left + (int)f, (int)bottom - (int)f,(int) right,(int) bottom, (int)bcolor);
        Gui.drawRect((int)right - (int)f,(int) top + (int)f, (int)right, (int)bottom - (int)f, (int)bcolor);
    }
}
