/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.StitcherException;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.data.AnimationMetadataSection;
/*     */ import net.minecraft.client.resources.data.TextureMetadataSection;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import optfine.Config;
/*     */ import optfine.ConnectedTextures;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ import optfine.TextureUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TextureMap extends AbstractTexture implements ITickableTextureObject
/*     */ {
/*  38 */   private static final Logger logger = ;
/*  39 */   public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
/*  40 */   public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
/*     */   private final List listAnimatedSprites;
/*     */   private final Map mapRegisteredSprites;
/*     */   private final Map mapUploadedSprites;
/*     */   private final String basePath;
/*     */   private final IIconCreator iconCreator;
/*     */   private int mipmapLevels;
/*     */   private final TextureAtlasSprite missingImage;
/*     */   private static final String __OBFID = "CL_00001058";
/*     */   
/*     */   public TextureMap(String p_i46099_1_)
/*     */   {
/*  52 */     this(p_i46099_1_, null);
/*     */   }
/*     */   
/*     */   public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn)
/*     */   {
/*  57 */     this.listAnimatedSprites = Lists.newArrayList();
/*  58 */     this.mapRegisteredSprites = Maps.newHashMap();
/*  59 */     this.mapUploadedSprites = Maps.newHashMap();
/*  60 */     this.missingImage = new TextureAtlasSprite("missingno");
/*  61 */     this.basePath = p_i46100_1_;
/*  62 */     this.iconCreator = iconCreatorIn;
/*     */   }
/*     */   
/*     */   private void initMissingImage()
/*     */   {
/*  67 */     int i = getMinSpriteSize();
/*  68 */     int[] aint = getMissingImageData(i);
/*  69 */     this.missingImage.setIconWidth(i);
/*  70 */     this.missingImage.setIconHeight(i);
/*  71 */     int[][] aint1 = new int[this.mipmapLevels + 1][];
/*  72 */     aint1[0] = aint;
/*  73 */     this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] { aint1 }));
/*  74 */     this.missingImage.setIndexInMap(0);
/*     */   }
/*     */   
/*     */   public void loadTexture(IResourceManager resourceManager) throws IOException
/*     */   {
/*  79 */     if (this.iconCreator != null)
/*     */     {
/*  81 */       loadSprites(resourceManager, this.iconCreator);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_)
/*     */   {
/*  87 */     this.mapRegisteredSprites.clear();
/*  88 */     p_174943_2_.registerSprites(this);
/*     */     
/*  90 */     if (this.mipmapLevels >= 4)
/*     */     {
/*  92 */       this.mipmapLevels = detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
/*  93 */       Config.log("Mipmap levels: " + this.mipmapLevels);
/*     */     }
/*     */     
/*  96 */     initMissingImage();
/*  97 */     deleteGlTexture();
/*  98 */     loadTextureAtlas(resourceManager);
/*     */   }
/*     */   
/*     */   public void loadTextureAtlas(IResourceManager resourceManager)
/*     */   {
/* 103 */     Config.dbg("Multitexture: " + Config.isMultiTexture());
/*     */     
/* 105 */     if (Config.isMultiTexture())
/*     */     {
/* 107 */       for (Object textureatlassprite : this.mapUploadedSprites.values())
/*     */       {
/* 109 */         ((TextureAtlasSprite)textureatlassprite).deleteSpriteTexture();
/*     */       }
/*     */     }
/*     */     
/* 113 */     ConnectedTextures.updateIcons(this);
/* 114 */     int l1 = Minecraft.getGLMaximumTextureSize();
/* 115 */     Stitcher stitcher = new Stitcher(l1, l1, true, 0, this.mipmapLevels);
/* 116 */     this.mapUploadedSprites.clear();
/* 117 */     this.listAnimatedSprites.clear();
/* 118 */     int i = Integer.MAX_VALUE;
/* 119 */     Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[] { this });
/* 120 */     int j = getMinSpriteSize();
/* 121 */     int k = 1 << this.mipmapLevels;
/*     */     ResourceLocation resourcelocation;
/* 123 */     for (Object entry : this.mapRegisteredSprites.entrySet())
/*     */     {
/* 125 */       TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)((Map.Entry)entry).getValue();
/* 126 */       resourcelocation = new ResourceLocation(textureatlassprite1.getIconName());
/* 127 */       ResourceLocation resourcelocation1 = completeResourceLocation(resourcelocation, 0);
/*     */       
/* 129 */       if (!textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation))
/*     */       {
/*     */         try
/*     */         {
/* 133 */           IResource iresource = resourceManager.getResource(resourcelocation1);
/* 134 */           BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
/* 135 */           abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
/*     */           
/* 137 */           if ((this.mipmapLevels > 0) && (abufferedimage != null))
/*     */           {
/* 139 */             int l = abufferedimage[0].getWidth();
/* 140 */             abufferedimage[0] = TextureUtils.scaleToPowerOfTwo(abufferedimage[0], j);
/* 141 */             int i1 = abufferedimage[0].getWidth();
/*     */             
/* 143 */             if (!TextureUtils.isPowerOfTwo(l))
/*     */             {
/* 145 */               Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + l + " -> " + i1);
/*     */             }
/*     */           }
/*     */           
/* 149 */           TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
/*     */           
/* 151 */           if (texturemetadatasection != null)
/*     */           {
/* 153 */             List list = texturemetadatasection.getListMipmaps();
/*     */             
/* 155 */             if (!list.isEmpty())
/*     */             {
/* 157 */               int k1 = abufferedimage[0].getWidth();
/* 158 */               int j1 = abufferedimage[0].getHeight();
/*     */               
/* 160 */               if ((MathHelper.roundUpToPowerOfTwo(k1) != k1) || (MathHelper.roundUpToPowerOfTwo(j1) != j1))
/*     */               {
/* 162 */                 throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
/*     */               }
/*     */             }
/*     */             
/* 166 */             Iterator iterator = list.iterator();
/*     */             
/* 168 */             while (iterator.hasNext())
/*     */             {
/* 170 */               int j3 = ((Integer)iterator.next()).intValue();
/*     */               
/* 172 */               if ((j3 > 0) && (j3 < abufferedimage.length - 1) && (abufferedimage[j3] == null))
/*     */               {
/* 174 */                 ResourceLocation resourcelocation2 = completeResourceLocation(resourcelocation, j3);
/*     */                 
/*     */                 try
/*     */                 {
/* 178 */                   abufferedimage[j3] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation2).getInputStream());
/*     */                 }
/*     */                 catch (IOException ioexception)
/*     */                 {
/* 182 */                   logger.error("Unable to load miplevel {} from: {}", new Object[] { Integer.valueOf(j3), resourcelocation2, ioexception });
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 188 */           AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
/* 189 */           textureatlassprite1.loadSprite(abufferedimage, animationmetadatasection);
/*     */         }
/*     */         catch (RuntimeException runtimeexception)
/*     */         {
/* 193 */           logger.error("Unable to parse metadata from " + resourcelocation1, runtimeexception);
/* 194 */           continue;
/*     */         }
/*     */         catch (IOException ioexception1)
/*     */         {
/* 198 */           logger.error("Using missing texture, unable to load " + resourcelocation1 + ", " + ioexception1.getClass().getName());
/* 199 */           continue;
/*     */         }
/*     */         
/* 202 */         i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
/* 203 */         int k2 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));
/*     */         
/* 205 */         if (k2 < k)
/*     */         {
/* 207 */           logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { resourcelocation1, Integer.valueOf(textureatlassprite1.getIconWidth()), Integer.valueOf(textureatlassprite1.getIconHeight()), Integer.valueOf(MathHelper.calculateLogBaseTwo(k)), Integer.valueOf(MathHelper.calculateLogBaseTwo(k2)) });
/* 208 */           k = k2;
/*     */         }
/*     */         
/* 211 */         stitcher.addSprite(textureatlassprite1);
/*     */       }
/* 213 */       else if (!textureatlassprite1.load(resourceManager, resourcelocation))
/*     */       {
/* 215 */         i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
/* 216 */         stitcher.addSprite(textureatlassprite1);
/*     */       }
/*     */     }
/*     */     
/* 220 */     int i2 = Math.min(i, k);
/* 221 */     int j2 = MathHelper.calculateLogBaseTwo(i2);
/*     */     
/* 223 */     if (j2 < 0)
/*     */     {
/* 225 */       j2 = 0;
/*     */     }
/*     */     
/* 228 */     if (j2 < this.mipmapLevels)
/*     */     {
/* 230 */       logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(j2), Integer.valueOf(i2) });
/* 231 */       this.mipmapLevels = j2;
/*     */     }
/*     */     final TextureAtlasSprite textureatlassprite2;
/* 234 */     for (Object textureatlassprite20 : this.mapRegisteredSprites.values())
/*     */     {
/* 236 */       textureatlassprite2 = (TextureAtlasSprite)textureatlassprite20;
/*     */       
/*     */       try
/*     */       {
/* 240 */         textureatlassprite2.generateMipmaps(this.mipmapLevels);
/*     */       }
/*     */       catch (Throwable throwable1)
/*     */       {
/* 244 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
/* 245 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
/* 246 */         crashreportcategory.addCrashSectionCallable("Sprite name", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00001059";
/*     */           
/*     */           public String call() throws Exception {
/* 251 */             return textureatlassprite2.getIconName();
/*     */           }
/* 253 */         });
/* 254 */         crashreportcategory.addCrashSectionCallable("Sprite size", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00001060";
/*     */           
/*     */           public String call() throws Exception {
/* 259 */             return textureatlassprite2.getIconWidth() + " x " + textureatlassprite2.getIconHeight();
/*     */           }
/* 261 */         });
/* 262 */         crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00001061";
/*     */           
/*     */           public String call() throws Exception {
/* 267 */             return textureatlassprite2.getFrameCount() + " frames";
/*     */           }
/* 269 */         });
/* 270 */         crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
/* 271 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */     
/* 275 */     this.missingImage.generateMipmaps(this.mipmapLevels);
/* 276 */     stitcher.addSprite(this.missingImage);
/*     */     
/*     */     try
/*     */     {
/* 280 */       stitcher.doStitch();
/*     */     }
/*     */     catch (StitcherException stitcherexception)
/*     */     {
/* 284 */       throw stitcherexception;
/*     */     }
/*     */     
/* 287 */     logger.info("Created: {}x{} {}-atlas", new Object[] { Integer.valueOf(stitcher.getCurrentWidth()), Integer.valueOf(stitcher.getCurrentHeight()), this.basePath });
/* 288 */     TextureUtil.allocateTextureImpl(getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
/* 289 */     HashMap hashmap = Maps.newHashMap(this.mapRegisteredSprites);
/*     */     String s;
/* 291 */     for (Object textureatlassprite30 : stitcher.getStichSlots())
/*     */     {
/* 293 */       TextureAtlasSprite textureatlassprite3 = (TextureAtlasSprite)textureatlassprite30;
/* 294 */       s = textureatlassprite3.getIconName();
/* 295 */       hashmap.remove(s);
/* 296 */       this.mapUploadedSprites.put(s, textureatlassprite3);
/*     */       
/*     */       try
/*     */       {
/* 300 */         TextureUtil.uploadTextureMipmap(textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/* 304 */         CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
/* 305 */         CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
/* 306 */         crashreportcategory1.addCrashSection("Atlas path", this.basePath);
/* 307 */         crashreportcategory1.addCrashSection("Sprite", textureatlassprite3);
/* 308 */         throw new ReportedException(crashreport1);
/*     */       }
/*     */       
/* 311 */       if (textureatlassprite3.hasAnimationMetadata())
/*     */       {
/* 313 */         this.listAnimatedSprites.add(textureatlassprite3);
/*     */       }
/*     */     }
/*     */     
/* 317 */     for (Object textureatlassprite4 : hashmap.values())
/*     */     {
/* 319 */       ((TextureAtlasSprite)textureatlassprite4).copyFrom(this.missingImage);
/*     */     }
/*     */     
/* 322 */     if (Config.isMultiTexture())
/*     */     {
/* 324 */       int l2 = stitcher.getCurrentWidth();
/* 325 */       int i3 = stitcher.getCurrentHeight();
/*     */       
/* 327 */       for (Object textureatlassprite50 : stitcher.getStichSlots())
/*     */       {
/* 329 */         TextureAtlasSprite textureatlassprite5 = (TextureAtlasSprite)textureatlassprite50;
/* 330 */         textureatlassprite5.sheetWidth = l2;
/* 331 */         textureatlassprite5.sheetHeight = i3;
/* 332 */         textureatlassprite5.mipmapLevels = this.mipmapLevels;
/* 333 */         TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;
/*     */         
/* 335 */         if (textureatlassprite6 != null)
/*     */         {
/* 337 */           textureatlassprite6.sheetWidth = l2;
/* 338 */           textureatlassprite6.sheetHeight = i3;
/* 339 */           textureatlassprite6.mipmapLevels = this.mipmapLevels;
/* 340 */           textureatlassprite5.bindSpriteTexture();
/* 341 */           boolean flag = false;
/* 342 */           boolean flag1 = true;
/* 343 */           TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag, flag1);
/*     */         }
/*     */       }
/*     */       
/* 347 */       Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
/*     */     }
/*     */     
/* 350 */     Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[] { this });
/*     */     
/* 352 */     if (Config.equals(System.getProperty("saveTextureMap"), "true"))
/*     */     {
/* 354 */       TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
/*     */     }
/*     */   }
/*     */   
/*     */   private ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_)
/*     */   {
/* 360 */     return p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] { this.basePath, location.getResourcePath(), ".png" })) : isAbsoluteLocation(location) ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "mipmap" + p_147634_2_ + ".png") : p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] { this.basePath, location.getResourcePath(), Integer.valueOf(p_147634_2_), ".png" }));
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getAtlasSprite(String iconName)
/*     */   {
/* 365 */     TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapUploadedSprites.get(iconName);
/*     */     
/* 367 */     if (textureatlassprite == null)
/*     */     {
/* 369 */       textureatlassprite = this.missingImage;
/*     */     }
/*     */     
/* 372 */     return textureatlassprite;
/*     */   }
/*     */   
/*     */   public void updateAnimations()
/*     */   {
/* 377 */     TextureUtil.bindTexture(getGlTextureId());
/*     */     
/* 379 */     for (Object textureatlassprite0 : this.listAnimatedSprites)
/*     */     {
/* 381 */       TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)textureatlassprite0;
/*     */       
/* 383 */       if (isTerrainAnimationActive(textureatlassprite))
/*     */       {
/* 385 */         textureatlassprite.updateAnimation();
/*     */       }
/*     */     }
/*     */     
/* 389 */     if (Config.isMultiTexture())
/*     */     {
/* 391 */       for (Object textureatlassprite10 : this.listAnimatedSprites)
/*     */       {
/* 393 */         TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)textureatlassprite10;
/*     */         
/* 395 */         if (isTerrainAnimationActive(textureatlassprite1))
/*     */         {
/* 397 */           TextureAtlasSprite textureatlassprite2 = textureatlassprite1.spriteSingle;
/*     */           
/* 399 */           if (textureatlassprite2 != null)
/*     */           {
/* 401 */             textureatlassprite1.bindSpriteTexture();
/* 402 */             textureatlassprite2.updateAnimation();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 407 */       TextureUtil.bindTexture(getGlTextureId());
/*     */     }
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite registerSprite(ResourceLocation location)
/*     */   {
/* 413 */     if (location == null)
/*     */     {
/* 415 */       throw new IllegalArgumentException("Location cannot be null!");
/*     */     }
/*     */     
/*     */ 
/* 419 */     TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapRegisteredSprites.get(location.toString());
/*     */     
/* 421 */     if ((textureatlassprite == null) && (Reflector.ModLoader_getCustomAnimationLogic.exists()))
/*     */     {
/* 423 */       textureatlassprite = (TextureAtlasSprite)Reflector.call(Reflector.ModLoader_getCustomAnimationLogic, new Object[] { location });
/*     */     }
/*     */     
/* 426 */     if (textureatlassprite == null)
/*     */     {
/* 428 */       textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
/* 429 */       this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
/*     */       
/* 431 */       if (((textureatlassprite instanceof TextureAtlasSprite)) && (textureatlassprite.getIndexInMap() < 0))
/*     */       {
/* 433 */         textureatlassprite.setIndexInMap(this.mapRegisteredSprites.size());
/*     */       }
/*     */     }
/*     */     
/* 437 */     return textureatlassprite;
/*     */   }
/*     */   
/*     */ 
/*     */   public void tick()
/*     */   {
/* 443 */     updateAnimations();
/*     */   }
/*     */   
/*     */   public void setMipmapLevels(int mipmapLevelsIn)
/*     */   {
/* 448 */     this.mipmapLevels = mipmapLevelsIn;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getMissingSprite()
/*     */   {
/* 453 */     return this.missingImage;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_)
/*     */   {
/* 458 */     ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
/* 459 */     return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
/*     */   }
/*     */   
/*     */   public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_)
/*     */   {
/* 464 */     if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_))
/*     */     {
/* 466 */       this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
/*     */       
/* 468 */       if (p_setTextureEntry_2_.getIndexInMap() < 0)
/*     */       {
/* 470 */         p_setTextureEntry_2_.setIndexInMap(this.mapRegisteredSprites.size());
/*     */       }
/*     */       
/* 473 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 477 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_)
/*     */   {
/* 483 */     String s = p_isAbsoluteLocation_1_.getResourcePath();
/* 484 */     return isAbsoluteLocationPath(s);
/*     */   }
/*     */   
/*     */   private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_)
/*     */   {
/* 489 */     String s = p_isAbsoluteLocationPath_1_.toLowerCase();
/* 490 */     return (s.startsWith("mcpatcher/")) || (s.startsWith("optifine/"));
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_)
/*     */   {
/* 495 */     ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
/* 496 */     return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
/*     */   }
/*     */   
/*     */   private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_)
/*     */   {
/* 501 */     return (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill) && (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow) ? Config.isAnimatedLava() : (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill) && (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow) ? Config.isAnimatedFire() : (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0) && (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1) ? Config.isAnimatedTerrain() : p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal ? Config.isAnimatedPortal() : Config.isAnimatedWater();
/*     */   }
/*     */   
/*     */   public int getCountRegisteredSprites()
/*     */   {
/* 506 */     return this.mapRegisteredSprites.size();
/*     */   }
/*     */   
/*     */   private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_)
/*     */   {
/* 511 */     int i = detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
/*     */     
/* 513 */     if (i < 16)
/*     */     {
/* 515 */       i = 16;
/*     */     }
/*     */     
/* 518 */     i = MathHelper.roundUpToPowerOfTwo(i);
/*     */     
/* 520 */     if (i > 16)
/*     */     {
/* 522 */       Config.log("Sprite size: " + i);
/*     */     }
/*     */     
/* 525 */     int j = MathHelper.calculateLogBaseTwo(i);
/*     */     
/* 527 */     if (j < 4)
/*     */     {
/* 529 */       j = 4;
/*     */     }
/*     */     
/* 532 */     return j;
/*     */   }
/*     */   
/*     */   private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_)
/*     */   {
/* 537 */     Map map = new HashMap();
/*     */     
/* 539 */     for (Object entry : p_detectMinimumSpriteSize_1_.entrySet())
/*     */     {
/* 541 */       TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)((Map.Entry)entry).getValue();
/* 542 */       ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
/* 543 */       ResourceLocation resourcelocation1 = completeResourceLocation(resourcelocation, 0);
/*     */       
/* 545 */       if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation))
/*     */       {
/*     */         try
/*     */         {
/* 549 */           IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);
/*     */           
/* 551 */           if (iresource != null)
/*     */           {
/* 553 */             InputStream inputstream = iresource.getInputStream();
/*     */             
/* 555 */             if (inputstream != null)
/*     */             {
/* 557 */               Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
/*     */               
/* 559 */               if (dimension != null)
/*     */               {
/* 561 */                 int i = dimension.width;
/* 562 */                 int j = MathHelper.roundUpToPowerOfTwo(i);
/*     */                 
/* 564 */                 if (!map.containsKey(Integer.valueOf(j)))
/*     */                 {
/* 566 */                   map.put(Integer.valueOf(j), Integer.valueOf(1));
/*     */                 }
/*     */                 else
/*     */                 {
/* 570 */                   int k = ((Integer)map.get(Integer.valueOf(j))).intValue();
/* 571 */                   map.put(Integer.valueOf(j), Integer.valueOf(k + 1));
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 584 */     int l = 0;
/* 585 */     Set set = map.keySet();
/* 586 */     Set set1 = new TreeSet(set);
/*     */     
/*     */     int l1;
/* 589 */     for (Iterator iterator = set1.iterator(); iterator.hasNext(); l += l1)
/*     */     {
/* 591 */       int j1 = ((Integer)iterator.next()).intValue();
/* 592 */       l1 = ((Integer)map.get(Integer.valueOf(j1))).intValue();
/*     */     }
/*     */     
/* 595 */     int i1 = 16;
/* 596 */     int k1 = 0;
/* 597 */     int l1 = l * p_detectMinimumSpriteSize_3_ / 100;
/* 598 */     Iterator iterator1 = set1.iterator();
/*     */     
/* 600 */     while (iterator1.hasNext())
/*     */     {
/* 602 */       int i2 = ((Integer)iterator1.next()).intValue();
/* 603 */       int j2 = ((Integer)map.get(Integer.valueOf(i2))).intValue();
/* 604 */       k1 += j2;
/*     */       
/* 606 */       if (i2 > i1)
/*     */       {
/* 608 */         i1 = i2;
/*     */       }
/*     */       
/* 611 */       if (k1 > l1)
/*     */       {
/* 613 */         return i1;
/*     */       }
/*     */     }
/*     */     
/* 617 */     return i1;
/*     */   }
/*     */   
/*     */   private int getMinSpriteSize()
/*     */   {
/* 622 */     int i = 1 << this.mipmapLevels;
/*     */     
/* 624 */     if (i < 16)
/*     */     {
/* 626 */       i = 16;
/*     */     }
/*     */     
/* 629 */     return i;
/*     */   }
/*     */   
/*     */   private int[] getMissingImageData(int p_getMissingImageData_1_)
/*     */   {
/* 634 */     BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
/* 635 */     bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
/* 636 */     BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
/* 637 */     int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
/* 638 */     bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
/* 639 */     return aint;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\TextureMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */