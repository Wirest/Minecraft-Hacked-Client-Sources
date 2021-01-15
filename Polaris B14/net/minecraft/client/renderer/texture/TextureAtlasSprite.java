/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.data.AnimationFrame;
/*     */ import net.minecraft.client.resources.data.AnimationMetadataSection;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import optfine.Config;
/*     */ import optfine.TextureUtils;
/*     */ 
/*     */ public class TextureAtlasSprite
/*     */ {
/*     */   private final String iconName;
/*  23 */   protected List framesTextureData = Lists.newArrayList();
/*     */   protected int[][] interpolatedFrameData;
/*     */   private AnimationMetadataSection animationMetadata;
/*     */   protected boolean rotated;
/*     */   protected int originX;
/*     */   protected int originY;
/*     */   protected int width;
/*     */   protected int height;
/*     */   private float minU;
/*     */   private float maxU;
/*     */   private float minV;
/*     */   private float maxV;
/*     */   protected int frameCounter;
/*     */   protected int tickCounter;
/*  37 */   private static String locationNameClock = "builtin/clock";
/*  38 */   private static String locationNameCompass = "builtin/compass";
/*     */   private static final String __OBFID = "CL_00001062";
/*  40 */   private int indexInMap = -1;
/*     */   public float baseU;
/*     */   public float baseV;
/*     */   public int sheetWidth;
/*     */   public int sheetHeight;
/*  45 */   public int glSpriteTextureId = -1;
/*  46 */   public TextureAtlasSprite spriteSingle = null;
/*  47 */   public boolean isSpriteSingle = false;
/*  48 */   public int mipmapLevels = 0;
/*     */   
/*     */   private TextureAtlasSprite(TextureAtlasSprite p_i9_1_)
/*     */   {
/*  52 */     this.iconName = p_i9_1_.iconName;
/*  53 */     this.isSpriteSingle = true;
/*     */   }
/*     */   
/*     */   protected TextureAtlasSprite(String spriteName)
/*     */   {
/*  58 */     this.iconName = spriteName;
/*     */     
/*  60 */     if (Config.isMultiTexture())
/*     */     {
/*  62 */       this.spriteSingle = new TextureAtlasSprite(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation)
/*     */   {
/*  68 */     String s = spriteResourceLocation.toString();
/*  69 */     return locationNameCompass.equals(s) ? new TextureCompass(s) : locationNameClock.equals(s) ? new TextureClock(s) : new TextureAtlasSprite(s);
/*     */   }
/*     */   
/*     */   public static void setLocationNameClock(String clockName)
/*     */   {
/*  74 */     locationNameClock = clockName;
/*     */   }
/*     */   
/*     */   public static void setLocationNameCompass(String compassName)
/*     */   {
/*  79 */     locationNameCompass = compassName;
/*     */   }
/*     */   
/*     */   public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn)
/*     */   {
/*  84 */     this.originX = originInX;
/*  85 */     this.originY = originInY;
/*  86 */     this.rotated = rotatedIn;
/*  87 */     float f = (float)(0.009999999776482582D / inX);
/*  88 */     float f1 = (float)(0.009999999776482582D / inY);
/*  89 */     this.minU = (originInX / (float)inX + f);
/*  90 */     this.maxU = ((originInX + this.width) / (float)inX - f);
/*  91 */     this.minV = (originInY / inY + f1);
/*  92 */     this.maxV = ((originInY + this.height) / inY - f1);
/*  93 */     this.baseU = Math.min(this.minU, this.maxU);
/*  94 */     this.baseV = Math.min(this.minV, this.maxV);
/*     */     
/*  96 */     if (this.spriteSingle != null)
/*     */     {
/*  98 */       this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyFrom(TextureAtlasSprite atlasSpirit)
/*     */   {
/* 104 */     this.originX = atlasSpirit.originX;
/* 105 */     this.originY = atlasSpirit.originY;
/* 106 */     this.width = atlasSpirit.width;
/* 107 */     this.height = atlasSpirit.height;
/* 108 */     this.rotated = atlasSpirit.rotated;
/* 109 */     this.minU = atlasSpirit.minU;
/* 110 */     this.maxU = atlasSpirit.maxU;
/* 111 */     this.minV = atlasSpirit.minV;
/* 112 */     this.maxV = atlasSpirit.maxV;
/*     */     
/* 114 */     if (this.spriteSingle != null)
/*     */     {
/* 116 */       this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOriginX()
/*     */   {
/* 125 */     return this.originX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOriginY()
/*     */   {
/* 133 */     return this.originY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIconWidth()
/*     */   {
/* 141 */     return this.width;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIconHeight()
/*     */   {
/* 149 */     return this.height;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getMinU()
/*     */   {
/* 157 */     return this.minU;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getMaxU()
/*     */   {
/* 165 */     return this.maxU;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getInterpolatedU(double u)
/*     */   {
/* 173 */     float f = this.maxU - this.minU;
/* 174 */     return this.minU + f * (float)u / 16.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getMinV()
/*     */   {
/* 182 */     return this.minV;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getMaxV()
/*     */   {
/* 190 */     return this.maxV;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getInterpolatedV(double v)
/*     */   {
/* 198 */     float f = this.maxV - this.minV;
/* 199 */     return this.minV + f * ((float)v / 16.0F);
/*     */   }
/*     */   
/*     */   public String getIconName()
/*     */   {
/* 204 */     return this.iconName;
/*     */   }
/*     */   
/*     */   public void updateAnimation()
/*     */   {
/* 209 */     this.tickCounter += 1;
/*     */     
/* 211 */     if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
/*     */     {
/* 213 */       int i = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 214 */       int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
/* 215 */       this.frameCounter = ((this.frameCounter + 1) % j);
/* 216 */       this.tickCounter = 0;
/* 217 */       int k = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 218 */       boolean flag = false;
/* 219 */       boolean flag1 = this.isSpriteSingle;
/*     */       
/* 221 */       if ((i != k) && (k >= 0) && (k < this.framesTextureData.size()))
/*     */       {
/* 223 */         TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, flag, flag1);
/*     */       }
/*     */     }
/* 226 */     else if (this.animationMetadata.isInterpolate())
/*     */     {
/* 228 */       updateAnimationInterpolated();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateAnimationInterpolated()
/*     */   {
/* 234 */     double d0 = 1.0D - this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
/* 235 */     int i = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 236 */     int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
/* 237 */     int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
/*     */     
/* 239 */     if ((i != k) && (k >= 0) && (k < this.framesTextureData.size()))
/*     */     {
/* 241 */       int[][] aint = (int[][])this.framesTextureData.get(i);
/* 242 */       int[][] aint1 = (int[][])this.framesTextureData.get(k);
/*     */       
/* 244 */       if ((this.interpolatedFrameData == null) || (this.interpolatedFrameData.length != aint.length))
/*     */       {
/* 246 */         this.interpolatedFrameData = new int[aint.length][];
/*     */       }
/*     */       
/* 249 */       for (int l = 0; l < aint.length; l++)
/*     */       {
/* 251 */         if (this.interpolatedFrameData[l] == null)
/*     */         {
/* 253 */           this.interpolatedFrameData[l] = new int[aint[l].length];
/*     */         }
/*     */         
/* 256 */         if ((l < aint1.length) && (aint1[l].length == aint[l].length))
/*     */         {
/* 258 */           for (int i1 = 0; i1 < aint[l].length; i1++)
/*     */           {
/* 260 */             int j1 = aint[l][i1];
/* 261 */             int k1 = aint1[l][i1];
/* 262 */             int l1 = (int)(((j1 & 0xFF0000) >> 16) * d0 + ((k1 & 0xFF0000) >> 16) * (1.0D - d0));
/* 263 */             int i2 = (int)(((j1 & 0xFF00) >> 8) * d0 + ((k1 & 0xFF00) >> 8) * (1.0D - d0));
/* 264 */             int j2 = (int)((j1 & 0xFF) * d0 + (k1 & 0xFF) * (1.0D - d0));
/* 265 */             this.interpolatedFrameData[l][i1] = (j1 & 0xFF000000 | l1 << 16 | i2 << 8 | j2);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 270 */       TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public int[][] getFrameTextureData(int index)
/*     */   {
/* 276 */     return (int[][])this.framesTextureData.get(index);
/*     */   }
/*     */   
/*     */   public int getFrameCount()
/*     */   {
/* 281 */     return this.framesTextureData.size();
/*     */   }
/*     */   
/*     */   public void setIconWidth(int newWidth)
/*     */   {
/* 286 */     this.width = newWidth;
/*     */     
/* 288 */     if (this.spriteSingle != null)
/*     */     {
/* 290 */       this.spriteSingle.setIconWidth(this.width);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setIconHeight(int newHeight)
/*     */   {
/* 296 */     this.height = newHeight;
/*     */     
/* 298 */     if (this.spriteSingle != null)
/*     */     {
/* 300 */       this.spriteSingle.setIconHeight(this.height);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadSprite(BufferedImage[] images, AnimationMetadataSection meta) throws IOException
/*     */   {
/* 306 */     resetSprite();
/* 307 */     int i = images[0].getWidth();
/* 308 */     int j = images[0].getHeight();
/* 309 */     this.width = i;
/* 310 */     this.height = j;
/* 311 */     int[][] aint = new int[images.length][];
/*     */     
/* 313 */     for (int k = 0; k < images.length; k++)
/*     */     {
/* 315 */       BufferedImage bufferedimage = images[k];
/*     */       
/* 317 */       if (bufferedimage != null)
/*     */       {
/* 319 */         if ((k > 0) && ((bufferedimage.getWidth() != i >> k) || (bufferedimage.getHeight() != j >> k)))
/*     */         {
/* 321 */           throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] { Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k) }));
/*     */         }
/*     */         
/* 324 */         aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
/* 325 */         bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
/*     */       }
/*     */     }
/*     */     
/* 329 */     if (meta == null)
/*     */     {
/* 331 */       if (j != i)
/*     */       {
/* 333 */         throw new RuntimeException("broken aspect ratio and not an animation");
/*     */       }
/*     */       
/* 336 */       this.framesTextureData.add(aint);
/*     */     }
/*     */     else
/*     */     {
/* 340 */       int j1 = j / i;
/* 341 */       int k1 = i;
/* 342 */       int l = i;
/* 343 */       this.height = this.width;
/*     */       
/* 345 */       if (meta.getFrameCount() > 0)
/*     */       {
/* 347 */         Iterator iterator = meta.getFrameIndexSet().iterator();
/*     */         
/* 349 */         while (iterator.hasNext())
/*     */         {
/* 351 */           int i1 = ((Integer)iterator.next()).intValue();
/*     */           
/* 353 */           if (i1 >= j1)
/*     */           {
/* 355 */             throw new RuntimeException("invalid frameindex " + i1);
/*     */           }
/*     */           
/* 358 */           allocateFrameTextureData(i1);
/* 359 */           this.framesTextureData.set(i1, getFrameTextureData(aint, k1, l, i1));
/*     */         }
/*     */         
/* 362 */         this.animationMetadata = meta;
/*     */       }
/*     */       else
/*     */       {
/* 366 */         ArrayList arraylist = Lists.newArrayList();
/*     */         
/* 368 */         for (int i2 = 0; i2 < j1; i2++)
/*     */         {
/* 370 */           this.framesTextureData.add(getFrameTextureData(aint, k1, l, i2));
/* 371 */           arraylist.add(new AnimationFrame(i2, -1));
/*     */         }
/*     */         
/* 374 */         this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, meta.getFrameTime(), meta.isInterpolate());
/*     */       }
/*     */     }
/*     */     
/* 378 */     for (int l1 = 0; l1 < this.framesTextureData.size(); l1++)
/*     */     {
/* 380 */       int[][] aint1 = (int[][])this.framesTextureData.get(l1);
/*     */       
/* 382 */       if ((aint1 != null) && (!this.iconName.startsWith("minecraft:blocks/leaves_")))
/*     */       {
/* 384 */         for (int j2 = 0; j2 < aint1.length; j2++)
/*     */         {
/* 386 */           int[] aint2 = aint1[j2];
/* 387 */           fixTransparentColor(aint2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 392 */     if (this.spriteSingle != null)
/*     */     {
/* 394 */       this.spriteSingle.loadSprite(images, meta);
/*     */     }
/*     */   }
/*     */   
/*     */   public void generateMipmaps(int level)
/*     */   {
/* 400 */     ArrayList arraylist = Lists.newArrayList();
/*     */     
/* 402 */     for (int i = 0; i < this.framesTextureData.size(); i++)
/*     */     {
/* 404 */       final int[][] aint = (int[][])this.framesTextureData.get(i);
/*     */       
/* 406 */       if (aint != null)
/*     */       {
/*     */         try
/*     */         {
/* 410 */           arraylist.add(TextureUtil.generateMipmapData(level, this.width, aint));
/*     */         }
/*     */         catch (Throwable throwable)
/*     */         {
/* 414 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
/* 415 */           CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
/* 416 */           crashreportcategory.addCrashSection("Frame index", Integer.valueOf(i));
/* 417 */           crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable()
/*     */           {
/*     */             private static final String __OBFID = "CL_00001063";
/*     */             
/*     */             public String call() throws Exception {
/* 422 */               StringBuilder stringbuilder = new StringBuilder();
/*     */               int[][] arrayOfInt;
/* 424 */               int j = (arrayOfInt = aint).length; for (int i = 0; i < j; i++) { int[] aint1 = arrayOfInt[i];
/*     */                 
/* 426 */                 if (stringbuilder.length() > 0)
/*     */                 {
/* 428 */                   stringbuilder.append(", ");
/*     */                 }
/*     */                 
/* 431 */                 stringbuilder.append(aint1 == null ? "null" : Integer.valueOf(aint1.length));
/*     */               }
/*     */               
/* 434 */               return stringbuilder.toString();
/*     */             }
/* 436 */           });
/* 437 */           throw new net.minecraft.util.ReportedException(crashreport);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 442 */     setFramesTextureData(arraylist);
/*     */     
/* 444 */     if (this.spriteSingle != null)
/*     */     {
/* 446 */       this.spriteSingle.generateMipmaps(level);
/*     */     }
/*     */   }
/*     */   
/*     */   private void allocateFrameTextureData(int index)
/*     */   {
/* 452 */     if (this.framesTextureData.size() <= index)
/*     */     {
/* 454 */       for (int i = this.framesTextureData.size(); i <= index; i++)
/*     */       {
/* 456 */         this.framesTextureData.add(null);
/*     */       }
/*     */     }
/*     */     
/* 460 */     if (this.spriteSingle != null)
/*     */     {
/* 462 */       this.spriteSingle.allocateFrameTextureData(index);
/*     */     }
/*     */   }
/*     */   
/*     */   private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_)
/*     */   {
/* 468 */     int[][] aint = new int[data.length][];
/*     */     
/* 470 */     for (int i = 0; i < data.length; i++)
/*     */     {
/* 472 */       int[] aint1 = data[i];
/*     */       
/* 474 */       if (aint1 != null)
/*     */       {
/* 476 */         aint[i] = new int[(rows >> i) * (columns >> i)];
/* 477 */         System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
/*     */       }
/*     */     }
/*     */     
/* 481 */     return aint;
/*     */   }
/*     */   
/*     */   public void clearFramesTextureData()
/*     */   {
/* 486 */     this.framesTextureData.clear();
/*     */     
/* 488 */     if (this.spriteSingle != null)
/*     */     {
/* 490 */       this.spriteSingle.clearFramesTextureData();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasAnimationMetadata()
/*     */   {
/* 496 */     return this.animationMetadata != null;
/*     */   }
/*     */   
/*     */   public void setFramesTextureData(List newFramesTextureData)
/*     */   {
/* 501 */     this.framesTextureData = newFramesTextureData;
/*     */     
/* 503 */     if (this.spriteSingle != null)
/*     */     {
/* 505 */       this.spriteSingle.setFramesTextureData(newFramesTextureData);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetSprite()
/*     */   {
/* 511 */     this.animationMetadata = null;
/* 512 */     setFramesTextureData(Lists.newArrayList());
/* 513 */     this.frameCounter = 0;
/* 514 */     this.tickCounter = 0;
/*     */     
/* 516 */     if (this.spriteSingle != null)
/*     */     {
/* 518 */       this.spriteSingle.resetSprite();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 524 */     return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
/*     */   }
/*     */   
/*     */   public boolean hasCustomLoader(IResourceManager p_hasCustomLoader_1_, ResourceLocation p_hasCustomLoader_2_)
/*     */   {
/* 529 */     return false;
/*     */   }
/*     */   
/*     */   public boolean load(IResourceManager p_load_1_, ResourceLocation p_load_2_)
/*     */   {
/* 534 */     return true;
/*     */   }
/*     */   
/*     */   public int getIndexInMap()
/*     */   {
/* 539 */     return this.indexInMap;
/*     */   }
/*     */   
/*     */   public void setIndexInMap(int p_setIndexInMap_1_)
/*     */   {
/* 544 */     this.indexInMap = p_setIndexInMap_1_;
/*     */   }
/*     */   
/*     */   private void fixTransparentColor(int[] p_fixTransparentColor_1_)
/*     */   {
/* 549 */     if (p_fixTransparentColor_1_ != null)
/*     */     {
/* 551 */       long i = 0L;
/* 552 */       long j = 0L;
/* 553 */       long k = 0L;
/* 554 */       long l = 0L;
/*     */       
/* 556 */       for (int i1 = 0; i1 < p_fixTransparentColor_1_.length; i1++)
/*     */       {
/* 558 */         int j1 = p_fixTransparentColor_1_[i1];
/* 559 */         int k1 = j1 >> 24 & 0xFF;
/*     */         
/* 561 */         if (k1 >= 16)
/*     */         {
/* 563 */           int l1 = j1 >> 16 & 0xFF;
/* 564 */           int i2 = j1 >> 8 & 0xFF;
/* 565 */           int j2 = j1 & 0xFF;
/* 566 */           i += l1;
/* 567 */           j += i2;
/* 568 */           k += j2;
/* 569 */           l += 1L;
/*     */         }
/*     */       }
/*     */       
/* 573 */       if (l > 0L)
/*     */       {
/* 575 */         int l2 = (int)(i / l);
/* 576 */         int i3 = (int)(j / l);
/* 577 */         int j3 = (int)(k / l);
/* 578 */         int k3 = l2 << 16 | i3 << 8 | j3;
/*     */         
/* 580 */         for (int l3 = 0; l3 < p_fixTransparentColor_1_.length; l3++)
/*     */         {
/* 582 */           int i4 = p_fixTransparentColor_1_[l3];
/* 583 */           int k2 = i4 >> 24 & 0xFF;
/*     */           
/* 585 */           if (k2 <= 16)
/*     */           {
/* 587 */             p_fixTransparentColor_1_[l3] = k3;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double getSpriteU16(float p_getSpriteU16_1_)
/*     */   {
/* 596 */     float f = this.maxU - this.minU;
/* 597 */     return (p_getSpriteU16_1_ - this.minU) / f * 16.0F;
/*     */   }
/*     */   
/*     */   public double getSpriteV16(float p_getSpriteV16_1_)
/*     */   {
/* 602 */     float f = this.maxV - this.minV;
/* 603 */     return (p_getSpriteV16_1_ - this.minV) / f * 16.0F;
/*     */   }
/*     */   
/*     */   public void bindSpriteTexture()
/*     */   {
/* 608 */     if (this.glSpriteTextureId < 0)
/*     */     {
/* 610 */       this.glSpriteTextureId = TextureUtil.glGenTextures();
/* 611 */       TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
/* 612 */       TextureUtils.applyAnisotropicLevel();
/*     */     }
/*     */     
/* 615 */     TextureUtils.bindTexture(this.glSpriteTextureId);
/*     */   }
/*     */   
/*     */   public void deleteSpriteTexture()
/*     */   {
/* 620 */     if (this.glSpriteTextureId >= 0)
/*     */     {
/* 622 */       TextureUtil.deleteTexture(this.glSpriteTextureId);
/* 623 */       this.glSpriteTextureId = -1;
/*     */     }
/*     */   }
/*     */   
/*     */   public float toSingleU(float p_toSingleU_1_)
/*     */   {
/* 629 */     p_toSingleU_1_ -= this.baseU;
/* 630 */     float f = this.sheetWidth / this.width;
/* 631 */     p_toSingleU_1_ *= f;
/* 632 */     return p_toSingleU_1_;
/*     */   }
/*     */   
/*     */   public float toSingleV(float p_toSingleV_1_)
/*     */   {
/* 637 */     p_toSingleV_1_ -= this.baseV;
/* 638 */     float f = this.sheetHeight / this.height;
/* 639 */     p_toSingleV_1_ *= f;
/* 640 */     return p_toSingleV_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\TextureAtlasSprite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */