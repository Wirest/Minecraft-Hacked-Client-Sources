/*     */ package optfine;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.resources.AbstractResourcePack;
/*     */ import net.minecraft.client.resources.IResourcePack;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ public class TextureAnimations
/*     */ {
/*  26 */   private static TextureAnimation[] textureAnimations = null;
/*     */   
/*     */   public static void reset()
/*     */   {
/*  30 */     textureAnimations = null;
/*     */   }
/*     */   
/*     */   public static void update()
/*     */   {
/*  35 */     textureAnimations = null;
/*  36 */     IResourcePack[] airesourcepack = Config.getResourcePacks();
/*  37 */     textureAnimations = getTextureAnimations(airesourcepack);
/*     */     
/*  39 */     if (Config.isAnimatedTextures())
/*     */     {
/*  41 */       updateAnimations();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateCustomAnimations()
/*     */   {
/*  47 */     if (textureAnimations != null)
/*     */     {
/*  49 */       if (Config.isAnimatedTextures())
/*     */       {
/*  51 */         updateAnimations();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateAnimations()
/*     */   {
/*  58 */     if (textureAnimations != null)
/*     */     {
/*  60 */       for (int i = 0; i < textureAnimations.length; i++)
/*     */       {
/*  62 */         TextureAnimation textureanimation = textureAnimations[i];
/*  63 */         textureanimation.updateTexture();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static TextureAnimation[] getTextureAnimations(IResourcePack[] p_getTextureAnimations_0_)
/*     */   {
/*  70 */     List list = new ArrayList();
/*     */     
/*  72 */     for (int i = 0; i < p_getTextureAnimations_0_.length; i++)
/*     */     {
/*  74 */       IResourcePack iresourcepack = p_getTextureAnimations_0_[i];
/*  75 */       TextureAnimation[] atextureanimation = getTextureAnimations(iresourcepack);
/*     */       
/*  77 */       if (atextureanimation != null)
/*     */       {
/*  79 */         list.addAll(Arrays.asList(atextureanimation));
/*     */       }
/*     */     }
/*     */     
/*  83 */     TextureAnimation[] atextureanimation1 = (TextureAnimation[])list.toArray(new TextureAnimation[list.size()]);
/*  84 */     return atextureanimation1;
/*     */   }
/*     */   
/*     */   public static TextureAnimation[] getTextureAnimations(IResourcePack p_getTextureAnimations_0_)
/*     */   {
/*  89 */     if (!(p_getTextureAnimations_0_ instanceof AbstractResourcePack))
/*     */     {
/*  91 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  95 */     AbstractResourcePack abstractresourcepack = (AbstractResourcePack)p_getTextureAnimations_0_;
/*  96 */     File file1 = ResourceUtils.getResourcePackFile(abstractresourcepack);
/*     */     
/*  98 */     if (file1 == null)
/*     */     {
/* 100 */       return null;
/*     */     }
/* 102 */     if (!file1.exists())
/*     */     {
/* 104 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 108 */     String[] astring = null;
/*     */     
/* 110 */     if (file1.isFile())
/*     */     {
/* 112 */       astring = getAnimationPropertiesZip(file1);
/*     */     }
/*     */     else
/*     */     {
/* 116 */       astring = getAnimationPropertiesDir(file1);
/*     */     }
/*     */     
/* 119 */     if (astring == null)
/*     */     {
/* 121 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 125 */     List list = new ArrayList();
/*     */     
/* 127 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 129 */       String s = astring[i];
/* 130 */       Config.dbg("Texture animation: " + s);
/*     */       
/*     */       try
/*     */       {
/* 134 */         ResourceLocation resourcelocation = new ResourceLocation(s);
/* 135 */         InputStream inputstream = p_getTextureAnimations_0_.getInputStream(resourcelocation);
/* 136 */         Properties properties = new Properties();
/* 137 */         properties.load(inputstream);
/* 138 */         TextureAnimation textureanimation = makeTextureAnimation(properties, resourcelocation);
/*     */         
/* 140 */         if (textureanimation != null)
/*     */         {
/* 142 */           ResourceLocation resourcelocation1 = new ResourceLocation(textureanimation.getDstTex());
/*     */           
/* 144 */           if (Config.getDefiningResourcePack(resourcelocation1) != p_getTextureAnimations_0_)
/*     */           {
/* 146 */             Config.dbg("Skipped: " + s + ", target texture not loaded from same resource pack");
/*     */           }
/*     */           else
/*     */           {
/* 150 */             list.add(textureanimation);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (FileNotFoundException filenotfoundexception)
/*     */       {
/* 156 */         Config.warn("File not found: " + filenotfoundexception.getMessage());
/*     */       }
/*     */       catch (IOException ioexception)
/*     */       {
/* 160 */         ioexception.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 164 */     TextureAnimation[] atextureanimation = (TextureAnimation[])list.toArray(new TextureAnimation[list.size()]);
/* 165 */     return atextureanimation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TextureAnimation makeTextureAnimation(Properties p_makeTextureAnimation_0_, ResourceLocation p_makeTextureAnimation_1_)
/*     */   {
/* 173 */     String s = p_makeTextureAnimation_0_.getProperty("from");
/* 174 */     String s1 = p_makeTextureAnimation_0_.getProperty("to");
/* 175 */     int i = Config.parseInt(p_makeTextureAnimation_0_.getProperty("x"), -1);
/* 176 */     int j = Config.parseInt(p_makeTextureAnimation_0_.getProperty("y"), -1);
/* 177 */     int k = Config.parseInt(p_makeTextureAnimation_0_.getProperty("w"), -1);
/* 178 */     int l = Config.parseInt(p_makeTextureAnimation_0_.getProperty("h"), -1);
/*     */     
/* 180 */     if ((s != null) && (s1 != null))
/*     */     {
/* 182 */       if ((i >= 0) && (j >= 0) && (k >= 0) && (l >= 0))
/*     */       {
/* 184 */         String s2 = TextureUtils.getBasePath(p_makeTextureAnimation_1_.getResourcePath());
/* 185 */         s = TextureUtils.fixResourcePath(s, s2);
/* 186 */         s1 = TextureUtils.fixResourcePath(s1, s2);
/* 187 */         byte[] abyte = getCustomTextureData(s, k);
/*     */         
/* 189 */         if (abyte == null)
/*     */         {
/* 191 */           Config.warn("TextureAnimation: Source texture not found: " + s1);
/* 192 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 196 */         ResourceLocation resourcelocation = new ResourceLocation(s1);
/*     */         
/* 198 */         if (!Config.hasResource(resourcelocation))
/*     */         {
/* 200 */           Config.warn("TextureAnimation: Target texture not found: " + s1);
/* 201 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 205 */         TextureAnimation textureanimation = new TextureAnimation(s, abyte, s1, resourcelocation, i, j, k, l, p_makeTextureAnimation_0_, 1);
/* 206 */         return textureanimation;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 212 */       Config.warn("TextureAnimation: Invalid coordinates");
/* 213 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 218 */     Config.warn("TextureAnimation: Source or target texture not specified");
/* 219 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String[] getAnimationPropertiesDir(File p_getAnimationPropertiesDir_0_)
/*     */   {
/* 225 */     File file1 = new File(p_getAnimationPropertiesDir_0_, "anim");
/*     */     
/* 227 */     if (!file1.exists())
/*     */     {
/* 229 */       return null;
/*     */     }
/* 231 */     if (!file1.isDirectory())
/*     */     {
/* 233 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 237 */     File[] afile = file1.listFiles();
/*     */     
/* 239 */     if (afile == null)
/*     */     {
/* 241 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 245 */     List list = new ArrayList();
/*     */     
/* 247 */     for (int i = 0; i < afile.length; i++)
/*     */     {
/* 249 */       File file2 = afile[i];
/* 250 */       String s = file2.getName();
/*     */       
/* 252 */       if ((!s.startsWith("custom_")) && (s.endsWith(".properties")) && (file2.isFile()) && (file2.canRead()))
/*     */       {
/* 254 */         Config.dbg("TextureAnimation: anim/" + file2.getName());
/* 255 */         list.add("/anim/" + s);
/*     */       }
/*     */     }
/*     */     
/* 259 */     String[] astring = (String[])list.toArray(new String[list.size()]);
/* 260 */     return astring;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String[] getAnimationPropertiesZip(File p_getAnimationPropertiesZip_0_)
/*     */   {
/*     */     try
/*     */     {
/* 269 */       ZipFile zipfile = new ZipFile(p_getAnimationPropertiesZip_0_);
/* 270 */       Enumeration enumeration = zipfile.entries();
/* 271 */       List list = new ArrayList();
/*     */       
/* 273 */       while (enumeration.hasMoreElements())
/*     */       {
/* 275 */         ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
/* 276 */         String s = zipentry.getName();
/*     */         
/* 278 */         if ((s.startsWith("assets/minecraft/mcpatcher/anim/")) && (!s.startsWith("assets/minecraft/mcpatcher/anim/custom_")) && (s.endsWith(".properties")))
/*     */         {
/* 280 */           String s1 = "assets/minecraft/";
/* 281 */           s = s.substring(s1.length());
/* 282 */           list.add(s);
/*     */         }
/*     */       }
/*     */       
/* 286 */       return (String[])list.toArray(new String[list.size()]);
/*     */ 
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 291 */       ioexception.printStackTrace(); }
/* 292 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] getCustomTextureData(String p_getCustomTextureData_0_, int p_getCustomTextureData_1_)
/*     */   {
/* 298 */     byte[] abyte = loadImage(p_getCustomTextureData_0_, p_getCustomTextureData_1_);
/*     */     
/* 300 */     if (abyte == null)
/*     */     {
/* 302 */       abyte = loadImage("/anim" + p_getCustomTextureData_0_, p_getCustomTextureData_1_);
/*     */     }
/*     */     
/* 305 */     return abyte;
/*     */   }
/*     */   
/*     */   private static byte[] loadImage(String p_loadImage_0_, int p_loadImage_1_)
/*     */   {
/* 310 */     GameSettings gamesettings = Config.getGameSettings();
/*     */     
/*     */     try
/*     */     {
/* 314 */       ResourceLocation resourcelocation = new ResourceLocation(p_loadImage_0_);
/* 315 */       InputStream inputstream = Config.getResourceStream(resourcelocation);
/*     */       
/* 317 */       if (inputstream == null)
/*     */       {
/* 319 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 323 */       BufferedImage bufferedimage = readTextureImage(inputstream);
/*     */       
/* 325 */       if (bufferedimage == null)
/*     */       {
/* 327 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 331 */       if ((p_loadImage_1_ > 0) && (bufferedimage.getWidth() != p_loadImage_1_))
/*     */       {
/* 333 */         double d0 = bufferedimage.getHeight() / bufferedimage.getWidth();
/* 334 */         int j = (int)(p_loadImage_1_ * d0);
/* 335 */         bufferedimage = scaleBufferedImage(bufferedimage, p_loadImage_1_, j);
/*     */       }
/*     */       
/* 338 */       int k2 = bufferedimage.getWidth();
/* 339 */       int i = bufferedimage.getHeight();
/* 340 */       int[] aint = new int[k2 * i];
/* 341 */       byte[] abyte = new byte[k2 * i * 4];
/* 342 */       bufferedimage.getRGB(0, 0, k2, i, aint, 0, k2);
/*     */       
/* 344 */       for (int k = 0; k < aint.length; k++)
/*     */       {
/* 346 */         int l = aint[k] >> 24 & 0xFF;
/* 347 */         int i1 = aint[k] >> 16 & 0xFF;
/* 348 */         int j1 = aint[k] >> 8 & 0xFF;
/* 349 */         int k1 = aint[k] & 0xFF;
/*     */         
/* 351 */         if ((gamesettings != null) && (gamesettings.anaglyph))
/*     */         {
/* 353 */           int l1 = (i1 * 30 + j1 * 59 + k1 * 11) / 100;
/* 354 */           int i2 = (i1 * 30 + j1 * 70) / 100;
/* 355 */           int j2 = (i1 * 30 + k1 * 70) / 100;
/* 356 */           i1 = l1;
/* 357 */           j1 = i2;
/* 358 */           k1 = j2;
/*     */         }
/*     */         
/* 361 */         abyte[(k * 4 + 0)] = ((byte)i1);
/* 362 */         abyte[(k * 4 + 1)] = ((byte)j1);
/* 363 */         abyte[(k * 4 + 2)] = ((byte)k1);
/* 364 */         abyte[(k * 4 + 3)] = ((byte)l);
/*     */       }
/*     */       
/* 367 */       return abyte;
/*     */ 
/*     */     }
/*     */     catch (FileNotFoundException var18)
/*     */     {
/*     */ 
/* 373 */       return null;
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 377 */       exception.printStackTrace(); }
/* 378 */     return null;
/*     */   }
/*     */   
/*     */   private static BufferedImage readTextureImage(InputStream p_readTextureImage_0_)
/*     */     throws IOException
/*     */   {
/* 384 */     BufferedImage bufferedimage = ImageIO.read(p_readTextureImage_0_);
/* 385 */     p_readTextureImage_0_.close();
/* 386 */     return bufferedimage;
/*     */   }
/*     */   
/*     */   public static BufferedImage scaleBufferedImage(BufferedImage p_scaleBufferedImage_0_, int p_scaleBufferedImage_1_, int p_scaleBufferedImage_2_)
/*     */   {
/* 391 */     BufferedImage bufferedimage = new BufferedImage(p_scaleBufferedImage_1_, p_scaleBufferedImage_2_, 2);
/* 392 */     Graphics2D graphics2d = bufferedimage.createGraphics();
/* 393 */     graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/* 394 */     graphics2d.drawImage(p_scaleBufferedImage_0_, 0, 0, p_scaleBufferedImage_1_, p_scaleBufferedImage_2_, null);
/* 395 */     return bufferedimage;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\TextureAnimations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */