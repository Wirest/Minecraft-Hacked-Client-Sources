/*      */ package optfine;
/*      */ 
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockRedstoneWire;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EntityFX;
/*      */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.ColorizerFoliage;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ 
/*      */ public class CustomColorizer
/*      */ {
/*   36 */   private static int[] grassColors = null;
/*   37 */   private static int[] waterColors = null;
/*   38 */   private static int[] foliageColors = null;
/*   39 */   private static int[] foliagePineColors = null;
/*   40 */   private static int[] foliageBirchColors = null;
/*   41 */   private static int[] swampFoliageColors = null;
/*   42 */   private static int[] swampGrassColors = null;
/*   43 */   private static int[][] blockPalettes = null;
/*   44 */   private static int[][] paletteColors = null;
/*   45 */   private static int[] skyColors = null;
/*   46 */   private static int[] fogColors = null;
/*   47 */   private static int[] underwaterColors = null;
/*   48 */   private static float[][][] lightMapsColorsRgb = null;
/*   49 */   private static int[] lightMapsHeight = null;
/*   50 */   private static float[][] sunRgbs = new float[16][3];
/*   51 */   private static float[][] torchRgbs = new float[16][3];
/*   52 */   private static int[] redstoneColors = null;
/*   53 */   private static int[] stemColors = null;
/*   54 */   private static int[] myceliumParticleColors = null;
/*   55 */   private static boolean useDefaultColorMultiplier = true;
/*   56 */   private static int particleWaterColor = -1;
/*   57 */   private static int particlePortalColor = -1;
/*   58 */   private static int lilyPadColor = -1;
/*   59 */   private static Vec3 fogColorNether = null;
/*   60 */   private static Vec3 fogColorEnd = null;
/*   61 */   private static Vec3 skyColorEnd = null;
/*      */   private static final int TYPE_NONE = 0;
/*      */   private static final int TYPE_GRASS = 1;
/*      */   private static final int TYPE_FOLIAGE = 2;
/*   65 */   private static Random random = new Random();
/*      */   
/*      */   public static void update()
/*      */   {
/*   69 */     grassColors = null;
/*   70 */     waterColors = null;
/*   71 */     foliageColors = null;
/*   72 */     foliageBirchColors = null;
/*   73 */     foliagePineColors = null;
/*   74 */     swampGrassColors = null;
/*   75 */     swampFoliageColors = null;
/*   76 */     skyColors = null;
/*   77 */     fogColors = null;
/*   78 */     underwaterColors = null;
/*   79 */     redstoneColors = null;
/*   80 */     stemColors = null;
/*   81 */     myceliumParticleColors = null;
/*   82 */     lightMapsColorsRgb = null;
/*   83 */     lightMapsHeight = null;
/*   84 */     lilyPadColor = -1;
/*   85 */     particleWaterColor = -1;
/*   86 */     particlePortalColor = -1;
/*   87 */     fogColorNether = null;
/*   88 */     fogColorEnd = null;
/*   89 */     skyColorEnd = null;
/*   90 */     blockPalettes = null;
/*   91 */     paletteColors = null;
/*   92 */     useDefaultColorMultiplier = true;
/*   93 */     String s = "mcpatcher/colormap/";
/*   94 */     grassColors = getCustomColors("textures/colormap/grass.png", 65536);
/*   95 */     foliageColors = getCustomColors("textures/colormap/foliage.png", 65536);
/*   96 */     String[] astring = { "water.png", "watercolorX.png" };
/*   97 */     waterColors = getCustomColors(s, astring, 65536);
/*      */     
/*   99 */     if (Config.isCustomColors())
/*      */     {
/*  101 */       String[] astring1 = { "pine.png", "pinecolor.png" };
/*  102 */       foliagePineColors = getCustomColors(s, astring1, 65536);
/*  103 */       String[] astring2 = { "birch.png", "birchcolor.png" };
/*  104 */       foliageBirchColors = getCustomColors(s, astring2, 65536);
/*  105 */       String[] astring3 = { "swampgrass.png", "swampgrasscolor.png" };
/*  106 */       swampGrassColors = getCustomColors(s, astring3, 65536);
/*  107 */       String[] astring4 = { "swampfoliage.png", "swampfoliagecolor.png" };
/*  108 */       swampFoliageColors = getCustomColors(s, astring4, 65536);
/*  109 */       String[] astring5 = { "sky0.png", "skycolor0.png" };
/*  110 */       skyColors = getCustomColors(s, astring5, 65536);
/*  111 */       String[] astring6 = { "fog0.png", "fogcolor0.png" };
/*  112 */       fogColors = getCustomColors(s, astring6, 65536);
/*  113 */       String[] astring7 = { "underwater.png", "underwatercolor.png" };
/*  114 */       underwaterColors = getCustomColors(s, astring7, 65536);
/*  115 */       String[] astring8 = { "redstone.png", "redstonecolor.png" };
/*  116 */       redstoneColors = getCustomColors(s, astring8, 16);
/*  117 */       String[] astring9 = { "stem.png", "stemcolor.png" };
/*  118 */       stemColors = getCustomColors(s, astring9, 8);
/*  119 */       String[] astring10 = { "myceliumparticle.png", "myceliumparticlecolor.png" };
/*  120 */       myceliumParticleColors = getCustomColors(s, astring10, -1);
/*  121 */       int[][] aint = new int[3][];
/*  122 */       lightMapsColorsRgb = new float[3][][];
/*  123 */       lightMapsHeight = new int[3];
/*      */       
/*  125 */       for (int i = 0; i < aint.length; i++)
/*      */       {
/*  127 */         String s1 = "mcpatcher/lightmap/world" + (i - 1) + ".png";
/*  128 */         aint[i] = getCustomColors(s1, -1);
/*      */         
/*  130 */         if (aint[i] != null)
/*      */         {
/*  132 */           lightMapsColorsRgb[i] = toRgb(aint[i]);
/*      */         }
/*      */         
/*  135 */         lightMapsHeight[i] = getTextureHeight(s1, 32);
/*      */       }
/*      */       
/*  138 */       readColorProperties("mcpatcher/color.properties");
/*  139 */       updateUseDefaultColorMultiplier();
/*      */     }
/*      */   }
/*      */   
/*      */   private static int getTextureHeight(String p_getTextureHeight_0_, int p_getTextureHeight_1_)
/*      */   {
/*      */     try
/*      */     {
/*  147 */       InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));
/*      */       
/*  149 */       if (inputstream == null)
/*      */       {
/*  151 */         return p_getTextureHeight_1_;
/*      */       }
/*      */       
/*      */ 
/*  155 */       BufferedImage bufferedimage = javax.imageio.ImageIO.read(inputstream);
/*  156 */       return bufferedimage == null ? p_getTextureHeight_1_ : bufferedimage.getHeight();
/*      */     }
/*      */     catch (IOException var4) {}
/*      */     
/*      */ 
/*  161 */     return p_getTextureHeight_1_;
/*      */   }
/*      */   
/*      */ 
/*      */   private static float[][] toRgb(int[] p_toRgb_0_)
/*      */   {
/*  167 */     float[][] afloat = new float[p_toRgb_0_.length][3];
/*      */     
/*  169 */     for (int i = 0; i < p_toRgb_0_.length; i++)
/*      */     {
/*  171 */       int j = p_toRgb_0_[i];
/*  172 */       float f = (j >> 16 & 0xFF) / 255.0F;
/*  173 */       float f1 = (j >> 8 & 0xFF) / 255.0F;
/*  174 */       float f2 = (j & 0xFF) / 255.0F;
/*  175 */       float[] afloat1 = afloat[i];
/*  176 */       afloat1[0] = f;
/*  177 */       afloat1[1] = f1;
/*  178 */       afloat1[2] = f2;
/*      */     }
/*      */     
/*  181 */     return afloat;
/*      */   }
/*      */   
/*      */   private static void readColorProperties(String p_readColorProperties_0_)
/*      */   {
/*      */     try
/*      */     {
/*  188 */       ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
/*  189 */       InputStream inputstream = Config.getResourceStream(resourcelocation);
/*      */       
/*  191 */       if (inputstream == null)
/*      */       {
/*  193 */         return;
/*      */       }
/*      */       
/*  196 */       Config.log("Loading " + p_readColorProperties_0_);
/*  197 */       Properties properties = new Properties();
/*  198 */       properties.load(inputstream);
/*  199 */       lilyPadColor = readColor(properties, "lilypad");
/*  200 */       particleWaterColor = readColor(properties, new String[] { "particle.water", "drop.water" });
/*  201 */       particlePortalColor = readColor(properties, "particle.portal");
/*  202 */       fogColorNether = readColorVec3(properties, "fog.nether");
/*  203 */       fogColorEnd = readColorVec3(properties, "fog.end");
/*  204 */       skyColorEnd = readColorVec3(properties, "sky.end");
/*  205 */       readCustomPalettes(properties, p_readColorProperties_0_);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (FileNotFoundException var4) {}catch (IOException ioexception)
/*      */     {
/*      */ 
/*      */ 
/*  213 */       ioexception.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void readCustomPalettes(Properties p_readCustomPalettes_0_, String p_readCustomPalettes_1_)
/*      */   {
/*  219 */     blockPalettes = new int['Ā'][1];
/*      */     
/*  221 */     for (int i = 0; i < 256; i++)
/*      */     {
/*  223 */       blockPalettes[i][0] = -1;
/*      */     }
/*      */     
/*  226 */     String s7 = "palette.block.";
/*  227 */     Map map = new HashMap();
/*      */     
/*  229 */     for (Object s : p_readCustomPalettes_0_.keySet())
/*      */     {
/*  231 */       String s1 = p_readCustomPalettes_0_.getProperty((String)s);
/*      */       
/*  233 */       if (((String)s).startsWith(s7))
/*      */       {
/*  235 */         map.put(s, s1);
/*      */       }
/*      */     }
/*      */     
/*  239 */     String[] astring2 = (String[])map.keySet().toArray(new String[map.size()]);
/*  240 */     paletteColors = new int[astring2.length][];
/*      */     
/*  242 */     for (int l = 0; l < astring2.length; l++)
/*      */     {
/*  244 */       String s8 = astring2[l];
/*  245 */       String s2 = p_readCustomPalettes_0_.getProperty(s8);
/*  246 */       Config.log("Block palette: " + s8 + " = " + s2);
/*  247 */       String s3 = s8.substring(s7.length());
/*  248 */       String s4 = TextureUtils.getBasePath(p_readCustomPalettes_1_);
/*  249 */       s3 = TextureUtils.fixResourcePath(s3, s4);
/*  250 */       int[] aint = getCustomColors(s3, 65536);
/*  251 */       paletteColors[l] = aint;
/*  252 */       String[] astring = Config.tokenize(s2, " ,;");
/*      */       
/*  254 */       for (int j = 0; j < astring.length; j++)
/*      */       {
/*  256 */         String s5 = astring[j];
/*  257 */         int k = -1;
/*      */         
/*  259 */         if (s5.contains(":"))
/*      */         {
/*  261 */           String[] astring1 = Config.tokenize(s5, ":");
/*  262 */           s5 = astring1[0];
/*  263 */           String s6 = astring1[1];
/*  264 */           k = Config.parseInt(s6, -1);
/*      */           
/*  266 */           if ((k < 0) || (k > 15))
/*      */           {
/*  268 */             Config.log("Invalid block metadata: " + s5 + " in palette: " + s8);
/*  269 */             continue;
/*      */           }
/*      */         }
/*      */         
/*  273 */         int i1 = Config.parseInt(s5, -1);
/*      */         
/*  275 */         if ((i1 >= 0) && (i1 <= 255))
/*      */         {
/*  277 */           if ((i1 != Block.getIdFromBlock(Blocks.grass)) && (i1 != Block.getIdFromBlock(Blocks.tallgrass)) && (i1 != Block.getIdFromBlock(Blocks.leaves)) && (i1 != Block.getIdFromBlock(Blocks.vine)))
/*      */           {
/*  279 */             if (k == -1)
/*      */             {
/*  281 */               blockPalettes[i1][0] = l;
/*      */             }
/*      */             else
/*      */             {
/*  285 */               if (blockPalettes[i1].length < 16)
/*      */               {
/*  287 */                 blockPalettes[i1] = new int[16];
/*  288 */                 Arrays.fill(blockPalettes[i1], -1);
/*      */               }
/*      */               
/*  291 */               blockPalettes[i1][k] = l;
/*      */             }
/*      */             
/*      */           }
/*      */         }
/*      */         else {
/*  297 */           Config.log("Invalid block index: " + i1 + " in palette: " + s8);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static int readColor(Properties p_readColor_0_, String[] p_readColor_1_)
/*      */   {
/*  305 */     for (int i = 0; i < p_readColor_1_.length; i++)
/*      */     {
/*  307 */       String s = p_readColor_1_[i];
/*  308 */       int j = readColor(p_readColor_0_, s);
/*      */       
/*  310 */       if (j >= 0)
/*      */       {
/*  312 */         return j;
/*      */       }
/*      */     }
/*      */     
/*  316 */     return -1;
/*      */   }
/*      */   
/*      */   private static int readColor(Properties p_readColor_0_, String p_readColor_1_)
/*      */   {
/*  321 */     String s = p_readColor_0_.getProperty(p_readColor_1_);
/*      */     
/*  323 */     if (s == null)
/*      */     {
/*  325 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  331 */       int i = Integer.parseInt(s, 16) & 0xFFFFFF;
/*  332 */       Config.log("Custom color: " + p_readColor_1_ + " = " + s);
/*  333 */       return i;
/*      */     }
/*      */     catch (NumberFormatException var4)
/*      */     {
/*  337 */       Config.log("Invalid custom color: " + p_readColor_1_ + " = " + s); }
/*  338 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static Vec3 readColorVec3(Properties p_readColorVec3_0_, String p_readColorVec3_1_)
/*      */   {
/*  345 */     int i = readColor(p_readColorVec3_0_, p_readColorVec3_1_);
/*      */     
/*  347 */     if (i < 0)
/*      */     {
/*  349 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  353 */     int j = i >> 16 & 0xFF;
/*  354 */     int k = i >> 8 & 0xFF;
/*  355 */     int l = i & 0xFF;
/*  356 */     float f = j / 255.0F;
/*  357 */     float f1 = k / 255.0F;
/*  358 */     float f2 = l / 255.0F;
/*  359 */     return new Vec3(f, f1, f2);
/*      */   }
/*      */   
/*      */ 
/*      */   private static int[] getCustomColors(String p_getCustomColors_0_, String[] p_getCustomColors_1_, int p_getCustomColors_2_)
/*      */   {
/*  365 */     for (int i = 0; i < p_getCustomColors_1_.length; i++)
/*      */     {
/*  367 */       String s = p_getCustomColors_1_[i];
/*  368 */       s = p_getCustomColors_0_ + s;
/*  369 */       int[] aint = getCustomColors(s, p_getCustomColors_2_);
/*      */       
/*  371 */       if (aint != null)
/*      */       {
/*  373 */         return aint;
/*      */       }
/*      */     }
/*      */     
/*  377 */     return null;
/*      */   }
/*      */   
/*      */   private static int[] getCustomColors(String p_getCustomColors_0_, int p_getCustomColors_1_)
/*      */   {
/*      */     try
/*      */     {
/*  384 */       ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);
/*  385 */       InputStream inputstream = Config.getResourceStream(resourcelocation);
/*      */       
/*  387 */       if (inputstream == null)
/*      */       {
/*  389 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  393 */       int[] aint = net.minecraft.client.renderer.texture.TextureUtil.readImageData(Config.getResourceManager(), resourcelocation);
/*      */       
/*  395 */       if (aint == null)
/*      */       {
/*  397 */         return null;
/*      */       }
/*  399 */       if ((p_getCustomColors_1_ > 0) && (aint.length != p_getCustomColors_1_))
/*      */       {
/*  401 */         Config.log("Invalid custom colors length: " + aint.length + ", path: " + p_getCustomColors_0_);
/*  402 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  406 */       Config.log("Loading custom colors: " + p_getCustomColors_0_);
/*  407 */       return aint;
/*      */ 
/*      */     }
/*      */     catch (FileNotFoundException var5)
/*      */     {
/*      */ 
/*  413 */       return null;
/*      */     }
/*      */     catch (IOException ioexception)
/*      */     {
/*  417 */       ioexception.printStackTrace(); }
/*  418 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public static void updateUseDefaultColorMultiplier()
/*      */   {
/*  424 */     useDefaultColorMultiplier = (foliageBirchColors == null) && (foliagePineColors == null) && (swampGrassColors == null) && (swampFoliageColors == null) && (blockPalettes == null) && (Config.isSwampColors()) && (Config.isSmoothBiomes());
/*      */   }
/*      */   
/*      */   public static int getColorMultiplier(BakedQuad p_getColorMultiplier_0_, Block p_getColorMultiplier_1_, IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, RenderEnv p_getColorMultiplier_4_)
/*      */   {
/*  429 */     if (useDefaultColorMultiplier)
/*      */     {
/*  431 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  435 */     int[] aint = null;
/*  436 */     int[] aint1 = null;
/*      */     
/*  438 */     if (blockPalettes != null)
/*      */     {
/*  440 */       int i = p_getColorMultiplier_4_.getBlockId();
/*      */       
/*  442 */       if ((i >= 0) && (i < 256))
/*      */       {
/*  444 */         int[] aint2 = blockPalettes[i];
/*  445 */         int j = -1;
/*      */         
/*  447 */         if (aint2.length > 1)
/*      */         {
/*  449 */           int k = p_getColorMultiplier_4_.getMetadata();
/*  450 */           j = aint2[k];
/*      */         }
/*      */         else
/*      */         {
/*  454 */           j = aint2[0];
/*      */         }
/*      */         
/*  457 */         if (j >= 0)
/*      */         {
/*  459 */           aint = paletteColors[j];
/*      */         }
/*      */       }
/*      */       
/*  463 */       if (aint != null)
/*      */       {
/*  465 */         if (Config.isSmoothBiomes())
/*      */         {
/*  467 */           return getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, aint, aint, 0, 0, p_getColorMultiplier_4_);
/*      */         }
/*      */         
/*  470 */         return getCustomColor(aint, p_getColorMultiplier_2_, p_getColorMultiplier_3_);
/*      */       }
/*      */     }
/*      */     
/*  474 */     if (!p_getColorMultiplier_0_.hasTintIndex())
/*      */     {
/*  476 */       return -1;
/*      */     }
/*  478 */     if (p_getColorMultiplier_1_ == Blocks.waterlily)
/*      */     {
/*  480 */       return getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
/*      */     }
/*  482 */     if ((p_getColorMultiplier_1_ instanceof net.minecraft.block.BlockStem))
/*      */     {
/*  484 */       return getStemColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
/*      */     }
/*      */     
/*      */ 
/*  488 */     boolean flag = Config.isSwampColors();
/*  489 */     boolean flag1 = false;
/*  490 */     int l = 0;
/*  491 */     int i1 = 0;
/*      */     
/*  493 */     if ((p_getColorMultiplier_1_ != Blocks.grass) && (p_getColorMultiplier_1_ != Blocks.tallgrass))
/*      */     {
/*  495 */       if (p_getColorMultiplier_1_ == Blocks.leaves)
/*      */       {
/*  497 */         l = 2;
/*  498 */         flag1 = Config.isSmoothBiomes();
/*  499 */         i1 = p_getColorMultiplier_4_.getMetadata();
/*      */         
/*  501 */         if ((i1 & 0x3) == 1)
/*      */         {
/*  503 */           aint = foliagePineColors;
/*      */         }
/*  505 */         else if ((i1 & 0x3) == 2)
/*      */         {
/*  507 */           aint = foliageBirchColors;
/*      */         }
/*      */         else
/*      */         {
/*  511 */           aint = foliageColors;
/*      */           
/*  513 */           if (flag)
/*      */           {
/*  515 */             aint1 = swampFoliageColors;
/*      */           }
/*      */           else
/*      */           {
/*  519 */             aint1 = aint;
/*      */           }
/*      */         }
/*      */       }
/*  523 */       else if (p_getColorMultiplier_1_ == Blocks.vine)
/*      */       {
/*  525 */         l = 2;
/*  526 */         flag1 = Config.isSmoothBiomes();
/*  527 */         aint = foliageColors;
/*      */         
/*  529 */         if (flag)
/*      */         {
/*  531 */           aint1 = swampFoliageColors;
/*      */         }
/*      */         else
/*      */         {
/*  535 */           aint1 = aint;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  541 */       l = 1;
/*  542 */       flag1 = Config.isSmoothBiomes();
/*  543 */       aint = grassColors;
/*      */       
/*  545 */       if (flag)
/*      */       {
/*  547 */         aint1 = swampGrassColors;
/*      */       }
/*      */       else
/*      */       {
/*  551 */         aint1 = aint;
/*      */       }
/*      */     }
/*      */     
/*  555 */     if (flag1)
/*      */     {
/*  557 */       return getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, aint, aint1, l, i1, p_getColorMultiplier_4_);
/*      */     }
/*      */     
/*      */ 
/*  561 */     if ((aint1 != aint) && (p_getColorMultiplier_2_.getBiomeGenForCoords(p_getColorMultiplier_3_) == BiomeGenBase.swampland))
/*      */     {
/*  563 */       aint = aint1;
/*      */     }
/*      */     
/*  566 */     return aint != null ? getCustomColor(aint, p_getColorMultiplier_2_, p_getColorMultiplier_3_) : -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getSmoothColorMultiplier(Block p_getSmoothColorMultiplier_0_, IBlockAccess p_getSmoothColorMultiplier_1_, BlockPos p_getSmoothColorMultiplier_2_, int[] p_getSmoothColorMultiplier_3_, int[] p_getSmoothColorMultiplier_4_, int p_getSmoothColorMultiplier_5_, int p_getSmoothColorMultiplier_6_, RenderEnv p_getSmoothColorMultiplier_7_)
/*      */   {
/*  574 */     int i = 0;
/*  575 */     int j = 0;
/*  576 */     int k = 0;
/*  577 */     int l = p_getSmoothColorMultiplier_2_.getX();
/*  578 */     int i1 = p_getSmoothColorMultiplier_2_.getY();
/*  579 */     int j1 = p_getSmoothColorMultiplier_2_.getZ();
/*  580 */     BlockPosM blockposm = p_getSmoothColorMultiplier_7_.getColorizerBlockPos();
/*      */     
/*  582 */     for (int k1 = l - 1; k1 <= l + 1; k1++)
/*      */     {
/*  584 */       for (int l1 = j1 - 1; l1 <= j1 + 1; l1++)
/*      */       {
/*  586 */         blockposm.setXyz(k1, i1, l1);
/*  587 */         int[] aint = p_getSmoothColorMultiplier_3_;
/*      */         
/*  589 */         if ((p_getSmoothColorMultiplier_4_ != p_getSmoothColorMultiplier_3_) && (p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm) == BiomeGenBase.swampland))
/*      */         {
/*  591 */           aint = p_getSmoothColorMultiplier_4_;
/*      */         }
/*      */         
/*  594 */         int i2 = 0;
/*      */         
/*  596 */         if (aint == null)
/*      */         {
/*  598 */           switch (p_getSmoothColorMultiplier_5_)
/*      */           {
/*      */           case 1: 
/*  601 */             i2 = p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm).getGrassColorAtPos(blockposm);
/*  602 */             break;
/*      */           
/*      */           case 2: 
/*  605 */             if ((p_getSmoothColorMultiplier_6_ & 0x3) == 1)
/*      */             {
/*  607 */               i2 = ColorizerFoliage.getFoliageColorPine();
/*      */             }
/*  609 */             else if ((p_getSmoothColorMultiplier_6_ & 0x3) == 2)
/*      */             {
/*  611 */               i2 = ColorizerFoliage.getFoliageColorBirch();
/*      */             }
/*      */             else
/*      */             {
/*  615 */               i2 = p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm).getFoliageColorAtPos(blockposm);
/*      */             }
/*      */             
/*  618 */             break;
/*      */           
/*      */           default: 
/*  621 */             i2 = p_getSmoothColorMultiplier_0_.colorMultiplier(p_getSmoothColorMultiplier_1_, blockposm);
/*      */             
/*  623 */             break;
/*      */           }
/*      */         } else {
/*  626 */           i2 = getCustomColor(aint, p_getSmoothColorMultiplier_1_, blockposm);
/*      */         }
/*      */         
/*  629 */         i += (i2 >> 16 & 0xFF);
/*  630 */         j += (i2 >> 8 & 0xFF);
/*  631 */         k += (i2 & 0xFF);
/*      */       }
/*      */     }
/*      */     
/*  635 */     int j2 = i / 9;
/*  636 */     int k2 = j / 9;
/*  637 */     int l2 = k / 9;
/*  638 */     return j2 << 16 | k2 << 8 | l2;
/*      */   }
/*      */   
/*      */   public static int getFluidColor(Block p_getFluidColor_0_, IBlockAccess p_getFluidColor_1_, BlockPos p_getFluidColor_2_)
/*      */   {
/*  643 */     return !Config.isSwampColors() ? 16777215 : waterColors != null ? getCustomColor(waterColors, p_getFluidColor_1_, p_getFluidColor_2_) : Config.isSmoothBiomes() ? getSmoothColor(waterColors, p_getFluidColor_1_, p_getFluidColor_2_.getX(), p_getFluidColor_2_.getY(), p_getFluidColor_2_.getZ(), 3, 1) : p_getFluidColor_0_.getMaterial() != Material.water ? p_getFluidColor_0_.colorMultiplier(p_getFluidColor_1_, p_getFluidColor_2_) : p_getFluidColor_0_.colorMultiplier(p_getFluidColor_1_, p_getFluidColor_2_);
/*      */   }
/*      */   
/*      */   private static int getCustomColor(int[] p_getCustomColor_0_, IBlockAccess p_getCustomColor_1_, BlockPos p_getCustomColor_2_)
/*      */   {
/*  648 */     BiomeGenBase biomegenbase = p_getCustomColor_1_.getBiomeGenForCoords(p_getCustomColor_2_);
/*  649 */     double d0 = MathHelper.clamp_float(biomegenbase.getFloatTemperature(p_getCustomColor_2_), 0.0F, 1.0F);
/*  650 */     double d1 = MathHelper.clamp_float(biomegenbase.getFloatRainfall(), 0.0F, 1.0F);
/*  651 */     d1 *= d0;
/*  652 */     int i = (int)((1.0D - d0) * 255.0D);
/*  653 */     int j = (int)((1.0D - d1) * 255.0D);
/*  654 */     return p_getCustomColor_0_[(j << 8 | i)] & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   public static void updatePortalFX(EntityFX p_updatePortalFX_0_)
/*      */   {
/*  659 */     if (particlePortalColor >= 0)
/*      */     {
/*  661 */       int i = particlePortalColor;
/*  662 */       int j = i >> 16 & 0xFF;
/*  663 */       int k = i >> 8 & 0xFF;
/*  664 */       int l = i & 0xFF;
/*  665 */       float f = j / 255.0F;
/*  666 */       float f1 = k / 255.0F;
/*  667 */       float f2 = l / 255.0F;
/*  668 */       p_updatePortalFX_0_.setRBGColorF(f, f1, f2);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void updateMyceliumFX(EntityFX p_updateMyceliumFX_0_)
/*      */   {
/*  674 */     if (myceliumParticleColors != null)
/*      */     {
/*  676 */       int i = myceliumParticleColors[random.nextInt(myceliumParticleColors.length)];
/*  677 */       int j = i >> 16 & 0xFF;
/*  678 */       int k = i >> 8 & 0xFF;
/*  679 */       int l = i & 0xFF;
/*  680 */       float f = j / 255.0F;
/*  681 */       float f1 = k / 255.0F;
/*  682 */       float f2 = l / 255.0F;
/*  683 */       p_updateMyceliumFX_0_.setRBGColorF(f, f1, f2);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void updateReddustFX(EntityFX p_updateReddustFX_0_, IBlockAccess p_updateReddustFX_1_, double p_updateReddustFX_2_, double p_updateReddustFX_4_, double p_updateReddustFX_6_)
/*      */   {
/*  689 */     if (redstoneColors != null)
/*      */     {
/*  691 */       IBlockState iblockstate = p_updateReddustFX_1_.getBlockState(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_));
/*  692 */       int i = getRedstoneLevel(iblockstate, 15);
/*  693 */       int j = getRedstoneColor(i);
/*      */       
/*  695 */       if (j != -1)
/*      */       {
/*  697 */         int k = j >> 16 & 0xFF;
/*  698 */         int l = j >> 8 & 0xFF;
/*  699 */         int i1 = j & 0xFF;
/*  700 */         float f = k / 255.0F;
/*  701 */         float f1 = l / 255.0F;
/*  702 */         float f2 = i1 / 255.0F;
/*  703 */         p_updateReddustFX_0_.setRBGColorF(f, f1, f2);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static int getRedstoneLevel(IBlockState p_getRedstoneLevel_0_, int p_getRedstoneLevel_1_)
/*      */   {
/*  710 */     Block block = p_getRedstoneLevel_0_.getBlock();
/*      */     
/*  712 */     if (!(block instanceof BlockRedstoneWire))
/*      */     {
/*  714 */       return p_getRedstoneLevel_1_;
/*      */     }
/*      */     
/*      */ 
/*  718 */     Object object = p_getRedstoneLevel_0_.getValue(BlockRedstoneWire.POWER);
/*      */     
/*  720 */     if (!(object instanceof Integer))
/*      */     {
/*  722 */       return p_getRedstoneLevel_1_;
/*      */     }
/*      */     
/*      */ 
/*  726 */     Integer integer = (Integer)object;
/*  727 */     return integer.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getRedstoneColor(int p_getRedstoneColor_0_)
/*      */   {
/*  734 */     return (p_getRedstoneColor_0_ >= 0) && (p_getRedstoneColor_0_ <= 15) ? redstoneColors[p_getRedstoneColor_0_] & 0xFFFFFF : redstoneColors == null ? -1 : -1;
/*      */   }
/*      */   
/*      */   public static void updateWaterFX(EntityFX p_updateWaterFX_0_, IBlockAccess p_updateWaterFX_1_, double p_updateWaterFX_2_, double p_updateWaterFX_4_, double p_updateWaterFX_6_)
/*      */   {
/*  739 */     if (waterColors != null)
/*      */     {
/*  741 */       int i = getFluidColor(Blocks.water, p_updateWaterFX_1_, new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_));
/*  742 */       int j = i >> 16 & 0xFF;
/*  743 */       int k = i >> 8 & 0xFF;
/*  744 */       int l = i & 0xFF;
/*  745 */       float f = j / 255.0F;
/*  746 */       float f1 = k / 255.0F;
/*  747 */       float f2 = l / 255.0F;
/*      */       
/*  749 */       if (particleWaterColor >= 0)
/*      */       {
/*  751 */         int i1 = particleWaterColor >> 16 & 0xFF;
/*  752 */         int j1 = particleWaterColor >> 8 & 0xFF;
/*  753 */         int k1 = particleWaterColor & 0xFF;
/*  754 */         f *= i1 / 255.0F;
/*  755 */         f1 *= j1 / 255.0F;
/*  756 */         f2 *= k1 / 255.0F;
/*      */       }
/*      */       
/*  759 */       p_updateWaterFX_0_.setRBGColorF(f, f1, f2);
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getLilypadColorMultiplier(IBlockAccess p_getLilypadColorMultiplier_0_, BlockPos p_getLilypadColorMultiplier_1_)
/*      */   {
/*  765 */     return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_) : lilyPadColor;
/*      */   }
/*      */   
/*      */   public static Vec3 getFogColorNether(Vec3 p_getFogColorNether_0_)
/*      */   {
/*  770 */     return fogColorNether == null ? p_getFogColorNether_0_ : fogColorNether;
/*      */   }
/*      */   
/*      */   public static Vec3 getFogColorEnd(Vec3 p_getFogColorEnd_0_)
/*      */   {
/*  775 */     return fogColorEnd == null ? p_getFogColorEnd_0_ : fogColorEnd;
/*      */   }
/*      */   
/*      */   public static Vec3 getSkyColorEnd(Vec3 p_getSkyColorEnd_0_)
/*      */   {
/*  780 */     return skyColorEnd == null ? p_getSkyColorEnd_0_ : skyColorEnd;
/*      */   }
/*      */   
/*      */   public static Vec3 getSkyColor(Vec3 p_getSkyColor_0_, IBlockAccess p_getSkyColor_1_, double p_getSkyColor_2_, double p_getSkyColor_4_, double p_getSkyColor_6_)
/*      */   {
/*  785 */     if (skyColors == null)
/*      */     {
/*  787 */       return p_getSkyColor_0_;
/*      */     }
/*      */     
/*      */ 
/*  791 */     int i = getSmoothColor(skyColors, p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 7, 1);
/*  792 */     int j = i >> 16 & 0xFF;
/*  793 */     int k = i >> 8 & 0xFF;
/*  794 */     int l = i & 0xFF;
/*  795 */     float f = j / 255.0F;
/*  796 */     float f1 = k / 255.0F;
/*  797 */     float f2 = l / 255.0F;
/*  798 */     float f3 = (float)p_getSkyColor_0_.xCoord / 0.5F;
/*  799 */     float f4 = (float)p_getSkyColor_0_.yCoord / 0.66275F;
/*  800 */     float f5 = (float)p_getSkyColor_0_.zCoord;
/*  801 */     f *= f3;
/*  802 */     f1 *= f4;
/*  803 */     f2 *= f5;
/*  804 */     return new Vec3(f, f1, f2);
/*      */   }
/*      */   
/*      */ 
/*      */   public static Vec3 getFogColor(Vec3 p_getFogColor_0_, IBlockAccess p_getFogColor_1_, double p_getFogColor_2_, double p_getFogColor_4_, double p_getFogColor_6_)
/*      */   {
/*  810 */     if (fogColors == null)
/*      */     {
/*  812 */       return p_getFogColor_0_;
/*      */     }
/*      */     
/*      */ 
/*  816 */     int i = getSmoothColor(fogColors, p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 7, 1);
/*  817 */     int j = i >> 16 & 0xFF;
/*  818 */     int k = i >> 8 & 0xFF;
/*  819 */     int l = i & 0xFF;
/*  820 */     float f = j / 255.0F;
/*  821 */     float f1 = k / 255.0F;
/*  822 */     float f2 = l / 255.0F;
/*  823 */     float f3 = (float)p_getFogColor_0_.xCoord / 0.753F;
/*  824 */     float f4 = (float)p_getFogColor_0_.yCoord / 0.8471F;
/*  825 */     float f5 = (float)p_getFogColor_0_.zCoord;
/*  826 */     f *= f3;
/*  827 */     f1 *= f4;
/*  828 */     f2 *= f5;
/*  829 */     return new Vec3(f, f1, f2);
/*      */   }
/*      */   
/*      */ 
/*      */   public static Vec3 getUnderwaterColor(IBlockAccess p_getUnderwaterColor_0_, double p_getUnderwaterColor_1_, double p_getUnderwaterColor_3_, double p_getUnderwaterColor_5_)
/*      */   {
/*  835 */     if (underwaterColors == null)
/*      */     {
/*  837 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  841 */     int i = getSmoothColor(underwaterColors, p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, 7, 1);
/*  842 */     int j = i >> 16 & 0xFF;
/*  843 */     int k = i >> 8 & 0xFF;
/*  844 */     int l = i & 0xFF;
/*  845 */     float f = j / 255.0F;
/*  846 */     float f1 = k / 255.0F;
/*  847 */     float f2 = l / 255.0F;
/*  848 */     return new Vec3(f, f1, f2);
/*      */   }
/*      */   
/*      */ 
/*      */   public static int getSmoothColor(int[] p_getSmoothColor_0_, IBlockAccess p_getSmoothColor_1_, double p_getSmoothColor_2_, double p_getSmoothColor_4_, double p_getSmoothColor_6_, int p_getSmoothColor_8_, int p_getSmoothColor_9_)
/*      */   {
/*  854 */     if (p_getSmoothColor_0_ == null)
/*      */     {
/*  856 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  860 */     int i = MathHelper.floor_double(p_getSmoothColor_2_);
/*  861 */     int j = MathHelper.floor_double(p_getSmoothColor_4_);
/*  862 */     int k = MathHelper.floor_double(p_getSmoothColor_6_);
/*  863 */     int l = p_getSmoothColor_8_ * p_getSmoothColor_9_ / 2;
/*  864 */     int i1 = 0;
/*  865 */     int j1 = 0;
/*  866 */     int k1 = 0;
/*  867 */     int l1 = 0;
/*  868 */     BlockPosM blockposm = new BlockPosM(0, 0, 0);
/*      */     
/*  870 */     for (int i2 = i - l; i2 <= i + l; i2 += p_getSmoothColor_9_)
/*      */     {
/*  872 */       for (int j2 = k - l; j2 <= k + l; j2 += p_getSmoothColor_9_)
/*      */       {
/*  874 */         blockposm.setXyz(i2, j, j2);
/*  875 */         int k2 = getCustomColor(p_getSmoothColor_0_, p_getSmoothColor_1_, blockposm);
/*  876 */         i1 += (k2 >> 16 & 0xFF);
/*  877 */         j1 += (k2 >> 8 & 0xFF);
/*  878 */         k1 += (k2 & 0xFF);
/*  879 */         l1++;
/*      */       }
/*      */     }
/*      */     
/*  883 */     int l2 = i1 / l1;
/*  884 */     int i3 = j1 / l1;
/*  885 */     int j3 = k1 / l1;
/*  886 */     return l2 << 16 | i3 << 8 | j3;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int mixColors(int p_mixColors_0_, int p_mixColors_1_, float p_mixColors_2_)
/*      */   {
/*  892 */     if (p_mixColors_2_ <= 0.0F)
/*      */     {
/*  894 */       return p_mixColors_1_;
/*      */     }
/*  896 */     if (p_mixColors_2_ >= 1.0F)
/*      */     {
/*  898 */       return p_mixColors_0_;
/*      */     }
/*      */     
/*      */ 
/*  902 */     float f = 1.0F - p_mixColors_2_;
/*  903 */     int i = p_mixColors_0_ >> 16 & 0xFF;
/*  904 */     int j = p_mixColors_0_ >> 8 & 0xFF;
/*  905 */     int k = p_mixColors_0_ & 0xFF;
/*  906 */     int l = p_mixColors_1_ >> 16 & 0xFF;
/*  907 */     int i1 = p_mixColors_1_ >> 8 & 0xFF;
/*  908 */     int j1 = p_mixColors_1_ & 0xFF;
/*  909 */     int k1 = (int)(i * p_mixColors_2_ + l * f);
/*  910 */     int l1 = (int)(j * p_mixColors_2_ + i1 * f);
/*  911 */     int i2 = (int)(k * p_mixColors_2_ + j1 * f);
/*  912 */     return k1 << 16 | l1 << 8 | i2;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int averageColor(int p_averageColor_0_, int p_averageColor_1_)
/*      */   {
/*  918 */     int i = p_averageColor_0_ >> 16 & 0xFF;
/*  919 */     int j = p_averageColor_0_ >> 8 & 0xFF;
/*  920 */     int k = p_averageColor_0_ & 0xFF;
/*  921 */     int l = p_averageColor_1_ >> 16 & 0xFF;
/*  922 */     int i1 = p_averageColor_1_ >> 8 & 0xFF;
/*  923 */     int j1 = p_averageColor_1_ & 0xFF;
/*  924 */     int k1 = (i + l) / 2;
/*  925 */     int l1 = (j + i1) / 2;
/*  926 */     int i2 = (k + j1) / 2;
/*  927 */     return k1 << 16 | l1 << 8 | i2;
/*      */   }
/*      */   
/*      */   public static int getStemColorMultiplier(Block p_getStemColorMultiplier_0_, IBlockAccess p_getStemColorMultiplier_1_, BlockPos p_getStemColorMultiplier_2_, RenderEnv p_getStemColorMultiplier_3_)
/*      */   {
/*  932 */     if (stemColors == null)
/*      */     {
/*  934 */       return p_getStemColorMultiplier_0_.colorMultiplier(p_getStemColorMultiplier_1_, p_getStemColorMultiplier_2_);
/*      */     }
/*      */     
/*      */ 
/*  938 */     int i = p_getStemColorMultiplier_3_.getMetadata();
/*      */     
/*  940 */     if (i < 0)
/*      */     {
/*  942 */       i = 0;
/*      */     }
/*      */     
/*  945 */     if (i >= stemColors.length)
/*      */     {
/*  947 */       i = stemColors.length - 1;
/*      */     }
/*      */     
/*  950 */     return stemColors[i];
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean updateLightmap(World p_updateLightmap_0_, float p_updateLightmap_1_, int[] p_updateLightmap_2_, boolean p_updateLightmap_3_)
/*      */   {
/*  956 */     if (p_updateLightmap_0_ == null)
/*      */     {
/*  958 */       return false;
/*      */     }
/*  960 */     if (lightMapsColorsRgb == null)
/*      */     {
/*  962 */       return false;
/*      */     }
/*  964 */     if (!Config.isCustomColors())
/*      */     {
/*  966 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  970 */     int i = p_updateLightmap_0_.provider.getDimensionId();
/*      */     
/*  972 */     if ((i >= -1) && (i <= 1))
/*      */     {
/*  974 */       int j = i + 1;
/*  975 */       float[][] afloat = lightMapsColorsRgb[j];
/*      */       
/*  977 */       if (afloat == null)
/*      */       {
/*  979 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  983 */       int k = lightMapsHeight[j];
/*      */       
/*  985 */       if ((p_updateLightmap_3_) && (k < 64))
/*      */       {
/*  987 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  991 */       int l = afloat.length / k;
/*      */       
/*  993 */       if (l < 16)
/*      */       {
/*  995 */         Config.warn("Invalid lightmap width: " + l + " for: /environment/lightmap" + i + ".png");
/*  996 */         lightMapsColorsRgb[j] = null;
/*  997 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1001 */       int i1 = 0;
/*      */       
/* 1003 */       if (p_updateLightmap_3_)
/*      */       {
/* 1005 */         i1 = l * 16 * 2;
/*      */       }
/*      */       
/* 1008 */       float f = 1.1666666F * (p_updateLightmap_0_.getSunBrightness(1.0F) - 0.2F);
/*      */       
/* 1010 */       if (p_updateLightmap_0_.getLastLightningBolt() > 0)
/*      */       {
/* 1012 */         f = 1.0F;
/*      */       }
/*      */       
/* 1015 */       f = Config.limitTo1(f);
/* 1016 */       float f1 = f * (l - 1);
/* 1017 */       float f2 = Config.limitTo1(p_updateLightmap_1_ + 0.5F) * (l - 1);
/* 1018 */       float f3 = Config.limitTo1(Config.getGameSettings().gammaSetting);
/* 1019 */       boolean flag = f3 > 1.0E-4F;
/* 1020 */       getLightMapColumn(afloat, f1, i1, l, sunRgbs);
/* 1021 */       getLightMapColumn(afloat, f2, i1 + 16 * l, l, torchRgbs);
/* 1022 */       float[] afloat1 = new float[3];
/*      */       
/* 1024 */       for (int j1 = 0; j1 < 16; j1++)
/*      */       {
/* 1026 */         for (int k1 = 0; k1 < 16; k1++)
/*      */         {
/* 1028 */           for (int l1 = 0; l1 < 3; l1++)
/*      */           {
/* 1030 */             float f4 = Config.limitTo1(sunRgbs[j1][l1] + torchRgbs[k1][l1]);
/*      */             
/* 1032 */             if (flag)
/*      */             {
/* 1034 */               float f5 = 1.0F - f4;
/* 1035 */               f5 = 1.0F - f5 * f5 * f5 * f5;
/* 1036 */               f4 = f3 * f5 + (1.0F - f3) * f4;
/*      */             }
/*      */             
/* 1039 */             afloat1[l1] = f4;
/*      */           }
/*      */           
/* 1042 */           int i2 = (int)(afloat1[0] * 255.0F);
/* 1043 */           int j2 = (int)(afloat1[1] * 255.0F);
/* 1044 */           int k2 = (int)(afloat1[2] * 255.0F);
/* 1045 */           p_updateLightmap_2_[(j1 * 16 + k1)] = (0xFF000000 | i2 << 16 | j2 << 8 | k2);
/*      */         }
/*      */       }
/*      */       
/* 1049 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1056 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void getLightMapColumn(float[][] p_getLightMapColumn_0_, float p_getLightMapColumn_1_, int p_getLightMapColumn_2_, int p_getLightMapColumn_3_, float[][] p_getLightMapColumn_4_)
/*      */   {
/* 1063 */     int i = (int)Math.floor(p_getLightMapColumn_1_);
/* 1064 */     int j = (int)Math.ceil(p_getLightMapColumn_1_);
/*      */     
/* 1066 */     if (i == j)
/*      */     {
/* 1068 */       for (int i1 = 0; i1 < 16; i1++)
/*      */       {
/* 1070 */         float[] afloat3 = p_getLightMapColumn_0_[(p_getLightMapColumn_2_ + i1 * p_getLightMapColumn_3_ + i)];
/* 1071 */         float[] afloat4 = p_getLightMapColumn_4_[i1];
/*      */         
/* 1073 */         for (int j1 = 0; j1 < 3; j1++)
/*      */         {
/* 1075 */           afloat4[j1] = afloat3[j1];
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1081 */       float f = 1.0F - (p_getLightMapColumn_1_ - i);
/* 1082 */       float f1 = 1.0F - (j - p_getLightMapColumn_1_);
/*      */       
/* 1084 */       for (int k = 0; k < 16; k++)
/*      */       {
/* 1086 */         float[] afloat = p_getLightMapColumn_0_[(p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + i)];
/* 1087 */         float[] afloat1 = p_getLightMapColumn_0_[(p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + j)];
/* 1088 */         float[] afloat2 = p_getLightMapColumn_4_[k];
/*      */         
/* 1090 */         for (int l = 0; l < 3; l++)
/*      */         {
/* 1092 */           afloat2[l] = (afloat[l] * f + afloat1[l] * f1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Vec3 getWorldFogColor(Vec3 p_getWorldFogColor_0_, WorldClient p_getWorldFogColor_1_, Entity p_getWorldFogColor_2_, float p_getWorldFogColor_3_)
/*      */   {
/* 1100 */     int i = p_getWorldFogColor_1_.provider.getDimensionId();
/*      */     
/* 1102 */     switch (i)
/*      */     {
/*      */     case -1: 
/* 1105 */       p_getWorldFogColor_0_ = getFogColorNether(p_getWorldFogColor_0_);
/* 1106 */       break;
/*      */     
/*      */     case 0: 
/* 1109 */       Minecraft minecraft = Minecraft.getMinecraft();
/* 1110 */       p_getWorldFogColor_0_ = getFogColor(p_getWorldFogColor_0_, minecraft.theWorld, p_getWorldFogColor_2_.posX, p_getWorldFogColor_2_.posY + 1.0D, p_getWorldFogColor_2_.posZ);
/* 1111 */       break;
/*      */     
/*      */     case 1: 
/* 1114 */       p_getWorldFogColor_0_ = getFogColorEnd(p_getWorldFogColor_0_);
/*      */     }
/*      */     
/* 1117 */     return p_getWorldFogColor_0_;
/*      */   }
/*      */   
/*      */   public static Vec3 getWorldSkyColor(Vec3 p_getWorldSkyColor_0_, WorldClient p_getWorldSkyColor_1_, Entity p_getWorldSkyColor_2_, float p_getWorldSkyColor_3_)
/*      */   {
/* 1122 */     int i = p_getWorldSkyColor_1_.provider.getDimensionId();
/*      */     
/* 1124 */     switch (i)
/*      */     {
/*      */     case 0: 
/* 1127 */       Minecraft minecraft = Minecraft.getMinecraft();
/* 1128 */       p_getWorldSkyColor_0_ = getSkyColor(p_getWorldSkyColor_0_, minecraft.theWorld, p_getWorldSkyColor_2_.posX, p_getWorldSkyColor_2_.posY + 1.0D, p_getWorldSkyColor_2_.posZ);
/* 1129 */       break;
/*      */     
/*      */     case 1: 
/* 1132 */       p_getWorldSkyColor_0_ = getSkyColorEnd(p_getWorldSkyColor_0_);
/*      */     }
/*      */     
/* 1135 */     return p_getWorldSkyColor_0_;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CustomColorizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */