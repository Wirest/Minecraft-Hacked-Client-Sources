package org.m0jang.crystal.Utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Font.Fonts;
import shadersmod.client.Shaders;

public class RenderUtils {
   public static void renderOne() {
      checkSetupFBO();
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(3.0F);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void checkSetupFBO() {
      Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
      if (fbo != null && fbo.depthBuffer > -1) {
         setupFBO(fbo);
         fbo.depthBuffer = -1;
      }

   }

   public static void setupFBO(Framebuffer fbo) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
      int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
      Minecraft.getMinecraft();
      int var10002 = Minecraft.displayWidth;
      Minecraft.getMinecraft();
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, var10002, Minecraft.displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
   }

   public static void renderTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6914);
   }

   public static void renderThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1032, 6913);
   }

   public static void renderFour(EntityLivingBase base) {
      setColor(getTeamColor(base));
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public static void renderFour(int color) {
      setColor(color);
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public static int getTeamColor(Entity player) {
      int var2 = 16777215;
      if (player instanceof EntityPlayer) {
         ScorePlayerTeam var6 = (ScorePlayerTeam)((EntityPlayer)player).getTeam();
         if (var6 != null) {
            String var7 = FontRenderer.getFormatFromString(var6.getColorPrefix());
            if (var7.length() >= 2) {
               var2 = Wrapper.mc.fontRendererObj.getColorCode(var7.charAt(1));
            }
         }
      }

      return var2;
   }

   public static void renderFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
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

   public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glLineWidth(1.0F);
      setColor(color);
      GL11.glBegin(1);
      GL11.glVertex3d(x1, y1, z1);
      GL11.glVertex3d(x2, y2, z2);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void enableGL3D(float lineWidth) {
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      Shaders.disableLightmap();
      Shaders.disableFog();
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(lineWidth);
   }

   public static void disableGL3D() {
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDepthMask(true);
      GL11.glCullFace(1029);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawRect(float x, float y, float x1, float y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
      enableGL2D();
      GL11.glColor4f(r, g, b, a);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawRect(float x, float y, float x1, float y1) {
      GL11.glBegin(7);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
   }

   public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
      enableGL2D();
      drawRect(x, y, x1, y1, inside);
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

   public static void beginCrop(float x, float y, float width, float height) {
      int scaleFactor = getScaleFactor();
      GL11.glEnable(3089);
      GL11.glScissor((int)(x * (float)scaleFactor), (int)((float)Display.getHeight() - y * (float)scaleFactor), (int)(width * (float)scaleFactor), (int)(height * (float)scaleFactor));
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

   public static void endCrop() {
      GL11.glDisable(3089);
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

   public static void drawTracerLine(double x, double y, double z, int color, float alpha, float lineWdith) {
      GL11.glPushMatrix();
      enableGL3D(lineWdith);
      setColor(color);
      GL11.glBegin(2);
      Minecraft.getMinecraft();
      GL11.glVertex3d(0.0D, (double)Minecraft.thePlayer.getEyeHeight(), 0.0D);
      GL11.glVertex3d(x, y, z);
      GL11.glEnd();
      disableGL3D();
      GL11.glPopMatrix();
   }

   public static void drawBorderedRect(int x, int y, int x1, int y1, int width, Color internalColor, Color borderColor) {
      Gui.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor.getRGB());
      Gui.drawRect(x + width, y, x1 - width, y + width, borderColor.getRGB());
      Gui.drawRect(x, y, x + width, y1, borderColor.getRGB());
      Gui.drawRect(x1 - width, y, x1, y1, borderColor.getRGB());
      Gui.drawRect(x + width, y1 - width, x1 - width, y1, borderColor.getRGB());
   }

   public static void renderNameTag(String name, Entity entity, int borderWidth, double scale, int color) {
      RenderManager renderManager = Minecraft.getMinecraft().renderManager;
      int stringWidth = Fonts.segoe18.getStringWidth(name);
      Minecraft.getMinecraft();
      double distance = (double)Minecraft.thePlayer.getDistanceToEntity(entity);
      GL11.glPushMatrix();
      enableRender3D(true);
      double[] coords = EntityUtils.interpolate(entity);
      double x = coords[0] - RenderManager.renderPosX;
      double y = coords[1] - RenderManager.renderPosY + (double)entity.height + 0.5D;
      double z = coords[2] - RenderManager.renderPosZ;
      GL11.glTranslated(x, y, z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glScaled(-0.027D, -0.027D, 0.027D);
      double scaleSize = 10.0D - scale;
      if (distance > scaleSize) {
         GL11.glScaled(distance / scaleSize, distance / scaleSize, distance / scaleSize);
      }

      boolean resizeNametag = true;
      if (resizeNametag) {
         double x1 = entity.posX;
         double y1 = entity.posY;
         double z1 = entity.posZ;
         Minecraft.getMinecraft();
         double diffX = x1 - Minecraft.thePlayer.posX;
         Minecraft.getMinecraft();
         double diffY = y1 - Minecraft.thePlayer.posY;
         Minecraft.getMinecraft();
         double diffZ = z1 - Minecraft.thePlayer.posZ;
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, distance) * 180.0D / 3.141592653589793D));
         Minecraft.getMinecraft();
         float diffYaw = Math.abs(MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw));
         Minecraft.getMinecraft();
         float diffPitch = Math.abs(MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch));
         float factor = (float)((75.0D - Math.sqrt((double)(diffYaw * diffYaw + diffPitch * diffPitch))) / 50.0D);
         if (factor > 1.0F) {
            factor = 1.0F;
         }

         if (factor < 0.0F) {
            factor = 0.0F;
         }

         GL11.glScaled((double)factor, (double)factor, (double)factor);
      }

      GL11.glRotatef(RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
      GL11.glBegin(7);
      GL11.glVertex3d((double)(-stringWidth / 2 - 2), -2.0D, 0.0D);
      GL11.glVertex3d((double)(-stringWidth / 2 - 2), 9.0D, 0.0D);
      GL11.glVertex3d((double)(stringWidth / 2 + 2), 9.0D, 0.0D);
      GL11.glVertex3d((double)(stringWidth / 2 + 2), -2.0D, 0.0D);
      GL11.glEnd();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      Fonts.segoe18.drawString(name, (float)(-stringWidth / 2), 0.0F, color);
      GL11.glDisable(3553);
      disableRender3D(true);
      GL11.glPopMatrix();
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

   public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
      float red = 0.003921569F * (float)redRGB;
      float green = 0.003921569F * (float)greenRGB;
      float blue = 0.003921569F * (float)blueRGB;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawBorderRect(int left, int top, int right, int bottom, int bcolor, int icolor, int bwidth) {
      Gui.drawRect(left + bwidth, top + bwidth, right - bwidth, bottom - bwidth, icolor);
      Gui.drawRect(left, top + 1, left + bwidth, bottom - 1, bcolor);
      Gui.drawRect(left + bwidth, top, right, top + bwidth, bcolor);
      Gui.drawRect(left + bwidth, bottom - bwidth, right, bottom, bcolor);
      Gui.drawRect(right - bwidth, top + bwidth, right, bottom - bwidth, bcolor);
   }

   public static void drawRectZZ(double x, double y, double x1, double y1) {
      GL11.glBegin(7);
      GL11.glVertex2f((float)x, (float)y1);
      GL11.glVertex2f((float)x1, (float)y1);
      GL11.glVertex2f((float)x1, (float)y);
      GL11.glVertex2f((float)x, (float)y);
      GL11.glEnd();
   }

   public static int transparency(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.startDrawing(3);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      tessellator.draw();
      worldRenderer.startDrawing(3);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      tessellator.draw();
      worldRenderer.startDrawing(1);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      tessellator.draw();
   }

   public static void drawBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      tessellator.draw();
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
      worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
      worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
      worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
      tessellator.draw();
   }

   public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawBlockESP(double x, double y, double z, int maincoolor, int borderColor, float lineWidth) {
      float alpha = (float)(maincoolor >> 24 & 255) / 255.0F;
      float red = (float)(maincoolor >> 16 & 255) / 255.0F;
      float green = (float)(maincoolor >> 8 & 255) / 255.0F;
      float blue = (float)(maincoolor & 255) / 255.0F;
      float lineAlpha = (float)(borderColor >> 24 & 255) / 255.0F;
      float lineRed = (float)(borderColor >> 16 & 255) / 255.0F;
      float lineGreen = (float)(borderColor >> 8 & 255) / 255.0F;
      float lineBlue = (float)(borderColor & 255) / 255.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawFilledBBESP(AxisAlignedBB axisalignedbb, int color) {
      GL11.glPushMatrix();
      float red = (float)(color >> 24 & 255) / 255.0F;
      float green = (float)(color >> 16 & 255) / 255.0F;
      float blue = (float)(color >> 8 & 255) / 255.0F;
      float alpha = (float)(color & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawFilledBox(axisalignedbb);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawFilledBox(AxisAlignedBB boundingBox) {
      if (boundingBox != null) {
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glEnd();
         GL11.glBegin(7);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
         GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
         GL11.glEnd();
      }
   }

   public static void drawBoundingBoxESP(AxisAlignedBB axisalignedbb, float width, int color) {
      GL11.glPushMatrix();
      float red = (float)(color >> 24 & 255) / 255.0F;
      float green = (float)(color >> 16 & 255) / 255.0F;
      float blue = (float)(color >> 8 & 255) / 255.0F;
      float alpha = (float)(color & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(width);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBox(axisalignedbb);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawClickTPESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y + 1.1D, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y + 1.1D, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(3.0F);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void setColor(int colorHex) {
      float alpha = (float)(colorHex >> 24 & 255) / 255.0F;
      float red = (float)(colorHex >> 16 & 255) / 255.0F;
      float green = (float)(colorHex >> 8 & 255) / 255.0F;
      float blue = (float)(colorHex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawHat(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawCircle(float x, float y, float radius, float lineWidth, int color) {
      enableGL2D();
      setColor(color);
      GL11.glLineWidth(lineWidth);
      int vertices = (int)Math.min(Math.max(radius, 45.0F), 360.0F);
      GL11.glBegin(2);

      for(int i = 0; i < vertices; ++i) {
         double angleRadians = 6.283185307179586D * (double)i / (double)vertices;
         GL11.glVertex2d((double)x + Math.sin(angleRadians) * (double)radius, (double)y + Math.cos(angleRadians) * (double)radius);
      }

      GL11.glEnd();
      disableGL2D();
   }

   public static void drawCircle(int x, int y, double r, int c) {
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f2 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f2, f3, f4, f);
      GL11.glBegin(2);

      for(int i = 0; i <= 360; ++i) {
         double x2 = Math.sin((double)i * 3.141592653589793D / 180.0D) * r;
         double y2 = Math.cos((double)i * 3.141592653589793D / 180.0D) * r;
         GL11.glVertex2d((double)x + x2, (double)y + y2);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawFilledCircle(float x, float y, float radius, int color) {
      enableGL2D();
      setColor(color);
      int vertices = (int)Math.min(Math.max(radius, 45.0F), 360.0F);
      GL11.glBegin(9);

      for(int i = 0; i < vertices; ++i) {
         double angleRadians = 6.283185307179586D * (double)i / (double)vertices;
         GL11.glVertex2d((double)x + Math.sin(angleRadians) * (double)radius, (double)y + Math.cos(angleRadians) * (double)radius);
      }

      GL11.glEnd();
      disableGL2D();
      drawCircle(x, y, radius, 1.5F, 16777215);
   }

   public static void drawFilledCircle(int x, int y, double r, int c) {
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f2 = (float)(c >> 16 & 255) / 255.0F;
      float f3 = (float)(c >> 8 & 255) / 255.0F;
      float f4 = (float)(c & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f2, f3, f4, f);
      GL11.glBegin(6);

      for(int i = 0; i <= 360; ++i) {
         double x2 = Math.sin((double)i * 3.141592653589793D / 180.0D) * r;
         double y2 = Math.cos((double)i * 3.141592653589793D / 180.0D) * r;
         GL11.glVertex2d((double)x + x2, (double)y + y2);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void dr(double i, double j, double k, double l, int i1) {
      double k2;
      if (i < k) {
         k2 = i;
         i = k;
         k = k2;
      }

      if (j < l) {
         k2 = j;
         j = l;
         l = k2;
      }

      float f = (float)(i1 >> 24 & 255) / 255.0F;
      float f2 = (float)(i1 >> 16 & 255) / 255.0F;
      float f3 = (float)(i1 >> 8 & 255) / 255.0F;
      float f4 = (float)(i1 & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f2, f3, f4, f);
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(i, l, 0.0D);
      worldRenderer.addVertex(k, l, 0.0D);
      worldRenderer.addVertex(k, j, 0.0D);
      worldRenderer.addVertex(i, j, 0.0D);
      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
      float var7 = (float)(startColor >> 24 & 255) / 255.0F;
      float var8 = (float)(startColor >> 16 & 255) / 255.0F;
      float var9 = (float)(startColor >> 8 & 255) / 255.0F;
      float var10 = (float)(startColor & 255) / 255.0F;
      float var11 = (float)(endColor >> 24 & 255) / 255.0F;
      float var12 = (float)(endColor >> 16 & 255) / 255.0F;
      float var13 = (float)(endColor >> 8 & 255) / 255.0F;
      float var14 = (float)(endColor & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator var15 = Tessellator.getInstance();
      WorldRenderer var16 = var15.getWorldRenderer();
      var16.startDrawingQuads();
      var16.func_178960_a(var8, var9, var10, var7);
      var16.addVertex(right, top, 0.0D);
      var16.addVertex(left, top, 0.0D);
      var16.func_178960_a(var12, var13, var14, var11);
      var16.addVertex(left, bottom, 0.0D);
      var16.addVertex(right, bottom, 0.0D);
      var15.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public static void drawBorderedRectZ(float left, float top, float right, float bottom, float borderWidth, int borderColor, int color) {
      float alpha = (float)(borderColor >> 24 & 255) / 255.0F;
      float red = (float)(borderColor >> 16 & 255) / 255.0F;
      float green = (float)(borderColor >> 8 & 255) / 255.0F;
      float blue = (float)(borderColor & 255) / 255.0F;
      GlStateManager.pushMatrix();
      drawRects((double)left, (double)top, (double)right, (double)bottom, new Color(red, green, blue, alpha));
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(red, green, blue, alpha);
      if (borderWidth == 1.0F) {
         GL11.glEnable(2848);
      }

      GL11.glLineWidth(borderWidth);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.startDrawing(1);
      worldRenderer.addVertex((double)left, (double)top, 0.0D);
      worldRenderer.addVertex((double)left, (double)bottom, 0.0D);
      worldRenderer.addVertex((double)right, (double)bottom, 0.0D);
      worldRenderer.addVertex((double)right, (double)top, 0.0D);
      worldRenderer.addVertex((double)left, (double)top, 0.0D);
      worldRenderer.addVertex((double)right, (double)top, 0.0D);
      worldRenderer.addVertex((double)left, (double)bottom, 0.0D);
      worldRenderer.addVertex((double)right, (double)bottom, 0.0D);
      tessellator.draw();
      GL11.glLineWidth(2.0F);
      if (borderWidth == 1.0F) {
         GL11.glDisable(2848);
      }

      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public static void drawRects(double left, double top, double right, double bottom, Color color) {
      enableGL2D();
      glColor(color);
      drawRectZZ(left, top, right, bottom);
      disableGL2D();
   }

   public static void drawBorderedGradientRect(double left, double top, double right, double bottom, float borderWidth, int borderColor, int startColor, int endColor) {
      float alpha = (float)(borderColor >> 24 & 255) / 255.0F;
      float red = (float)(borderColor >> 16 & 255) / 255.0F;
      float green = (float)(borderColor >> 8 & 255) / 255.0F;
      float blue = (float)(borderColor & 255) / 255.0F;
      GlStateManager.pushMatrix();
      drawGradientRect(left, top, right, bottom, startColor, endColor);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(red, green, blue, alpha);
      if (borderWidth == 1.0F) {
         GL11.glEnable(2848);
      }

      GL11.glLineWidth(borderWidth);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.startDrawing(1);
      worldRenderer.addVertex(left, top, 0.0D);
      worldRenderer.addVertex(left, bottom, 0.0D);
      worldRenderer.addVertex(right, bottom, 0.0D);
      worldRenderer.addVertex(right, top, 0.0D);
      worldRenderer.addVertex(left, top, 0.0D);
      worldRenderer.addVertex(right, top, 0.0D);
      worldRenderer.addVertex(left, bottom, 0.0D);
      worldRenderer.addVertex(right, bottom, 0.0D);
      tessellator.draw();
      GL11.glLineWidth(2.0F);
      if (borderWidth == 1.0F) {
         GL11.glDisable(2848);
      }

      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn) {
      if (ent.isEntityAlive()) {
         double x = getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
         double y = getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
         double z = getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
         boundingBox(ent, x, y, z, hexColor, hexColorIn);
      }
   }

   public static double getDiff(double lastI, double i, float ticks, double ownI) {
      return lastI + (i - lastI) * (double)ticks - ownI;
   }

   public static void boundingBox(Entity entity, double x, double y, double z, int color, int colorIn) {
      GlStateManager.pushMatrix();
      GL11.glLineWidth(1.0F);
      AxisAlignedBB var11 = entity.getEntityBoundingBox();
      AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
      if (color != 0) {
         GlStateManager.disableDepth();
         filledBox(var12, colorIn, true);
         GlStateManager.disableLighting();
         RenderGlobal.drawOutlinedBoundingBox(var12, color);
      }

      GlStateManager.popMatrix();
   }

   public static void filledBox(AxisAlignedBB boundingBox, int color, boolean shouldColor) {
      GlStateManager.pushMatrix();
      float var11 = (float)(color >> 24 & 255) / 255.0F;
      float var12 = (float)(color >> 16 & 255) / 255.0F;
      float var13 = (float)(color >> 8 & 255) / 255.0F;
      float var14 = (float)(color & 255) / 255.0F;
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      if (shouldColor) {
         GlStateManager.color(var12, var13, var14, var11);
      }

      boolean draw = true;
      worldRenderer.startDrawing(7);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(7);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(7);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(7);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(7);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
      worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
      Tessellator.getInstance().draw();
      worldRenderer.startDrawing(7);
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

   public static void drawBorderedRectZ(float left, float top, float right, float bottom, int borderColor, int color) {
      drawBorderedRectZ(left, top, right, bottom, 1.0F, borderColor, color);
   }

   public static void drawBorderedGradientRect(double left, double top, double right, double bottom, int borderColor, int startColor, int endColor) {
      drawBorderedGradientRect(left, top, right, bottom, 1.0F, borderColor, startColor, endColor);
   }

   public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
      GL11.glPushMatrix();
      GL11.glTranslated(posX, posY, posZ);
      GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
      setColor(rgb);
      enableGL3D(1.0F);
      Cylinder c = new Cylinder();
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      c.setDrawStyle(100011);
      c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
      disableGL3D();
      GL11.glPopMatrix();
   }

   public static void drawExeterCrossESP(EntityLivingBase entity, int rgb, double x, double y, double z) {
      AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - 0.4D, y, z - 0.4D, x + 0.4D, y + 2.0D, z + 0.4D);
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GlStateManager.translate(-x, -y, -z);
      enableGL3D(1.0F);
      setColor(rgb);
      drawOutlinedBoundingBox(axisAlignedBB);
      disableGL3D();
      GlStateManager.popMatrix();
   }

   public static void drawOutlinedBox(AxisAlignedBB box) {
      if (box != null) {
         GL11.glBegin(3);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(3);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glEnd();
         GL11.glBegin(1);
         GL11.glVertex3d(box.minX, box.minY, box.minZ);
         GL11.glVertex3d(box.minX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.minZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
         GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
         GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
         GL11.glVertex3d(box.minX, box.minY, box.maxZ);
         GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
         GL11.glEnd();
      }

   }

   public static void drawLaserPoint(int rgb, double posX, double posY, double posZ) {
      GL11.glPushMatrix();
      enableGL3D(2.0F);
      setColor((new Color(255, 0, 0)).getRGB());
      GL11.glBegin(2);
      Minecraft.getMinecraft();
      GL11.glVertex3d(0.0D, (double)Minecraft.thePlayer.getEyeHeight(), 0.0D);
      GL11.glVertex3d(posX, posY, posZ);
      GL11.glEnd();
      disableGL3D();
      GL11.glPopMatrix();
   }

   public static void drawLine2D(double x1, double y1, double x2, double y2, float width, int color) {
      GL11.glEnable(3042);
      GL11.glDisable(2884);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
      setColor(color);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2d(x1, y1);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
   }

   public static class ColorUtils {
      public int RGBtoHEX(int r, int g, int b, int a) {
         return (a << 24) + (r << 16) + (g << 8) + b;
      }

      public Color getRainbow(long offset, float fade) {
         float hue = (float)(System.nanoTime() + offset) / 5.0E9F % 1.0F;
         long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
         Color c = new Color((int)color);
         return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
      }

      public static Color glColor(int color, float alpha) {
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         GL11.glColor4f(red, green, blue, alpha);
         return new Color(red, green, blue, alpha);
      }

      public void glColor(Color color) {
         GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      }

      public Color glColor(int hex) {
         float alpha = (float)(hex >> 24 & 255) / 256.0F;
         float red = (float)(hex >> 16 & 255) / 255.0F;
         float green = (float)(hex >> 8 & 255) / 255.0F;
         float blue = (float)(hex & 255) / 255.0F;
         GL11.glColor4f(red, green, blue, alpha);
         return new Color(red, green, blue, alpha);
      }

      public Color glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
         float red = 0.003921569F * (float)redRGB;
         float green = 0.003921569F * (float)greenRGB;
         float blue = 0.003921569F * (float)blueRGB;
         GL11.glColor4f(red, green, blue, alpha);
         return new Color(red, green, blue, alpha);
      }

      public static int transparency(int color, double alpha) {
         Color c = new Color(color);
         float r = 0.003921569F * (float)c.getRed();
         float g = 0.003921569F * (float)c.getGreen();
         float b = 0.003921569F * (float)c.getBlue();
         return (new Color(r, g, b, (float)alpha)).getRGB();
      }

      public static float[] getRGBA(int color) {
         float a = (float)(color >> 24 & 255) / 255.0F;
         float r = (float)(color >> 16 & 255) / 255.0F;
         float g = (float)(color >> 8 & 255) / 255.0F;
         float b = (float)(color & 255) / 255.0F;
         return new float[]{r, g, b, a};
      }

      public static int intFromHex(String hex) {
         try {
            return Integer.parseInt(hex, 15);
         } catch (NumberFormatException var2) {
            return -1;
         }
      }

      public static String hexFromInt(int color) {
         return hexFromInt(new Color(color));
      }

      public static String hexFromInt(Color color) {
         return Integer.toHexString(color.getRGB()).substring(2);
      }
   }
}
