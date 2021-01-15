/*      */ package optfine;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockPane;
/*      */ import net.minecraft.block.BlockQuartz;
/*      */ import net.minecraft.block.BlockRotatedPillar;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.BlockModelShapes;
/*      */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*      */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.resources.AbstractResourcePack;
/*      */ import net.minecraft.client.resources.DefaultResourcePack;
/*      */ import net.minecraft.client.resources.IResourcePack;
/*      */ import net.minecraft.client.resources.model.IBakedModel;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ 
/*      */ public class ConnectedTextures
/*      */ {
/*   40 */   private static Map[] spriteQuadMaps = null;
/*   41 */   private static ConnectedProperties[][] blockProperties = null;
/*   42 */   private static ConnectedProperties[][] tileProperties = null;
/*   43 */   private static boolean multipass = false;
/*      */   private static final int Y_NEG_DOWN = 0;
/*      */   private static final int Y_POS_UP = 1;
/*      */   private static final int Z_NEG_NORTH = 2;
/*      */   private static final int Z_POS_SOUTH = 3;
/*      */   private static final int X_NEG_WEST = 4;
/*      */   private static final int X_POS_EAST = 5;
/*      */   private static final int Y_AXIS = 0;
/*      */   private static final int Z_AXIS = 1;
/*      */   private static final int X_AXIS = 2;
/*   53 */   private static final String[] propSuffixes = { "", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
/*   54 */   private static final int[] ctmIndexes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46 };
/*   55 */   public static final IBlockState AIR_DEFAULT_STATE = net.minecraft.init.Blocks.air.getDefaultState();
/*   56 */   private static TextureAtlasSprite emptySprite = null;
/*      */   
/*      */   public static synchronized BakedQuad getConnectedTexture(IBlockAccess p_getConnectedTexture_0_, IBlockState p_getConnectedTexture_1_, BlockPos p_getConnectedTexture_2_, BakedQuad p_getConnectedTexture_3_, RenderEnv p_getConnectedTexture_4_)
/*      */   {
/*   60 */     TextureAtlasSprite textureatlassprite = p_getConnectedTexture_3_.getSprite();
/*      */     
/*   62 */     if (textureatlassprite == null)
/*      */     {
/*   64 */       return p_getConnectedTexture_3_;
/*      */     }
/*      */     
/*      */ 
/*   68 */     Block block = p_getConnectedTexture_1_.getBlock();
/*   69 */     EnumFacing enumfacing = p_getConnectedTexture_3_.getFace();
/*      */     
/*   71 */     if (((block instanceof BlockPane)) && (textureatlassprite.getIconName().startsWith("minecraft:blocks/glass_pane_top")))
/*      */     {
/*   73 */       IBlockState iblockstate = p_getConnectedTexture_0_.getBlockState(p_getConnectedTexture_2_.offset(p_getConnectedTexture_3_.getFace()));
/*      */       
/*   75 */       if (iblockstate == p_getConnectedTexture_1_)
/*      */       {
/*   77 */         return getQuad(emptySprite, block, p_getConnectedTexture_1_, p_getConnectedTexture_3_);
/*      */       }
/*      */     }
/*      */     
/*   81 */     TextureAtlasSprite textureatlassprite1 = getConnectedTextureMultiPass(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, enumfacing, textureatlassprite, p_getConnectedTexture_4_);
/*   82 */     return textureatlassprite1 == textureatlassprite ? p_getConnectedTexture_3_ : getQuad(textureatlassprite1, block, p_getConnectedTexture_1_, p_getConnectedTexture_3_);
/*      */   }
/*      */   
/*      */ 
/*      */   private static BakedQuad getQuad(TextureAtlasSprite p_getQuad_0_, Block p_getQuad_1_, IBlockState p_getQuad_2_, BakedQuad p_getQuad_3_)
/*      */   {
/*   88 */     if (spriteQuadMaps == null)
/*      */     {
/*   90 */       return p_getQuad_3_;
/*      */     }
/*      */     
/*      */ 
/*   94 */     int i = p_getQuad_0_.getIndexInMap();
/*      */     
/*   96 */     if ((i >= 0) && (i < spriteQuadMaps.length))
/*      */     {
/*   98 */       Map map = spriteQuadMaps[i];
/*      */       
/*  100 */       if (map == null)
/*      */       {
/*  102 */         map = new java.util.IdentityHashMap(1);
/*  103 */         spriteQuadMaps[i] = map;
/*      */       }
/*      */       
/*  106 */       BakedQuad bakedquad = (BakedQuad)map.get(p_getQuad_3_);
/*      */       
/*  108 */       if (bakedquad == null)
/*      */       {
/*  110 */         bakedquad = makeSpriteQuad(p_getQuad_3_, p_getQuad_0_);
/*  111 */         map.put(p_getQuad_3_, bakedquad);
/*      */       }
/*      */       
/*  114 */       return bakedquad;
/*      */     }
/*      */     
/*      */ 
/*  118 */     return p_getQuad_3_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static BakedQuad makeSpriteQuad(BakedQuad p_makeSpriteQuad_0_, TextureAtlasSprite p_makeSpriteQuad_1_)
/*      */   {
/*  125 */     int[] aint = (int[])p_makeSpriteQuad_0_.getVertexData().clone();
/*  126 */     TextureAtlasSprite textureatlassprite = p_makeSpriteQuad_0_.getSprite();
/*      */     
/*  128 */     for (int i = 0; i < 4; i++)
/*      */     {
/*  130 */       fixVertex(aint, i, textureatlassprite, p_makeSpriteQuad_1_);
/*      */     }
/*      */     
/*  133 */     BakedQuad bakedquad = new BakedQuad(aint, p_makeSpriteQuad_0_.getTintIndex(), p_makeSpriteQuad_0_.getFace(), p_makeSpriteQuad_1_);
/*  134 */     return bakedquad;
/*      */   }
/*      */   
/*      */   private static void fixVertex(int[] p_fixVertex_0_, int p_fixVertex_1_, TextureAtlasSprite p_fixVertex_2_, TextureAtlasSprite p_fixVertex_3_)
/*      */   {
/*  139 */     int i = 7 * p_fixVertex_1_;
/*  140 */     float f = Float.intBitsToFloat(p_fixVertex_0_[(i + 4)]);
/*  141 */     float f1 = Float.intBitsToFloat(p_fixVertex_0_[(i + 4 + 1)]);
/*  142 */     double d0 = p_fixVertex_2_.getSpriteU16(f);
/*  143 */     double d1 = p_fixVertex_2_.getSpriteV16(f1);
/*  144 */     p_fixVertex_0_[(i + 4)] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedU(d0));
/*  145 */     p_fixVertex_0_[(i + 4 + 1)] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedV(d1));
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite getConnectedTextureMultiPass(IBlockAccess p_getConnectedTextureMultiPass_0_, IBlockState p_getConnectedTextureMultiPass_1_, BlockPos p_getConnectedTextureMultiPass_2_, EnumFacing p_getConnectedTextureMultiPass_3_, TextureAtlasSprite p_getConnectedTextureMultiPass_4_, RenderEnv p_getConnectedTextureMultiPass_5_)
/*      */   {
/*  150 */     TextureAtlasSprite textureatlassprite = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, p_getConnectedTextureMultiPass_4_, true, p_getConnectedTextureMultiPass_5_);
/*      */     
/*  152 */     if (!multipass)
/*      */     {
/*  154 */       return textureatlassprite;
/*      */     }
/*  156 */     if (textureatlassprite == p_getConnectedTextureMultiPass_4_)
/*      */     {
/*  158 */       return textureatlassprite;
/*      */     }
/*      */     
/*      */ 
/*  162 */     TextureAtlasSprite textureatlassprite1 = textureatlassprite;
/*      */     
/*  164 */     for (int i = 0; i < 3; i++)
/*      */     {
/*  166 */       TextureAtlasSprite textureatlassprite2 = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, textureatlassprite1, false, p_getConnectedTextureMultiPass_5_);
/*      */       
/*  168 */       if (textureatlassprite2 == textureatlassprite1) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  173 */       textureatlassprite1 = textureatlassprite2;
/*      */     }
/*      */     
/*  176 */     return textureatlassprite1;
/*      */   }
/*      */   
/*      */ 
/*      */   public static TextureAtlasSprite getConnectedTextureSingle(IBlockAccess p_getConnectedTextureSingle_0_, IBlockState p_getConnectedTextureSingle_1_, BlockPos p_getConnectedTextureSingle_2_, EnumFacing p_getConnectedTextureSingle_3_, TextureAtlasSprite p_getConnectedTextureSingle_4_, boolean p_getConnectedTextureSingle_5_, RenderEnv p_getConnectedTextureSingle_6_)
/*      */   {
/*  182 */     Block block = p_getConnectedTextureSingle_1_.getBlock();
/*      */     
/*  184 */     if (tileProperties != null)
/*      */     {
/*  186 */       int i = p_getConnectedTextureSingle_4_.getIndexInMap();
/*      */       
/*  188 */       if ((i >= 0) && (i < tileProperties.length))
/*      */       {
/*  190 */         ConnectedProperties[] aconnectedproperties = tileProperties[i];
/*      */         
/*  192 */         if (aconnectedproperties != null)
/*      */         {
/*  194 */           int j = p_getConnectedTextureSingle_6_.getMetadata();
/*  195 */           int k = getSide(p_getConnectedTextureSingle_3_);
/*      */           
/*  197 */           for (int l = 0; l < aconnectedproperties.length; l++)
/*      */           {
/*  199 */             ConnectedProperties connectedproperties = aconnectedproperties[l];
/*      */             
/*  201 */             if (connectedproperties != null)
/*      */             {
/*  203 */               int i1 = p_getConnectedTextureSingle_6_.getBlockId();
/*      */               
/*  205 */               if (connectedproperties.matchesBlock(i1))
/*      */               {
/*  207 */                 TextureAtlasSprite textureatlassprite = getConnectedTexture(connectedproperties, p_getConnectedTextureSingle_0_, p_getConnectedTextureSingle_1_, p_getConnectedTextureSingle_2_, k, p_getConnectedTextureSingle_4_, j, p_getConnectedTextureSingle_6_);
/*      */                 
/*  209 */                 if (textureatlassprite != null)
/*      */                 {
/*  211 */                   return textureatlassprite;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  220 */     if ((blockProperties != null) && (p_getConnectedTextureSingle_5_))
/*      */     {
/*  222 */       int j1 = p_getConnectedTextureSingle_6_.getBlockId();
/*      */       
/*  224 */       if ((j1 >= 0) && (j1 < blockProperties.length))
/*      */       {
/*  226 */         ConnectedProperties[] aconnectedproperties1 = blockProperties[j1];
/*      */         
/*  228 */         if (aconnectedproperties1 != null)
/*      */         {
/*  230 */           int k1 = p_getConnectedTextureSingle_6_.getMetadata();
/*  231 */           int l1 = getSide(p_getConnectedTextureSingle_3_);
/*      */           
/*  233 */           for (int i2 = 0; i2 < aconnectedproperties1.length; i2++)
/*      */           {
/*  235 */             ConnectedProperties connectedproperties1 = aconnectedproperties1[i2];
/*      */             
/*  237 */             if ((connectedproperties1 != null) && (connectedproperties1.matchesIcon(p_getConnectedTextureSingle_4_)))
/*      */             {
/*  239 */               TextureAtlasSprite textureatlassprite1 = getConnectedTexture(connectedproperties1, p_getConnectedTextureSingle_0_, p_getConnectedTextureSingle_1_, p_getConnectedTextureSingle_2_, l1, p_getConnectedTextureSingle_4_, k1, p_getConnectedTextureSingle_6_);
/*      */               
/*  241 */               if (textureatlassprite1 != null)
/*      */               {
/*  243 */                 return textureatlassprite1;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  251 */     return p_getConnectedTextureSingle_4_;
/*      */   }
/*      */   
/*      */   public static int getSide(EnumFacing p_getSide_0_)
/*      */   {
/*  256 */     if (p_getSide_0_ == null)
/*      */     {
/*  258 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  262 */     switch (p_getSide_0_)
/*      */     {
/*      */     case DOWN: 
/*  265 */       return 0;
/*      */     
/*      */     case EAST: 
/*  268 */       return 1;
/*      */     
/*      */     case WEST: 
/*  271 */       return 5;
/*      */     
/*      */     case UP: 
/*  274 */       return 4;
/*      */     
/*      */     case NORTH: 
/*  277 */       return 2;
/*      */     
/*      */     case SOUTH: 
/*  280 */       return 3;
/*      */     }
/*      */     
/*  283 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static EnumFacing getFacing(int p_getFacing_0_)
/*      */   {
/*  290 */     switch (p_getFacing_0_)
/*      */     {
/*      */     case 0: 
/*  293 */       return EnumFacing.DOWN;
/*      */     
/*      */     case 1: 
/*  296 */       return EnumFacing.UP;
/*      */     
/*      */     case 2: 
/*  299 */       return EnumFacing.NORTH;
/*      */     
/*      */     case 3: 
/*  302 */       return EnumFacing.SOUTH;
/*      */     
/*      */     case 4: 
/*  305 */       return EnumFacing.WEST;
/*      */     
/*      */     case 5: 
/*  308 */       return EnumFacing.EAST;
/*      */     }
/*      */     
/*  311 */     return EnumFacing.UP;
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTexture(ConnectedProperties p_getConnectedTexture_0_, IBlockAccess p_getConnectedTexture_1_, IBlockState p_getConnectedTexture_2_, BlockPos p_getConnectedTexture_3_, int p_getConnectedTexture_4_, TextureAtlasSprite p_getConnectedTexture_5_, int p_getConnectedTexture_6_, RenderEnv p_getConnectedTexture_7_)
/*      */   {
/*  317 */     int i = p_getConnectedTexture_3_.getY();
/*      */     
/*  319 */     if ((i >= p_getConnectedTexture_0_.minHeight) && (i <= p_getConnectedTexture_0_.maxHeight))
/*      */     {
/*  321 */       if (p_getConnectedTexture_0_.biomes != null)
/*      */       {
/*  323 */         BiomeGenBase biomegenbase = p_getConnectedTexture_1_.getBiomeGenForCoords(p_getConnectedTexture_3_);
/*  324 */         boolean flag = false;
/*      */         
/*  326 */         for (int j = 0; j < p_getConnectedTexture_0_.biomes.length; j++)
/*      */         {
/*  328 */           BiomeGenBase biomegenbase1 = p_getConnectedTexture_0_.biomes[j];
/*      */           
/*  330 */           if (biomegenbase == biomegenbase1)
/*      */           {
/*  332 */             flag = true;
/*  333 */             break;
/*      */           }
/*      */         }
/*      */         
/*  337 */         if (!flag)
/*      */         {
/*  339 */           return null;
/*      */         }
/*      */       }
/*      */       
/*  343 */       int l = 0;
/*  344 */       int i1 = p_getConnectedTexture_6_;
/*  345 */       Block block = p_getConnectedTexture_2_.getBlock();
/*      */       
/*  347 */       if ((block instanceof BlockRotatedPillar))
/*      */       {
/*  349 */         l = getWoodAxis(p_getConnectedTexture_4_, p_getConnectedTexture_6_);
/*  350 */         i1 = p_getConnectedTexture_6_ & 0x3;
/*      */       }
/*      */       
/*  353 */       if ((block instanceof BlockQuartz))
/*      */       {
/*  355 */         l = getQuartzAxis(p_getConnectedTexture_4_, p_getConnectedTexture_6_);
/*      */         
/*  357 */         if (i1 > 2)
/*      */         {
/*  359 */           i1 = 2;
/*      */         }
/*      */       }
/*      */       
/*  363 */       if ((p_getConnectedTexture_4_ >= 0) && (p_getConnectedTexture_0_.faces != 63))
/*      */       {
/*  365 */         int j1 = p_getConnectedTexture_4_;
/*      */         
/*  367 */         if (l != 0)
/*      */         {
/*  369 */           j1 = fixSideByAxis(p_getConnectedTexture_4_, l);
/*      */         }
/*      */         
/*  372 */         if ((1 << j1 & p_getConnectedTexture_0_.faces) == 0)
/*      */         {
/*  374 */           return null;
/*      */         }
/*      */       }
/*      */       
/*  378 */       if (p_getConnectedTexture_0_.metadatas != null)
/*      */       {
/*  380 */         int[] aint = p_getConnectedTexture_0_.metadatas;
/*  381 */         boolean flag1 = false;
/*      */         
/*  383 */         for (int k = 0; k < aint.length; k++)
/*      */         {
/*  385 */           if (aint[k] == i1)
/*      */           {
/*  387 */             flag1 = true;
/*  388 */             break;
/*      */           }
/*      */         }
/*      */         
/*  392 */         if (!flag1)
/*      */         {
/*  394 */           return null;
/*      */         }
/*      */       }
/*      */       
/*  398 */       switch (p_getConnectedTexture_0_.method)
/*      */       {
/*      */       case 1: 
/*  401 */         return getConnectedTextureCtm(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_, p_getConnectedTexture_7_);
/*      */       
/*      */       case 2: 
/*  404 */         return getConnectedTextureHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, l, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_);
/*      */       
/*      */       case 3: 
/*  407 */         return getConnectedTextureTop(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, l, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_);
/*      */       
/*      */       case 4: 
/*  410 */         return getConnectedTextureRandom(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_);
/*      */       
/*      */       case 5: 
/*  413 */         return getConnectedTextureRepeat(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_);
/*      */       
/*      */       case 6: 
/*  416 */         return getConnectedTextureVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, l, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_);
/*      */       
/*      */       case 7: 
/*  419 */         return getConnectedTextureFixed(p_getConnectedTexture_0_);
/*      */       
/*      */       case 8: 
/*  422 */         return getConnectedTextureHorizontalVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, l, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_);
/*      */       
/*      */       case 9: 
/*  425 */         return getConnectedTextureVerticalHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, l, p_getConnectedTexture_4_, p_getConnectedTexture_5_, p_getConnectedTexture_6_);
/*      */       }
/*      */       
/*  428 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  433 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int fixSideByAxis(int p_fixSideByAxis_0_, int p_fixSideByAxis_1_)
/*      */   {
/*  439 */     switch (p_fixSideByAxis_1_)
/*      */     {
/*      */     case 0: 
/*  442 */       return p_fixSideByAxis_0_;
/*      */     
/*      */     case 1: 
/*  445 */       switch (p_fixSideByAxis_0_)
/*      */       {
/*      */       case 0: 
/*  448 */         return 2;
/*      */       
/*      */       case 1: 
/*  451 */         return 3;
/*      */       
/*      */       case 2: 
/*  454 */         return 1;
/*      */       
/*      */       case 3: 
/*  457 */         return 0;
/*      */       }
/*      */       
/*  460 */       return p_fixSideByAxis_0_;
/*      */     
/*      */ 
/*      */     case 2: 
/*  464 */       switch (p_fixSideByAxis_0_)
/*      */       {
/*      */       case 0: 
/*  467 */         return 4;
/*      */       
/*      */       case 1: 
/*  470 */         return 5;
/*      */       
/*      */       case 2: 
/*      */       case 3: 
/*      */       default: 
/*  475 */         return p_fixSideByAxis_0_;
/*      */       
/*      */       case 4: 
/*  478 */         return 1;
/*      */       }
/*      */       
/*  481 */       return 0;
/*      */     }
/*      */     
/*      */     
/*  485 */     return p_fixSideByAxis_0_;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int getWoodAxis(int p_getWoodAxis_0_, int p_getWoodAxis_1_)
/*      */   {
/*  491 */     int i = (p_getWoodAxis_1_ & 0xC) >> 2;
/*      */     
/*  493 */     switch (i)
/*      */     {
/*      */     case 1: 
/*  496 */       return 2;
/*      */     
/*      */     case 2: 
/*  499 */       return 1;
/*      */     }
/*      */     
/*  502 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int getQuartzAxis(int p_getQuartzAxis_0_, int p_getQuartzAxis_1_)
/*      */   {
/*  508 */     switch (p_getQuartzAxis_1_)
/*      */     {
/*      */     case 3: 
/*  511 */       return 2;
/*      */     
/*      */     case 4: 
/*  514 */       return 1;
/*      */     }
/*      */     
/*  517 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureRandom(ConnectedProperties p_getConnectedTextureRandom_0_, BlockPos p_getConnectedTextureRandom_1_, int p_getConnectedTextureRandom_2_)
/*      */   {
/*  523 */     if (p_getConnectedTextureRandom_0_.tileIcons.length == 1)
/*      */     {
/*  525 */       return p_getConnectedTextureRandom_0_.tileIcons[0];
/*      */     }
/*      */     
/*      */ 
/*  529 */     int i = p_getConnectedTextureRandom_2_ / p_getConnectedTextureRandom_0_.symmetry * p_getConnectedTextureRandom_0_.symmetry;
/*  530 */     int j = Config.getRandom(p_getConnectedTextureRandom_1_, i) & 0x7FFFFFFF;
/*  531 */     int k = 0;
/*      */     
/*  533 */     if (p_getConnectedTextureRandom_0_.weights == null)
/*      */     {
/*  535 */       k = j % p_getConnectedTextureRandom_0_.tileIcons.length;
/*      */     }
/*      */     else
/*      */     {
/*  539 */       int l = j % p_getConnectedTextureRandom_0_.sumAllWeights;
/*  540 */       int[] aint = p_getConnectedTextureRandom_0_.sumWeights;
/*      */       
/*  542 */       for (int i1 = 0; i1 < aint.length; i1++)
/*      */       {
/*  544 */         if (l < aint[i1])
/*      */         {
/*  546 */           k = i1;
/*  547 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  552 */     return p_getConnectedTextureRandom_0_.tileIcons[k];
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureFixed(ConnectedProperties p_getConnectedTextureFixed_0_)
/*      */   {
/*  558 */     return p_getConnectedTextureFixed_0_.tileIcons[0];
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite getConnectedTextureRepeat(ConnectedProperties p_getConnectedTextureRepeat_0_, BlockPos p_getConnectedTextureRepeat_1_, int p_getConnectedTextureRepeat_2_)
/*      */   {
/*  563 */     if (p_getConnectedTextureRepeat_0_.tileIcons.length == 1)
/*      */     {
/*  565 */       return p_getConnectedTextureRepeat_0_.tileIcons[0];
/*      */     }
/*      */     
/*      */ 
/*  569 */     int i = p_getConnectedTextureRepeat_1_.getX();
/*  570 */     int j = p_getConnectedTextureRepeat_1_.getY();
/*  571 */     int k = p_getConnectedTextureRepeat_1_.getZ();
/*  572 */     int l = 0;
/*  573 */     int i1 = 0;
/*      */     
/*  575 */     switch (p_getConnectedTextureRepeat_2_)
/*      */     {
/*      */     case 0: 
/*  578 */       l = i;
/*  579 */       i1 = k;
/*  580 */       break;
/*      */     
/*      */     case 1: 
/*  583 */       l = i;
/*  584 */       i1 = k;
/*  585 */       break;
/*      */     
/*      */     case 2: 
/*  588 */       l = -i - 1;
/*  589 */       i1 = -j;
/*  590 */       break;
/*      */     
/*      */     case 3: 
/*  593 */       l = i;
/*  594 */       i1 = -j;
/*  595 */       break;
/*      */     
/*      */     case 4: 
/*  598 */       l = k;
/*  599 */       i1 = -j;
/*  600 */       break;
/*      */     
/*      */     case 5: 
/*  603 */       l = -k - 1;
/*  604 */       i1 = -j;
/*      */     }
/*      */     
/*  607 */     l = l % p_getConnectedTextureRepeat_0_.width;
/*  608 */     i1 %= p_getConnectedTextureRepeat_0_.height;
/*      */     
/*  610 */     if (l < 0)
/*      */     {
/*  612 */       l += p_getConnectedTextureRepeat_0_.width;
/*      */     }
/*      */     
/*  615 */     if (i1 < 0)
/*      */     {
/*  617 */       i1 += p_getConnectedTextureRepeat_0_.height;
/*      */     }
/*      */     
/*  620 */     int j1 = i1 * p_getConnectedTextureRepeat_0_.width + l;
/*  621 */     return p_getConnectedTextureRepeat_0_.tileIcons[j1];
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureCtm(ConnectedProperties p_getConnectedTextureCtm_0_, IBlockAccess p_getConnectedTextureCtm_1_, IBlockState p_getConnectedTextureCtm_2_, BlockPos p_getConnectedTextureCtm_3_, int p_getConnectedTextureCtm_4_, TextureAtlasSprite p_getConnectedTextureCtm_5_, int p_getConnectedTextureCtm_6_, RenderEnv p_getConnectedTextureCtm_7_)
/*      */   {
/*  627 */     boolean[] aboolean = p_getConnectedTextureCtm_7_.getBorderFlags();
/*      */     
/*  629 */     switch (p_getConnectedTextureCtm_4_)
/*      */     {
/*      */     case 0: 
/*  632 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  633 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  634 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  635 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  636 */       break;
/*      */     
/*      */     case 1: 
/*  639 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  640 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  641 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  642 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  643 */       break;
/*      */     
/*      */     case 2: 
/*  646 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  647 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  648 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  649 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  650 */       break;
/*      */     
/*      */     case 3: 
/*  653 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  654 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  655 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  656 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  657 */       break;
/*      */     
/*      */     case 4: 
/*  660 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  661 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  662 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  663 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  664 */       break;
/*      */     
/*      */     case 5: 
/*  667 */       aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  668 */       aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  669 */       aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*  670 */       aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_);
/*      */     }
/*      */     
/*  673 */     int i = 0;
/*      */     
/*  675 */     if ((aboolean[0] & (aboolean[1] != 0 ? 0 : 1) & (aboolean[2] != 0 ? 0 : 1) & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  677 */       i = 3;
/*      */     }
/*  679 */     else if (((aboolean[0] != 0 ? 0 : 1) & aboolean[1] & (aboolean[2] != 0 ? 0 : 1) & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  681 */       i = 1;
/*      */     }
/*  683 */     else if (((aboolean[0] != 0 ? 0 : 1) & (aboolean[1] != 0 ? 0 : 1) & aboolean[2] & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  685 */       i = 12;
/*      */     }
/*  687 */     else if (((aboolean[0] != 0 ? 0 : 1) & (aboolean[1] != 0 ? 0 : 1) & (aboolean[2] != 0 ? 0 : 1) & aboolean[3]) != 0)
/*      */     {
/*  689 */       i = 36;
/*      */     }
/*  691 */     else if ((aboolean[0] & aboolean[1] & (aboolean[2] != 0 ? 0 : 1) & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  693 */       i = 2;
/*      */     }
/*  695 */     else if (((aboolean[0] != 0 ? 0 : 1) & (aboolean[1] != 0 ? 0 : 1) & aboolean[2] & aboolean[3]) != 0)
/*      */     {
/*  697 */       i = 24;
/*      */     }
/*  699 */     else if ((aboolean[0] & (aboolean[1] != 0 ? 0 : 1) & aboolean[2] & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  701 */       i = 15;
/*      */     }
/*  703 */     else if ((aboolean[0] & (aboolean[1] != 0 ? 0 : 1) & (aboolean[2] != 0 ? 0 : 1) & aboolean[3]) != 0)
/*      */     {
/*  705 */       i = 39;
/*      */     }
/*  707 */     else if (((aboolean[0] != 0 ? 0 : 1) & aboolean[1] & aboolean[2] & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  709 */       i = 13;
/*      */     }
/*  711 */     else if (((aboolean[0] != 0 ? 0 : 1) & aboolean[1] & (aboolean[2] != 0 ? 0 : 1) & aboolean[3]) != 0)
/*      */     {
/*  713 */       i = 37;
/*      */     }
/*  715 */     else if (((aboolean[0] != 0 ? 0 : 1) & aboolean[1] & aboolean[2] & aboolean[3]) != 0)
/*      */     {
/*  717 */       i = 25;
/*      */     }
/*  719 */     else if ((aboolean[0] & (aboolean[1] != 0 ? 0 : 1) & aboolean[2] & aboolean[3]) != 0)
/*      */     {
/*  721 */       i = 27;
/*      */     }
/*  723 */     else if ((aboolean[0] & aboolean[1] & (aboolean[2] != 0 ? 0 : 1) & aboolean[3]) != 0)
/*      */     {
/*  725 */       i = 38;
/*      */     }
/*  727 */     else if ((aboolean[0] & aboolean[1] & aboolean[2] & (aboolean[3] != 0 ? 0 : 1)) != 0)
/*      */     {
/*  729 */       i = 14;
/*      */     }
/*  731 */     else if ((aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) != 0)
/*      */     {
/*  733 */       i = 26;
/*      */     }
/*      */     
/*  736 */     if (i == 0)
/*      */     {
/*  738 */       return p_getConnectedTextureCtm_0_.tileIcons[i];
/*      */     }
/*  740 */     if (!Config.isConnectedTexturesFancy())
/*      */     {
/*  742 */       return p_getConnectedTextureCtm_0_.tileIcons[i];
/*      */     }
/*      */     
/*      */ 
/*  746 */     switch (p_getConnectedTextureCtm_4_)
/*      */     {
/*      */     case 0: 
/*  749 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  750 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  751 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  752 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  753 */       break;
/*      */     
/*      */     case 1: 
/*  756 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  757 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  758 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  759 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  760 */       break;
/*      */     
/*      */     case 2: 
/*  763 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  764 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  765 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  766 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  767 */       break;
/*      */     
/*      */     case 3: 
/*  770 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  771 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().down(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  772 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  773 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().up(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  774 */       break;
/*      */     
/*      */     case 4: 
/*  777 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  778 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  779 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  780 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  781 */       break;
/*      */     
/*      */     case 5: 
/*  784 */       aboolean[0] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  785 */       aboolean[1] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  786 */       aboolean[2] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().north(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*  787 */       aboolean[3] = (isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().south(), p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_) ? 0 : true);
/*      */     }
/*      */     
/*  790 */     if ((i == 13) && (aboolean[0] != 0))
/*      */     {
/*  792 */       i = 4;
/*      */     }
/*  794 */     else if ((i == 15) && (aboolean[1] != 0))
/*      */     {
/*  796 */       i = 5;
/*      */     }
/*  798 */     else if ((i == 37) && (aboolean[2] != 0))
/*      */     {
/*  800 */       i = 16;
/*      */     }
/*  802 */     else if ((i == 39) && (aboolean[3] != 0))
/*      */     {
/*  804 */       i = 17;
/*      */     }
/*  806 */     else if ((i == 14) && (aboolean[0] != 0) && (aboolean[1] != 0))
/*      */     {
/*  808 */       i = 7;
/*      */     }
/*  810 */     else if ((i == 25) && (aboolean[0] != 0) && (aboolean[2] != 0))
/*      */     {
/*  812 */       i = 6;
/*      */     }
/*  814 */     else if ((i == 27) && (aboolean[3] != 0) && (aboolean[1] != 0))
/*      */     {
/*  816 */       i = 19;
/*      */     }
/*  818 */     else if ((i == 38) && (aboolean[3] != 0) && (aboolean[2] != 0))
/*      */     {
/*  820 */       i = 18;
/*      */     }
/*  822 */     else if ((i == 14) && (aboolean[0] == 0) && (aboolean[1] != 0))
/*      */     {
/*  824 */       i = 31;
/*      */     }
/*  826 */     else if ((i == 25) && (aboolean[0] != 0) && (aboolean[2] == 0))
/*      */     {
/*  828 */       i = 30;
/*      */     }
/*  830 */     else if ((i == 27) && (aboolean[3] == 0) && (aboolean[1] != 0))
/*      */     {
/*  832 */       i = 41;
/*      */     }
/*  834 */     else if ((i == 38) && (aboolean[3] != 0) && (aboolean[2] == 0))
/*      */     {
/*  836 */       i = 40;
/*      */     }
/*  838 */     else if ((i == 14) && (aboolean[0] != 0) && (aboolean[1] == 0))
/*      */     {
/*  840 */       i = 29;
/*      */     }
/*  842 */     else if ((i == 25) && (aboolean[0] == 0) && (aboolean[2] != 0))
/*      */     {
/*  844 */       i = 28;
/*      */     }
/*  846 */     else if ((i == 27) && (aboolean[3] != 0) && (aboolean[1] == 0))
/*      */     {
/*  848 */       i = 43;
/*      */     }
/*  850 */     else if ((i == 38) && (aboolean[3] == 0) && (aboolean[2] != 0))
/*      */     {
/*  852 */       i = 42;
/*      */     }
/*  854 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] != 0) && (aboolean[2] != 0) && (aboolean[3] != 0))
/*      */     {
/*  856 */       i = 46;
/*      */     }
/*  858 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] != 0) && (aboolean[2] != 0) && (aboolean[3] != 0))
/*      */     {
/*  860 */       i = 9;
/*      */     }
/*  862 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] == 0) && (aboolean[2] != 0) && (aboolean[3] != 0))
/*      */     {
/*  864 */       i = 21;
/*      */     }
/*  866 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] != 0) && (aboolean[2] == 0) && (aboolean[3] != 0))
/*      */     {
/*  868 */       i = 8;
/*      */     }
/*  870 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] != 0) && (aboolean[2] != 0) && (aboolean[3] == 0))
/*      */     {
/*  872 */       i = 20;
/*      */     }
/*  874 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] != 0) && (aboolean[2] == 0) && (aboolean[3] == 0))
/*      */     {
/*  876 */       i = 11;
/*      */     }
/*  878 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] == 0) && (aboolean[2] != 0) && (aboolean[3] != 0))
/*      */     {
/*  880 */       i = 22;
/*      */     }
/*  882 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] != 0) && (aboolean[2] == 0) && (aboolean[3] != 0))
/*      */     {
/*  884 */       i = 23;
/*      */     }
/*  886 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] == 0) && (aboolean[2] != 0) && (aboolean[3] == 0))
/*      */     {
/*  888 */       i = 10;
/*      */     }
/*  890 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] == 0) && (aboolean[2] == 0) && (aboolean[3] != 0))
/*      */     {
/*  892 */       i = 34;
/*      */     }
/*  894 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] != 0) && (aboolean[2] != 0) && (aboolean[3] == 0))
/*      */     {
/*  896 */       i = 35;
/*      */     }
/*  898 */     else if ((i == 26) && (aboolean[0] != 0) && (aboolean[1] == 0) && (aboolean[2] == 0) && (aboolean[3] == 0))
/*      */     {
/*  900 */       i = 32;
/*      */     }
/*  902 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] != 0) && (aboolean[2] == 0) && (aboolean[3] == 0))
/*      */     {
/*  904 */       i = 33;
/*      */     }
/*  906 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] == 0) && (aboolean[2] != 0) && (aboolean[3] == 0))
/*      */     {
/*  908 */       i = 44;
/*      */     }
/*  910 */     else if ((i == 26) && (aboolean[0] == 0) && (aboolean[1] == 0) && (aboolean[2] == 0) && (aboolean[3] != 0))
/*      */     {
/*  912 */       i = 45;
/*      */     }
/*      */     
/*  915 */     return p_getConnectedTextureCtm_0_.tileIcons[i];
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean isNeighbour(ConnectedProperties p_isNeighbour_0_, IBlockAccess p_isNeighbour_1_, IBlockState p_isNeighbour_2_, BlockPos p_isNeighbour_3_, int p_isNeighbour_4_, TextureAtlasSprite p_isNeighbour_5_, int p_isNeighbour_6_)
/*      */   {
/*  921 */     IBlockState iblockstate = p_isNeighbour_1_.getBlockState(p_isNeighbour_3_);
/*      */     
/*  923 */     if (p_isNeighbour_2_ == iblockstate)
/*      */     {
/*  925 */       return true;
/*      */     }
/*  927 */     if (p_isNeighbour_0_.connect == 2)
/*      */     {
/*  929 */       if (iblockstate == null)
/*      */       {
/*  931 */         return false;
/*      */       }
/*  933 */       if (iblockstate == AIR_DEFAULT_STATE)
/*      */       {
/*  935 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  939 */       TextureAtlasSprite textureatlassprite = getNeighbourIcon(p_isNeighbour_1_, p_isNeighbour_3_, iblockstate, p_isNeighbour_4_);
/*  940 */       return textureatlassprite == p_isNeighbour_5_;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  945 */     return iblockstate != null;
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getNeighbourIcon(IBlockAccess p_getNeighbourIcon_0_, BlockPos p_getNeighbourIcon_1_, IBlockState p_getNeighbourIcon_2_, int p_getNeighbourIcon_3_)
/*      */   {
/*  951 */     p_getNeighbourIcon_2_ = p_getNeighbourIcon_2_.getBlock().getActualState(p_getNeighbourIcon_2_, p_getNeighbourIcon_0_, p_getNeighbourIcon_1_);
/*  952 */     IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(p_getNeighbourIcon_2_);
/*      */     
/*  954 */     if (ibakedmodel == null)
/*      */     {
/*  956 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  960 */     EnumFacing enumfacing = getFacing(p_getNeighbourIcon_3_);
/*  961 */     List list = ibakedmodel.getFaceQuads(enumfacing);
/*      */     
/*  963 */     if (list.size() > 0)
/*      */     {
/*  965 */       BakedQuad bakedquad1 = (BakedQuad)list.get(0);
/*  966 */       return bakedquad1.getSprite();
/*      */     }
/*      */     
/*      */ 
/*  970 */     List list1 = ibakedmodel.getGeneralQuads();
/*      */     
/*  972 */     for (int i = 0; i < list1.size(); i++)
/*      */     {
/*  974 */       BakedQuad bakedquad = (BakedQuad)list1.get(i);
/*      */       
/*  976 */       if (bakedquad.getFace() == enumfacing)
/*      */       {
/*  978 */         return bakedquad.getSprite();
/*      */       }
/*      */     }
/*      */     
/*  982 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureHorizontal(ConnectedProperties p_getConnectedTextureHorizontal_0_, IBlockAccess p_getConnectedTextureHorizontal_1_, IBlockState p_getConnectedTextureHorizontal_2_, BlockPos p_getConnectedTextureHorizontal_3_, int p_getConnectedTextureHorizontal_4_, int p_getConnectedTextureHorizontal_5_, TextureAtlasSprite p_getConnectedTextureHorizontal_6_, int p_getConnectedTextureHorizontal_7_)
/*      */   {
/*  991 */     boolean flag = false;
/*  992 */     boolean flag1 = false;
/*      */     
/*      */ 
/*  995 */     switch (p_getConnectedTextureHorizontal_4_)
/*      */     {
/*      */     case 0: 
/*  998 */       switch (p_getConnectedTextureHorizontal_5_)
/*      */       {
/*      */       case 0: 
/*      */       case 1: 
/* 1002 */         return null;
/*      */       
/*      */       case 2: 
/* 1005 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1006 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1007 */         break;
/*      */       
/*      */       case 3: 
/* 1010 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1011 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1012 */         break;
/*      */       
/*      */       case 4: 
/* 1015 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1016 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1017 */         break;
/*      */       
/*      */       case 5: 
/* 1020 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1021 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/*      */       }
/*      */       
/* 1024 */       break;
/*      */     
/*      */ 
/*      */     case 1: 
/* 1028 */       switch (p_getConnectedTextureHorizontal_5_)
/*      */       {
/*      */       case 0: 
/* 1031 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1032 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1033 */         break;
/*      */       
/*      */       case 1: 
/* 1036 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1037 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1038 */         break;
/*      */       
/*      */       case 2: 
/*      */       case 3: 
/* 1042 */         return null;
/*      */       
/*      */       case 4: 
/* 1045 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1046 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1047 */         break;
/*      */       
/*      */       case 5: 
/* 1050 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1051 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/*      */       }
/*      */       
/* 1054 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 1058 */       switch (p_getConnectedTextureHorizontal_5_)
/*      */       {
/*      */       case 0: 
/* 1061 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1062 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1063 */         break;
/*      */       
/*      */       case 1: 
/* 1066 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1067 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1068 */         break;
/*      */       
/*      */       case 2: 
/* 1071 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1072 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1073 */         break;
/*      */       
/*      */       case 3: 
/* 1076 */         flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1077 */         flag1 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
/* 1078 */         break;
/*      */       
/*      */       case 4: 
/*      */       case 5: 
/* 1082 */         return null;
/*      */       }
/*      */       break;
/*      */     }
/* 1086 */     int i = 3;
/*      */     
/* 1088 */     if (flag)
/*      */     {
/* 1090 */       if (flag1)
/*      */       {
/* 1092 */         i = 1;
/*      */       }
/*      */       else
/*      */       {
/* 1096 */         i = 2;
/*      */       }
/*      */     }
/* 1099 */     else if (flag1)
/*      */     {
/* 1101 */       i = 0;
/*      */     }
/*      */     else
/*      */     {
/* 1105 */       i = 3;
/*      */     }
/*      */     
/* 1108 */     return p_getConnectedTextureHorizontal_0_.tileIcons[i];
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite getConnectedTextureVertical(ConnectedProperties p_getConnectedTextureVertical_0_, IBlockAccess p_getConnectedTextureVertical_1_, IBlockState p_getConnectedTextureVertical_2_, BlockPos p_getConnectedTextureVertical_3_, int p_getConnectedTextureVertical_4_, int p_getConnectedTextureVertical_5_, TextureAtlasSprite p_getConnectedTextureVertical_6_, int p_getConnectedTextureVertical_7_)
/*      */   {
/* 1113 */     boolean flag = false;
/* 1114 */     boolean flag1 = false;
/*      */     
/* 1116 */     switch (p_getConnectedTextureVertical_4_)
/*      */     {
/*      */     case 0: 
/* 1119 */       if ((p_getConnectedTextureVertical_5_ == 1) || (p_getConnectedTextureVertical_5_ == 0))
/*      */       {
/* 1121 */         return null;
/*      */       }
/*      */       
/* 1124 */       flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/* 1125 */       flag1 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/* 1126 */       break;
/*      */     
/*      */     case 1: 
/* 1129 */       if ((p_getConnectedTextureVertical_5_ == 3) || (p_getConnectedTextureVertical_5_ == 2))
/*      */       {
/* 1131 */         return null;
/*      */       }
/*      */       
/* 1134 */       flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.south(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/* 1135 */       flag1 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.north(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/* 1136 */       break;
/*      */     
/*      */     case 2: 
/* 1139 */       if ((p_getConnectedTextureVertical_5_ == 5) || (p_getConnectedTextureVertical_5_ == 4))
/*      */       {
/* 1141 */         return null;
/*      */       }
/*      */       
/* 1144 */       flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.west(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/* 1145 */       flag1 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.east(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
/*      */     }
/*      */     
/* 1148 */     int i = 3;
/*      */     
/* 1150 */     if (flag)
/*      */     {
/* 1152 */       if (flag1)
/*      */       {
/* 1154 */         i = 1;
/*      */       }
/*      */       else
/*      */       {
/* 1158 */         i = 2;
/*      */       }
/*      */     }
/* 1161 */     else if (flag1)
/*      */     {
/* 1163 */       i = 0;
/*      */     }
/*      */     else
/*      */     {
/* 1167 */       i = 3;
/*      */     }
/*      */     
/* 1170 */     return p_getConnectedTextureVertical_0_.tileIcons[i];
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite getConnectedTextureHorizontalVertical(ConnectedProperties p_getConnectedTextureHorizontalVertical_0_, IBlockAccess p_getConnectedTextureHorizontalVertical_1_, IBlockState p_getConnectedTextureHorizontalVertical_2_, BlockPos p_getConnectedTextureHorizontalVertical_3_, int p_getConnectedTextureHorizontalVertical_4_, int p_getConnectedTextureHorizontalVertical_5_, TextureAtlasSprite p_getConnectedTextureHorizontalVertical_6_, int p_getConnectedTextureHorizontalVertical_7_)
/*      */   {
/* 1175 */     TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureHorizontalVertical_0_.tileIcons;
/* 1176 */     TextureAtlasSprite textureatlassprite = getConnectedTextureHorizontal(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
/*      */     
/* 1178 */     if ((textureatlassprite != null) && (textureatlassprite != p_getConnectedTextureHorizontalVertical_6_) && (textureatlassprite != atextureatlassprite[3]))
/*      */     {
/* 1180 */       return textureatlassprite;
/*      */     }
/*      */     
/*      */ 
/* 1184 */     TextureAtlasSprite textureatlassprite1 = getConnectedTextureVertical(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
/* 1185 */     return textureatlassprite1 == atextureatlassprite[2] ? atextureatlassprite[6] : textureatlassprite1 == atextureatlassprite[1] ? atextureatlassprite[5] : textureatlassprite1 == atextureatlassprite[0] ? atextureatlassprite[4] : textureatlassprite1;
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(ConnectedProperties p_getConnectedTextureVerticalHorizontal_0_, IBlockAccess p_getConnectedTextureVerticalHorizontal_1_, IBlockState p_getConnectedTextureVerticalHorizontal_2_, BlockPos p_getConnectedTextureVerticalHorizontal_3_, int p_getConnectedTextureVerticalHorizontal_4_, int p_getConnectedTextureVerticalHorizontal_5_, TextureAtlasSprite p_getConnectedTextureVerticalHorizontal_6_, int p_getConnectedTextureVerticalHorizontal_7_)
/*      */   {
/* 1191 */     TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureVerticalHorizontal_0_.tileIcons;
/* 1192 */     TextureAtlasSprite textureatlassprite = getConnectedTextureVertical(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
/*      */     
/* 1194 */     if ((textureatlassprite != null) && (textureatlassprite != p_getConnectedTextureVerticalHorizontal_6_) && (textureatlassprite != atextureatlassprite[3]))
/*      */     {
/* 1196 */       return textureatlassprite;
/*      */     }
/*      */     
/*      */ 
/* 1200 */     TextureAtlasSprite textureatlassprite1 = getConnectedTextureHorizontal(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
/* 1201 */     return textureatlassprite1 == atextureatlassprite[2] ? atextureatlassprite[6] : textureatlassprite1 == atextureatlassprite[1] ? atextureatlassprite[5] : textureatlassprite1 == atextureatlassprite[0] ? atextureatlassprite[4] : textureatlassprite1;
/*      */   }
/*      */   
/*      */ 
/*      */   private static TextureAtlasSprite getConnectedTextureTop(ConnectedProperties p_getConnectedTextureTop_0_, IBlockAccess p_getConnectedTextureTop_1_, IBlockState p_getConnectedTextureTop_2_, BlockPos p_getConnectedTextureTop_3_, int p_getConnectedTextureTop_4_, int p_getConnectedTextureTop_5_, TextureAtlasSprite p_getConnectedTextureTop_6_, int p_getConnectedTextureTop_7_)
/*      */   {
/* 1207 */     boolean flag = false;
/*      */     
/* 1209 */     switch (p_getConnectedTextureTop_4_)
/*      */     {
/*      */     case 0: 
/* 1212 */       if ((p_getConnectedTextureTop_5_ == 1) || (p_getConnectedTextureTop_5_ == 0))
/*      */       {
/* 1214 */         return null;
/*      */       }
/*      */       
/* 1217 */       flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.up(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
/* 1218 */       break;
/*      */     
/*      */     case 1: 
/* 1221 */       if ((p_getConnectedTextureTop_5_ == 3) || (p_getConnectedTextureTop_5_ == 2))
/*      */       {
/* 1223 */         return null;
/*      */       }
/*      */       
/* 1226 */       flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.south(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
/* 1227 */       break;
/*      */     
/*      */     case 2: 
/* 1230 */       if ((p_getConnectedTextureTop_5_ == 5) || (p_getConnectedTextureTop_5_ == 4))
/*      */       {
/* 1232 */         return null;
/*      */       }
/*      */       
/* 1235 */       flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.east(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
/*      */     }
/*      */     
/* 1238 */     if (flag)
/*      */     {
/* 1240 */       return p_getConnectedTextureTop_0_.tileIcons[0];
/*      */     }
/*      */     
/*      */ 
/* 1244 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public static void updateIcons(TextureMap p_updateIcons_0_)
/*      */   {
/* 1250 */     blockProperties = null;
/* 1251 */     tileProperties = null;
/*      */     
/* 1253 */     if (Config.isConnectedTextures())
/*      */     {
/* 1255 */       IResourcePack[] airesourcepack = Config.getResourcePacks();
/*      */       
/* 1257 */       for (int i = airesourcepack.length - 1; i >= 0; i--)
/*      */       {
/* 1259 */         IResourcePack iresourcepack = airesourcepack[i];
/* 1260 */         updateIcons(p_updateIcons_0_, iresourcepack);
/*      */       }
/*      */       
/* 1263 */       updateIcons(p_updateIcons_0_, Config.getDefaultResourcePack());
/* 1264 */       ResourceLocation resourcelocation = new ResourceLocation("mcpatcher/ctm/default/empty");
/* 1265 */       emptySprite = p_updateIcons_0_.registerSprite(resourcelocation);
/* 1266 */       spriteQuadMaps = new Map[p_updateIcons_0_.getCountRegisteredSprites() + 1];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void updateIconEmpty(TextureMap p_updateIconEmpty_0_) {}
/*      */   
/*      */ 
/*      */   public static void updateIcons(TextureMap p_updateIcons_0_, IResourcePack p_updateIcons_1_)
/*      */   {
/* 1276 */     String[] astring = collectFiles(p_updateIcons_1_, "mcpatcher/ctm/", ".properties");
/* 1277 */     Arrays.sort(astring);
/* 1278 */     List list = makePropertyList(tileProperties);
/* 1279 */     List list1 = makePropertyList(blockProperties);
/*      */     
/* 1281 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/* 1283 */       String s = astring[i];
/* 1284 */       Config.dbg("ConnectedTextures: " + s);
/*      */       
/*      */       try
/*      */       {
/* 1288 */         ResourceLocation resourcelocation = new ResourceLocation(s);
/* 1289 */         InputStream inputstream = p_updateIcons_1_.getInputStream(resourcelocation);
/*      */         
/* 1291 */         if (inputstream == null)
/*      */         {
/* 1293 */           Config.warn("ConnectedTextures file not found: " + s);
/*      */         }
/*      */         else
/*      */         {
/* 1297 */           Properties properties = new Properties();
/* 1298 */           properties.load(inputstream);
/* 1299 */           ConnectedProperties connectedproperties = new ConnectedProperties(properties, s);
/*      */           
/* 1301 */           if (connectedproperties.isValid(s))
/*      */           {
/* 1303 */             connectedproperties.updateIcons(p_updateIcons_0_);
/* 1304 */             addToTileList(connectedproperties, list);
/* 1305 */             addToBlockList(connectedproperties, list1);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (FileNotFoundException var11)
/*      */       {
/* 1311 */         Config.warn("ConnectedTextures file not found: " + s);
/*      */       }
/*      */       catch (IOException ioexception)
/*      */       {
/* 1315 */         ioexception.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/* 1319 */     blockProperties = propertyListToArray(list1);
/* 1320 */     tileProperties = propertyListToArray(list);
/* 1321 */     multipass = detectMultipass();
/* 1322 */     Config.dbg("Multipass connected textures: " + multipass);
/*      */   }
/*      */   
/*      */   private static List makePropertyList(ConnectedProperties[][] p_makePropertyList_0_)
/*      */   {
/* 1327 */     List list = new ArrayList();
/*      */     
/* 1329 */     if (p_makePropertyList_0_ != null)
/*      */     {
/* 1331 */       for (int i = 0; i < p_makePropertyList_0_.length; i++)
/*      */       {
/* 1333 */         ConnectedProperties[] aconnectedproperties = p_makePropertyList_0_[i];
/* 1334 */         List list1 = null;
/*      */         
/* 1336 */         if (aconnectedproperties != null)
/*      */         {
/* 1338 */           list1 = new ArrayList(Arrays.asList(aconnectedproperties));
/*      */         }
/*      */         
/* 1341 */         list.add(list1);
/*      */       }
/*      */     }
/*      */     
/* 1345 */     return list;
/*      */   }
/*      */   
/*      */   private static boolean detectMultipass()
/*      */   {
/* 1350 */     List list = new ArrayList();
/*      */     
/* 1352 */     for (int i = 0; i < tileProperties.length; i++)
/*      */     {
/* 1354 */       ConnectedProperties[] aconnectedproperties = tileProperties[i];
/*      */       
/* 1356 */       if (aconnectedproperties != null)
/*      */       {
/* 1358 */         list.addAll(Arrays.asList(aconnectedproperties));
/*      */       }
/*      */     }
/*      */     
/* 1362 */     for (int k = 0; k < blockProperties.length; k++)
/*      */     {
/* 1364 */       ConnectedProperties[] aconnectedproperties2 = blockProperties[k];
/*      */       
/* 1366 */       if (aconnectedproperties2 != null)
/*      */       {
/* 1368 */         list.addAll(Arrays.asList(aconnectedproperties2));
/*      */       }
/*      */     }
/*      */     
/* 1372 */     ConnectedProperties[] aconnectedproperties1 = (ConnectedProperties[])list.toArray(new ConnectedProperties[list.size()]);
/* 1373 */     Set set1 = new HashSet();
/* 1374 */     Set set = new HashSet();
/*      */     
/* 1376 */     for (int j = 0; j < aconnectedproperties1.length; j++)
/*      */     {
/* 1378 */       ConnectedProperties connectedproperties = aconnectedproperties1[j];
/*      */       
/* 1380 */       if (connectedproperties.matchTileIcons != null)
/*      */       {
/* 1382 */         set1.addAll(Arrays.asList(connectedproperties.matchTileIcons));
/*      */       }
/*      */       
/* 1385 */       if (connectedproperties.tileIcons != null)
/*      */       {
/* 1387 */         set.addAll(Arrays.asList(connectedproperties.tileIcons));
/*      */       }
/*      */     }
/*      */     
/* 1391 */     set1.retainAll(set);
/* 1392 */     return !set1.isEmpty();
/*      */   }
/*      */   
/*      */   private static ConnectedProperties[][] propertyListToArray(List p_propertyListToArray_0_)
/*      */   {
/* 1397 */     ConnectedProperties[][] aconnectedproperties = new ConnectedProperties[p_propertyListToArray_0_.size()][];
/*      */     
/* 1399 */     for (int i = 0; i < p_propertyListToArray_0_.size(); i++)
/*      */     {
/* 1401 */       List list = (List)p_propertyListToArray_0_.get(i);
/*      */       
/* 1403 */       if (list != null)
/*      */       {
/* 1405 */         ConnectedProperties[] aconnectedproperties1 = (ConnectedProperties[])list.toArray(new ConnectedProperties[list.size()]);
/* 1406 */         aconnectedproperties[i] = aconnectedproperties1;
/*      */       }
/*      */     }
/*      */     
/* 1410 */     return aconnectedproperties;
/*      */   }
/*      */   
/*      */   private static void addToTileList(ConnectedProperties p_addToTileList_0_, List p_addToTileList_1_)
/*      */   {
/* 1415 */     if (p_addToTileList_0_.matchTileIcons != null)
/*      */     {
/* 1417 */       for (int i = 0; i < p_addToTileList_0_.matchTileIcons.length; i++)
/*      */       {
/* 1419 */         TextureAtlasSprite textureatlassprite = p_addToTileList_0_.matchTileIcons[i];
/*      */         
/* 1421 */         if (!(textureatlassprite instanceof TextureAtlasSprite))
/*      */         {
/* 1423 */           Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + textureatlassprite + ", name: " + textureatlassprite.getIconName());
/*      */         }
/*      */         else
/*      */         {
/* 1427 */           int j = textureatlassprite.getIndexInMap();
/*      */           
/* 1429 */           if (j < 0)
/*      */           {
/* 1431 */             Config.warn("Invalid tile ID: " + j + ", icon: " + textureatlassprite.getIconName());
/*      */           }
/*      */           else
/*      */           {
/* 1435 */             addToList(p_addToTileList_0_, p_addToTileList_1_, j);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void addToBlockList(ConnectedProperties p_addToBlockList_0_, List p_addToBlockList_1_)
/*      */   {
/* 1444 */     if (p_addToBlockList_0_.matchBlocks != null)
/*      */     {
/* 1446 */       for (int i = 0; i < p_addToBlockList_0_.matchBlocks.length; i++)
/*      */       {
/* 1448 */         int j = p_addToBlockList_0_.matchBlocks[i];
/*      */         
/* 1450 */         if (j < 0)
/*      */         {
/* 1452 */           Config.warn("Invalid block ID: " + j);
/*      */         }
/*      */         else
/*      */         {
/* 1456 */           addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void addToList(ConnectedProperties p_addToList_0_, List p_addToList_1_, int p_addToList_2_)
/*      */   {
/* 1464 */     while (p_addToList_2_ >= p_addToList_1_.size())
/*      */     {
/* 1466 */       p_addToList_1_.add(null);
/*      */     }
/*      */     
/* 1469 */     List list = (List)p_addToList_1_.get(p_addToList_2_);
/*      */     
/* 1471 */     if (list == null)
/*      */     {
/* 1473 */       list = new ArrayList();
/* 1474 */       p_addToList_1_.set(p_addToList_2_, list);
/*      */     }
/*      */     
/* 1477 */     list.add(p_addToList_0_);
/*      */   }
/*      */   
/*      */   private static String[] collectFiles(IResourcePack p_collectFiles_0_, String p_collectFiles_1_, String p_collectFiles_2_)
/*      */   {
/* 1482 */     if ((p_collectFiles_0_ instanceof DefaultResourcePack))
/*      */     {
/* 1484 */       return collectFilesDefault(p_collectFiles_0_);
/*      */     }
/* 1486 */     if (!(p_collectFiles_0_ instanceof AbstractResourcePack))
/*      */     {
/* 1488 */       return new String[0];
/*      */     }
/*      */     
/*      */ 
/* 1492 */     AbstractResourcePack abstractresourcepack = (AbstractResourcePack)p_collectFiles_0_;
/* 1493 */     File file1 = ResourceUtils.getResourcePackFile(abstractresourcepack);
/* 1494 */     return file1.isFile() ? collectFilesZIP(file1, p_collectFiles_1_, p_collectFiles_2_) : file1.isDirectory() ? collectFilesFolder(file1, "", p_collectFiles_1_, p_collectFiles_2_) : file1 == null ? new String[0] : new String[0];
/*      */   }
/*      */   
/*      */ 
/*      */   private static String[] collectFilesDefault(IResourcePack p_collectFilesDefault_0_)
/*      */   {
/* 1500 */     List list = new ArrayList();
/* 1501 */     String[] astring = getDefaultCtmPaths();
/*      */     
/* 1503 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/* 1505 */       String s = astring[i];
/* 1506 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/*      */       
/* 1508 */       if (p_collectFilesDefault_0_.resourceExists(resourcelocation))
/*      */       {
/* 1510 */         list.add(s);
/*      */       }
/*      */     }
/*      */     
/* 1514 */     String[] astring1 = (String[])list.toArray(new String[list.size()]);
/* 1515 */     return astring1;
/*      */   }
/*      */   
/*      */   private static String[] getDefaultCtmPaths()
/*      */   {
/* 1520 */     List list = new ArrayList();
/* 1521 */     String s = "mcpatcher/ctm/default/";
/*      */     
/* 1523 */     if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png")))
/*      */     {
/* 1525 */       list.add(s + "glass.properties");
/* 1526 */       list.add(s + "glasspane.properties");
/*      */     }
/*      */     
/* 1529 */     if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png")))
/*      */     {
/* 1531 */       list.add(s + "bookshelf.properties");
/*      */     }
/*      */     
/* 1534 */     if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png")))
/*      */     {
/* 1536 */       list.add(s + "sandstone.properties");
/*      */     }
/*      */     
/* 1539 */     String[] astring = { "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
/*      */     
/* 1541 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/* 1543 */       String s1 = astring[i];
/*      */       
/* 1545 */       if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + s1 + ".png")))
/*      */       {
/* 1547 */         list.add(s + i + "_glass_" + s1 + "/glass_" + s1 + ".properties");
/* 1548 */         list.add(s + i + "_glass_" + s1 + "/glass_pane_" + s1 + ".properties");
/*      */       }
/*      */     }
/*      */     
/* 1552 */     String[] astring1 = (String[])list.toArray(new String[list.size()]);
/* 1553 */     return astring1;
/*      */   }
/*      */   
/*      */   private static String[] collectFilesFolder(File p_collectFilesFolder_0_, String p_collectFilesFolder_1_, String p_collectFilesFolder_2_, String p_collectFilesFolder_3_)
/*      */   {
/* 1558 */     List list = new ArrayList();
/* 1559 */     String s = "assets/minecraft/";
/* 1560 */     File[] afile = p_collectFilesFolder_0_.listFiles();
/*      */     
/* 1562 */     if (afile == null)
/*      */     {
/* 1564 */       return new String[0];
/*      */     }
/*      */     
/*      */ 
/* 1568 */     for (int i = 0; i < afile.length; i++)
/*      */     {
/* 1570 */       File file1 = afile[i];
/*      */       
/* 1572 */       if (file1.isFile())
/*      */       {
/* 1574 */         String s3 = p_collectFilesFolder_1_ + file1.getName();
/*      */         
/* 1576 */         if (s3.startsWith(s))
/*      */         {
/* 1578 */           s3 = s3.substring(s.length());
/*      */           
/* 1580 */           if ((s3.startsWith(p_collectFilesFolder_2_)) && (s3.endsWith(p_collectFilesFolder_3_)))
/*      */           {
/* 1582 */             list.add(s3);
/*      */           }
/*      */         }
/*      */       }
/* 1586 */       else if (file1.isDirectory())
/*      */       {
/* 1588 */         String s1 = p_collectFilesFolder_1_ + file1.getName() + "/";
/* 1589 */         String[] astring = collectFilesFolder(file1, s1, p_collectFilesFolder_2_, p_collectFilesFolder_3_);
/*      */         
/* 1591 */         for (int j = 0; j < astring.length; j++)
/*      */         {
/* 1593 */           String s2 = astring[j];
/* 1594 */           list.add(s2);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1599 */     String[] astring1 = (String[])list.toArray(new String[list.size()]);
/* 1600 */     return astring1;
/*      */   }
/*      */   
/*      */ 
/*      */   private static String[] collectFilesZIP(File p_collectFilesZIP_0_, String p_collectFilesZIP_1_, String p_collectFilesZIP_2_)
/*      */   {
/* 1606 */     List list = new ArrayList();
/* 1607 */     String s = "assets/minecraft/";
/*      */     
/*      */     try
/*      */     {
/* 1611 */       ZipFile zipfile = new ZipFile(p_collectFilesZIP_0_);
/* 1612 */       Enumeration enumeration = zipfile.entries();
/*      */       
/* 1614 */       while (enumeration.hasMoreElements())
/*      */       {
/* 1616 */         ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
/* 1617 */         String s1 = zipentry.getName();
/*      */         
/* 1619 */         if (s1.startsWith(s))
/*      */         {
/* 1621 */           s1 = s1.substring(s.length());
/*      */           
/* 1623 */           if ((s1.startsWith(p_collectFilesZIP_1_)) && (s1.endsWith(p_collectFilesZIP_2_)))
/*      */           {
/* 1625 */             list.add(s1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1630 */       zipfile.close();
/* 1631 */       return (String[])list.toArray(new String[list.size()]);
/*      */ 
/*      */     }
/*      */     catch (IOException ioexception)
/*      */     {
/* 1636 */       ioexception.printStackTrace(); }
/* 1637 */     return new String[0];
/*      */   }
/*      */   
/*      */ 
/*      */   public static int getPaneTextureIndex(boolean p_getPaneTextureIndex_0_, boolean p_getPaneTextureIndex_1_, boolean p_getPaneTextureIndex_2_, boolean p_getPaneTextureIndex_3_)
/*      */   {
/* 1643 */     return p_getPaneTextureIndex_3_ ? 16 : p_getPaneTextureIndex_2_ ? 48 : p_getPaneTextureIndex_3_ ? 32 : (!p_getPaneTextureIndex_1_) && (p_getPaneTextureIndex_0_) ? 1 : p_getPaneTextureIndex_3_ ? 17 : p_getPaneTextureIndex_2_ ? 49 : p_getPaneTextureIndex_3_ ? 33 : (p_getPaneTextureIndex_1_) && (!p_getPaneTextureIndex_0_) ? 3 : p_getPaneTextureIndex_3_ ? 19 : p_getPaneTextureIndex_2_ ? 51 : p_getPaneTextureIndex_3_ ? 35 : (p_getPaneTextureIndex_1_) && (p_getPaneTextureIndex_0_) ? 2 : p_getPaneTextureIndex_3_ ? 18 : p_getPaneTextureIndex_2_ ? 50 : p_getPaneTextureIndex_3_ ? 34 : 0;
/*      */   }
/*      */   
/*      */   public static int getReversePaneTextureIndex(int p_getReversePaneTextureIndex_0_)
/*      */   {
/* 1648 */     int i = p_getReversePaneTextureIndex_0_ % 16;
/* 1649 */     return i == 3 ? p_getReversePaneTextureIndex_0_ - 2 : i == 1 ? p_getReversePaneTextureIndex_0_ + 2 : p_getReversePaneTextureIndex_0_;
/*      */   }
/*      */   
/*      */   public static TextureAtlasSprite getCtmTexture(ConnectedProperties p_getCtmTexture_0_, int p_getCtmTexture_1_, TextureAtlasSprite p_getCtmTexture_2_)
/*      */   {
/* 1654 */     if (p_getCtmTexture_0_.method != 1)
/*      */     {
/* 1656 */       return p_getCtmTexture_2_;
/*      */     }
/* 1658 */     if ((p_getCtmTexture_1_ >= 0) && (p_getCtmTexture_1_ < ctmIndexes.length))
/*      */     {
/* 1660 */       int i = ctmIndexes[p_getCtmTexture_1_];
/* 1661 */       TextureAtlasSprite[] atextureatlassprite = p_getCtmTexture_0_.tileIcons;
/* 1662 */       return (i >= 0) && (i < atextureatlassprite.length) ? atextureatlassprite[i] : p_getCtmTexture_2_;
/*      */     }
/*      */     
/*      */ 
/* 1666 */     return p_getCtmTexture_2_;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ConnectedTextures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */