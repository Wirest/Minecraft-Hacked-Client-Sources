/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.SimpleTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ThreadDownloadImageData extends SimpleTexture
/*     */ {
/*  21 */   private static final Logger logger = ;
/*  22 */   private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
/*     */   private final File cacheFile;
/*     */   private final String imageUrl;
/*     */   private final IImageBuffer imageBuffer;
/*     */   private BufferedImage bufferedImage;
/*     */   private Thread imageThread;
/*     */   private boolean textureUploaded;
/*     */   private static final String __OBFID = "CL_00001049";
/*  30 */   public Boolean imageFound = null;
/*     */   
/*     */   public ThreadDownloadImageData(File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, IImageBuffer imageBufferIn)
/*     */   {
/*  34 */     super(textureResourceLocation);
/*  35 */     this.cacheFile = cacheFileIn;
/*  36 */     this.imageUrl = imageUrlIn;
/*  37 */     this.imageBuffer = imageBufferIn;
/*     */   }
/*     */   
/*     */   private void checkTextureUploaded()
/*     */   {
/*  42 */     if ((!this.textureUploaded) && (this.bufferedImage != null))
/*     */     {
/*  44 */       if (this.textureLocation != null)
/*     */       {
/*  46 */         deleteGlTexture();
/*     */       }
/*     */       
/*  49 */       TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
/*  50 */       this.textureUploaded = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getGlTextureId()
/*     */   {
/*  56 */     checkTextureUploaded();
/*  57 */     return super.getGlTextureId();
/*     */   }
/*     */   
/*     */   public void setBufferedImage(BufferedImage bufferedImageIn)
/*     */   {
/*  62 */     this.bufferedImage = bufferedImageIn;
/*     */     
/*  64 */     if (this.imageBuffer != null)
/*     */     {
/*  66 */       this.imageBuffer.skinAvailable();
/*     */     }
/*     */     
/*  69 */     this.imageFound = Boolean.valueOf(this.bufferedImage != null);
/*     */   }
/*     */   
/*     */   public void loadTexture(IResourceManager resourceManager) throws IOException
/*     */   {
/*  74 */     if ((this.bufferedImage == null) && (this.textureLocation != null))
/*     */     {
/*  76 */       super.loadTexture(resourceManager);
/*     */     }
/*     */     
/*  79 */     if (this.imageThread == null)
/*     */     {
/*  81 */       if ((this.cacheFile != null) && (this.cacheFile.isFile()))
/*     */       {
/*  83 */         logger.debug("Loading http texture from local cache ({})", new Object[] { this.cacheFile });
/*     */         
/*     */         try
/*     */         {
/*  87 */           this.bufferedImage = ImageIO.read(this.cacheFile);
/*     */           
/*  89 */           if (this.imageBuffer != null)
/*     */           {
/*  91 */             setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
/*     */           }
/*     */           
/*  94 */           this.imageFound = Boolean.valueOf(this.bufferedImage != null);
/*     */         }
/*     */         catch (IOException ioexception)
/*     */         {
/*  98 */           logger.error("Couldn't load skin " + this.cacheFile, ioexception);
/*  99 */           loadTextureFromServer();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 104 */         loadTextureFromServer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void loadTextureFromServer()
/*     */   {
/* 111 */     this.imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet())
/*     */     {
/*     */       private static final String __OBFID = "CL_00001050";
/*     */       
/*     */       public void run() {
/* 116 */         HttpURLConnection httpurlconnection = null;
/* 117 */         ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}", new Object[] { ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile });
/*     */         
/*     */         try
/*     */         {
/* 121 */           httpurlconnection = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
/* 122 */           httpurlconnection.setDoInput(true);
/* 123 */           httpurlconnection.setDoOutput(false);
/* 124 */           httpurlconnection.connect();
/*     */           
/* 126 */           if (httpurlconnection.getResponseCode() / 100 != 2) {
/*     */             return;
/*     */           }
/*     */           
/*     */           BufferedImage bufferedimage;
/*     */           
/*     */           BufferedImage bufferedimage;
/* 133 */           if (ThreadDownloadImageData.this.cacheFile != null)
/*     */           {
/* 135 */             FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
/* 136 */             bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
/*     */           }
/*     */           else
/*     */           {
/* 140 */             bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
/*     */           }
/*     */           
/* 143 */           if (ThreadDownloadImageData.this.imageBuffer != null)
/*     */           {
/* 145 */             bufferedimage = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(bufferedimage);
/*     */           }
/*     */           
/* 148 */           ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 152 */           ThreadDownloadImageData.logger.error("Couldn't download http texture", exception);
/* 153 */           return;
/*     */         }
/*     */         finally
/*     */         {
/* 157 */           if (httpurlconnection != null)
/*     */           {
/* 159 */             httpurlconnection.disconnect();
/*     */           }
/*     */           
/* 162 */           ThreadDownloadImageData.this.imageFound = Boolean.valueOf(ThreadDownloadImageData.this.bufferedImage != null);
/*     */         }
/* 157 */         if (httpurlconnection != null)
/*     */         {
/* 159 */           httpurlconnection.disconnect();
/*     */         }
/*     */         
/* 162 */         ThreadDownloadImageData.this.imageFound = Boolean.valueOf(ThreadDownloadImageData.this.bufferedImage != null);
/*     */       }
/*     */       
/* 165 */     };
/* 166 */     this.imageThread.setDaemon(true);
/* 167 */     this.imageThread.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ThreadDownloadImageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */