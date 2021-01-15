package org.darkstorm.minecraft.gui.util;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4b;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;

import java.awt.Color;
import java.awt.Point;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RenderUtil {

	public static void scissorBox(int x, int y, int xend, int yend) {
		int width = xend - x;
		int height = yend - y;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int factor = sr.getScaleFactor();
		int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
		glScissor(x * factor, bottomY * factor, width * factor, height * factor);
	}

	public static void setupLineSmooth() {
		glEnable(GL_BLEND);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_LINE_SMOOTH);
		glDisable(GL_TEXTURE_2D);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
		glShadeModel(GL_SMOOTH);
	}

	public static void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ,
			float thickness) {
		glPushMatrix();
		setupLineSmooth();
		glLineWidth(thickness);
		glBegin(GL_LINES);
		glVertex3d(startX, startY, startZ);
		glVertex3d(endX, endY, endZ);
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_LINE_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_MULTISAMPLE);
		glDisable(GL_SAMPLE_ALPHA_TO_COVERAGE);
		glPopMatrix();
	}

	/*
	 * public static void drawTexturedModalRect(int par1, int par2, int par3,
	 * int par4, int par5, int par6) { float var7 = 0.00390625F; float var8 =
	 * 0.00390625F; Tessellator var9 = Tessellator.getInstance(); WorldRenderer
	 * var10 = var9.getWorldRenderer(); var10.startDrawingQuads();
	 * var10.addVertexWithUV((par1 + 0), (par2 + par6), 0, ((par3 + 0) * var7),
	 * ((par4 + par6) * var8)); var10.addVertexWithUV((par1 + par5), (par2 +
	 * par6), 0, ((par3 + par5) * var7), ((par4 + par6) * var8));
	 * var10.addVertexWithUV((par1 + par5), (par2 + 0), 0, ((par3 + par5) *
	 * var7), ((par4 + 0) * var8)); var10.addVertexWithUV((par1 + 0), (par2 +
	 * 0), 0, ((par3 + 0) * var7), ((par4 + 0) * var8)); var9.draw(); }
	 */
	public static void drawTexturedModalRect(int textureId, int posX, int posY, int width, int height) {
		double halfHeight = (height / 2);
		double halfWidth = (width / 2);

		glDisable(GL_CULL_FACE);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glPushMatrix();
		glTranslated(posX + halfWidth, posY + halfHeight, 0);
		glScalef(width, height, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4f(1F, 1F, 1F, 1f);
		glBegin(GL_TRIANGLES);
		glNormal3f(0, 0, 1);
		glTexCoord2f(1, 1);
		glVertex2d(1, 1);
		glTexCoord2f(0, 1);
		glVertex2d(-1, 1);
		glTexCoord2f(0, 0);
		glVertex2d(-1, -1);
		glTexCoord2f(0, 0);
		glVertex2d(-1, -1);
		glTexCoord2f(1, 0);
		glVertex2d(1, -1);
		glTexCoord2f(1, 1);
		glVertex2d(1, 1);
		glEnd();

		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

	public static int interpolateColor(int rgba1, int rgba2, float percent) {
		int r1 = rgba1 & 0xFF, g1 = rgba1 >> 8 & 0xFF, b1 = rgba1 >> 16 & 0xFF, a1 = rgba1 >> 24 & 0xFF;
		int r2 = rgba2 & 0xFF, g2 = rgba2 >> 8 & 0xFF, b2 = rgba2 >> 16 & 0xFF, a2 = rgba2 >> 24 & 0xFF;

		int r = (int) (r1 < r2 ? r1 + (r2 - r1) * percent : r2 + (r1 - r2) * percent);
		int g = (int) (g1 < g2 ? g1 + (g2 - g1) * percent : g2 + (g1 - g2) * percent);
		int b = (int) (b1 < b2 ? b1 + (b2 - b1) * percent : b2 + (b1 - b2) * percent);
		int a = (int) (a1 < a2 ? a1 + (a2 - a1) * percent : a2 + (a1 - a2) * percent);

		return r | g << 8 | b << 16 | a << 24;
	}

	public static void setColor(Color c) {
		glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

	public static Color toColor(int rgba) {
		int r = rgba & 0xFF, g = rgba >> 8 & 0xFF, b = rgba >> 16 & 0xFF, a = rgba >> 24 & 0xFF;
		return new Color(r, g, b, a);
	}

	public static int toRGBA(Color c) {
		return c.getRed() | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
	}

	public static void setColor(int rgba) {
		int r = rgba & 0xFF, g = rgba >> 8 & 0xFF, b = rgba >> 16 & 0xFF, a = rgba >> 24 & 0xFF;
		glColor4b((byte) r, (byte) g, (byte) b, (byte) a);
	}

	public static Point calculateMouseLocation() {
		Minecraft minecraft = Minecraft.getMinecraft();
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		scaleFactor = 2;
		return new Point(Mouse.getX() / scaleFactor,
				minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1);
	}

}