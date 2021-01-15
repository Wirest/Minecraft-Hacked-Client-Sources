/*     */ package optfine;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.ITickableTextureObject;
/*     */ import net.minecraft.client.renderer.texture.SimpleTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.IReloadableResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.ContextCapabilities;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextureUtils
/*     */ {
/*     */   public static final String texGrassTop = "grass_top";
/*     */   public static final String texStone = "stone";
/*     */   public static final String texDirt = "dirt";
/*     */   public static final String texCoarseDirt = "coarse_dirt";
/*     */   public static final String texGrassSide = "grass_side";
/*     */   public static final String texStoneslabSide = "stone_slab_side";
/*     */   public static final String texStoneslabTop = "stone_slab_top";
/*     */   public static final String texBedrock = "bedrock";
/*     */   public static final String texSand = "sand";
/*     */   public static final String texGravel = "gravel";
/*     */   public static final String texLogOak = "log_oak";
/*     */   public static final String texLogBigOak = "log_big_oak";
/*     */   public static final String texLogAcacia = "log_acacia";
/*     */   public static final String texLogSpruce = "log_spruce";
/*     */   public static final String texLogBirch = "log_birch";
/*     */   public static final String texLogJungle = "log_jungle";
/*     */   public static final String texLogOakTop = "log_oak_top";
/*     */   public static final String texLogBigOakTop = "log_big_oak_top";
/*     */   public static final String texLogAcaciaTop = "log_acacia_top";
/*     */   public static final String texLogSpruceTop = "log_spruce_top";
/*     */   public static final String texLogBirchTop = "log_birch_top";
/*     */   public static final String texLogJungleTop = "log_jungle_top";
/*     */   public static final String texLeavesOak = "leaves_oak";
/*     */   public static final String texLeavesBigOak = "leaves_big_oak";
/*     */   public static final String texLeavesAcacia = "leaves_acacia";
/*     */   public static final String texLeavesBirch = "leaves_birch";
/*     */   public static final String texLeavesSpuce = "leaves_spruce";
/*     */   public static final String texLeavesJungle = "leaves_jungle";
/*     */   public static final String texGoldOre = "gold_ore";
/*     */   public static final String texIronOre = "iron_ore";
/*     */   public static final String texCoalOre = "coal_ore";
/*     */   public static final String texObsidian = "obsidian";
/*     */   public static final String texGrassSideOverlay = "grass_side_overlay";
/*     */   public static final String texSnow = "snow";
/*     */   public static final String texGrassSideSnowed = "grass_side_snowed";
/*     */   public static final String texMyceliumSide = "mycelium_side";
/*     */   public static final String texMyceliumTop = "mycelium_top";
/*     */   public static final String texDiamondOre = "diamond_ore";
/*     */   public static final String texRedstoneOre = "redstone_ore";
/*     */   public static final String texLapisOre = "lapis_ore";
/*     */   public static final String texCactusSide = "cactus_side";
/*     */   public static final String texClay = "clay";
/*     */   public static final String texFarmlandWet = "farmland_wet";
/*     */   public static final String texFarmlandDry = "farmland_dry";
/*     */   public static final String texNetherrack = "netherrack";
/*     */   public static final String texSoulSand = "soul_sand";
/*     */   public static final String texGlowstone = "glowstone";
/*     */   public static final String texLeavesSpruce = "leaves_spruce";
/*     */   public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
/*     */   public static final String texEndStone = "end_stone";
/*     */   public static final String texSandstoneTop = "sandstone_top";
/*     */   public static final String texSandstoneBottom = "sandstone_bottom";
/*     */   public static final String texRedstoneLampOff = "redstone_lamp_off";
/*     */   public static final String texRedstoneLampOn = "redstone_lamp_on";
/*     */   public static final String texWaterStill = "water_still";
/*     */   public static final String texWaterFlow = "water_flow";
/*     */   public static final String texLavaStill = "lava_still";
/*     */   public static final String texLavaFlow = "lava_flow";
/*     */   public static final String texFireLayer0 = "fire_layer_0";
/*     */   public static final String texFireLayer1 = "fire_layer_1";
/*     */   public static final String texPortal = "portal";
/*     */   public static final String texGlass = "glass";
/*     */   public static final String texGlassPaneTop = "glass_pane_top";
/*     */   public static TextureAtlasSprite iconGrassTop;
/*     */   public static TextureAtlasSprite iconGrassSide;
/*     */   public static TextureAtlasSprite iconGrassSideOverlay;
/*     */   public static TextureAtlasSprite iconSnow;
/*     */   public static TextureAtlasSprite iconGrassSideSnowed;
/*     */   public static TextureAtlasSprite iconMyceliumSide;
/*     */   public static TextureAtlasSprite iconMyceliumTop;
/*     */   public static TextureAtlasSprite iconWaterStill;
/*     */   public static TextureAtlasSprite iconWaterFlow;
/*     */   public static TextureAtlasSprite iconLavaStill;
/*     */   public static TextureAtlasSprite iconLavaFlow;
/*     */   public static TextureAtlasSprite iconPortal;
/*     */   public static TextureAtlasSprite iconFireLayer0;
/*     */   public static TextureAtlasSprite iconFireLayer1;
/*     */   public static TextureAtlasSprite iconGlass;
/*     */   public static TextureAtlasSprite iconGlassPaneTop;
/*     */   public static final String SPRITE_LOCATION_PREFIX = "minecraft:blocks/";
/* 113 */   private static IntBuffer staticBuffer = GLAllocation.createDirectIntBuffer(256);
/*     */   
/*     */   public static void update()
/*     */   {
/* 117 */     TextureMap texturemap = getTextureMapBlocks();
/*     */     
/* 119 */     if (texturemap != null)
/*     */     {
/* 121 */       String s = "minecraft:blocks/";
/* 122 */       iconGrassTop = texturemap.getSpriteSafe(s + "grass_top");
/* 123 */       iconGrassSide = texturemap.getSpriteSafe(s + "grass_side");
/* 124 */       iconGrassSideOverlay = texturemap.getSpriteSafe(s + "grass_side_overlay");
/* 125 */       iconSnow = texturemap.getSpriteSafe(s + "snow");
/* 126 */       iconGrassSideSnowed = texturemap.getSpriteSafe(s + "grass_side_snowed");
/* 127 */       iconMyceliumSide = texturemap.getSpriteSafe(s + "mycelium_side");
/* 128 */       iconMyceliumTop = texturemap.getSpriteSafe(s + "mycelium_top");
/* 129 */       iconWaterStill = texturemap.getSpriteSafe(s + "water_still");
/* 130 */       iconWaterFlow = texturemap.getSpriteSafe(s + "water_flow");
/* 131 */       iconLavaStill = texturemap.getSpriteSafe(s + "lava_still");
/* 132 */       iconLavaFlow = texturemap.getSpriteSafe(s + "lava_flow");
/* 133 */       iconFireLayer0 = texturemap.getSpriteSafe(s + "fire_layer_0");
/* 134 */       iconFireLayer1 = texturemap.getSpriteSafe(s + "fire_layer_1");
/* 135 */       iconPortal = texturemap.getSpriteSafe(s + "portal");
/* 136 */       iconGlass = texturemap.getSpriteSafe(s + "glass");
/* 137 */       iconGlassPaneTop = texturemap.getSpriteSafe(s + "glass_pane_top");
/*     */     }
/*     */   }
/*     */   
/*     */   public static BufferedImage fixTextureDimensions(String p_fixTextureDimensions_0_, BufferedImage p_fixTextureDimensions_1_)
/*     */   {
/* 143 */     if ((p_fixTextureDimensions_0_.startsWith("/mob/zombie")) || (p_fixTextureDimensions_0_.startsWith("/mob/pigzombie")))
/*     */     {
/* 145 */       int i = p_fixTextureDimensions_1_.getWidth();
/* 146 */       int j = p_fixTextureDimensions_1_.getHeight();
/*     */       
/* 148 */       if (i == j * 2)
/*     */       {
/* 150 */         BufferedImage bufferedimage = new BufferedImage(i, j * 2, 2);
/* 151 */         Graphics2D graphics2d = bufferedimage.createGraphics();
/* 152 */         graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/* 153 */         graphics2d.drawImage(p_fixTextureDimensions_1_, 0, 0, i, j, null);
/* 154 */         return bufferedimage;
/*     */       }
/*     */     }
/*     */     
/* 158 */     return p_fixTextureDimensions_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int ceilPowerOfTwo(int p_ceilPowerOfTwo_0_)
/*     */   {
/* 165 */     for (int i = 1; i < p_ceilPowerOfTwo_0_; i *= 2) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     return i;
/*     */   }
/*     */   
/*     */   public static int getPowerOfTwo(int p_getPowerOfTwo_0_)
/*     */   {
/* 175 */     int i = 1;
/*     */     
/*     */ 
/* 178 */     for (int j = 0; i < p_getPowerOfTwo_0_; j++)
/*     */     {
/* 180 */       i *= 2;
/*     */     }
/*     */     
/* 183 */     return j;
/*     */   }
/*     */   
/*     */   public static int twoToPower(int p_twoToPower_0_)
/*     */   {
/* 188 */     int i = 1;
/*     */     
/* 190 */     for (int j = 0; j < p_twoToPower_0_; j++)
/*     */     {
/* 192 */       i *= 2;
/*     */     }
/*     */     
/* 195 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void refreshBlockTextures() {}
/*     */   
/*     */ 
/*     */   public static ITextureObject getTexture(String p_getTexture_0_)
/*     */   {
/* 204 */     return getTexture(new ResourceLocation(p_getTexture_0_));
/*     */   }
/*     */   
/*     */   public static ITextureObject getTexture(ResourceLocation p_getTexture_0_)
/*     */   {
/* 209 */     ITextureObject itextureobject = Config.getTextureManager().getTexture(p_getTexture_0_);
/*     */     
/* 211 */     if (itextureobject != null)
/*     */     {
/* 213 */       return itextureobject;
/*     */     }
/* 215 */     if (!Config.hasResource(p_getTexture_0_))
/*     */     {
/* 217 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 221 */     SimpleTexture simpletexture = new SimpleTexture(p_getTexture_0_);
/* 222 */     Config.getTextureManager().loadTexture(p_getTexture_0_, simpletexture);
/* 223 */     return simpletexture;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void resourcesReloaded(IResourceManager p_resourcesReloaded_0_)
/*     */   {
/* 229 */     if (getTextureMapBlocks() != null)
/*     */     {
/* 231 */       Config.dbg("*** Reloading custom textures ***");
/* 232 */       CustomSky.reset();
/* 233 */       TextureAnimations.reset();
/* 234 */       update();
/* 235 */       NaturalTextures.update();
/* 236 */       BetterGrass.update();
/* 237 */       BetterSnow.update();
/* 238 */       TextureAnimations.update();
/* 239 */       CustomColorizer.update();
/* 240 */       CustomSky.update();
/* 241 */       RandomMobs.resetTextures();
/* 242 */       Config.updateTexturePackClouds();
/* 243 */       Config.getTextureManager().tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public static TextureMap getTextureMapBlocks()
/*     */   {
/* 249 */     return Minecraft.getMinecraft().getTextureMapBlocks();
/*     */   }
/*     */   
/*     */   public static void registerResourceListener()
/*     */   {
/* 254 */     IResourceManager iresourcemanager = Config.getResourceManager();
/*     */     
/* 256 */     if ((iresourcemanager instanceof IReloadableResourceManager))
/*     */     {
/* 258 */       IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager)iresourcemanager;
/* 259 */       IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener()
/*     */       {
/*     */         public void onResourceManagerReload(IResourceManager resourceManager)
/*     */         {
/* 263 */           TextureUtils.resourcesReloaded(resourceManager);
/*     */         }
/* 265 */       };
/* 266 */       ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
/*     */     }
/*     */     
/* 269 */     ITickableTextureObject itickabletextureobject = new ITickableTextureObject()
/*     */     {
/*     */       public void tick() {}
/*     */       
/*     */ 
/*     */       public void loadTexture(IResourceManager resourceManager)
/*     */         throws IOException
/*     */       {}
/*     */       
/*     */       public int getGlTextureId()
/*     */       {
/* 280 */         return 0;
/*     */       }
/*     */       
/*     */ 
/*     */       public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_) {}
/*     */       
/*     */ 
/*     */       public void restoreLastBlurMipmap() {}
/* 288 */     };
/* 289 */     ResourceLocation resourcelocation = new ResourceLocation("optifine/TickableTextures");
/* 290 */     Config.getTextureManager().loadTickableTexture(resourcelocation, itickabletextureobject);
/*     */   }
/*     */   
/*     */   public static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_)
/*     */   {
/* 295 */     String s = "assets/minecraft/";
/*     */     
/* 297 */     if (p_fixResourcePath_0_.startsWith(s))
/*     */     {
/* 299 */       p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(s.length());
/* 300 */       return p_fixResourcePath_0_;
/*     */     }
/* 302 */     if (p_fixResourcePath_0_.startsWith("./"))
/*     */     {
/* 304 */       p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
/*     */       
/* 306 */       if (!p_fixResourcePath_1_.endsWith("/"))
/*     */       {
/* 308 */         p_fixResourcePath_1_ = p_fixResourcePath_1_ + "/";
/*     */       }
/*     */       
/* 311 */       p_fixResourcePath_0_ = p_fixResourcePath_1_ + p_fixResourcePath_0_;
/* 312 */       return p_fixResourcePath_0_;
/*     */     }
/*     */     
/*     */ 
/* 316 */     String s1 = "mcpatcher/";
/*     */     
/* 318 */     if (p_fixResourcePath_0_.startsWith("~/"))
/*     */     {
/* 320 */       p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
/* 321 */       p_fixResourcePath_0_ = s1 + p_fixResourcePath_0_;
/* 322 */       return p_fixResourcePath_0_;
/*     */     }
/* 324 */     if (p_fixResourcePath_0_.startsWith("/"))
/*     */     {
/* 326 */       p_fixResourcePath_0_ = s1 + p_fixResourcePath_0_.substring(1);
/* 327 */       return p_fixResourcePath_0_;
/*     */     }
/*     */     
/*     */ 
/* 331 */     return p_fixResourcePath_0_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getBasePath(String p_getBasePath_0_)
/*     */   {
/* 338 */     int i = p_getBasePath_0_.lastIndexOf('/');
/* 339 */     return i < 0 ? "" : p_getBasePath_0_.substring(0, i);
/*     */   }
/*     */   
/*     */   public static void applyAnisotropicLevel()
/*     */   {
/* 344 */     if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic)
/*     */     {
/* 346 */       float f = GL11.glGetFloat(34047);
/* 347 */       float f1 = Config.getAnisotropicFilterLevel();
/* 348 */       f1 = Math.min(f1, f);
/* 349 */       GL11.glTexParameterf(3553, 34046, f1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bindTexture(int p_bindTexture_0_)
/*     */   {
/* 355 */     GlStateManager.bindTexture(p_bindTexture_0_);
/*     */   }
/*     */   
/*     */   public static boolean isPowerOfTwo(int p_isPowerOfTwo_0_)
/*     */   {
/* 360 */     int i = MathHelper.roundUpToPowerOfTwo(p_isPowerOfTwo_0_);
/* 361 */     return i == p_isPowerOfTwo_0_;
/*     */   }
/*     */   
/*     */   public static BufferedImage scaleToPowerOfTwo(BufferedImage p_scaleToPowerOfTwo_0_, int p_scaleToPowerOfTwo_1_)
/*     */   {
/* 366 */     if (p_scaleToPowerOfTwo_0_ == null)
/*     */     {
/* 368 */       return p_scaleToPowerOfTwo_0_;
/*     */     }
/*     */     
/*     */ 
/* 372 */     int i = p_scaleToPowerOfTwo_0_.getWidth();
/* 373 */     int j = p_scaleToPowerOfTwo_0_.getHeight();
/* 374 */     int k = Math.max(i, p_scaleToPowerOfTwo_1_);
/* 375 */     k = MathHelper.roundUpToPowerOfTwo(k);
/*     */     
/* 377 */     if (k == i)
/*     */     {
/* 379 */       return p_scaleToPowerOfTwo_0_;
/*     */     }
/*     */     
/*     */ 
/* 383 */     int l = j * k / i;
/* 384 */     BufferedImage bufferedimage = new BufferedImage(k, l, 2);
/* 385 */     Graphics2D graphics2d = bufferedimage.createGraphics();
/* 386 */     Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/*     */     
/* 388 */     if (k % i != 0)
/*     */     {
/* 390 */       object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
/*     */     }
/*     */     
/* 393 */     graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
/* 394 */     graphics2d.drawImage(p_scaleToPowerOfTwo_0_, 0, 0, k, l, null);
/* 395 */     return bufferedimage;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static java.awt.Dimension getImageSize(java.io.InputStream p_getImageSize_0_, String p_getImageSize_1_)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic 595	javax/imageio/ImageIO:getImageReadersBySuffix	(Ljava/lang/String;)Ljava/util/Iterator;
/*     */     //   4: astore_2
/*     */     //   5: aload_2
/*     */     //   6: invokeinterface 601 1 0
/*     */     //   11: ifeq +86 -> 97
/*     */     //   14: aload_2
/*     */     //   15: invokeinterface 605 1 0
/*     */     //   20: checkcast 607	javax/imageio/ImageReader
/*     */     //   23: astore_3
/*     */     //   24: aload_0
/*     */     //   25: invokestatic 611	javax/imageio/ImageIO:createImageInputStream	(Ljava/lang/Object;)Ljavax/imageio/stream/ImageInputStream;
/*     */     //   28: astore 5
/*     */     //   30: aload_3
/*     */     //   31: aload 5
/*     */     //   33: invokevirtual 615	javax/imageio/ImageReader:setInput	(Ljava/lang/Object;)V
/*     */     //   36: aload_3
/*     */     //   37: aload_3
/*     */     //   38: invokevirtual 618	javax/imageio/ImageReader:getMinIndex	()I
/*     */     //   41: invokevirtual 620	javax/imageio/ImageReader:getWidth	(I)I
/*     */     //   44: istore 6
/*     */     //   46: aload_3
/*     */     //   47: aload_3
/*     */     //   48: invokevirtual 618	javax/imageio/ImageReader:getMinIndex	()I
/*     */     //   51: invokevirtual 622	javax/imageio/ImageReader:getHeight	(I)I
/*     */     //   54: istore 7
/*     */     //   56: new 624	java/awt/Dimension
/*     */     //   59: dup
/*     */     //   60: iload 6
/*     */     //   62: iload 7
/*     */     //   64: invokespecial 627	java/awt/Dimension:<init>	(II)V
/*     */     //   67: astore 4
/*     */     //   69: goto +21 -> 90
/*     */     //   72: astore 5
/*     */     //   74: aload_3
/*     */     //   75: invokevirtual 632	javax/imageio/ImageReader:dispose	()V
/*     */     //   78: goto -73 -> 5
/*     */     //   81: astore 8
/*     */     //   83: aload_3
/*     */     //   84: invokevirtual 632	javax/imageio/ImageReader:dispose	()V
/*     */     //   87: aload 8
/*     */     //   89: athrow
/*     */     //   90: aload_3
/*     */     //   91: invokevirtual 632	javax/imageio/ImageReader:dispose	()V
/*     */     //   94: aload 4
/*     */     //   96: areturn
/*     */     //   97: aconst_null
/*     */     //   98: areturn
/*     */     // Line number table:
/*     */     //   Java source line #402	-> byte code offset #0
/*     */     //   Java source line #406	-> byte code offset #5
/*     */     //   Java source line #408	-> byte code offset #14
/*     */     //   Java source line #413	-> byte code offset #24
/*     */     //   Java source line #414	-> byte code offset #30
/*     */     //   Java source line #415	-> byte code offset #36
/*     */     //   Java source line #416	-> byte code offset #46
/*     */     //   Java source line #417	-> byte code offset #56
/*     */     //   Java source line #418	-> byte code offset #69
/*     */     //   Java source line #419	-> byte code offset #72
/*     */     //   Java source line #425	-> byte code offset #74
/*     */     //   Java source line #421	-> byte code offset #78
/*     */     //   Java source line #424	-> byte code offset #81
/*     */     //   Java source line #425	-> byte code offset #83
/*     */     //   Java source line #426	-> byte code offset #87
/*     */     //   Java source line #425	-> byte code offset #90
/*     */     //   Java source line #428	-> byte code offset #94
/*     */     //   Java source line #431	-> byte code offset #97
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	99	0	p_getImageSize_0_	java.io.InputStream
/*     */     //   0	99	1	p_getImageSize_1_	String
/*     */     //   4	11	2	iterator	java.util.Iterator
/*     */     //   23	68	3	imagereader	javax.imageio.ImageReader
/*     */     //   67	3	4	dimension	java.awt.Dimension
/*     */     //   90	5	4	dimension	java.awt.Dimension
/*     */     //   28	4	5	imageinputstream	javax.imageio.stream.ImageInputStream
/*     */     //   72	3	5	var11	IOException
/*     */     //   44	17	6	i	int
/*     */     //   54	9	7	j	int
/*     */     //   81	7	8	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   24	69	72	java/io/IOException
/*     */     //   24	74	81	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\TextureUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */