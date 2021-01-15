package saint.utilities;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
   private static final NahrFont font = new NahrFont("Verdana", 18.0F);
   private static final ScaledResolution sr;

   static {
      sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
   }

   public static final ScaledResolution getScaledRes() {
      ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      return scaledRes;
   }

   public static void drawHollowRect(float posX, float posY, float posX2, float posY2, float width, int color, boolean center) {
      float corners = width / 2.0F;
      float side = width / 2.0F;
      if (center) {
         drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
         drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
         drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
         drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
      } else {
         drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
         drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
         drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
         drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
      }

   }

   public static void drawGradientBorderedRect(float posX, float posY, float posX2, float posY2, float width, int color, int startColor, int endColor, boolean center) {
      drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
      drawHollowRect(posX, posY, posX2, posY2, width, color, center);
   }

   public static void drawCoolLines(AxisAlignedBB mask) {
      GL11.glPushMatrix();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glPopMatrix();
   }

   public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
      drawRect(x, y, x2, y2, col2);
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glLineWidth(l1);
      GL11.glBegin(1);
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
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public static void drawBorderedCorneredRect(float x, float y, float x2, float y2, float lineWidth, int lineColor, int bgColor) {
      drawRect(x, y, x2, y2, bgColor);
      float f = (float)(lineColor >> 24 & 255) / 255.0F;
      float f1 = (float)(lineColor >> 16 & 255) / 255.0F;
      float f2 = (float)(lineColor >> 8 & 255) / 255.0F;
      float f3 = (float)(lineColor & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glEnable(3553);
      drawRect(x - 1.0F, y, x2 + 1.0F, y - lineWidth, lineColor);
      drawRect(x, y, x - lineWidth, y2, lineColor);
      drawRect(x - 1.0F, y2, x2 + 1.0F, y2 + lineWidth, lineColor);
      drawRect(x2, y, x2 + lineWidth, y2, lineColor);
      GL11.glDisable(3553);
      GL11.glDisable(3042);
   }

   public static double interp(double from, double to, double pct) {
      return from + (to - from) * pct;
   }

   public static double interpPlayerX() {
      return interp(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.posX, (double)Minecraft.getMinecraft().timer.renderPartialTicks);
   }

   public static double interpPlayerY() {
      return interp(Minecraft.getMinecraft().thePlayer.lastTickPosY, Minecraft.getMinecraft().thePlayer.posY, (double)Minecraft.getMinecraft().timer.renderPartialTicks);
   }

   public static double interpPlayerZ() {
      return interp(Minecraft.getMinecraft().thePlayer.lastTickPosZ, Minecraft.getMinecraft().thePlayer.posZ, (double)Minecraft.getMinecraft().timer.renderPartialTicks);
   }

   public static void drawFilledBox(AxisAlignedBB mask) {
      WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
      Tessellator tessellator = Tessellator.instance;
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.maxZ);
      worldRenderer.addVertex(mask.minX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.minX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.minZ);
      worldRenderer.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      worldRenderer.addVertex(mask.maxX, mask.minY, mask.maxZ);
      tessellator.draw();
   }

   public static void glColor(Color color) {
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      GL11.glEnable(1536);
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
      GL11.glDisable(1536);
   }

   public static void drawLines(AxisAlignedBB mask) {
      GL11.glPushMatrix();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
      GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glBegin(2);
      GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
      GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
      GL11.glEnd();
      GL11.glPopMatrix();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB mask) {
      WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
      Tessellator var1 = Tessellator.instance;
      var2.startDrawing(3);
      var2.addVertex(mask.minX, mask.minY, mask.minZ);
      var2.addVertex(mask.maxX, mask.minY, mask.minZ);
      var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
      var2.addVertex(mask.minX, mask.minY, mask.maxZ);
      var2.addVertex(mask.minX, mask.minY, mask.minZ);
      var1.draw();
      var2.startDrawing(3);
      var2.addVertex(mask.minX, mask.maxY, mask.minZ);
      var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
      var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
      var2.addVertex(mask.minX, mask.maxY, mask.minZ);
      var1.draw();
      var2.startDrawing(1);
      var2.addVertex(mask.minX, mask.minY, mask.minZ);
      var2.addVertex(mask.minX, mask.maxY, mask.minZ);
      var2.addVertex(mask.maxX, mask.minY, mask.minZ);
      var2.addVertex(mask.maxX, mask.maxY, mask.minZ);
      var2.addVertex(mask.maxX, mask.minY, mask.maxZ);
      var2.addVertex(mask.maxX, mask.maxY, mask.maxZ);
      var2.addVertex(mask.minX, mask.minY, mask.maxZ);
      var2.addVertex(mask.minX, mask.maxY, mask.maxZ);
      var1.draw();
   }

   public static void drawRect(float g, float h, float i, float j, int col1) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(7);
      GL11.glVertex2d((double)i, (double)h);
      GL11.glVertex2d((double)g, (double)h);
      GL11.glVertex2d((double)g, (double)j);
      GL11.glVertex2d((double)i, (double)j);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public static final NahrFont getNahrFont() {
      return font;
   }
}
