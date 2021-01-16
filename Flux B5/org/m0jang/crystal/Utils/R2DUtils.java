package org.m0jang.crystal.Utils;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Wrapper;

public class R2DUtils {
   public static void drawStringWithShadow(String text, float x, float y, int color) {
      Wrapper.mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
   }

   public void drawSmallString(String s, int x, int y, int color) {
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawStringWithShadow(s, (float)(x * 2), (float)(y * 2), color);
      GL11.glPopMatrix();
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

   public static void drawRect(float x, float y, float x1, float y1, int color) {
      enableGL2D();
      RenderUtils.glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
      enableGL2D();
      RenderUtils.glColor(internalColor);
      drawRect(x + width, y + width, x1 - width, y1 - width);
      RenderUtils.glColor(borderColor);
      drawRect(x + width, y, x1 - width, y + width);
      drawRect(x, y, x + width, y1);
      drawRect(x1 - width, y, x1, y1);
      drawRect(x + width, y1 - width, x1 - width, y1);
      disableGL2D();
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
      enableGL2D();
      x *= 2.0F;
      x1 *= 2.0F;
      y *= 2.0F;
      y1 *= 2.0F;
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawVLine(x, y, y1, borderC);
      drawVLine(x1 - 1.0F, y, y1, borderC);
      drawHLine(x, x1 - 1.0F, y, borderC);
      drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
      drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      RenderUtils.glColor(topColor);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      RenderUtils.glColor(bottomColor);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawHLine(float x, float y, float x1, int y1) {
      if (y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
   }

   public static void drawVLine(float x, float y, float x1, int y1) {
      if (x1 < y) {
         float var5 = y;
         y = x1;
         x1 = var5;
      }

      drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
   }

   public static void drawHLine(float x, float y, float x1, int y1, int y2) {
      if (y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
   }

   public static void drawRect(float x, float y, float x1, float y1) {
      GL11.glBegin(7);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
   }

   public static void drawTri(double x1, double y1, double x2, double y2, double x3, double y3, double width, Color c) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      RenderUtils.glColor(c);
      GL11.glLineWidth((float)width);
      GL11.glBegin(3);
      GL11.glVertex2d(x1, y1);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x3, y3);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawFilledCircle(int x, int y, double radius, Color c) {
      RenderUtils.glColor(c);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GL11.glDisable(3553);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.001F);
      Tessellator tess = Tessellator.getInstance();
      WorldRenderer render = tess.getWorldRenderer();

      for(double i = 0.0D; i < 360.0D; ++i) {
         double cs = i * 3.141592653589793D / 180.0D;
         double ps = (i - 1.0D) * 3.141592653589793D / 180.0D;
         double[] outer = new double[]{Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius};
         render.startDrawing(6);
         render.addVertex((double)x + outer[2], (double)y + outer[3], 0.0D);
         render.addVertex((double)x + outer[0], (double)y + outer[1], 0.0D);
         render.addVertex((double)x, (double)y, 0.0D);
         tess.draw();
      }

      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.disableAlpha();
      GL11.glEnable(3553);
   }
}
