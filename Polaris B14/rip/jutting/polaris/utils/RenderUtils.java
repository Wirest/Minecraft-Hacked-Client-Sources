/*      */ package rip.jutting.polaris.utils;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Timer;
/*      */ import org.lwjgl.opengl.EXTFramebufferObject;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ 
/*      */ public class RenderUtils
/*      */ {
/*   28 */   private static final Frustum frustum = new Frustum();
/*      */   
/*      */   public static double interpolate(double newPos, double oldPos)
/*      */   {
/*   32 */     return oldPos + (newPos - oldPos) * Minecraft.getMinecraft().timer.renderPartialTicks;
/*      */   }
/*      */   
/*      */   public static boolean isInFrustumView(Entity ent) {
/*   36 */     Entity current = Minecraft.getMinecraft().getRenderViewEntity();
/*   37 */     double x = interpolate(current.posX, current.lastTickPosX);double y = interpolate(current.posY, current.lastTickPosY);
/*   38 */     double z = interpolate(current.posZ, current.lastTickPosZ);
/*   39 */     frustum.setPosition(x, y, z);
/*   40 */     return (frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox())) || (ent.ignoreFrustumCheck);
/*      */   }
/*      */   
/*      */   public static class R2DUtils {
/*      */     public static void enableGL2D() {
/*   45 */       GL11.glDisable(2929);
/*   46 */       GL11.glEnable(3042);
/*   47 */       GL11.glDisable(3553);
/*   48 */       GL11.glBlendFunc(770, 771);
/*   49 */       GL11.glDepthMask(true);
/*   50 */       GL11.glEnable(2848);
/*   51 */       GL11.glHint(3154, 4354);
/*   52 */       GL11.glHint(3155, 4354);
/*      */     }
/*      */     
/*      */     public static void disableGL2D() {
/*   56 */       GL11.glEnable(3553);
/*   57 */       GL11.glDisable(3042);
/*   58 */       GL11.glEnable(2929);
/*   59 */       GL11.glDisable(2848);
/*   60 */       GL11.glHint(3154, 4352);
/*   61 */       GL11.glHint(3155, 4352);
/*      */     }
/*      */     
/*      */     public void drawCustomImage(double x, double y, double xwidth, double ywidth, ResourceLocation image) {
/*   65 */       double par1 = x + xwidth;
/*   66 */       double par2 = y + ywidth;
/*   67 */       GL11.glDisable(2929);
/*   68 */       GL11.glDepthMask(false);
/*   69 */       OpenGlHelper.glBlendFunc(770, 771, 1, 0);
/*   70 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*   71 */       Minecraft.getMinecraft().getTextureManager().bindTexture(image);
/*   72 */       Tessellator var3 = Tessellator.getInstance();
/*   73 */       WorldRenderer var4 = var3.getWorldRenderer();
/*   74 */       var4.startDrawingQuads();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*   79 */       var3.draw();
/*   80 */       GL11.glDepthMask(true);
/*   81 */       GL11.glEnable(2929);
/*   82 */       GL11.glEnable(3008);
/*   83 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public void drawCustomImage(double x, double y, double xwidth, double ywidth, ResourceLocation image, int[] color) {
/*   87 */       double par1 = x + xwidth;
/*   88 */       double par2 = y + ywidth;
/*   89 */       GL11.glDisable(2929);
/*   90 */       GL11.glDepthMask(false);
/*   91 */       OpenGlHelper.glBlendFunc(770, 771, 1, 0);
/*   92 */       GL11.glColor4f(color[0], color[1], color[2], color[3]);
/*   93 */       Minecraft.getMinecraft().getTextureManager().bindTexture(image);
/*   94 */       Tessellator var3 = Tessellator.getInstance();
/*   95 */       WorldRenderer var4 = var3.getWorldRenderer();
/*   96 */       var4.startDrawingQuads();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  101 */       var3.draw();
/*  102 */       GL11.glDepthMask(true);
/*  103 */       GL11.glEnable(2929);
/*  104 */       GL11.glEnable(3008);
/*  105 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color)
/*      */     {
/*  110 */       GlStateManager.pushMatrix();
/*  111 */       GlStateManager.translate(posX, posY, posZ);
/*  112 */       GL11.glNormal3f(0.0F, 0.0F, 0.0F);
/*  113 */       GlStateManager.rotate(-net.minecraft.client.renderer.entity.RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/*  114 */       GlStateManager.scale(-0.1D, -0.1D, 0.1D);
/*  115 */       GL11.glDisable(2896);
/*  116 */       GL11.glDisable(2929);
/*  117 */       GL11.glEnable(3042);
/*  118 */       GL11.glBlendFunc(770, 771);
/*  119 */       GlStateManager.depthMask(true);
/*  120 */       drawRect(7.0D, -20.0D, 7.300000190734863D, -17.5D, color);
/*  121 */       drawRect(-7.300000190734863D, -20.0D, -7.0D, -17.5D, color);
/*  122 */       drawRect(4.0D, -20.299999237060547D, 7.300000190734863D, -20.0D, color);
/*  123 */       drawRect(-7.300000190734863D, -20.299999237060547D, -4.0D, -20.0D, color);
/*  124 */       drawRect(-7.0D, 3.0D, -4.0D, 3.299999952316284D, color);
/*  125 */       drawRect(4.0D, 3.0D, 7.0D, 3.299999952316284D, color);
/*  126 */       drawRect(-7.300000190734863D, 0.8D, -7.0D, 3.299999952316284D, color);
/*  127 */       drawRect(7.0D, 0.5D, 7.300000190734863D, 3.299999952316284D, color);
/*  128 */       drawRect(7.0D, -20.0D, 7.300000190734863D, -17.5D, color);
/*  129 */       drawRect(-7.300000190734863D, -20.0D, -7.0D, -17.5D, color);
/*  130 */       drawRect(4.0D, -20.299999237060547D, 7.300000190734863D, -20.0D, color);
/*  131 */       drawRect(-7.300000190734863D, -20.299999237060547D, -4.0D, -20.0D, color);
/*  132 */       drawRect(-7.0D, 3.0D, -4.0D, 3.299999952316284D, color);
/*  133 */       drawRect(4.0D, 3.0D, 7.0D, 3.299999952316284D, color);
/*  134 */       drawRect(-7.300000190734863D, 0.8D, -7.0D, 3.299999952316284D, color);
/*  135 */       drawRect(7.0D, 0.5D, 7.300000190734863D, 3.299999952316284D, color);
/*  136 */       GL11.glDisable(3042);
/*  137 */       GL11.glEnable(2929);
/*  138 */       GlStateManager.popMatrix();
/*      */     }
/*      */     
/*      */     public static void drawRect(double x2, double y2, double x1, double y1, int color) {
/*  142 */       enableGL2D();
/*  143 */       glColor(color);
/*  144 */       drawRect(x2, y2, x1, y1);
/*  145 */       disableGL2D();
/*      */     }
/*      */     
/*  148 */     private static void drawRect(double x2, double y2, double x1, double y1) { GL11.glBegin(7);
/*  149 */       GL11.glVertex2d(x2, y1);
/*  150 */       GL11.glVertex2d(x1, y1);
/*  151 */       GL11.glVertex2d(x1, y2);
/*  152 */       GL11.glVertex2d(x2, y2);
/*  153 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void glColor(int hex) {
/*  157 */       float alpha = (hex >> 24 & 0xFF) / 255.0F;
/*  158 */       float red = (hex >> 16 & 0xFF) / 255.0F;
/*  159 */       float green = (hex >> 8 & 0xFF) / 255.0F;
/*  160 */       float blue = (hex & 0xFF) / 255.0F;
/*  161 */       GL11.glColor4f(red, green, blue, alpha);
/*      */     }
/*      */     
/*      */     public static void drawRect(float x, float y, float x1, float y1, int color) {
/*  165 */       enableGL2D();
/*  166 */       Helper.colorUtils().glColor(color);
/*  167 */       drawRect(x, y, x1, y1);
/*  168 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
/*  172 */       enableGL2D();
/*  173 */       Helper.colorUtils().glColor(borderColor);
/*  174 */       drawRect(x + width, y, x1 - width, y + width);
/*  175 */       drawRect(x, y, x + width, y1);
/*  176 */       drawRect(x1 - width, y, x1, y1);
/*  177 */       drawRect(x + width, y1 - width, x1 - width, y1);
/*  178 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
/*  182 */       enableGL2D();
/*  183 */       glColor(internalColor);
/*  184 */       drawRect(x + width, y + width, x1 - width, y1 - width);
/*  185 */       glColor(borderColor);
/*  186 */       drawRect(x + width, y, x1 - width, y + width);
/*  187 */       drawRect(x, y, x + width, y1);
/*  188 */       drawRect(x1 - width, y, x1, y1);
/*  189 */       drawRect(x + width, y1 - width, x1 - width, y1);
/*  190 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawBorderedRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
/*  194 */       enableGL2D();
/*  195 */       glColor(internalColor);
/*  196 */       drawRect(x + width, y + width, x1 - width, y1 - width);
/*  197 */       glColor(borderColor);
/*  198 */       drawRect(x + width, y, x1 - width, y + width);
/*  199 */       drawRect(x, y, x + width, y1);
/*  200 */       drawRect(x1 - width, y, x1, y1);
/*  201 */       drawRect(x + width, y1 - width, x1 - width, y1);
/*  202 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
/*  206 */       enableGL2D();
/*  207 */       x *= 2.0F;
/*  208 */       x1 *= 2.0F;
/*  209 */       y *= 2.0F;
/*  210 */       y1 *= 2.0F;
/*  211 */       GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  212 */       drawVLine(x, y, y1, borderC);
/*  213 */       drawVLine(x1 - 1.0F, y, y1, borderC);
/*  214 */       drawHLine(x, x1 - 1.0F, y, borderC);
/*  215 */       drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
/*  216 */       drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
/*  217 */       GL11.glScalef(2.0F, 2.0F, 2.0F);
/*  218 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
/*  222 */       enableGL2D();
/*  223 */       GL11.glShadeModel(7425);
/*  224 */       GL11.glBegin(7);
/*  225 */       Helper.colorUtils().glColor(topColor);
/*  226 */       GL11.glVertex2f(x, y1);
/*  227 */       GL11.glVertex2f(x1, y1);
/*  228 */       Helper.colorUtils().glColor(bottomColor);
/*  229 */       GL11.glVertex2f(x1, y);
/*  230 */       GL11.glVertex2f(x, y);
/*  231 */       GL11.glEnd();
/*  232 */       GL11.glShadeModel(7424);
/*  233 */       disableGL2D();
/*      */     }
/*      */     
/*      */     public static void drawHLine(float x, float y, float x1, int y1) {
/*  237 */       if (y < x) {
/*  238 */         float var5 = x;
/*  239 */         x = y;
/*  240 */         y = var5;
/*      */       }
/*  242 */       drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
/*      */     }
/*      */     
/*      */     public static void drawVLine(float x, float y, float x1, int y1) {
/*  246 */       if (x1 < y) {
/*  247 */         float var5 = y;
/*  248 */         y = x1;
/*  249 */         x1 = var5;
/*      */       }
/*  251 */       drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
/*      */     }
/*      */     
/*      */     public static void drawHLine(float x, float y, float x1, int y1, int y2) {
/*  255 */       if (y < x) {
/*  256 */         float var5 = x;
/*  257 */         x = y;
/*  258 */         y = var5;
/*      */       }
/*  260 */       drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
/*      */     }
/*      */     
/*      */     public static void drawRect(float x, float y, float x1, float y1) {
/*  264 */       GL11.glBegin(7);
/*  265 */       GL11.glVertex2f(x, y1);
/*  266 */       GL11.glVertex2f(x1, y1);
/*  267 */       GL11.glVertex2f(x1, y);
/*  268 */       GL11.glVertex2f(x, y);
/*  269 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void drawTri(double x1, double y1, double x2, double y2, double x3, double y3, double width, Color c) {
/*  273 */       GL11.glEnable(3042);
/*  274 */       GL11.glDisable(3553);
/*  275 */       GL11.glEnable(2848);
/*  276 */       GL11.glBlendFunc(770, 771);
/*  277 */       Helper.colorUtils().glColor(c);
/*  278 */       GL11.glLineWidth((float)width);
/*  279 */       GL11.glBegin(3);
/*  280 */       GL11.glVertex2d(x1, y1);
/*  281 */       GL11.glVertex2d(x2, y2);
/*  282 */       GL11.glVertex2d(x3, y3);
/*  283 */       GL11.glEnd();
/*  284 */       GL11.glDisable(2848);
/*  285 */       GL11.glEnable(3553);
/*  286 */       GL11.glDisable(3042);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class R3DUtils
/*      */   {
/*      */     public static void startDrawing() {
/*  293 */       GL11.glEnable(3042);
/*  294 */       GL11.glEnable(3042);
/*  295 */       GL11.glBlendFunc(770, 771);
/*  296 */       GL11.glEnable(2848);
/*  297 */       GL11.glDisable(3553);
/*  298 */       GL11.glDisable(2929);
/*  299 */       Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
/*      */     }
/*      */     
/*      */     public static void stopDrawing() {
/*  303 */       GL11.glDisable(3042);
/*  304 */       GL11.glEnable(3553);
/*  305 */       GL11.glDisable(2848);
/*  306 */       GL11.glDisable(3042);
/*  307 */       GL11.glEnable(2929);
/*      */     }
/*      */     
/*      */     public void drawRombo(double x, double y, double z) {
/*  311 */       Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
/*  312 */       y += 1.0D;
/*  313 */       GL11.glBegin(4);
/*  314 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  315 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  316 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  317 */       GL11.glEnd();
/*  318 */       GL11.glBegin(4);
/*  319 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  320 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  321 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  322 */       GL11.glEnd();
/*  323 */       GL11.glBegin(4);
/*  324 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  325 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  326 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  327 */       GL11.glEnd();
/*  328 */       GL11.glBegin(4);
/*  329 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  330 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  331 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  332 */       GL11.glEnd();
/*  333 */       GL11.glBegin(4);
/*  334 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  335 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  336 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  337 */       GL11.glEnd();
/*  338 */       GL11.glBegin(4);
/*  339 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  340 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  341 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  342 */       GL11.glEnd();
/*  343 */       GL11.glBegin(4);
/*  344 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  345 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  346 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  347 */       GL11.glEnd();
/*  348 */       GL11.glBegin(4);
/*  349 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  350 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  351 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  352 */       GL11.glEnd();
/*  353 */       GL11.glBegin(4);
/*  354 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  355 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  356 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  357 */       GL11.glEnd();
/*  358 */       GL11.glBegin(4);
/*  359 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  360 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  361 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  362 */       GL11.glEnd();
/*  363 */       GL11.glBegin(4);
/*  364 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  365 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  366 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  367 */       GL11.glEnd();
/*  368 */       GL11.glBegin(4);
/*  369 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  370 */       GL11.glVertex3d(x, y + 1.0D, z);
/*  371 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  372 */       GL11.glEnd();
/*  373 */       GL11.glBegin(4);
/*  374 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  375 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  376 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  377 */       GL11.glEnd();
/*  378 */       GL11.glBegin(4);
/*  379 */       GL11.glVertex3d(x + 0.5D, y, z);
/*  380 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  381 */       GL11.glVertex3d(x, y, z - 0.5D);
/*  382 */       GL11.glEnd();
/*  383 */       GL11.glBegin(4);
/*  384 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  385 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  386 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  387 */       GL11.glEnd();
/*  388 */       GL11.glBegin(4);
/*  389 */       GL11.glVertex3d(x - 0.5D, y, z);
/*  390 */       GL11.glVertex3d(x, y - 1.0D, z);
/*  391 */       GL11.glVertex3d(x, y, z + 0.5D);
/*  392 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void drawFilledBox(AxisAlignedBB mask) {
/*  396 */       GL11.glBegin(4);
/*  397 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  398 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  399 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  400 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  401 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  402 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  403 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  404 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  405 */       GL11.glEnd();
/*  406 */       GL11.glBegin(4);
/*  407 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  408 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  409 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  410 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  411 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  412 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  413 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  414 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  415 */       GL11.glEnd();
/*  416 */       GL11.glBegin(4);
/*  417 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  418 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  419 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  420 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  421 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  422 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  423 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  424 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  425 */       GL11.glEnd();
/*  426 */       GL11.glBegin(4);
/*  427 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  428 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  429 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  430 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  431 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  432 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  433 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  434 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  435 */       GL11.glEnd();
/*  436 */       GL11.glBegin(4);
/*  437 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  438 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  439 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  440 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  441 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  442 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  443 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  444 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  445 */       GL11.glEnd();
/*  446 */       GL11.glBegin(4);
/*  447 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
/*  448 */       GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
/*  449 */       GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
/*  450 */       GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
/*  451 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
/*  452 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
/*  453 */       GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
/*  454 */       GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
/*  455 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
/*  459 */       GL11.glBegin(3);
/*  460 */       GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
/*  461 */       GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
/*  462 */       GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
/*  463 */       GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
/*  464 */       GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
/*  465 */       GL11.glEnd();
/*  466 */       GL11.glBegin(3);
/*  467 */       GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
/*  468 */       GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
/*  469 */       GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
/*  470 */       GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
/*  471 */       GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
/*  472 */       GL11.glEnd();
/*  473 */       GL11.glBegin(1);
/*  474 */       GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
/*  475 */       GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
/*  476 */       GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
/*  477 */       GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
/*  478 */       GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
/*  479 */       GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
/*  480 */       GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
/*  481 */       GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
/*  482 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void drawOutlinedBox(AxisAlignedBB box)
/*      */     {
/*  487 */       if (box == null) {
/*  488 */         return;
/*      */       }
/*  490 */       Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
/*  491 */       GL11.glBegin(3);
/*  492 */       GL11.glVertex3d(box.minX, box.minY, box.minZ);
/*  493 */       GL11.glVertex3d(box.maxX, box.minY, box.minZ);
/*  494 */       GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
/*  495 */       GL11.glVertex3d(box.minX, box.minY, box.maxZ);
/*  496 */       GL11.glVertex3d(box.minX, box.minY, box.minZ);
/*  497 */       GL11.glEnd();
/*  498 */       GL11.glBegin(3);
/*  499 */       GL11.glVertex3d(box.minX, box.maxY, box.minZ);
/*  500 */       GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
/*  501 */       GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
/*  502 */       GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
/*  503 */       GL11.glVertex3d(box.minX, box.maxY, box.minZ);
/*  504 */       GL11.glEnd();
/*  505 */       GL11.glBegin(1);
/*  506 */       GL11.glVertex3d(box.minX, box.minY, box.minZ);
/*  507 */       GL11.glVertex3d(box.minX, box.maxY, box.minZ);
/*  508 */       GL11.glVertex3d(box.maxX, box.minY, box.minZ);
/*  509 */       GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
/*  510 */       GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
/*  511 */       GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
/*  512 */       GL11.glVertex3d(box.minX, box.minY, box.maxZ);
/*  513 */       GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
/*  514 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static void drawTracerLine1(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth)
/*      */     {
/*  519 */       boolean temp = Minecraft.getMinecraft().gameSettings.viewBobbing;
/*  520 */       Minecraft.getMinecraft().gameSettings.viewBobbing = false;
/*  521 */       EntityRenderer var10000 = Minecraft.getMinecraft().entityRenderer;
/*  522 */       Timer var10001 = Minecraft.getMinecraft().timer;
/*  523 */       var10000.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 2);
/*  524 */       Minecraft.getMinecraft().gameSettings.viewBobbing = temp;
/*  525 */       GL11.glPushMatrix();
/*  526 */       GL11.glEnable(3042);
/*  527 */       GL11.glEnable(2848);
/*  528 */       GL11.glDisable(2929);
/*  529 */       GL11.glDisable(3553);
/*  530 */       GL11.glBlendFunc(770, 771);
/*  531 */       GL11.glEnable(3042);
/*  532 */       GL11.glLineWidth(1.0F);
/*  533 */       GL11.glColor4f(red, green, blue, alpha);
/*  534 */       GL11.glBegin(2);
/*  535 */       Minecraft.getMinecraft();
/*  536 */       GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
/*  537 */       GL11.glVertex3d(x, y, z);
/*  538 */       GL11.glEnd();
/*  539 */       GL11.glDisable(3042);
/*  540 */       GL11.glEnable(3553);
/*  541 */       GL11.glEnable(2929);
/*  542 */       GL11.glDisable(2848);
/*  543 */       GL11.glDisable(3042);
/*  544 */       GL11.glPopMatrix();
/*      */     }
/*      */     
/*      */     public static int getBlockColor(Block block)
/*      */     {
/*  549 */       int color = 0;
/*  550 */       switch (Block.getIdFromBlock(block)) {
/*      */       case 14: 
/*      */       case 41: 
/*  553 */         color = -1711477173;
/*  554 */         break;
/*      */       
/*      */       case 15: 
/*      */       case 42: 
/*  558 */         color = -1715420992;
/*  559 */         break;
/*      */       
/*      */       case 16: 
/*      */       case 173: 
/*  563 */         color = -1724434633;
/*  564 */         break;
/*      */       
/*      */       case 21: 
/*      */       case 22: 
/*  568 */         color = -1726527803;
/*  569 */         break;
/*      */       
/*      */       case 49: 
/*  572 */         color = -1724108714;
/*  573 */         break;
/*      */       
/*      */       case 54: 
/*      */       case 146: 
/*  577 */         color = -1711292672;
/*  578 */         break;
/*      */       
/*      */       case 56: 
/*      */       case 57: 
/*      */       case 138: 
/*  583 */         color = -1721897739;
/*  584 */         break;
/*      */       
/*      */       case 61: 
/*      */       case 62: 
/*  588 */         color = -1711395081;
/*  589 */         break;
/*      */       
/*      */       case 73: 
/*      */       case 74: 
/*      */       case 152: 
/*  594 */         color = -1711341568;
/*  595 */         break;
/*      */       
/*      */       case 89: 
/*  598 */         color = -1712594866;
/*  599 */         break;
/*      */       
/*      */       case 129: 
/*      */       case 133: 
/*  603 */         color = -1726489246;
/*  604 */         break;
/*      */       
/*      */       case 130: 
/*  607 */         color = -1713438249;
/*  608 */         break;
/*      */       
/*      */       case 52: 
/*  611 */         color = 805728308;
/*  612 */         break;
/*      */       
/*      */       default: 
/*  615 */         color = -1711276033;
/*      */       }
/*      */       
/*      */       
/*  619 */       return color == 0 ? 806752583 : color;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ColorUtils
/*      */   {
/*      */     public int RGBtoHEX(int r, int g, int b, int a)
/*      */     {
/*  627 */       return (a << 24) + (r << 16) + (g << 8) + b;
/*      */     }
/*      */     
/*      */     public static Color getRainbow(long offset, float fade) {
/*  631 */       float hue = (float)(System.nanoTime() + offset) / 8.0E9F % 1.0F;
/*  632 */       long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
/*  633 */       Color c = new Color((int)color);
/*  634 */       return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
/*      */     }
/*      */     
/*      */     public static Color blend(Color color1, Color color2, double ratio) {
/*  638 */       float r = (float)ratio;
/*  639 */       float ir = 1.0F - r;
/*      */       
/*  641 */       float[] rgb1 = new float[3];
/*  642 */       float[] rgb2 = new float[3];
/*      */       
/*  644 */       color1.getColorComponents(rgb1);
/*  645 */       color2.getColorComponents(rgb2);
/*      */       
/*  647 */       Color color = new Color(rgb1[0] * r + rgb2[0] * ir, 
/*  648 */         rgb1[1] * r + rgb2[1] * ir, 
/*  649 */         rgb1[2] * r + rgb2[2] * ir);
/*      */       
/*  651 */       return color;
/*      */     }
/*      */     
/*      */     public static Color glColor(int color, float alpha) {
/*  655 */       float red = (color >> 16 & 0xFF) / 255.0F;
/*  656 */       float green = (color >> 8 & 0xFF) / 255.0F;
/*  657 */       float blue = (color & 0xFF) / 255.0F;
/*  658 */       GL11.glColor4f(red, green, blue, alpha);
/*  659 */       return new Color(red, green, blue, alpha);
/*      */     }
/*      */     
/*      */     public void glColor(Color color) {
/*  663 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*      */     }
/*      */     
/*      */     public Color glColor(int hex) {
/*  667 */       float alpha = (hex >> 24 & 0xFF) / 256.0F;
/*  668 */       float red = (hex >> 16 & 0xFF) / 255.0F;
/*  669 */       float green = (hex >> 8 & 0xFF) / 255.0F;
/*  670 */       float blue = (hex & 0xFF) / 255.0F;
/*  671 */       GL11.glColor4f(red, green, blue, alpha);
/*  672 */       return new Color(red, green, blue, alpha);
/*      */     }
/*      */     
/*      */     public Color glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
/*  676 */       float red = 0.003921569F * redRGB;
/*  677 */       float green = 0.003921569F * greenRGB;
/*  678 */       float blue = 0.003921569F * blueRGB;
/*  679 */       GL11.glColor4f(red, green, blue, alpha);
/*  680 */       return new Color(red, green, blue, alpha);
/*      */     }
/*      */     
/*      */     public static int transparency(int color, double alpha) {
/*  684 */       Color c = new Color(color);
/*  685 */       float r = 0.003921569F * c.getRed();
/*  686 */       float g = 0.003921569F * c.getGreen();
/*  687 */       float b = 0.003921569F * c.getBlue();
/*  688 */       return new Color(r, g, b, (float)alpha).getRGB();
/*      */     }
/*      */     
/*      */     public static float[] getRGBA(int color) {
/*  692 */       float a = (color >> 24 & 0xFF) / 255.0F;
/*  693 */       float r = (color >> 16 & 0xFF) / 255.0F;
/*  694 */       float g = (color >> 8 & 0xFF) / 255.0F;
/*  695 */       float b = (color & 0xFF) / 255.0F;
/*  696 */       return new float[] { r, g, b, a };
/*      */     }
/*      */     
/*      */     public static int intFromHex(String hex) {
/*      */       try {
/*  701 */         return Integer.parseInt(hex, 15);
/*      */       }
/*      */       catch (NumberFormatException e) {}
/*  704 */       return -1;
/*      */     }
/*      */     
/*      */     public static String hexFromInt(int color)
/*      */     {
/*  709 */       return hexFromInt(new Color(color));
/*      */     }
/*      */     
/*      */     public static String hexFromInt(Color color) {
/*  713 */       return Integer.toHexString(color.getRGB()).substring(2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class Stencil
/*      */   {
/*  725 */     private static final Stencil INSTANCE = new Stencil();
/*      */     private final HashMap<Integer, StencilFunc> stencilFuncs;
/*      */     
/*      */     public Stencil() {
/*  729 */       this.stencilFuncs = new HashMap();
/*  730 */       this.layers = 1; }
/*      */     
/*      */     private int layers;
/*      */     private boolean renderMask;
/*  734 */     public static Stencil getInstance() { return INSTANCE; }
/*      */     
/*      */ 
/*      */     public void setRenderMask(boolean renderMask) {
/*  738 */       this.renderMask = renderMask;
/*      */     }
/*      */     
/*      */     public static void checkSetupFBO() {
/*  742 */       Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
/*  743 */       if ((fbo != null) && (fbo.depthBuffer > -1)) {
/*  744 */         EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
/*  745 */         int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
/*  746 */         EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
/*  747 */         EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  748 */         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
/*  749 */         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
/*  750 */         fbo.depthBuffer = -1;
/*      */       }
/*      */     }
/*      */     
/*      */     public static void setupFBO(Framebuffer fbo) {
/*  755 */       EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
/*  756 */       int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
/*  757 */       EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
/*  758 */       EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  759 */       EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
/*  760 */       EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
/*      */     }
/*      */     
/*      */     public void startLayer() {
/*  764 */       if (this.layers == 1) {
/*  765 */         GL11.glClearStencil(0);
/*  766 */         GL11.glClear(1024);
/*      */       }
/*  768 */       GL11.glEnable(2960);
/*  769 */       this.layers += 1;
/*  770 */       if (this.layers > getMaximumLayers()) {
/*  771 */         System.out.println("StencilUtil: Reached maximum amount of layers!");
/*  772 */         this.layers = 1;
/*      */       }
/*      */     }
/*      */     
/*      */     public void stopLayer() {
/*  777 */       if (this.layers == 1) {
/*  778 */         System.out.println("StencilUtil: No layers found!");
/*  779 */         return;
/*      */       }
/*  781 */       this.layers -= 1;
/*  782 */       if (this.layers == 1) {
/*  783 */         GL11.glDisable(2960);
/*      */       }
/*      */       else {
/*  786 */         StencilFunc lastStencilFunc = (StencilFunc)this.stencilFuncs.remove(Integer.valueOf(this.layers));
/*  787 */         if (lastStencilFunc != null) {
/*  788 */           lastStencilFunc.use();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void clear() {
/*  794 */       GL11.glClearStencil(0);
/*  795 */       GL11.glClear(1024);
/*  796 */       this.stencilFuncs.clear();
/*  797 */       this.layers = 1;
/*      */     }
/*      */     
/*      */     public void setBuffer() {
/*  801 */       setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, getMaximumLayers(), 7681, 7680, 7680));
/*      */     }
/*      */     
/*      */     public void setBuffer(boolean set) {
/*  805 */       setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, set ? this.layers : this.layers - 1, getMaximumLayers(), 7681, 7681, 7681));
/*      */     }
/*      */     
/*      */     public void cropOutside() {
/*  809 */       setStencilFunc(new StencilFunc(this, 517, this.layers, getMaximumLayers(), 7680, 7680, 7680));
/*      */     }
/*      */     
/*      */     public void cropInside() {
/*  813 */       setStencilFunc(new StencilFunc(this, 514, this.layers, getMaximumLayers(), 7680, 7680, 7680));
/*      */     }
/*      */     
/*      */     public void setStencilFunc(StencilFunc stencilFunc) {
/*  817 */       GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
/*  818 */       GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
/*  819 */       this.stencilFuncs.put(Integer.valueOf(this.layers), stencilFunc);
/*      */     }
/*      */     
/*      */     public StencilFunc getStencilFunc() {
/*  823 */       return (StencilFunc)this.stencilFuncs.get(Integer.valueOf(this.layers));
/*      */     }
/*      */     
/*      */     public int getLayer() {
/*  827 */       return this.layers;
/*      */     }
/*      */     
/*      */     public int getStencilBufferSize() {
/*  831 */       return GL11.glGetInteger(3415);
/*      */     }
/*      */     
/*      */     public int getMaximumLayers() {
/*  835 */       return (int)(Math.pow(2.0D, getStencilBufferSize()) - 1.0D);
/*      */     }
/*      */     
/*      */     public void createCirlce(double x, double y, double radius) {
/*  839 */       GL11.glBegin(6);
/*  840 */       for (int i = 0; i <= 360; i++) {
/*  841 */         double sin = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
/*  842 */         double cos = Math.cos(i * 3.141592653589793D / 180.0D) * radius;
/*  843 */         GL11.glVertex2d(x + sin, y + cos);
/*      */       }
/*  845 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public void createRect(double x, double y, double x2, double y2) {
/*  849 */       GL11.glBegin(7);
/*  850 */       GL11.glVertex2d(x, y2);
/*  851 */       GL11.glVertex2d(x2, y2);
/*  852 */       GL11.glVertex2d(x2, y);
/*  853 */       GL11.glVertex2d(x, y);
/*  854 */       GL11.glEnd();
/*      */     }
/*      */     
/*      */     public static class StencilFunc
/*      */     {
/*      */       public static int func_func;
/*      */       public static int func_ref;
/*      */       public static int func_mask;
/*      */       public static int op_fail;
/*      */       public static int op_zfail;
/*      */       public static int op_zpass;
/*      */       
/*      */       public StencilFunc(RenderUtils.Stencil paramStencil, int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
/*  867 */         func_func = func_func;
/*  868 */         func_ref = func_ref;
/*  869 */         func_mask = func_mask;
/*  870 */         op_fail = op_fail;
/*  871 */         op_zfail = op_zfail;
/*  872 */         op_zpass = op_zpass;
/*      */       }
/*      */       
/*      */       public void use() {
/*  876 */         GL11.glStencilFunc(func_func, func_ref, func_mask);
/*  877 */         GL11.glStencilOp(op_fail, op_zfail, op_zpass);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Camera
/*      */   {
/*      */     private final Minecraft mc;
/*      */     private Timer timer;
/*      */     private double posX;
/*      */     private double posY;
/*      */     private double posZ;
/*      */     private float rotationYaw;
/*      */     private float rotationPitch;
/*      */     
/*      */     public Camera(Entity entity) {
/*  893 */       this.mc = Minecraft.getMinecraft();
/*  894 */       if (this.timer == null) {
/*  895 */         this.timer = this.mc.timer;
/*      */       }
/*  897 */       this.posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks);
/*  898 */       this.posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks);
/*  899 */       this.posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks);
/*  900 */       setRotationYaw(entity.rotationYaw);
/*  901 */       setRotationPitch(entity.rotationPitch);
/*  902 */       if (((entity instanceof EntityPlayer)) && (Minecraft.getMinecraft().gameSettings.viewBobbing) && (entity == Minecraft.getMinecraft().thePlayer)) {
/*  903 */         EntityPlayer living1 = (EntityPlayer)entity;
/*  904 */         setRotationYaw(getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
/*  905 */         setRotationPitch(getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
/*      */       }
/*  907 */       else if ((entity instanceof EntityLivingBase)) {
/*  908 */         EntityLivingBase living2 = (EntityLivingBase)entity;
/*  909 */         setRotationYaw(living2.rotationYawHead);
/*      */       }
/*      */     }
/*      */     
/*      */     public Camera(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch) {
/*  914 */       this.mc = Minecraft.getMinecraft();
/*  915 */       this.posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks);
/*  916 */       this.posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks);
/*  917 */       this.posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks);
/*  918 */       setRotationYaw(entity.rotationYaw);
/*  919 */       setRotationPitch(entity.rotationPitch);
/*  920 */       if (((entity instanceof EntityPlayer)) && (Minecraft.getMinecraft().gameSettings.viewBobbing) && (entity == Minecraft.getMinecraft().thePlayer)) {
/*  921 */         EntityPlayer player = (EntityPlayer)entity;
/*  922 */         setRotationYaw(getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
/*  923 */         setRotationPitch(getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
/*      */       }
/*  925 */       this.posX += offsetX;
/*  926 */       this.posY += offsetY;
/*  927 */       this.posZ += offsetZ;
/*  928 */       this.rotationYaw += (float)offsetRotationYaw;
/*  929 */       this.rotationPitch += (float)offsetRotationPitch;
/*      */     }
/*      */     
/*      */     public Camera(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
/*  933 */       this.mc = Minecraft.getMinecraft();
/*  934 */       setPosX(posX);
/*  935 */       this.posY = posY;
/*  936 */       this.posZ = posZ;
/*  937 */       setRotationYaw(rotationYaw);
/*  938 */       setRotationPitch(rotationPitch);
/*      */     }
/*      */     
/*      */     public double getPosX() {
/*  942 */       return this.posX;
/*      */     }
/*      */     
/*      */     public void setPosX(double posX) {
/*  946 */       this.posX = posX;
/*      */     }
/*      */     
/*      */     public double getPosY() {
/*  950 */       return this.posY;
/*      */     }
/*      */     
/*      */     public void setPosY(double posY) {
/*  954 */       this.posY = posY;
/*      */     }
/*      */     
/*      */     public double getPosZ() {
/*  958 */       return this.posZ;
/*      */     }
/*      */     
/*      */     public void setPosZ(double posZ) {
/*  962 */       this.posZ = posZ;
/*      */     }
/*      */     
/*      */     public float getRotationYaw() {
/*  966 */       return this.rotationYaw;
/*      */     }
/*      */     
/*      */     public void setRotationYaw(float rotationYaw) {
/*  970 */       this.rotationYaw = rotationYaw;
/*      */     }
/*      */     
/*      */     public float getRotationPitch() {
/*  974 */       return this.rotationPitch;
/*      */     }
/*      */     
/*      */     public void setRotationPitch(float rotationPitch) {
/*  978 */       this.rotationPitch = rotationPitch;
/*      */     }
/*      */     
/*      */     public static float[] getRotation(double posX1, double posY1, double posZ1, double posX2, double posY2, double posZ2) {
/*  982 */       float[] rotation = new float[2];
/*  983 */       double diffX = posX2 - posX1;
/*  984 */       double diffZ = posZ2 - posZ1;
/*  985 */       double diffY = posY2 - posY1;
/*  986 */       double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
/*  987 */       double pitch = -Math.toDegrees(Math.atan(diffY / dist));
/*  988 */       rotation[1] = ((float)pitch);
/*  989 */       double yaw = 0.0D;
/*  990 */       if ((diffZ >= 0.0D) && (diffX >= 0.0D)) {
/*  991 */         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
/*      */       }
/*  993 */       else if ((diffZ >= 0.0D) && (diffX <= 0.0D)) {
/*  994 */         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
/*      */       }
/*  996 */       else if ((diffZ <= 0.0D) && (diffX >= 0.0D)) {
/*  997 */         yaw = -90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
/*      */       }
/*  999 */       else if ((diffZ <= 0.0D) && (diffX <= 0.0D)) {
/* 1000 */         yaw = 90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
/*      */       }
/* 1002 */       rotation[0] = ((float)yaw);
/* 1003 */       return rotation;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\RenderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */