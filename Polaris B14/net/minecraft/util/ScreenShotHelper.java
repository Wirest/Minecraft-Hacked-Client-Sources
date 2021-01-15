/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.nio.IntBuffer;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class ScreenShotHelper
/*     */ {
/*  23 */   private static final Logger logger = ;
/*  24 */   private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static IntBuffer pixelBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */   private static int[] pixelValues;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IChatComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer)
/*     */   {
/*  40 */     return saveScreenshot(gameDirectory, null, width, height, buffer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer)
/*     */   {
/*     */     try
/*     */     {
/*  51 */       File file1 = new File(gameDirectory, "screenshots");
/*  52 */       file1.mkdir();
/*     */       
/*  54 */       if (OpenGlHelper.isFramebufferEnabled())
/*     */       {
/*  56 */         width = buffer.framebufferTextureWidth;
/*  57 */         height = buffer.framebufferTextureHeight;
/*     */       }
/*     */       
/*  60 */       int i = width * height;
/*     */       
/*  62 */       if ((pixelBuffer == null) || (pixelBuffer.capacity() < i))
/*     */       {
/*  64 */         pixelBuffer = BufferUtils.createIntBuffer(i);
/*  65 */         pixelValues = new int[i];
/*     */       }
/*     */       
/*  68 */       GL11.glPixelStorei(3333, 1);
/*  69 */       GL11.glPixelStorei(3317, 1);
/*  70 */       pixelBuffer.clear();
/*     */       
/*  72 */       if (OpenGlHelper.isFramebufferEnabled())
/*     */       {
/*  74 */         GlStateManager.bindTexture(buffer.framebufferTexture);
/*  75 */         GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
/*     */       }
/*     */       else
/*     */       {
/*  79 */         GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
/*     */       }
/*     */       
/*  82 */       pixelBuffer.get(pixelValues);
/*  83 */       TextureUtil.processPixelValues(pixelValues, width, height);
/*  84 */       BufferedImage bufferedimage = null;
/*     */       
/*  86 */       if (OpenGlHelper.isFramebufferEnabled())
/*     */       {
/*  88 */         bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
/*  89 */         int j = buffer.framebufferTextureHeight - buffer.framebufferHeight;
/*     */         
/*  91 */         for (int k = j; k < buffer.framebufferTextureHeight; k++)
/*     */         {
/*  93 */           for (int l = 0; l < buffer.framebufferWidth; l++)
/*     */           {
/*  95 */             bufferedimage.setRGB(l, k - j, pixelValues[(k * buffer.framebufferTextureWidth + l)]);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 101 */         bufferedimage = new BufferedImage(width, height, 1);
/* 102 */         bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
/*     */       }
/*     */       
/*     */       File file2;
/*     */       File file2;
/* 107 */       if (screenshotName == null)
/*     */       {
/* 109 */         file2 = getTimestampedPNGFileForDirectory(file1);
/*     */       }
/*     */       else
/*     */       {
/* 113 */         file2 = new File(file1, screenshotName);
/*     */       }
/*     */       
/* 116 */       ImageIO.write(bufferedimage, "png", file2);
/* 117 */       IChatComponent ichatcomponent = new ChatComponentText(file2.getName());
/* 118 */       ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
/* 119 */       ichatcomponent.getChatStyle().setUnderlined(Boolean.valueOf(true));
/* 120 */       return new ChatComponentTranslation("screenshot.success", new Object[] { ichatcomponent });
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 124 */       logger.warn("Couldn't save screenshot", exception); }
/* 125 */     return new ChatComponentTranslation("screenshot.failure", tmp428_425);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static File getTimestampedPNGFileForDirectory(File gameDirectory)
/*     */   {
/* 137 */     String s = dateFormat.format(new Date()).toString();
/* 138 */     int i = 1;
/*     */     
/*     */     for (;;)
/*     */     {
/* 142 */       File file1 = new File(gameDirectory, s + (i == 1 ? "" : new StringBuilder("_").append(i).toString()) + ".png");
/*     */       
/* 144 */       if (!file1.exists())
/*     */       {
/* 146 */         return file1;
/*     */       }
/*     */       
/* 149 */       i++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ScreenShotHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */