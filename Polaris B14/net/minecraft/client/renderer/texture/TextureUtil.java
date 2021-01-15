/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.IntBuffer;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import optfine.Config;
/*     */ import optfine.Mipmaps;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextureUtil
/*     */ {
/*  27 */   private static final Logger logger = ;
/*  28 */   private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
/*  29 */   public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
/*  30 */   public static final int[] missingTextureData = missingTexture.getTextureData();
/*     */   
/*     */ 
/*     */ 
/*     */   public static int glGenTextures()
/*     */   {
/*  36 */     return GlStateManager.generateTexture();
/*     */   }
/*     */   
/*     */   public static void deleteTexture(int textureId)
/*     */   {
/*  41 */     GlStateManager.deleteTexture(textureId);
/*     */   }
/*     */   
/*     */   public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_)
/*     */   {
/*  46 */     return uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
/*     */   }
/*     */   
/*     */   public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_)
/*     */   {
/*  51 */     bindTexture(textureId);
/*  52 */     uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
/*     */   }
/*     */   
/*     */   public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_)
/*     */   {
/*  57 */     int[][] aint = new int[p_147949_0_ + 1][];
/*  58 */     aint[0] = p_147949_2_[0];
/*     */     
/*  60 */     if (p_147949_0_ > 0)
/*     */     {
/*  62 */       boolean flag = false;
/*     */       
/*  64 */       for (int i = 0; i < p_147949_2_.length; i++)
/*     */       {
/*  66 */         if (p_147949_2_[0][i] >> 24 == 0)
/*     */         {
/*  68 */           flag = true;
/*  69 */           break;
/*     */         }
/*     */       }
/*     */       
/*  73 */       for (int l1 = 1; l1 <= p_147949_0_; l1++)
/*     */       {
/*  75 */         if (p_147949_2_[l1] != null)
/*     */         {
/*  77 */           aint[l1] = p_147949_2_[l1];
/*     */         }
/*     */         else
/*     */         {
/*  81 */           int[] aint1 = aint[(l1 - 1)];
/*  82 */           int[] aint2 = new int[aint1.length >> 2];
/*  83 */           int j = p_147949_1_ >> l1;
/*  84 */           int k = aint2.length / j;
/*  85 */           int l = j << 1;
/*     */           
/*  87 */           for (int i1 = 0; i1 < j; i1++)
/*     */           {
/*  89 */             for (int j1 = 0; j1 < k; j1++)
/*     */             {
/*  91 */               int k1 = 2 * (i1 + j1 * l);
/*  92 */               aint2[(i1 + j1 * j)] = blendColors(aint1[(k1 + 0)], aint1[(k1 + 1)], aint1[(k1 + 0 + l)], aint1[(k1 + 1 + l)], flag);
/*     */             }
/*     */           }
/*     */           
/*  96 */           aint[l1] = aint2;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 101 */     return aint;
/*     */   }
/*     */   
/*     */   private static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_)
/*     */   {
/* 106 */     return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
/*     */   }
/*     */   
/*     */   private static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_)
/*     */   {
/* 111 */     float f = (float)Math.pow((p_147944_0_ >> p_147944_4_ & 0xFF) / 255.0F, 2.2D);
/* 112 */     float f1 = (float)Math.pow((p_147944_1_ >> p_147944_4_ & 0xFF) / 255.0F, 2.2D);
/* 113 */     float f2 = (float)Math.pow((p_147944_2_ >> p_147944_4_ & 0xFF) / 255.0F, 2.2D);
/* 114 */     float f3 = (float)Math.pow((p_147944_3_ >> p_147944_4_ & 0xFF) / 255.0F, 2.2D);
/* 115 */     float f4 = (float)Math.pow((f + f1 + f2 + f3) * 0.25D, 0.45454545454545453D);
/* 116 */     return (int)(f4 * 255.0D);
/*     */   }
/*     */   
/*     */   public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_)
/*     */   {
/* 121 */     for (int i = 0; i < p_147955_0_.length; i++)
/*     */     {
/* 123 */       int[] aint = p_147955_0_[i];
/* 124 */       uploadTextureSub(i, aint, p_147955_1_ >> i, p_147955_2_ >> i, p_147955_3_ >> i, p_147955_4_ >> i, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_)
/*     */   {
/* 130 */     int i = 4194304 / p_147947_2_;
/* 131 */     setTextureBlurMipmap(p_147947_6_, p_147947_8_);
/* 132 */     setTextureClamped(p_147947_7_);
/*     */     
/*     */     int j;
/* 135 */     for (int k = 0; k < p_147947_2_ * p_147947_3_; k += p_147947_2_ * j)
/*     */     {
/* 137 */       int l = k / p_147947_2_;
/* 138 */       j = Math.min(i, p_147947_3_ - l);
/* 139 */       int i1 = p_147947_2_ * j;
/* 140 */       copyToBufferPos(p_147947_1_, k, i1);
/* 141 */       GL11.glTexSubImage2D(3553, p_147947_0_, p_147947_4_, p_147947_5_ + l, p_147947_2_, j, 32993, 33639, dataBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_)
/*     */   {
/* 147 */     allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
/* 148 */     return uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
/*     */   }
/*     */   
/*     */   public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_)
/*     */   {
/* 153 */     allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
/*     */   }
/*     */   
/*     */   public static void allocateTextureImpl(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_)
/*     */   {
/* 158 */     deleteTexture(p_180600_0_);
/* 159 */     bindTexture(p_180600_0_);
/*     */     
/* 161 */     if (p_180600_1_ >= 0)
/*     */     {
/* 163 */       GL11.glTexParameteri(3553, 33085, p_180600_1_);
/* 164 */       GL11.glTexParameterf(3553, 33082, 0.0F);
/* 165 */       GL11.glTexParameterf(3553, 33083, p_180600_1_);
/* 166 */       GL11.glTexParameterf(3553, 34049, 0.0F);
/*     */     }
/*     */     
/* 169 */     for (int i = 0; i <= p_180600_1_; i++)
/*     */     {
/* 171 */       GL11.glTexImage2D(3553, i, 6408, p_180600_2_ >> i, p_180600_3_ >> i, 0, 32993, 33639, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_)
/*     */   {
/* 177 */     bindTexture(textureId);
/* 178 */     uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
/* 179 */     return textureId;
/*     */   }
/*     */   
/*     */   private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_)
/*     */   {
/* 184 */     int i = p_110993_0_.getWidth();
/* 185 */     int j = p_110993_0_.getHeight();
/* 186 */     int k = 4194304 / i;
/* 187 */     int[] aint = new int[k * i];
/* 188 */     setTextureBlurred(p_110993_3_);
/* 189 */     setTextureClamped(p_110993_4_);
/*     */     
/* 191 */     for (int l = 0; l < i * j; l += i * k)
/*     */     {
/* 193 */       int i1 = l / i;
/* 194 */       int j1 = Math.min(k, j - i1);
/* 195 */       int k1 = i * j1;
/* 196 */       p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
/* 197 */       copyToBuffer(aint, k1);
/* 198 */       GL11.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i1, i, j1, 32993, 33639, dataBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setTextureClamped(boolean p_110997_0_)
/*     */   {
/* 204 */     if (p_110997_0_)
/*     */     {
/* 206 */       GL11.glTexParameteri(3553, 10242, 33071);
/* 207 */       GL11.glTexParameteri(3553, 10243, 33071);
/*     */     }
/*     */     else
/*     */     {
/* 211 */       GL11.glTexParameteri(3553, 10242, 10497);
/* 212 */       GL11.glTexParameteri(3553, 10243, 10497);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setTextureBlurred(boolean p_147951_0_)
/*     */   {
/* 218 */     setTextureBlurMipmap(p_147951_0_, false);
/*     */   }
/*     */   
/*     */   private static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_)
/*     */   {
/* 223 */     if (p_147954_0_)
/*     */     {
/* 225 */       GL11.glTexParameteri(3553, 10241, p_147954_1_ ? 9987 : 9729);
/* 226 */       GL11.glTexParameteri(3553, 10240, 9729);
/*     */     }
/*     */     else
/*     */     {
/* 230 */       int i = Config.getMipmapType();
/* 231 */       GL11.glTexParameteri(3553, 10241, p_147954_1_ ? i : 9728);
/* 232 */       GL11.glTexParameteri(3553, 10240, 9728);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_)
/*     */   {
/* 238 */     copyToBufferPos(p_110990_0_, 0, p_110990_1_);
/*     */   }
/*     */   
/*     */   private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_)
/*     */   {
/* 243 */     int[] aint = p_110994_0_;
/*     */     
/* 245 */     if (Minecraft.getMinecraft().gameSettings.anaglyph)
/*     */     {
/* 247 */       aint = updateAnaglyph(p_110994_0_);
/*     */     }
/*     */     
/* 250 */     dataBuffer.clear();
/* 251 */     dataBuffer.put(aint, p_110994_1_, p_110994_2_);
/* 252 */     dataBuffer.position(0).limit(p_110994_2_);
/*     */   }
/*     */   
/*     */   public static void bindTexture(int p_94277_0_)
/*     */   {
/* 257 */     GlStateManager.bindTexture(p_94277_0_);
/*     */   }
/*     */   
/*     */   public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation) throws IOException
/*     */   {
/* 262 */     BufferedImage bufferedimage = readBufferedImage(resourceManager.getResource(imageLocation).getInputStream());
/* 263 */     int i = bufferedimage.getWidth();
/* 264 */     int j = bufferedimage.getHeight();
/* 265 */     int[] aint = new int[i * j];
/* 266 */     bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
/* 267 */     return aint;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int[] updateAnaglyph(int[] p_110985_0_)
/*     */   {
/* 288 */     int[] aint = new int[p_110985_0_.length];
/*     */     
/* 290 */     for (int i = 0; i < p_110985_0_.length; i++)
/*     */     {
/* 292 */       aint[i] = anaglyphColor(p_110985_0_[i]);
/*     */     }
/*     */     
/* 295 */     return aint;
/*     */   }
/*     */   
/*     */   public static int anaglyphColor(int p_177054_0_)
/*     */   {
/* 300 */     int i = p_177054_0_ >> 24 & 0xFF;
/* 301 */     int j = p_177054_0_ >> 16 & 0xFF;
/* 302 */     int k = p_177054_0_ >> 8 & 0xFF;
/* 303 */     int l = p_177054_0_ & 0xFF;
/* 304 */     int i1 = (j * 30 + k * 59 + l * 11) / 100;
/* 305 */     int j1 = (j * 30 + k * 70) / 100;
/* 306 */     int k1 = (j * 30 + l * 70) / 100;
/* 307 */     return i << 24 | i1 << 16 | j1 << 8 | k1;
/*     */   }
/*     */   
/*     */   public static void saveGlTexture(String p_saveGlTexture_0_, int p_saveGlTexture_1_, int p_saveGlTexture_2_, int p_saveGlTexture_3_, int p_saveGlTexture_4_)
/*     */   {
/* 312 */     bindTexture(p_saveGlTexture_1_);
/* 313 */     GL11.glPixelStorei(3333, 1);
/* 314 */     GL11.glPixelStorei(3317, 1);
/*     */     
/* 316 */     for (int i = 0; i <= p_saveGlTexture_2_; i++)
/*     */     {
/* 318 */       File file1 = new File(p_saveGlTexture_0_ + "_" + i + ".png");
/* 319 */       int j = p_saveGlTexture_3_ >> i;
/* 320 */       int k = p_saveGlTexture_4_ >> i;
/* 321 */       int l = j * k;
/* 322 */       IntBuffer intbuffer = BufferUtils.createIntBuffer(l);
/* 323 */       int[] aint = new int[l];
/* 324 */       GL11.glGetTexImage(3553, i, 32993, 33639, intbuffer);
/* 325 */       intbuffer.get(aint);
/* 326 */       BufferedImage bufferedimage = new BufferedImage(j, k, 2);
/* 327 */       bufferedimage.setRGB(0, 0, j, k, aint, 0, j);
/*     */       
/*     */       try
/*     */       {
/* 331 */         ImageIO.write(bufferedimage, "png", file1);
/* 332 */         logger.debug("Exported png to: {}", new Object[] { file1.getAbsolutePath() });
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 336 */         logger.debug("Unable to write: ", exception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_)
/*     */   {
/* 343 */     int[] aint = new int[p_147953_1_];
/* 344 */     int i = p_147953_2_ / 2;
/*     */     
/* 346 */     for (int j = 0; j < i; j++)
/*     */     {
/* 348 */       System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
/* 349 */       System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
/* 350 */       System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 356 */     int i = -16777216;
/* 357 */     int j = -524040;
/* 358 */     int[] aint = { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040 };
/* 359 */     int[] aint1 = { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216 };
/* 360 */     int k = aint.length;
/*     */     
/* 362 */     for (int l = 0; l < 16; l++)
/*     */     {
/* 364 */       System.arraycopy(l < k ? aint : aint1, 0, missingTextureData, 16 * l, k);
/* 365 */       System.arraycopy(l < k ? aint1 : aint, 0, missingTextureData, 16 * l + k, k);
/*     */     }
/*     */     
/* 368 */     missingTexture.updateDynamicTexture(); }
/* 369 */   private static final int[] mipmapBuffer = new int[4];
/*     */   private static final String __OBFID = "CL_00001067";
/*     */   
/*     */   /* Error */
/*     */   public static BufferedImage readBufferedImage(java.io.InputStream imageStream)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokestatic 364	javax/imageio/ImageIO:read	(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;
/*     */     //   4: astore_1
/*     */     //   5: goto +10 -> 15
/*     */     //   8: astore_2
/*     */     //   9: aload_0
/*     */     //   10: invokestatic 372	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   13: aload_2
/*     */     //   14: athrow
/*     */     //   15: aload_0
/*     */     //   16: invokestatic 372	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   19: aload_1
/*     */     //   20: areturn
/*     */     // Line number table:
/*     */     //   Java source line #276	-> byte code offset #0
/*     */     //   Java source line #277	-> byte code offset #5
/*     */     //   Java source line #279	-> byte code offset #8
/*     */     //   Java source line #280	-> byte code offset #9
/*     */     //   Java source line #281	-> byte code offset #13
/*     */     //   Java source line #280	-> byte code offset #15
/*     */     //   Java source line #283	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	21	0	imageStream	java.io.InputStream
/*     */     //   4	2	1	bufferedimage	BufferedImage
/*     */     //   15	5	1	bufferedimage	BufferedImage
/*     */     //   8	6	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	8	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\TextureUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */