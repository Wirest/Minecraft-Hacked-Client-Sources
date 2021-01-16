package org.m0jang.crystal.GUI.click;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
   public static void beginCrop(float x, float y, float width, float height) {
      int scaleFactor = getScaleFactor();
      GL11.glEnable(3089);
      GL11.glScissor((int)(x * (float)scaleFactor), (int)((float)Display.getHeight() - y * (float)scaleFactor), (int)(width * (float)scaleFactor), (int)(height * (float)scaleFactor));
   }

   public static void endCrop() {
      GL11.glDisable(3089);
   }

   public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color) {
      drawLine3D(x1, y1, z1, x2, y2, z2, color, true);
   }

   public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color, boolean disableDepth) {
      enableRender3D(disableDepth);
      setColor(color);
      GL11.glBegin(1);
      GL11.glVertex3d(x1, y1, z1);
      GL11.glVertex3d(x2, y2, z2);
      GL11.glEnd();
      disableRender3D(disableDepth);
   }

   public static void drawLine2D(double x1, double y1, double x2, double y2, float width, int color) {
      enableRender2D();
      setColor(color);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2d(x1, y1);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      disableRender2D();
   }

   public static void drawPoint(int x, int y, float size, int color) {
      enableRender2D();
      setColor(color);
      GL11.glPointSize(size);
      GL11.glEnable(2832);
      GL11.glBegin(0);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glEnd();
      GL11.glDisable(2832);
      disableRender2D();
   }

   public static void drawTexturedRect(int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      var10.startDrawingQuads();
      var10.addVertexWithUV((double)(x + 0), (double)(y + height), 0.0D, (double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + textureHeight) * 0.00390625F));
      var10.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((float)(textureX + textureWidth) * 0.00390625F), (double)((float)(textureY + textureHeight) * 0.00390625F));
      var10.addVertexWithUV((double)(x + width), (double)(y + 0), 0.0D, (double)((float)(textureX + textureWidth) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F));
      var10.addVertexWithUV((double)(x + 0), (double)(y + 0), 0.0D, (double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F));
      var9.draw();
   }

   public static int getScaleFactor() {
      Minecraft var10002 = Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      int var10003 = Minecraft.displayWidth;
      Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(var10002, var10003, Minecraft.displayHeight);
      int scaleFactor = scaledResolution.getScaleFactor();
      return scaleFactor;
   }

   public static void drawOutlinedBox(AxisAlignedBB boundingBox, int color) {
      drawOutlinedBox(boundingBox, color, true);
   }

   public static void drawOutlinedBox(AxisAlignedBB boundingBox, int color, boolean disableDepth) {
      if (boundingBox != null) {
         enableRender3D(disableDepth);
         setColor(color);
         GL11.glBegin(3);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(3);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(1);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         disableRender3D(disableDepth);
      }

   }

   public static void drawBox(AxisAlignedBB boundingBox, int color) {
      drawBox(boundingBox, color, true);
   }

   public static void drawBox(AxisAlignedBB boundingBox, int color, boolean disableDepth) {
      if (boundingBox != null) {
         enableRender3D(disableDepth);
         setColor(color);
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glEnd();
         disableRender3D(disableDepth);
      }

   }

   public static void enableRender3D(boolean disableDepth) {
      if (disableDepth) {
         GL11.glDepthMask(false);
         GL11.glDisable(2929);
      }

      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glLineWidth(1.0F);
   }

   public static void disableRender3D(boolean enableDepth) {
      if (enableDepth) {
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
      }

      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void enableRender2D() {
      GL11.glEnable(3042);
      GL11.glDisable(2884);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
   }

   public static void disableRender2D() {
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
   }

   public static void setColor(int colorHex) {
      float alpha = (float)(colorHex >> 24 & 255) / 255.0F;
      float red = (float)(colorHex >> 16 & 255) / 255.0F;
      float green = (float)(colorHex >> 8 & 255) / 255.0F;
      float blue = (float)(colorHex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void drawBorderedRect(float x, float y, float width, float height, float borderWidth, int rectColor, int borderColor) {
      drawRect(x + borderWidth, y + borderWidth, width - borderWidth * 2.0F, height - borderWidth * 2.0F, rectColor);
      drawRect(x, y, width, borderWidth, borderColor);
      drawRect(x, y + borderWidth, borderWidth, height - borderWidth, borderColor);
      drawRect(x + width - borderWidth, y + borderWidth, borderWidth, height - borderWidth, borderColor);
      drawRect(x + borderWidth, y + height - borderWidth, width - borderWidth * 2.0F, borderWidth, borderColor);
   }

   public static void drawRect(float x, float y, float width, float height, int color) {
      enableRender2D();
      setColor(color);
      GL11.glBegin(7);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)(x + width), (double)y);
      GL11.glVertex2d((double)(x + width), (double)(y + height));
      GL11.glVertex2d((double)x, (double)(y + height));
      GL11.glEnd();
      disableRender2D();
   }

   public static void drawLogo(float x, float y, float width, int color) {
      float x1 = x + width * 0.0F;
      float x2 = x + width * 0.216F;
      float x3 = x + width * 0.33F;
      float x4 = x + width * 0.5F;
      float x5 = x + width * 0.67F;
      float x6 = x + width * 0.784F;
      float x7 = x + width * 1.0F;
      float y1 = y + width * 0.0F;
      float y2 = y + width * 0.205F;
      float y3 = y + width * 0.525F;
      float y4 = y + width * 1.139F;
      enableRender2D();
      GL11.glEnable(2881);
      setColor(color);
      GL11.glBegin(6);
      GL11.glVertex2d((double)x3, (double)y3);
      GL11.glVertex2d((double)x4, (double)y4);
      GL11.glVertex2d((double)x5, (double)y3);
      GL11.glVertex2d((double)x4, (double)y1);
      GL11.glEnd();
      GL11.glBegin(6);
      GL11.glVertex2d((double)x1, (double)y3);
      GL11.glVertex2d((double)x4, (double)y4);
      GL11.glVertex2d((double)x2, (double)y3);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glEnd();
      GL11.glBegin(6);
      GL11.glVertex2d((double)x7, (double)y3);
      GL11.glVertex2d((double)x4, (double)y4);
      GL11.glVertex2d((double)x6, (double)y3);
      GL11.glVertex2d((double)x6, (double)y2);
      GL11.glEnd();
      GL11.glDisable(2881);
      disableRender2D();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float edgeRadius, int color, float borderWidth, int borderColor) {
      if (edgeRadius < 0.0F) {
         edgeRadius = 0.0F;
      }

      if (edgeRadius > width / 2.0F) {
         edgeRadius = width / 2.0F;
      }

      if (edgeRadius > height / 2.0F) {
         edgeRadius = height / 2.0F;
      }

      drawRect(x + edgeRadius, y + edgeRadius, width - edgeRadius * 2.0F, height - edgeRadius * 2.0F, color);
      drawRect(x + edgeRadius, y, width - edgeRadius * 2.0F, edgeRadius, color);
      drawRect(x + edgeRadius, y + height - edgeRadius, width - edgeRadius * 2.0F, edgeRadius, color);
      drawRect(x, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0F, color);
      drawRect(x + width - edgeRadius, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0F, color);
      enableRender2D();
      setColor(color);
      GL11.glBegin(6);
      float centerX = x + edgeRadius;
      float centerY = y + edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      int vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      int i;
      double angleRadians;
      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586D * (double)(i + 180) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + width - edgeRadius;
      centerY = y + edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586D * (double)(i + 90) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + edgeRadius;
      centerY = y + height - edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586D * (double)(i + 270) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + width - edgeRadius;
      centerY = y + height - edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586D * (double)i / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      setColor(borderColor);
      GL11.glLineWidth(borderWidth);
      GL11.glBegin(3);
      centerX = x + edgeRadius;
      centerY = y + edgeRadius;
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586D * (double)(i + 180) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + edgeRadius), (double)y);
      GL11.glVertex2d((double)(x + width - edgeRadius), (double)y);
      centerX = x + width - edgeRadius;
      centerY = y + edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586D * (double)(i + 90) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + width), (double)(y + edgeRadius));
      GL11.glVertex2d((double)(x + width), (double)(y + height - edgeRadius));
      centerX = x + width - edgeRadius;
      centerY = y + height - edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586D * (double)i / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + width - edgeRadius), (double)(y + height));
      GL11.glVertex2d((double)(x + edgeRadius), (double)(y + height));
      centerX = x + edgeRadius;
      centerY = y + height - edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586D * (double)(i + 270) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)x, (double)(y + height - edgeRadius));
      GL11.glVertex2d((double)x, (double)(y + edgeRadius));
      GL11.glEnd();
      disableRender2D();
   }

   public static int getDisplayWidth() {
      Minecraft var10002 = Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      int var10003 = Minecraft.displayWidth;
      Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(var10002, var10003, Minecraft.displayHeight);
      int displayWidth = scaledResolution.getScaledWidth();
      return displayWidth;
   }

   public static int getDisplayHeight() {
      Minecraft var10002 = Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      int var10003 = Minecraft.displayWidth;
      Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(var10002, var10003, Minecraft.displayHeight);
      int displayHeight = scaledResolution.getScaledHeight();
      return displayHeight;
   }

   public static void drawCircle(float x, float y, float radius, float lineWidth, int color) {
      enableRender2D();
      setColor(color);
      GL11.glLineWidth(lineWidth);
      int vertices = (int)Math.min(Math.max(radius, 45.0F), 360.0F);
      GL11.glBegin(2);

      for(int i = 0; i < vertices; ++i) {
         double angleRadians = 6.283185307179586D * (double)i / (double)vertices;
         GL11.glVertex2d((double)x + Math.sin(angleRadians) * (double)radius, (double)y + Math.cos(angleRadians) * (double)radius);
      }

      GL11.glEnd();
      disableRender2D();
   }

   public static void drawFilledCircle(float x, float y, float radius, int color) {
      enableRender2D();
      setColor(color);
      int vertices = (int)Math.min(Math.max(radius, 45.0F), 360.0F);
      GL11.glBegin(9);

      for(int i = 0; i < vertices; ++i) {
         double angleRadians = 6.283185307179586D * (double)i / (double)vertices;
         GL11.glVertex2d((double)x + Math.sin(angleRadians) * (double)radius, (double)y + Math.cos(angleRadians) * (double)radius);
      }

      GL11.glEnd();
      disableRender2D();
      drawCircle(x, y, radius, 1.5F, 16777215);
   }

   public static void drawFilledCircleNoBorder(float x, float y, float radius, int color) {
      enableRender2D();
      setColor(color);
      int vertices = (int)Math.min(Math.max(radius, 45.0F), 360.0F);
      GL11.glBegin(9);

      for(int i = 0; i < vertices; ++i) {
         double angleRadians = 6.283185307179586D * (double)i / (double)vertices;
         GL11.glVertex2d((double)x + Math.sin(angleRadians) * (double)radius, (double)y + Math.cos(angleRadians) * (double)radius);
      }

      GL11.glEnd();
      disableRender2D();
   }

   public static int darker(int hexColor, int factor) {
      float alpha = (float)(hexColor >> 24 & 255);
      float red = Math.max((float)(hexColor >> 16 & 255) - (float)(hexColor >> 16 & 255) / (100.0F / (float)factor), 0.0F);
      float green = Math.max((float)(hexColor >> 8 & 255) - (float)(hexColor >> 8 & 255) / (100.0F / (float)factor), 0.0F);
      float blue = Math.max((float)(hexColor & 255) - (float)(hexColor & 255) / (100.0F / (float)factor), 0.0F);
      return (int)((float)(((int)alpha << 24) + ((int)red << 16) + ((int)green << 8)) + blue);
   }
}
