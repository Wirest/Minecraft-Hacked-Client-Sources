/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OutlineUtils
/*     */ {
/*     */   public static void renderOne()
/*     */   {
/*  17 */     checkSetupFBO();
/*  18 */     GL11.glPushAttrib(1048575);
/*  19 */     GL11.glDisable(3008);
/*  20 */     GL11.glDisable(3553);
/*  21 */     GL11.glDisable(2896);
/*  22 */     GL11.glEnable(3042);
/*  23 */     GL11.glBlendFunc(770, 771);
/*  24 */     GL11.glLineWidth(3.0F);
/*  25 */     GL11.glEnable(2848);
/*  26 */     GL11.glEnable(2960);
/*  27 */     GL11.glClear(1024);
/*  28 */     GL11.glClearStencil(15);
/*  29 */     GL11.glStencilFunc(512, 1, 15);
/*  30 */     GL11.glStencilOp(7681, 7681, 7681);
/*  31 */     GL11.glPolygonMode(1032, 6913);
/*     */   }
/*     */   
/*     */   public static void renderTwo()
/*     */   {
/*  36 */     GL11.glStencilFunc(512, 0, 15);
/*  37 */     GL11.glStencilOp(7681, 7681, 7681);
/*  38 */     GL11.glPolygonMode(1032, 6914);
/*     */   }
/*     */   
/*     */   public static void renderThree()
/*     */   {
/*  43 */     GL11.glStencilFunc(514, 1, 15);
/*  44 */     GL11.glStencilOp(7680, 7680, 7680);
/*  45 */     GL11.glPolygonMode(1032, 6913);
/*     */   }
/*     */   
/*     */   public static void renderFour()
/*     */   {
/*  50 */     setColor(new Color(255, 255, 255));
/*  51 */     GL11.glDepthMask(false);
/*  52 */     GL11.glDisable(2929);
/*  53 */     GL11.glEnable(10754);
/*  54 */     GL11.glPolygonOffset(1.0F, -2000000.0F);
/*  55 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
/*     */   }
/*     */   
/*     */   public static void renderFive()
/*     */   {
/*  60 */     GL11.glPolygonOffset(1.0F, 2000000.0F);
/*  61 */     GL11.glDisable(10754);
/*  62 */     GL11.glEnable(2929);
/*  63 */     GL11.glDepthMask(true);
/*  64 */     GL11.glDisable(2960);
/*  65 */     GL11.glDisable(2848);
/*  66 */     GL11.glHint(3154, 4352);
/*  67 */     GL11.glEnable(3042);
/*  68 */     GL11.glEnable(2896);
/*  69 */     GL11.glEnable(3553);
/*  70 */     GL11.glEnable(3008);
/*  71 */     GL11.glPopAttrib();
/*     */   }
/*     */   
/*     */   public static void setColor(Color c)
/*     */   {
/*  76 */     GL11.glColor4d(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void checkSetupFBO()
/*     */   {
/*  82 */     Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
/*     */     
/*     */ 
/*  85 */     if (fbo != null)
/*     */     {
/*     */ 
/*  88 */       if (fbo.depthBuffer > -1)
/*     */       {
/*     */ 
/*  91 */         setupFBO(fbo);
/*     */         
/*  93 */         fbo.depthBuffer = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setupFBO(Framebuffer fbo)
/*     */   {
/* 108 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
/*     */     
/* 110 */     int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
/*     */     
/*     */ 
/* 113 */     EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
/*     */     
/*     */ 
/*     */ 
/* 117 */     EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*     */     
/*     */ 
/*     */ 
/* 121 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
/*     */     
/*     */ 
/*     */ 
/* 125 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\OutlineUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */