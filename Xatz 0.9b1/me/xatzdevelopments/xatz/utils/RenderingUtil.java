package me.xatzdevelopments.xatz.utils;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import me.xatzdevelopments.xatz.client.Colors;

public class RenderingUtil {
   public static double[] convertTo2D(double x, double y, double z) {
      FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
      IntBuffer viewport = BufferUtils.createIntBuffer(16);
      FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
      FloatBuffer projection = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, modelView);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
      return result ? new double[]{(double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2)} : null;
   }

   public static double[] boundingBox(double x, double y, double z, AxisAlignedBB boundingBox) {
      double[] pos1 = convertTo2D(boundingBox.minX - x, boundingBox.minY - y, boundingBox.minZ - z);
      double[] pos2 = convertTo2D(boundingBox.maxX - x, boundingBox.minY - y, boundingBox.minZ - z);
      double[] pos3 = convertTo2D(boundingBox.maxX - x, boundingBox.minY - y, boundingBox.maxZ - z);
      double[] pos4 = convertTo2D(boundingBox.minX - x, boundingBox.minY - y, boundingBox.maxZ - z);
      double[] pos5 = convertTo2D(boundingBox.minX - x, boundingBox.maxY - y, boundingBox.minZ - z);
      double[] pos6 = convertTo2D(boundingBox.maxX - x, boundingBox.maxY - y, boundingBox.minZ - z);
      double[] pos7 = convertTo2D(boundingBox.maxX - x, boundingBox.maxY - y, boundingBox.maxZ - z);
      double[] pos8 = convertTo2D(boundingBox.minX - x, boundingBox.maxY - y, boundingBox.maxZ - z);
      boolean shouldRender = pos1[2] > 0.0D && pos1[2] <= 1.0D && pos2[2] > 0.0D && pos2[2] <= 1.0D && pos3[2] > 0.0D && pos3[2] <= 1.0D && pos4[2] > 0.0D && pos4[2] <= 1.0D && pos5[2] > 0.0D && pos5[2] <= 1.0D && pos6[2] > 0.0D && pos6[2] <= 1.0D && pos7[2] > 0.0D && pos7[2] <= 1.0D && pos8[2] > 0.0D && pos8[2] <= 1.0D;
      if (shouldRender) {
         return new double[]{-1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D, -1337.0D};
      } else {
         double startX = pos1[0];
         double startY = pos1[1];
         double endX = pos8[0];
         double endY = pos8[1];
         double[] xValues = new double[]{pos1[0], pos2[0], pos3[0], pos4[0], pos5[0], pos6[0], pos7[0], pos8[0]};
         double[] yValues = new double[]{pos1[1], pos2[1], pos3[1], pos4[1], pos5[1], pos6[1], pos7[1], pos8[1]};
         double[] var26 = xValues;
         int var27 = xValues.length;

         int var28;
         Double bdubs;
         for(var28 = 0; var28 < var27; ++var28) {
            bdubs = var26[var28];
            if (bdubs.doubleValue() < startX) {
               startX = bdubs.doubleValue();
            }
         }

         var26 = xValues;
         var27 = xValues.length;

         for(var28 = 0; var28 < var27; ++var28) {
            bdubs = var26[var28];
            if (bdubs.doubleValue() > endX) {
               endX = bdubs.doubleValue();
            }
         }

         var26 = yValues;
         var27 = yValues.length;

         for(var28 = 0; var28 < var27; ++var28) {
            bdubs = var26[var28];
            if (bdubs.doubleValue() < startY) {
               startY = bdubs.doubleValue();
            }
         }

         var26 = yValues;
         var27 = yValues.length;

         for(var28 = 0; var28 < var27; ++var28) {
            bdubs = var26[var28];
            if (bdubs.doubleValue() > endY) {
               endY = bdubs.doubleValue();
            }
         }

         return new double[]{pos1[0], pos1[1], pos2[0], pos2[1], pos3[0], pos3[1], pos4[0], pos4[1], pos5[0], pos5[1], pos6[0], pos6[1], pos7[0], pos7[1], pos8[0], pos8[1], startX, startY, endX, endY};
      }
   }

   public static void drawOutlinedString(String str, float x, float y, int color) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.fontRendererObj.drawString(str, x - 0.3F, y, Colors.getColor(0));
      mc.fontRendererObj.drawString(str, x + 0.3F, y, Colors.getColor(0));
      mc.fontRendererObj.drawString(str, x, y + 0.3F, Colors.getColor(0));
      mc.fontRendererObj.drawString(str, x, y - 0.3F, Colors.getColor(0));
      mc.fontRendererObj.drawString(str, x, y, color);
   }

   public static void setupRender(boolean start) {
      if (start) {
         GlStateManager.enableBlend();
         GL11.glEnable(2848);
         GlStateManager.disableDepth();
         GlStateManager.disableTexture2D();
         GlStateManager.blendFunc(770, 771);
         GL11.glHint(3154, 4354);
      } else {
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GL11.glDisable(2848);
         GlStateManager.enableDepth();
      }

      GlStateManager.depthMask(!start);
   }

   public static void drawFancy(double d, double e, double f2, double f3, int paramColor) {
      float alpha = (float)(paramColor >> 24 & 255) / 255.0F;
      float red = (float)(paramColor >> 16 & 255) / 255.0F;
      float green = (float)(paramColor >> 8 & 255) / 255.0F;
      float blue = (float)(paramColor & 255) / 255.0F;
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glPushMatrix();
      GL11.glEnable(2848);
      GL11.glEnable(2881);
      GL11.glEnable(2832);
      GL11.glEnable(3042);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(7);
      GL11.glVertex2d(f2 + 1.300000011920929D, e);
      GL11.glVertex2d(d + 1.0D, e);
      GL11.glVertex2d(d - 1.300000011920929D, f3);
      GL11.glVertex2d(f2 - 1.0D, f3);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glDisable(2881);
      GL11.glDisable(2832);
      GL11.glDisable(3042);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
   }

   public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
      GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
   }

   public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(left, top);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, bottom);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }

   public static void drawColorPicker(double left, double top, double right, double bottom, int col2) {
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(left, top);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2d(right, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }

   public static void rectangle(double left, double top, double right, double bottom, int color) {
      double var5;
      if (left < right) {
         var5 = left;
         left = right;
         right = var5;
      }

      if (top < bottom) {
         var5 = top;
         top = bottom;
         bottom = var5;
      }

      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var6 = (float)(color >> 16 & 255) / 255.0F;
      float var7 = (float)(color >> 8 & 255) / 255.0F;
      float var8 = (float)(color & 255) / 255.0F;
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var6, var7, var8, var11);
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(left, bottom, 0.0D);
      worldRenderer.addVertex(right, bottom, 0.0D);
      worldRenderer.addVertex(right, top, 0.0D);
      worldRenderer.addVertex(left, top, 0.0D);
      Tessellator.getInstance().draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawIcon(double x, double y, float u, float v, double width, double height, float textureWidth, float textureHeight) {
      float var8 = 1.0F / textureWidth;
      float var9 = 1.0F / textureHeight;
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      var11.startDrawingQuads();
      var11.addVertexWithUV(x, y + height, 0.0D, (double)(u * var8), (double)((v + (float)height) * var9));
      var11.addVertexWithUV(x + width, y + height, 0.0D, (double)((u + (float)width) * var8), (double)((v + (float)height) * var9));
      var11.addVertexWithUV(x + width, y, 0.0D, (double)((u + (float)width) * var8), (double)(v * var9));
      var11.addVertexWithUV(x, y, 0.0D, (double)(u * var8), (double)(v * var9));
      var10.draw();
   }

   public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
      rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x + width, y, x1 - width, y + width, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x, y, x + width, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x1 - width, y, x1, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void filledBox(AxisAlignedBB boundingBox, int color, boolean shouldColor) {
      GlStateManager.pushMatrix();
      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var6 = (float)(color >> 16 & 255) / 255.0F;
      float var7 = (float)(color >> 8 & 255) / 255.0F;
      float var8 = (float)(color & 255) / 255.0F;
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      if (shouldColor) {
         GlStateManager.color(var6, var7, var8, var11);
      }

      byte draw = 7;
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(draw);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      Tessellator.getInstance().draw();
      GlStateManager.depthMask(true);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox) {
      GlStateManager.pushMatrix();
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.startDrawing(3);
      var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      var1.draw();
      var2.startDrawing(3);
      var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      var1.draw();
      var2.startDrawing(1);
      var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      var1.draw();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
   }

   public static void drawLines(AxisAlignedBB boundingBox) {
      GL11.glPushMatrix();
      GL11.glBegin(2);
      GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      GL11.glEnd();
      GL11.glPopMatrix();
   }

   public static void drawBoundingBox(AxisAlignedBB axisalignedbb) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
      worldrender.startDrawingQuads();
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      tessellator.draw();
   }

   public static void draw3DLine(float x, float y, float z, int color) {
      pre3D();
      GL11.glLoadIdentity();
      Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var6 = (float)(color >> 16 & 255) / 255.0F;
      float var7 = (float)(color >> 8 & 255) / 255.0F;
      float var8 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(var6, var7, var8, var11);
      GL11.glLineWidth(0.5F);
      GL11.glBegin(3);
      GL11.glVertex3d(0.0D, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
      GL11.glVertex3d((double)x, (double)y, (double)z);
      GL11.glEnd();
      post3D();
   }

   public static void pre3D() {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      GL11.glHint(3154, 4354);
   }

   public static void post3D() {
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (!GL11.glIsEnabled(2896)) {
         GL11.glEnable(2896);
      }

   }

   public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
      float red = 0.003921569F * (float)redRGB;
      float green = 0.003921569F * (float)greenRGB;
      float blue = 0.003921569F * (float)blueRGB;
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

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawRect(float x, float y, float x1, float y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
      drawRect(x + 0.5F, y, x1 - 0.5F, y + 0.5F, insideC);
      drawRect(x + 0.5F, y1 - 0.5F, x1 - 0.5F, y1, insideC);
      drawRect(x, y + 0.5F, x1, y1 - 0.5F, insideC);
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
      float var11 = (float)(y1 >> 24 & 255) / 255.0F;
      float var6 = (float)(y1 >> 16 & 255) / 255.0F;
      float var7 = (float)(y1 >> 8 & 255) / 255.0F;
      float var8 = (float)(y1 & 255) / 255.0F;
      pre3D();
      Tessellator tes = Tessellator.getInstance();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      RenderHelper.disableStandardItemLighting();
      GL11.glBegin(6);
      GL11.glVertex2f(x, y);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x + (x1 - x) / 2.0F, y + 3.0F);
      GL11.glEnd();
      tes.draw();
      RenderHelper.enableStandardItemLighting();
      post3D();
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

   public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
      GL11.glPushMatrix();
      cx *= 2.0F;
      cy *= 2.0F;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      float theta = (float)(6.2831852D / (double)num_segments);
      float p = (float)Math.cos((double)theta);
      float s = (float)Math.sin((double)theta);
      float x = r *= 2.0F;
      float y = 0.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(2);

      for(int ii = 0; ii < num_segments; ++ii) {
         GL11.glVertex2f(x + cx, y + cy);
         float t = x;
         x = p * x - s * y;
         y = s * t + p * y;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
      GL11.glPopMatrix();
   }

   public static void drawBorderedCircle(int circleX, int circleY, double radius, double width, int borderColor, int innerColor) {
      enableGL2D();
      drawCircle((float)circleX, (float)circleY, (float)(radius - 0.5D + width), 72, borderColor);
      drawFullCircle(circleX, circleY, radius, innerColor);
      disableGL2D();
   }

   public static void drawCircleNew(float x, float y, float radius, int numberOfSides) {
      float z = 0.0F;
      int numberOfVertices = numberOfSides + 2;
      float doublePi = 6.2831855F;
   }

   public static void drawFullCircle(int cx, int cy, double r, int c) {
      r *= 2.0D;
      cx *= 2;
      cy *= 2;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(6);

      for(int i = 0; i <= 2160; ++i) {
         double x = Math.sin((double)i * 3.141592653589793D / 360.0D) * r;
         double y = Math.cos((double)i * 3.141592653589793D / 360.0D) * r;
         GL11.glVertex2d((double)cx + x, (double)cy + y);
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static Vec3 interpolateRender(EntityPlayer player) {
      float part = Minecraft.getMinecraft().timer.renderPartialTicks;
      double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)part;
      double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)part;
      double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)part;
      return new Vec3(interpX, interpY, interpZ);
   }
}
