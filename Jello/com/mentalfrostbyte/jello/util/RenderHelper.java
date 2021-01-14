package com.mentalfrostbyte.jello.util;


import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;


import org.lwjgl.opengl.ARBShaderObjects;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RenderHelper
{
 
 
 static {
    }
 
 public static final ScaledResolution getScaledRes() {
     final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
     return scaledRes;
 }
 
 public static boolean isHovering(final int x, final int y, final int x2, final int y2, final int mouseX, final int mouseY) {
     return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
 }
 public static void drawCircle(double x, double y, float radius, int color) {
     float alpha = (float)(color >> 24 & 255) / 255.0f;
     float red = (float)(color >> 16 & 255) / 255.0f;
     float green = (float)(color >> 8 & 255) / 255.0f;
     float blue = (float)(color & 255) / 255.0f;
     GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
     GL11.glBegin((int)9);
     int i = 0;
     while (i <= 360) {
         GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
         ++i;
     }
     GL11.glEnd();
 }
 public static String getPrettyName(final String name, final String splitBy) {
     String prettyName = "";
     final String[] actualNameSplit = name.split(splitBy);
     if (actualNameSplit.length > 0) {
         final String[] var3 = actualNameSplit;
         for (int var4 = actualNameSplit.length, var5 = 0; var5 < var4; ++var5) {
             String arg = var3[var5];
             arg = String.valueOf(arg.substring(0, 1).toUpperCase()) + arg.substring(1, arg.length()).toLowerCase();
             prettyName = String.valueOf(prettyName) + arg + " ";
         }
     }
     else {
         prettyName = String.valueOf(actualNameSplit[0].substring(0, 1).toUpperCase()) + actualNameSplit[0].substring(1, actualNameSplit[0].length()).toLowerCase();
     }
     return prettyName.trim();
 }
 
 public static Color rainbow(final long offset, final float fade) {
     final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
     final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
     final Color c = new Color((int)color);
     return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
 }
 
 public static void drawHollowRect(final float posX, final float posY, final float posX2, final float posY2, final float width, final int color, final boolean center) {
     final float corners = width / 2.0f;
     final float side = width / 2.0f;
     if (center) {
         drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
         drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
         drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
         drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
     }
     else {
         drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
         drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
         drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
         drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
     }
 }
 
 public static double interpolate(final double now, final double then) {
     return then + (now - then) * Minecraft.getMinecraft().timer.renderPartialTicks;
 }
 
 public static double[] interpolate(final Entity entity) {
     final double posX = interpolate(entity.posX, entity.lastTickPosX) - RenderManager.renderPosX;
     final double posY = interpolate(entity.posY, entity.lastTickPosY) - RenderManager.renderPosY;
     final double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - RenderManager.renderPosZ;
     return new double[] { posX, posY, posZ };
 }
 
 public static int createShader(final String shaderCode, final int shaderType) throws Exception {
     int shader = 0;
     try {
         shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
         if (shader == 0) {
             return 0;
         }
         ARBShaderObjects.glShaderSourceARB(shader, (CharSequence)shaderCode);
         ARBShaderObjects.glCompileShaderARB(shader);
         if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
             throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
         }
         return shader;
     }
     catch (Exception exc) {
         ARBShaderObjects.glDeleteObjectARB(shader);
         throw exc;
     }
 }
 
 public static String getLogInfo(final int obj) {
     return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
 }
 
 public static void drawGradientBorderedRect(final float posX, final float posY, final float posX2, final float posY2, final float width, final int color, final int startColor, final int endColor, final boolean center) {
     drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
     drawHollowRect(posX, posY, posX2, posY2, width, color, center);
 }
 
 public static void renderOne() {
     GL11.glPushAttrib(1048575);
     GL11.glDisable(3008);
     GL11.glDisable(3553);
     GL11.glDisable(2896);
     GL11.glEnable(3042);
     GL11.glBlendFunc(770, 771);
     GL11.glLineWidth(2.0f);
     GL11.glEnable(2848);
     GL11.glEnable(2960);
     GL11.glClear(1024);
     GL11.glClearStencil(15);
     GL11.glStencilFunc(512, 1, 15);
     GL11.glStencilOp(7681, 7681, 7681);
     GL11.glPolygonMode(1028, 6913);
 }
 
 public static void renderTwo() {
     GL11.glStencilFunc(512, 0, 15);
     GL11.glStencilOp(7681, 7681, 7681);
     GL11.glPolygonMode(1028, 6914);
 }
 
 public static void renderThree() {
     GL11.glStencilFunc(514, 1, 15);
     GL11.glStencilOp(7680, 7680, 7680);
     GL11.glPolygonMode(1028, 6913);
 }
 
 public static void renderFour(final Minecraft mc, final Entity renderEntity) {
     float[] color = { 0.0f, 0.9f, 0.0f };
     if (renderEntity instanceof EntityLivingBase) {
         final EntityLivingBase distance = (EntityLivingBase)renderEntity;
         final float distance2 = mc.thePlayer.getDistanceToEntity(distance);
         if (distance instanceof EntityPlayer) {
             color = new float[] { 0.3f, 0.7f, 1.0f };
         }
         else if (distance.isInvisibleToPlayer(mc.thePlayer)) {
             color = new float[] { 1.0f, 0.9f, 0.0f };
         }
         else if (distance.hurtTime > 0) {
             color = new float[] { 1.0f, 0.66f, 0.0f };
         }
         else if (distance2 <= 3.9f) {
             color = new float[] { 0.9f, 0.0f, 0.0f };
         }
     }
     else {
         final float distance3 = mc.thePlayer.getDistanceToEntity(renderEntity);
         if (renderEntity.isInvisibleToPlayer(mc.thePlayer)) {
             color = new float[] { 1.0f, 0.9f, 0.0f };
         }
         else if (distance3 <= 3.9f) {
             color = new float[] { 0.9f, 0.0f, 0.0f };
         }
     }
     GlStateManager.color(color[0], color[1], color[2], 1.0f);
     renderFour();
 }
 
 public static void renderFour() {
     GL11.glDepthMask(false);
     GL11.glDisable(2929);
     GL11.glEnable(10754);
     GL11.glPolygonOffset(1.0f, -2000000.0f);
     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
 }
 
 public static void renderFive() {
     GL11.glPolygonOffset(1.0f, 2000000.0f);
     GL11.glDisable(10754);
     GL11.glEnable(2929);
     GL11.glDepthMask(true);
     GL11.glDisable(2960);
     GL11.glDisable(2848);
     GL11.glHint(3154, 4352);
     GL11.glEnable(3042);
     GL11.glEnable(2896);
     GL11.glEnable(3553);
     GL11.glEnable(3008);
     GL11.glPopAttrib();
 }
 
 public static void drawCoolLines(final AxisAlignedBB mask) {
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
 
 public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
     drawRect(x, y, x2, y2, col2);
     final float f = (col1 >> 24 & 0xFF) / 255.0f;
     final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
     final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
     final float f4 = (col1 & 0xFF) / 255.0f;
     GL11.glEnable(3042);
     GL11.glDisable(3553);
     GL11.glBlendFunc(770, 771);
     GL11.glEnable(2848);
     GL11.glPushMatrix();
     GL11.glColor4f(f2, f3, f4, f);
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
 
 public static void drawBorders(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
     drawRect(x, y, x2, y2, col2);
     final float f = (col1 >> 24 & 0xFF) / 255.0f;
     final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
     final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
     final float f4 = (col1 & 0xFF) / 255.0f;
     GL11.glEnable(3042);
     GL11.glDisable(3553);
     GL11.glBlendFunc(770, 771);
     GL11.glEnable(2848);
     GL11.glPushMatrix();
     GL11.glColor4f(f2, f3, f4, f);
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
 
 public static void drawBorderedCorneredRect(final float x, final float y, final float x2, final float y2, final float lineWidth, final int lineColor, final int bgColor) {
     drawRect(x, y, x2, y2, bgColor);
     final float f = (lineColor >> 24 & 0xFF) / 255.0f;
     final float f2 = (lineColor >> 16 & 0xFF) / 255.0f;
     final float f3 = (lineColor >> 8 & 0xFF) / 255.0f;
     final float f4 = (lineColor & 0xFF) / 255.0f;
     drawRect(x - 1.0f, y - 1.0f, x2 + 1.0f, y, lineColor);
     drawRect(x - 1.0f, y, x, y2, lineColor);
     drawRect(x - 1.0f, y2, x2 + 1.0f, y2 + 1.0f, lineColor);
     drawRect(x2, y, x2 + 1.0f, y2, lineColor);
 }
 
 public static double interp(final double from, final double to, final double pct) {
     return from + (to - from) * pct;
 }
 
 public static double interpPlayerX() {
     return interp(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().timer.renderPartialTicks);
 }
 
 public static double interpPlayerY() {
     return interp(Minecraft.getMinecraft().thePlayer.lastTickPosY, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().timer.renderPartialTicks);
 }
 
 public static double interpPlayerZ() {
     return interp(Minecraft.getMinecraft().thePlayer.lastTickPosZ, Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().timer.renderPartialTicks);
 }
 

 
 public static void glColor(final Color color) {
     GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
 }
 
 public static void glColor(final int hex) {
     final float alpha = (hex >> 24 & 0xFF) / 255.0f;
     final float red = (hex >> 16 & 0xFF) / 255.0f;
     final float green = (hex >> 8 & 0xFF) / 255.0f;
     final float blue = (hex & 0xFF) / 255.0f;
     GL11.glColor4f(red, green, blue, alpha);
 }
 
 public static void drawGradientRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
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
 
 
 

 
 public static void drawRect(final float g, final float h, final float i, final float j, final int col1) {
     final float f = (col1 >> 24 & 0xFF) / 255.0f;
     final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
     final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
     final float f4 = (col1 & 0xFF) / 255.0f;
     GL11.glEnable(3042);
     GL11.glDisable(3553);
     GL11.glBlendFunc(770, 771);
     GL11.glEnable(2848);
     GL11.glPushMatrix();
     GL11.glColor4f(f2, f3, f4, 0.5f);
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
 
 public static void drawRectNoBlend(final float g, final float h, final float i, final float j, final int col1) {
     final float f = (col1 >> 24 & 0xFF) / 255.0f;
     final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
     final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
     final float f4 = (col1 & 0xFF) / 255.0f;
     GL11.glDisable(3553);
     GL11.glEnable(2848);
     GL11.glPushMatrix();
     GL11.glColor4f(f2, f3, f4, f);
     GL11.glBegin(7);
     GL11.glVertex2d((double)i, (double)h);
     GL11.glVertex2d((double)g, (double)h);
     GL11.glVertex2d((double)g, (double)j);
     GL11.glVertex2d((double)i, (double)j);
     GL11.glEnd();
     GL11.glPopMatrix();
     GL11.glEnable(3553);
     GL11.glDisable(2848);
 }
 public static void ProtocolEntityBox(AxisAlignedBB aa)
	{
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
		wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.maxX, aa.minY, aa.minZ);
		wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.minX, aa.minY, aa.minZ);
		wr.addVertex(aa.minX, aa.minY, aa.maxZ);
		ts.draw();
		wr.startDrawing(3);
		wr.addVertex(aa.minX, aa.maxY, aa.minZ);
		wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
		ts.draw();

	}

public static void drawFancy(final double d, final double e, final double f2, final double f3, final int paramColor) {
    final float alpha = (paramColor >> 24 & 0xFF) / 255.0f;
    final float red = (paramColor >> 16 & 0xFF) / 255.0f;
    final float green = (paramColor >> 8 & 0xFF) / 255.0f;
    final float blue = (paramColor & 0xFF) / 255.0f;
    GlStateManager.enableBlend();
    //GlStateManager.disableTextures();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GL11.glPushMatrix();
    GL11.glEnable(2848);
    GL11.glEnable(2881);
    GL11.glEnable(2832);
    GL11.glEnable(3042);
    GL11.glColor4f(red, green, blue, alpha);
    GL11.glBegin(7);
    GL11.glVertex2d(f2 + 1.300000011920929, e);
    GL11.glVertex2d(d + 1.0, e);
    GL11.glVertex2d(d - 1.300000011920929, f3);
    GL11.glVertex2d(f2 - 1.0, f3);
    GL11.glEnd();
    GL11.glDisable(2848);
    GL11.glDisable(2881);
    GL11.glDisable(2832);
    GL11.glDisable(3042);
   // GlStateManager.enableTextures();
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
}

public static void drawGradient(final double x, final double y, final double x2, final double y2, final int col1, final int col2) {
    final float f = (col1 >> 24 & 0xFF) / 255.0f;
    final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
    final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
    final float f4 = (col1 & 0xFF) / 255.0f;
    final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
    final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
    final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
    final float f8 = (col2 & 0xFF) / 255.0f;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glVertex2d(x2, y);
    GL11.glVertex2d(x, y);
    GL11.glColor4f(f6, f7, f8, f5);
    GL11.glVertex2d(x, y2);
    GL11.glVertex2d(x2, y2);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
}

public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
    final float f = (col1 >> 24 & 0xFF) / 255.0f;
    final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
    final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
    final float f4 = (col1 & 0xFF) / 255.0f;
    final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
    final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
    final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
    final float f8 = (col2 & 0xFF) / 255.0f;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glVertex2d(left, top);
    GL11.glVertex2d(left, bottom);
    GL11.glColor4f(f6, f7, f8, f5);
    GL11.glVertex2d(right, bottom);
    GL11.glVertex2d(right, top);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
}

public static void rectangle(double left, double top, double right, double bottom, final int color) {
    if (left < right) {
        final double var5 = left;
        left = right;
        right = var5;
    }
    if (top < bottom) {
        final double var5 = top;
        top = bottom;
        bottom = var5;
    }
    final float var6 = (color >> 24 & 0xFF) / 255.0f;
    final float var7 = (color >> 16 & 0xFF) / 255.0f;
    final float var8 = (color >> 8 & 0xFF) / 255.0f;
    final float var9 = (color & 0xFF) / 255.0f;
    final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
    GlStateManager.enableBlend();
    //GlStateManager.disableTextures();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color(var7, var8, var9, var6);
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(left, bottom, 0.0);
    worldRenderer.addVertex(right, bottom, 0.0);
    worldRenderer.addVertex(right, top, 0.0);
    worldRenderer.addVertex(left, top, 0.0);
    Tessellator.getInstance().draw();
   // GlStateManager.enableTextures();
    GlStateManager.disableBlend();
}

public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
    rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
    rectangle(x + width, y, x1 - width, y + width, borderColor);
    rectangle(x, y, x + width, y1, borderColor);
    rectangle(x1 - width, y, x1, y1, borderColor);
    rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
}

public static void filledBox(final AxisAlignedBB boundingBox, final int color, final boolean shouldColor) {
    GlStateManager.pushMatrix();
    final float var11 = (color >> 24 & 0xFF) / 255.0f;
    final float var12 = (color >> 16 & 0xFF) / 255.0f;
    final float var13 = (color >> 8 & 0xFF) / 255.0f;
    final float var14 = (color & 0xFF) / 255.0f;
    final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
    if (shouldColor) {
        GlStateManager.color(var12, var13, var14, var11);
    }
    final byte draw = 7;
    worldRenderer.startDrawing((int)draw);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
    Tessellator.getInstance().draw();
    worldRenderer.startDrawing((int)draw);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
    Tessellator.getInstance().draw();
    worldRenderer.startDrawing((int)draw);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
    Tessellator.getInstance().draw();
    worldRenderer.startDrawing((int)draw);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
    Tessellator.getInstance().draw();
    worldRenderer.startDrawing((int)draw);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
    worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
    Tessellator.getInstance().draw();
    worldRenderer.startDrawing((int)draw);
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
    GlStateManager.popMatrix();
}

public static void drawOutlinedBoundingBox(final AxisAlignedBB boundingBox) {
    final Tessellator var1 = Tessellator.getInstance();
    final WorldRenderer var2 = var1.getWorldRenderer();
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
}

public static void drawBoundingBox(final AxisAlignedBB axisalignedbb) {
    final Tessellator tessellator = Tessellator.getInstance();
    final WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
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

public static void drawLine3D(final float x, final float y, final float z, final float x1, final float y1, final float z1, final int color) {
    RenderHelper.pre3D();
    GL11.glLoadIdentity();
    Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
    final float var11 = (color >> 24 & 0xFF) / 255.0f;
    final float var12 = (color >> 16 & 0xFF) / 255.0f;
    final float var13 = (color >> 8 & 0xFF) / 255.0f;
    final float var14 = (color & 0xFF) / 255.0f;
    GL11.glColor4f(var12, var13, var14, var11);
    GL11.glLineWidth(0.5f);
    GL11.glBegin(3);
    GL11.glVertex3d((double)x, (double)y, (double)z);
    GL11.glVertex3d((double)x1, (double)y1, (double)z1);
    GL11.glEnd();
    RenderHelper.post3D();
}

public static void draw3DLine(final float x, final float y, final float z, final int color) {
    RenderHelper.pre3D();
    GL11.glLoadIdentity();
    Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
    final float var11 = (color >> 24 & 0xFF) / 255.0f;
    final float var12 = (color >> 16 & 0xFF) / 255.0f;
    final float var13 = (color >> 8 & 0xFF) / 255.0f;
    final float var14 = (color & 0xFF) / 255.0f;
    GL11.glColor4f(var12, var13, var14, var11);
    GL11.glLineWidth(0.5f);
    GL11.glBegin(3);
    GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
    GL11.glVertex3d((double)x, (double)y, (double)z);
    GL11.glEnd();
    RenderHelper.post3D();
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
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
}

public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
    final float red = 0.003921569f * redRGB;
    final float green = 0.003921569f * greenRGB;
    final float blue = 0.003921569f * blueRGB;
    GL11.glColor4f(red, green, blue, alpha);
}

public static void drawRect(final float x, final float y, final float x1, final float y1) {
    GL11.glBegin(7);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
}

public static void drawInternalRoundedRect3( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
  	 RenderHelper.enableGL2D();
  	    x *= 2.0F;
  	    y *= 2.0F;
  	    x1 *= 2.0F;
  	    y1 *= 2.0F;
  	    GL11.glScalef(0.5F, 0.5F, 0.5F);
  	    RenderHelper.drawVLine(x, y + 4.0F, y1 - 5.0F, borderC);
  	    RenderHelper.drawVLine(x1 - 1.0F, y + 4.0F, y1 - 5.0F, borderC);
  	    RenderHelper.drawHLine(x + 5.0F, x1 - 6.0F, y, borderC);
  	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
  	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
  	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
  	    //GlStateManager.enableAlpha();
  	    //TOP RIGHT
  	    RenderHelper.drawHLine(x1 - 4, x1 - 4.0F, y, insideC);
  	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y + 3, insideC);
  	    
  	    //TOP LEFT
  	    RenderHelper.drawHLine(x + 3.0F, x + 3.0F, y, insideC);
  	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y + 3, insideC);
  	    
  	    //BOTTOM LEFT
  	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y1 - 4, insideC);
  	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y1 - 0, insideC);
  	    
  	  //BOTTOM RIGHT
  	   RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y1 - 4, insideC);
  	    RenderHelper.drawHLine(x1 - 4, x1 - 4.0F, y1 - 1, insideC);
  	    
  	    RenderHelper.drawHLine(x + 5.0F, x1 - 6.0F, y1 - 1.0F, borderC);
  	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
  	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
  	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
  	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
  	    RenderHelper.drawGlColorRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
  	    GL11.glScalef(2.0F, 2.0F, 2.0F);
  	    RenderHelper.disableGL2D();
  }

public static void drawInternalRoundedRect2( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
  	 RenderHelper.enableGL2D();
  	    x *= 2.0F;
  	    y *= 2.0F;
  	    x1 *= 2.0F;
  	    y1 *= 2.0F;
  	    GL11.glScalef(0.5F, 0.5F, 0.5F);
 	   RenderHelper.drawVLine(x, y + 5.5F, y1 - 6.5F, borderC);
  	    RenderHelper.drawVLine(x + 2, y + 3F, y1 - 4F, borderC);
  	    
  	  //drawVLine(x1, y + 5.5F, y1 - 6.5F, borderC);
	    RenderHelper.drawVLine(x1 - 3, y + 3F, y1 - 4F, borderC);
  	    
  	    RenderHelper.drawVLine(x1 - 1.0F, y + 5.5F, y1 - 6.5F, borderC);
  	    
  	    RenderHelper.drawHLine(x + 6.5F, x1 - 7.5F, y, borderC);
  	    RenderHelper.drawHLine(x + 4F, x1 - 4.5F, y + 2, borderC);
  	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
  	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
  	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
  	    //GlStateManager.enableAlpha();
  	    //TOP RIGHT
  	    RenderHelper.drawHLine(x1 - 5.5f, x1 - 6.5F, y, insideC);
  	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y + 5.5f, insideC);
  	    
  	    //TOP LEFT
  	    RenderHelper.drawHLine(x + 4.5F, x + 5.5F, y, insideC);
  	    RenderHelper.drawVLine(x + 0.0F, y + 3.5f, y + 6.5f, insideC);
  	    
  	    //BOTTOM LEFT
  	//    drawVLine(x + 1.5F, y1 - 4f, y1 - 7.5f, borderC);
  	 //   drawHLine(x + 4.5F, x + 5.5F, y1  - 2.5f, borderC);
  	    
  	  RenderHelper.drawVLine(x + 0F, y1 - 7.5f, y1 - 5f, insideC);
    RenderHelper.drawVLine(x + 5.5F, y1 - 2.5f, y1 - 0.0f, insideC);
  	    
  	  //BOTTOM RIGHT
  	   RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y1 - 6.5f, insideC);
  	    RenderHelper.drawHLine(x1 - 5.5f, x1 - 6.5F, y1 - 1, insideC);
  	    
  	  RenderHelper.drawHLine(x + 4F, x1 - 4.5F, y1 - 2.5F, borderC);
  	    RenderHelper.drawHLine(x + 6.5F, x1 - 8.0F, y1 - 1.0F, borderC);
  	    
  	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
  	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
  	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
  	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
  	    
  	    
  	    RenderHelper.drawGlColorRect(x + 3F, y + 3F, x1 - 3F, y1 - 3F, borderC);
  	    GL11.glScalef(2.0F, 2.0F, 2.0F);
  	    RenderHelper.disableGL2D();
  }

public static void drawInternalRoundedRect1( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
   	 RenderHelper.enableGL2D();
   	    x *= 2.0F;
   	    y *= 2.0F;
   	    x1 *= 2.0F;
   	    y1 *= 2.0F;
   	    GL11.glScalef(0.5F, 0.5F, 0.5F);
   	    RenderHelper.drawVLine(x, y + 4.0F, y1 - 5.0F, borderC);
   	    RenderHelper.drawVLine(x1 - 1.0F, y + 4.0F, y1 - 5.0F, borderC);
   	    RenderHelper.drawHLine(x + 5.0F, x1 - 6.0F, y, borderC);
   	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
   	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
   	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
   	    //GlStateManager.enableAlpha();
   	    //TOP RIGHT
   	    RenderHelper.drawHLine(x1 - 4, x1 - 4.0F, y, insideC);
   	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y + 3, insideC);
   	    
   	    //TOP LEFT
   	    RenderHelper.drawHLine(x + 3.0F, x + 3.0F, y, insideC);
   	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y + 3, insideC);
   	    
   	    //BOTTOM LEFT
   	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y1 - 4, insideC);
   	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y1 - 0, insideC);
   	    
   	  //BOTTOM RIGHT
   	   RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y1 - 4, insideC);
   	    RenderHelper.drawHLine(x1 - 4, x1 - 4.0F, y1 - 1, insideC);
   	    
   	    RenderHelper.drawHLine(x + 5.0F, x1 - 6.0F, y1 - 1.0F, borderC);
   	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
   	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
   	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
   	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
   	    RenderHelper.drawGlColorRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
   	    GL11.glScalef(2.0F, 2.0F, 2.0F);
   	    RenderHelper.disableGL2D();
   }

public static void drawRoundedShadow( float x,  float y, float x1,  float y1) {
  	 RenderHelper.enableGL2D();
  	    x *= 2.0F;
  	    y *= 2.0F;
  	    x1 *= 2.0F;
  	    y1 *= 2.0F;
  	    GL11.glScalef(0.5F, 0.5F, 0.5F);
  	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
  	    drawInternalRoundedRect2(x - 02.5F, y-01.6F, x1+2.6F, y1+2.5F, 0x30505050, 0x10505050);
  	    drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0x50505050, 0x30606060);
  	    drawInternalRoundedRect2(x - 0.5F, y-0.6F, x1+0.6F, y1+0.5F, 0x60505050, 0x50505050);
  	   
  	    GL11.glScalef(2.0F, 2.0F, 2.0F);
  	    RenderHelper.disableGL2D();
  }

public static void drawRoundedRectWithShadow( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
   	 RenderHelper.enableGL2D();
   	    x *= 2.0F;
   	    y *= 2.0F;
   	    x1 *= 2.0F;
   	    y1 *= 2.0F;
   	    GL11.glScalef(0.5F, 0.5F, 0.5F);
   	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
   	    drawInternalRoundedRect2(x - 02.5F, y-01.6F, x1+2.6F, y1+2.5F, 0x30505050, 0x10505050);
   	    drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0x50505050, 0x30606060);
   	    drawInternalRoundedRect2(x - 0.5F, y-0.6F, x1+0.6F, y1+0.5F, 0x60505050, 0x50505050);
   	    RenderHelper.drawVLine(x, y + 2.0F, y1 - 3.0F, borderC);
   	    RenderHelper.drawVLine(x1 - 1.0F, y + 2.0F, y1 - 3.0F, borderC);
   	    RenderHelper.drawHLine(x + 3.0F, x1 - 4.0F, y, borderC);
   	   // GlStateManager.enableBlend();
   		//  GlStateManager.disableAlpha();
   	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
   	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
   	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
   		 
   	    //GlStateManager.enableAlpha();
   	    //TOP RIGHT
   	    RenderHelper.drawHLine(x1 - 3, x1 - 3.0F, y, insideC);
   	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y + 2, insideC);
   	    
   	    //TOP LEFT
   	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y, insideC);
   	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y + 2, insideC);
   	    
   	    //BOTTOM LEFT
   	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y1 - 3, insideC);
   	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y1 - 1, insideC);
   	    
   	  //BOTTOM RIGHT
   	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y1 - 3, insideC);
   	    RenderHelper.drawHLine(x1 - 3, x1 - 3.0F, y1 - 1, insideC);
   	    
   	    RenderHelper.drawHLine(x + 3.0F, x1 - 4.0F, y1 - 1.0F, borderC);
   	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
   	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
   	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
   	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
   	    RenderHelper.drawGlColorRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
   	    GL11.glScalef(2.0F, 2.0F, 2.0F);
   	    RenderHelper.disableGL2D();
   }

public static void drawRoundedRect( float x,  float y, float x1,  float y1,  int borderC,  int insideC) {
	 RenderHelper.enableGL2D();
	    x *= 2.0F;
	    y *= 2.0F;
	    x1 *= 2.0F;
	    y1 *= 2.0F;
	    GL11.glScalef(0.5F, 0.5F, 0.5F);
	    //drawInternalRoundedRect2(x - 01.5F, y-01.6F, x1+1.6F, y1+1.5F, 0xff606060, 0x90606060);
	   RenderHelper.drawVLine(x, y + 2.0F, y1 - 3.0F, borderC);
	    RenderHelper.drawVLine(x1 - 1.0F, y + 2.0F, y1 - 3.0F, borderC);
	    RenderHelper.drawHLine(x + 3.0F, x1 - 4.0F, y, borderC);
	   // GlStateManager.enableBlend();
		//  GlStateManager.disableAlpha();
	   /* drawHLine(x + 3.0F, x1 - 4.0F, y1, 0xaaafafaf);
	    drawHLine(x + 4.0F, x1 - 5.0F, y1+0.5f, 0x80cfcfcf);
	    drawHLine(x + 6.0F, x1 - 7.0F, y1+1, 0x20e7e7e7);*/
		 
	    //GlStateManager.enableAlpha();
	    //TOP RIGHT
	    RenderHelper.drawHLine(x1 - 3, x1 - 3.0F, y, insideC);
	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y + 2, insideC);
	    
	    //TOP LEFT
	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y, insideC);
	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y + 2, insideC);
	    
	    //BOTTOM LEFT
	    RenderHelper.drawHLine(x + 0.0F, x + 0.0F, y1 - 3, insideC);
	    RenderHelper.drawHLine(x + 2.0F, x + 2.0F, y1 - 1, insideC);
	    
	  //BOTTOM RIGHT
	    RenderHelper.drawHLine(x1 - 1, x1 - 1.0F, y1 - 3, insideC);
	    RenderHelper.drawHLine(x1 - 3, x1 - 3.0F, y1 - 1, insideC);
	    
	    RenderHelper.drawHLine(x + 3.0F, x1 - 4.0F, y1 - 1.0F, borderC);
	    //drawHLine(x + 2.0F, x + 1.0F, y + 1.0F, borderC);
	    //drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
	   // drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
	   // drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
	    RenderHelper.drawGlColorRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, borderC);
	    GL11.glScalef(2.0F, 2.0F, 2.0F);
	    RenderHelper.disableGL2D();
}

public static void drawVLine(float x, float y, float x1, int y1)
{
  if (x1 < y)
  {
    float var5 = y;
    y = x1;
    x1 = var5;
  }
  RenderHelper.drawGlColorRect(x, y + 1.0F, x + 1.0F, x1, y1);
}

public static void drawHLine(float x, float y, final float x1, final int y1) {
    if (y < x) {
        final float var5 = x;
        x = y;
        y = var5;
    }
    RenderHelper.drawGlColorRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
}

public static void drawVLine(final float x, final float y, final float x1, final float y1, final float width, final int color) {
    if (width <= 0.0f) {
        return;
    }
    GL11.glPushMatrix();
    pre3D();
    final float var11 = (color >> 24 & 0xFF) / 255.0f;
    final float var12 = (color >> 16 & 0xFF) / 255.0f;
    final float var13 = (color >> 8 & 0xFF) / 255.0f;
    final float var14 = (color & 0xFF) / 255.0f;
    GlStateManager.enableAlpha();
    GlStateManager.enableBlend();
    final int shade = GL11.glGetInteger(2900);
    GlStateManager.shadeModel(7425);
    GL11.glColor4f(var12, var13, var14, var11);
    final float line = GL11.glGetFloat(2849);
    GL11.glLineWidth(width);
    GL11.glBegin(3);
    GL11.glVertex3d((double)x, (double)y, 0.0);
    GL11.glVertex3d((double)x1, (double)y1, 0.0);
    GL11.glEnd();
    GlStateManager.shadeModel(shade);
    GL11.glLineWidth(line);
    post3D();
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
    //GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
}

public static void DrawFilledCircle(float x, float y, float radius, int fillcolor)
{
	GL11.glShadeModel(GL11.GL_SMOOTH);
    GL11.glBegin(GL11.GL_TRIANGLE_FAN);
    GL11.glColor4f(1,1,1,1);
    float y1 = y;
    float x1 = x;

    for (int i = 0; i <= 360; i++)
    {
        float degInRad = i * 5;
        float x2 = x + ((float)Math.cos(degInRad) * radius);
        float y2 = y + ((float)Math.sin(degInRad) * radius);                
        GL11.glVertex2f(x,y);                
        GL11.glVertex2f(x1,y1);                
        GL11.glVertex2f(x2,y2);
	    y1=y2;
	    x1=x2;
    }
    GL11.glEnd();
}

public static void DrawCircle(float x, float y, float radius, int color)
{
	GL11.glBegin(GL11.GL_LINE_LOOP);
	GL11.glColor4f(1,1,1,1);
    
    for (int i = 0; i <= 360; i++)
    {
        float degInRad = i * 5;                
        GL11.glVertex2f(x + ((float)Math.cos(degInRad) * radius), y + ((float)Math.sin(degInRad) * radius));
    }

    GL11.glEnd();
}

public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
    GL11.glPushMatrix();
    cx *= 2.0f;
    cy *= 2.0f;
    final float f = (c >> 24 & 0xFF) / 255.0f;
    final float f2 = (c >> 16 & 0xFF) / 255.0f;
    final float f3 = (c >> 8 & 0xFF) / 255.0f;
    final float f4 = (c & 0xFF) / 255.0f;
    final float theta = (float)(6.2831852 / num_segments);
    final float p = (float)Math.cos(theta);
    final float s = (float)Math.sin(theta);
    float x;
    r = (x = r * 2.0f);
    float y = 0.0f;
    enableGL2D();
    GL11.glScalef(0.5f, 0.5f, 0.5f);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(2);
    for (int ii = 0; ii < num_segments; ++ii) {
        GL11.glVertex2f(x + cx, y + cy);
        final float t = x;
        x = p * x - s * y;
        y = s * t + p * y;
    }
    GL11.glEnd();
    GL11.glScalef(2.0f, 2.0f, 2.0f);
    disableGL2D();
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GL11.glPopMatrix();
}

public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
    //  GL11.glEnable((int)3042);
      GL11.glDisable((int)3553);
      GL11.glBlendFunc((int)770, (int)771);
      GL11.glEnable((int)2848);
      GL11.glPushMatrix();
      float scale = 0.1f;
      GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
      RenderHelper.drawCircleDuplicate(x *= 10, y *= 10, radius *= 10.0f, insideC);
     // drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
      GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
      GL11.glPopMatrix();
      GL11.glEnable((int)3553);
    //  GL11.glDisable((int)3042);
      GL11.glDisable((int)2848);
  }

public static void drawUnfilledCircle(double x, double y, float radius, float lineWidth, int color) {
    float alpha = (float)(color >> 24 & 255) / 255.0f;
    float red = (float)(color >> 16 & 255) / 255.0f;
    float green = (float)(color >> 8 & 255) / 255.0f;
    float blue = (float)(color & 255) / 255.0f;
    GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    GL11.glLineWidth((float)lineWidth);
    GL11.glEnable((int)2848);
    GL11.glBegin((int)2);
    int i = 0;
    while (i <= 360) {
        GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        ++i;
    }
    GL11.glEnd();
    GL11.glDisable((int)2848);
}

public static void drawBorderedCircle(final float circleX, final float circleY, final double radius, final double width, final int borderColor, final int innerColor) {
    enableGL2D();
    GlStateManager.enableBlend();
    GL11.glEnable(2881);
    drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
    RenderHelper.drawFullCircle(circleX, circleY, (float)radius, innerColor);
    GlStateManager.disableBlend();
    GL11.glDisable(2881);
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    disableGL2D();
}

public static void drawCircleNew(final float x, final float y, final float radius, final int numberOfSides) {
    final float z = 0.0f;
    final int numberOfVertices = numberOfSides + 2;
    final float doublePi = 6.2831855f;
}

public static void drawFullCircle(float cx, float cy, float r, final int c) {
    r *= 2.0f;
    cx *= 2.0f;
    cy *= 2.0f;
    final float theta = 0.19634953f;
    final float p = (float)Math.cos(theta);
    final float s = (float)Math.sin(theta);
    float x = r;
    float y = 0.0f;
    enableGL2D();
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glEnable(3024);
    GL11.glScalef(0.5f, 0.5f, 0.5f);
    RenderHelper.glColorHex(c);
    GL11.glBegin(9);
    for (int ii = 0; ii < 32; ++ii) {
        GL11.glVertex2f(x + cx, y + cy);
        float t = x;
        x = p * x - s * y;
        y = s * t + p * y;
    }
    GL11.glEnd();
    GL11.glScalef(2.0f, 2.0f, 2.0f);
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    disableGL2D();
}

public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
		float alpha) {
	GL11.glPushMatrix();
	GL11.glEnable(GL11.GL_BLEND);
	GL11.glBlendFunc(770, 771);
	GL11.glDisable(GL11.GL_TEXTURE_2D);
	GL11.glEnable(GL11.GL_LINE_SMOOTH);
	GL11.glDisable(GL11.GL_DEPTH_TEST);
	GL11.glDepthMask(false);
	GL11.glColor4f(red, green, blue, alpha);
	drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
	GL11.glDisable(GL11.GL_LINE_SMOOTH);
	GL11.glEnable(GL11.GL_TEXTURE_2D);
	GL11.glEnable(GL11.GL_DEPTH_TEST);
	GL11.glDepthMask(true);
	GL11.glDisable(GL11.GL_BLEND);
	GL11.glPopMatrix();
}

public static void drawLines(final AxisAlignedBB boundingBox) {
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

public static void drawGlColorRect(final float x, final float y, final float x1, final float y1, final int color) {
    enableGL2D();
    glColor(color);
    drawRect(x, y, x1, y1);
    disableGL2D();
}

public static void drawCircleDuplicate(double x, double y, float radius, int color) {
    float alpha = (float)(color >> 24 & 255) / 255.0f;
    float red = (float)(color >> 16 & 255) / 255.0f;
    float green = (float)(color >> 8 & 255) / 255.0f;
    float blue = (float)(color & 255) / 255.0f;
    GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    GL11.glBegin((int)9);
    int i = 0;
    while (i <= 360) {
        GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        ++i;
    }
    GL11.glEnd();
}

public static void glColorHex(final int hex) {
    final float alpha = (hex >> 24 & 0xFF) / 255.0f;
    final float red = (hex >> 16 & 0xFF) / 255.0f;
    final float green = (hex >> 8 & 0xFF) / 255.0f;
    final float blue = (hex & 0xFF) / 255.0f;
    GL11.glColor4f(red, green, blue, alpha);
}

 
 
}
