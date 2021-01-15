/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ImageBufferDownload implements IImageBuffer
/*     */ {
/*     */   private int[] imageData;
/*     */   private int imageWidth;
/*     */   private int imageHeight;
/*     */   private static final String __OBFID = "CL_00000956";
/*     */   
/*     */   public BufferedImage parseUserSkin(BufferedImage image)
/*     */   {
/*  17 */     if (image == null)
/*     */     {
/*  19 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  23 */     this.imageWidth = 64;
/*  24 */     this.imageHeight = 64;
/*  25 */     int i = image.getWidth();
/*  26 */     int j = image.getHeight();
/*     */     
/*     */ 
/*  29 */     for (int k = 1; (this.imageWidth < i) || (this.imageHeight < j); k *= 2)
/*     */     {
/*  31 */       this.imageWidth *= 2;
/*  32 */       this.imageHeight *= 2;
/*     */     }
/*     */     
/*  35 */     BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
/*  36 */     Graphics graphics = bufferedimage.getGraphics();
/*  37 */     graphics.drawImage(image, 0, 0, null);
/*     */     
/*  39 */     if (image.getHeight() == 32 * k)
/*     */     {
/*  41 */       graphics.drawImage(bufferedimage, 24 * k, 48 * k, 20 * k, 52 * k, 4 * k, 16 * k, 8 * k, 20 * k, null);
/*  42 */       graphics.drawImage(bufferedimage, 28 * k, 48 * k, 24 * k, 52 * k, 8 * k, 16 * k, 12 * k, 20 * k, null);
/*  43 */       graphics.drawImage(bufferedimage, 20 * k, 52 * k, 16 * k, 64 * k, 8 * k, 20 * k, 12 * k, 32 * k, null);
/*  44 */       graphics.drawImage(bufferedimage, 24 * k, 52 * k, 20 * k, 64 * k, 4 * k, 20 * k, 8 * k, 32 * k, null);
/*  45 */       graphics.drawImage(bufferedimage, 28 * k, 52 * k, 24 * k, 64 * k, 0 * k, 20 * k, 4 * k, 32 * k, null);
/*  46 */       graphics.drawImage(bufferedimage, 32 * k, 52 * k, 28 * k, 64 * k, 12 * k, 20 * k, 16 * k, 32 * k, null);
/*  47 */       graphics.drawImage(bufferedimage, 40 * k, 48 * k, 36 * k, 52 * k, 44 * k, 16 * k, 48 * k, 20 * k, null);
/*  48 */       graphics.drawImage(bufferedimage, 44 * k, 48 * k, 40 * k, 52 * k, 48 * k, 16 * k, 52 * k, 20 * k, null);
/*  49 */       graphics.drawImage(bufferedimage, 36 * k, 52 * k, 32 * k, 64 * k, 48 * k, 20 * k, 52 * k, 32 * k, null);
/*  50 */       graphics.drawImage(bufferedimage, 40 * k, 52 * k, 36 * k, 64 * k, 44 * k, 20 * k, 48 * k, 32 * k, null);
/*  51 */       graphics.drawImage(bufferedimage, 44 * k, 52 * k, 40 * k, 64 * k, 40 * k, 20 * k, 44 * k, 32 * k, null);
/*  52 */       graphics.drawImage(bufferedimage, 48 * k, 52 * k, 44 * k, 64 * k, 52 * k, 20 * k, 56 * k, 32 * k, null);
/*     */     }
/*     */     
/*  55 */     graphics.dispose();
/*  56 */     this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
/*  57 */     setAreaOpaque(0, 0, 32 * k, 16 * k);
/*  58 */     setAreaTransparent(32 * k, 0, 64 * k, 32 * k);
/*  59 */     setAreaOpaque(0, 16 * k, 64 * k, 32 * k);
/*  60 */     setAreaTransparent(0, 32 * k, 16 * k, 48 * k);
/*  61 */     setAreaTransparent(16 * k, 32 * k, 40 * k, 48 * k);
/*  62 */     setAreaTransparent(40 * k, 32 * k, 56 * k, 48 * k);
/*  63 */     setAreaTransparent(0, 48 * k, 16 * k, 64 * k);
/*  64 */     setAreaOpaque(16 * k, 48 * k, 48 * k, 64 * k);
/*  65 */     setAreaTransparent(48 * k, 48 * k, 64 * k, 64 * k);
/*  66 */     return bufferedimage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void skinAvailable() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setAreaTransparent(int p_78434_1_, int p_78434_2_, int p_78434_3_, int p_78434_4_)
/*     */   {
/*  81 */     if (!hasTransparency(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_))
/*     */     {
/*  83 */       for (int i = p_78434_1_; i < p_78434_3_; i++)
/*     */       {
/*  85 */         for (int j = p_78434_2_; j < p_78434_4_; j++)
/*     */         {
/*  87 */           this.imageData[(i + j * this.imageWidth)] &= 0xFFFFFF;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setAreaOpaque(int p_78433_1_, int p_78433_2_, int p_78433_3_, int p_78433_4_)
/*     */   {
/*  98 */     for (int i = p_78433_1_; i < p_78433_3_; i++)
/*     */     {
/* 100 */       for (int j = p_78433_2_; j < p_78433_4_; j++)
/*     */       {
/* 102 */         this.imageData[(i + j * this.imageWidth)] |= 0xFF000000;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hasTransparency(int p_78435_1_, int p_78435_2_, int p_78435_3_, int p_78435_4_)
/*     */   {
/* 112 */     for (int i = p_78435_1_; i < p_78435_3_; i++)
/*     */     {
/* 114 */       for (int j = p_78435_2_; j < p_78435_4_; j++)
/*     */       {
/* 116 */         int k = this.imageData[(i + j * this.imageWidth)];
/*     */         
/* 118 */         if ((k >> 24 & 0xFF) < 128)
/*     */         {
/* 120 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ImageBufferDownload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */