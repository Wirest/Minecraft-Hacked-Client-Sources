/*      */ package optfine;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ 
/*      */ public class ConnectedProperties
/*      */ {
/*   18 */   public String name = null;
/*   19 */   public String basePath = null;
/*   20 */   public int[] matchBlocks = null;
/*   21 */   public String[] matchTiles = null;
/*   22 */   public int method = 0;
/*   23 */   public String[] tiles = null;
/*   24 */   public int connect = 0;
/*   25 */   public int faces = 63;
/*   26 */   public int[] metadatas = null;
/*   27 */   public BiomeGenBase[] biomes = null;
/*   28 */   public int minHeight = 0;
/*   29 */   public int maxHeight = 1024;
/*   30 */   public int renderPass = 0;
/*   31 */   public boolean innerSeams = false;
/*   32 */   public int width = 0;
/*   33 */   public int height = 0;
/*   34 */   public int[] weights = null;
/*   35 */   public int symmetry = 1;
/*   36 */   public int[] sumWeights = null;
/*   37 */   public int sumAllWeights = 1;
/*   38 */   public TextureAtlasSprite[] matchTileIcons = null;
/*   39 */   public TextureAtlasSprite[] tileIcons = null;
/*      */   public static final int METHOD_NONE = 0;
/*      */   public static final int METHOD_CTM = 1;
/*      */   public static final int METHOD_HORIZONTAL = 2;
/*      */   public static final int METHOD_TOP = 3;
/*      */   public static final int METHOD_RANDOM = 4;
/*      */   public static final int METHOD_REPEAT = 5;
/*      */   public static final int METHOD_VERTICAL = 6;
/*      */   public static final int METHOD_FIXED = 7;
/*      */   public static final int METHOD_HORIZONTAL_VERTICAL = 8;
/*      */   public static final int METHOD_VERTICAL_HORIZONTAL = 9;
/*      */   public static final int CONNECT_NONE = 0;
/*      */   public static final int CONNECT_BLOCK = 1;
/*      */   public static final int CONNECT_TILE = 2;
/*      */   public static final int CONNECT_MATERIAL = 3;
/*      */   public static final int CONNECT_UNKNOWN = 128;
/*      */   public static final int FACE_BOTTOM = 1;
/*      */   public static final int FACE_TOP = 2;
/*      */   public static final int FACE_NORTH = 4;
/*      */   public static final int FACE_SOUTH = 8;
/*      */   public static final int FACE_WEST = 16;
/*      */   public static final int FACE_EAST = 32;
/*      */   public static final int FACE_SIDES = 60;
/*      */   public static final int FACE_ALL = 63;
/*      */   public static final int FACE_UNKNOWN = 128;
/*      */   public static final int SYMMETRY_NONE = 1;
/*      */   public static final int SYMMETRY_OPPOSITE = 2;
/*      */   public static final int SYMMETRY_ALL = 6;
/*      */   public static final int SYMMETRY_UNKNOWN = 128;
/*      */   
/*      */   public ConnectedProperties(Properties p_i29_1_, String p_i29_2_)
/*      */   {
/*   71 */     this.name = parseName(p_i29_2_);
/*   72 */     this.basePath = parseBasePath(p_i29_2_);
/*   73 */     String s = p_i29_1_.getProperty("matchBlocks");
/*   74 */     IBlockState iblockstate = parseBlockState(s);
/*      */     
/*   76 */     if (iblockstate != null)
/*      */     {
/*   78 */       this.matchBlocks = new int[] { Block.getIdFromBlock(iblockstate.getBlock()) };
/*   79 */       this.metadatas = new int[] { iblockstate.getBlock().getMetaFromState(iblockstate) };
/*      */     }
/*      */     
/*   82 */     if (this.matchBlocks == null)
/*      */     {
/*   84 */       this.matchBlocks = parseBlockIds(s);
/*      */     }
/*      */     
/*   87 */     if (this.metadatas == null)
/*      */     {
/*   89 */       this.metadatas = parseInts(p_i29_1_.getProperty("metadata"));
/*      */     }
/*      */     
/*   92 */     this.matchTiles = parseMatchTiles(p_i29_1_.getProperty("matchTiles"));
/*   93 */     this.method = parseMethod(p_i29_1_.getProperty("method"));
/*   94 */     this.tiles = parseTileNames(p_i29_1_.getProperty("tiles"));
/*   95 */     this.connect = parseConnect(p_i29_1_.getProperty("connect"));
/*   96 */     this.faces = parseFaces(p_i29_1_.getProperty("faces"));
/*   97 */     this.biomes = parseBiomes(p_i29_1_.getProperty("biomes"));
/*   98 */     this.minHeight = parseInt(p_i29_1_.getProperty("minHeight"), -1);
/*   99 */     this.maxHeight = parseInt(p_i29_1_.getProperty("maxHeight"), 1024);
/*  100 */     this.renderPass = parseInt(p_i29_1_.getProperty("renderPass"));
/*  101 */     this.innerSeams = parseBoolean(p_i29_1_.getProperty("innerSeams"));
/*  102 */     this.width = parseInt(p_i29_1_.getProperty("width"));
/*  103 */     this.height = parseInt(p_i29_1_.getProperty("height"));
/*  104 */     this.weights = parseInts(p_i29_1_.getProperty("weights"));
/*  105 */     this.symmetry = parseSymmetry(p_i29_1_.getProperty("symmetry"));
/*      */   }
/*      */   
/*      */   private String[] parseMatchTiles(String p_parseMatchTiles_1_)
/*      */   {
/*  110 */     if (p_parseMatchTiles_1_ == null)
/*      */     {
/*  112 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  116 */     String[] astring = Config.tokenize(p_parseMatchTiles_1_, " ");
/*      */     
/*  118 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/*  120 */       String s = astring[i];
/*      */       
/*  122 */       if (s.endsWith(".png"))
/*      */       {
/*  124 */         s = s.substring(0, s.length() - 4);
/*      */       }
/*      */       
/*  127 */       s = TextureUtils.fixResourcePath(s, this.basePath);
/*  128 */       astring[i] = s;
/*      */     }
/*      */     
/*  131 */     return astring;
/*      */   }
/*      */   
/*      */ 
/*      */   private static String parseName(String p_parseName_0_)
/*      */   {
/*  137 */     String s = p_parseName_0_;
/*  138 */     int i = p_parseName_0_.lastIndexOf('/');
/*      */     
/*  140 */     if (i >= 0)
/*      */     {
/*  142 */       s = p_parseName_0_.substring(i + 1);
/*      */     }
/*      */     
/*  145 */     int j = s.lastIndexOf('.');
/*      */     
/*  147 */     if (j >= 0)
/*      */     {
/*  149 */       s = s.substring(0, j);
/*      */     }
/*      */     
/*  152 */     return s;
/*      */   }
/*      */   
/*      */   private static String parseBasePath(String p_parseBasePath_0_)
/*      */   {
/*  157 */     int i = p_parseBasePath_0_.lastIndexOf('/');
/*  158 */     return i < 0 ? "" : p_parseBasePath_0_.substring(0, i);
/*      */   }
/*      */   
/*      */   private static BiomeGenBase[] parseBiomes(String p_parseBiomes_0_)
/*      */   {
/*  163 */     if (p_parseBiomes_0_ == null)
/*      */     {
/*  165 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  169 */     String[] astring = Config.tokenize(p_parseBiomes_0_, " ");
/*  170 */     List list = new ArrayList();
/*      */     
/*  172 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/*  174 */       String s = astring[i];
/*  175 */       BiomeGenBase biomegenbase = findBiome(s);
/*      */       
/*  177 */       if (biomegenbase == null)
/*      */       {
/*  179 */         Config.warn("Biome not found: " + s);
/*      */       }
/*      */       else
/*      */       {
/*  183 */         list.add(biomegenbase);
/*      */       }
/*      */     }
/*      */     
/*  187 */     BiomeGenBase[] abiomegenbase = (BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]);
/*  188 */     return abiomegenbase;
/*      */   }
/*      */   
/*      */ 
/*      */   private static BiomeGenBase findBiome(String p_findBiome_0_)
/*      */   {
/*  194 */     p_findBiome_0_ = p_findBiome_0_.toLowerCase();
/*  195 */     BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
/*      */     
/*  197 */     for (int i = 0; i < abiomegenbase.length; i++)
/*      */     {
/*  199 */       BiomeGenBase biomegenbase = abiomegenbase[i];
/*      */       
/*  201 */       if (biomegenbase != null)
/*      */       {
/*  203 */         String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();
/*      */         
/*  205 */         if (s.equals(p_findBiome_0_))
/*      */         {
/*  207 */           return biomegenbase;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  212 */     return null;
/*      */   }
/*      */   
/*      */   private String[] parseTileNames(String p_parseTileNames_1_)
/*      */   {
/*  217 */     if (p_parseTileNames_1_ == null)
/*      */     {
/*  219 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  223 */     List list = new ArrayList();
/*  224 */     String[] astring = Config.tokenize(p_parseTileNames_1_, " ,");
/*      */     
/*      */ 
/*  227 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/*  229 */       String s = astring[i];
/*      */       
/*  231 */       if (s.contains("-"))
/*      */       {
/*  233 */         String[] astring1 = Config.tokenize(s, "-");
/*      */         
/*  235 */         if (astring1.length == 2)
/*      */         {
/*  237 */           int j = Config.parseInt(astring1[0], -1);
/*  238 */           int k = Config.parseInt(astring1[1], -1);
/*      */           
/*  240 */           if ((j >= 0) && (k >= 0))
/*      */           {
/*  242 */             if (j > k)
/*      */             {
/*  244 */               Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseTileNames_1_);
/*  245 */               continue;
/*      */             }
/*      */             
/*  248 */             int l = j;
/*      */             
/*      */ 
/*      */ 
/*  252 */             while (l <= k)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  257 */               list.add(String.valueOf(l));
/*  258 */               l++;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  264 */       list.add(s);
/*      */     }
/*      */     
/*  267 */     String[] astring2 = (String[])list.toArray(new String[list.size()]);
/*      */     
/*  269 */     for (int i1 = 0; i1 < astring2.length; i1++)
/*      */     {
/*  271 */       String s1 = astring2[i1];
/*  272 */       s1 = TextureUtils.fixResourcePath(s1, this.basePath);
/*      */       
/*  274 */       if ((!s1.startsWith(this.basePath)) && (!s1.startsWith("textures/")) && (!s1.startsWith("mcpatcher/")))
/*      */       {
/*  276 */         s1 = this.basePath + "/" + s1;
/*      */       }
/*      */       
/*  279 */       if (s1.endsWith(".png"))
/*      */       {
/*  281 */         s1 = s1.substring(0, s1.length() - 4);
/*      */       }
/*      */       
/*  284 */       String s2 = "textures/blocks/";
/*      */       
/*  286 */       if (s1.startsWith(s2))
/*      */       {
/*  288 */         s1 = s1.substring(s2.length());
/*      */       }
/*      */       
/*  291 */       if (s1.startsWith("/"))
/*      */       {
/*  293 */         s1 = s1.substring(1);
/*      */       }
/*      */       
/*  296 */       astring2[i1] = s1;
/*      */     }
/*      */     
/*  299 */     return astring2;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseInt(String p_parseInt_0_)
/*      */   {
/*  305 */     if (p_parseInt_0_ == null)
/*      */     {
/*  307 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  311 */     int i = Config.parseInt(p_parseInt_0_, -1);
/*      */     
/*  313 */     if (i < 0)
/*      */     {
/*  315 */       Config.warn("Invalid number: " + p_parseInt_0_);
/*      */     }
/*      */     
/*  318 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseInt(String p_parseInt_0_, int p_parseInt_1_)
/*      */   {
/*  324 */     if (p_parseInt_0_ == null)
/*      */     {
/*  326 */       return p_parseInt_1_;
/*      */     }
/*      */     
/*      */ 
/*  330 */     int i = Config.parseInt(p_parseInt_0_, -1);
/*      */     
/*  332 */     if (i < 0)
/*      */     {
/*  334 */       Config.warn("Invalid number: " + p_parseInt_0_);
/*  335 */       return p_parseInt_1_;
/*      */     }
/*      */     
/*      */ 
/*  339 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean parseBoolean(String p_parseBoolean_0_)
/*      */   {
/*  346 */     return p_parseBoolean_0_ == null ? false : p_parseBoolean_0_.toLowerCase().equals("true");
/*      */   }
/*      */   
/*      */   private static int parseSymmetry(String p_parseSymmetry_0_)
/*      */   {
/*  351 */     if (p_parseSymmetry_0_ == null)
/*      */     {
/*  353 */       return 1;
/*      */     }
/*  355 */     if (p_parseSymmetry_0_.equals("opposite"))
/*      */     {
/*  357 */       return 2;
/*      */     }
/*  359 */     if (p_parseSymmetry_0_.equals("all"))
/*      */     {
/*  361 */       return 6;
/*      */     }
/*      */     
/*      */ 
/*  365 */     Config.warn("Unknown symmetry: " + p_parseSymmetry_0_);
/*  366 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseFaces(String p_parseFaces_0_)
/*      */   {
/*  372 */     if (p_parseFaces_0_ == null)
/*      */     {
/*  374 */       return 63;
/*      */     }
/*      */     
/*      */ 
/*  378 */     String[] astring = Config.tokenize(p_parseFaces_0_, " ,");
/*  379 */     int i = 0;
/*      */     
/*  381 */     for (int j = 0; j < astring.length; j++)
/*      */     {
/*  383 */       String s = astring[j];
/*  384 */       int k = parseFace(s);
/*  385 */       i |= k;
/*      */     }
/*      */     
/*  388 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseFace(String p_parseFace_0_)
/*      */   {
/*  394 */     p_parseFace_0_ = p_parseFace_0_.toLowerCase();
/*      */     
/*  396 */     if ((!p_parseFace_0_.equals("bottom")) && (!p_parseFace_0_.equals("down")))
/*      */     {
/*  398 */       if ((!p_parseFace_0_.equals("top")) && (!p_parseFace_0_.equals("up")))
/*      */       {
/*  400 */         if (p_parseFace_0_.equals("north"))
/*      */         {
/*  402 */           return 4;
/*      */         }
/*  404 */         if (p_parseFace_0_.equals("south"))
/*      */         {
/*  406 */           return 8;
/*      */         }
/*  408 */         if (p_parseFace_0_.equals("east"))
/*      */         {
/*  410 */           return 32;
/*      */         }
/*  412 */         if (p_parseFace_0_.equals("west"))
/*      */         {
/*  414 */           return 16;
/*      */         }
/*  416 */         if (p_parseFace_0_.equals("sides"))
/*      */         {
/*  418 */           return 60;
/*      */         }
/*  420 */         if (p_parseFace_0_.equals("all"))
/*      */         {
/*  422 */           return 63;
/*      */         }
/*      */         
/*      */ 
/*  426 */         Config.warn("Unknown face: " + p_parseFace_0_);
/*  427 */         return 128;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  432 */       return 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  437 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseConnect(String p_parseConnect_0_)
/*      */   {
/*  443 */     if (p_parseConnect_0_ == null)
/*      */     {
/*  445 */       return 0;
/*      */     }
/*  447 */     if (p_parseConnect_0_.equals("block"))
/*      */     {
/*  449 */       return 1;
/*      */     }
/*  451 */     if (p_parseConnect_0_.equals("tile"))
/*      */     {
/*  453 */       return 2;
/*      */     }
/*  455 */     if (p_parseConnect_0_.equals("material"))
/*      */     {
/*  457 */       return 3;
/*      */     }
/*      */     
/*      */ 
/*  461 */     Config.warn("Unknown connect: " + p_parseConnect_0_);
/*  462 */     return 128;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int[] parseInts(String p_parseInts_0_)
/*      */   {
/*  468 */     if (p_parseInts_0_ == null)
/*      */     {
/*  470 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  474 */     List list = new ArrayList();
/*  475 */     String[] astring = Config.tokenize(p_parseInts_0_, " ,");
/*      */     
/*  477 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/*  479 */       String s = astring[i];
/*      */       
/*  481 */       if (s.contains("-"))
/*      */       {
/*  483 */         String[] astring1 = Config.tokenize(s, "-");
/*      */         
/*  485 */         if (astring1.length != 2)
/*      */         {
/*  487 */           Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseInts_0_);
/*      */         }
/*      */         else
/*      */         {
/*  491 */           int k = Config.parseInt(astring1[0], -1);
/*  492 */           int l = Config.parseInt(astring1[1], -1);
/*      */           
/*  494 */           if ((k >= 0) && (l >= 0) && (k <= l))
/*      */           {
/*  496 */             for (int i1 = k; i1 <= l; i1++)
/*      */             {
/*  498 */               list.add(Integer.valueOf(i1));
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/*  503 */             Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseInts_0_);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  509 */         int j = Config.parseInt(s, -1);
/*      */         
/*  511 */         if (j < 0)
/*      */         {
/*  513 */           Config.warn("Invalid number: " + s + ", when parsing: " + p_parseInts_0_);
/*      */         }
/*      */         else
/*      */         {
/*  517 */           list.add(Integer.valueOf(j));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  522 */     int[] aint = new int[list.size()];
/*      */     
/*  524 */     for (int j1 = 0; j1 < aint.length; j1++)
/*      */     {
/*  526 */       aint[j1] = ((Integer)list.get(j1)).intValue();
/*      */     }
/*      */     
/*  529 */     return aint;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int[] parseBlockIds(String p_parseBlockIds_0_)
/*      */   {
/*  535 */     if (p_parseBlockIds_0_ == null)
/*      */     {
/*  537 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  541 */     List list = new ArrayList();
/*  542 */     String[] astring = Config.tokenize(p_parseBlockIds_0_, " ,");
/*      */     
/*  544 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/*  546 */       String s = astring[i];
/*      */       
/*  548 */       if (s.contains("-"))
/*      */       {
/*  550 */         String[] astring1 = Config.tokenize(s, "-");
/*      */         
/*  552 */         if (astring1.length != 2)
/*      */         {
/*  554 */           Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseBlockIds_0_);
/*      */         }
/*      */         else
/*      */         {
/*  558 */           int k = parseBlockId(astring1[0]);
/*  559 */           int l = parseBlockId(astring1[1]);
/*      */           
/*  561 */           if ((k >= 0) && (l >= 0) && (k <= l))
/*      */           {
/*  563 */             for (int i1 = k; i1 <= l; i1++)
/*      */             {
/*  565 */               list.add(Integer.valueOf(i1));
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/*  570 */             Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseBlockIds_0_);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  576 */         int j = parseBlockId(s);
/*      */         
/*  578 */         if (j < 0)
/*      */         {
/*  580 */           Config.warn("Invalid block ID: " + s + ", when parsing: " + p_parseBlockIds_0_);
/*      */         }
/*      */         else
/*      */         {
/*  584 */           list.add(Integer.valueOf(j));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  589 */     int[] aint = new int[list.size()];
/*      */     
/*  591 */     for (int j1 = 0; j1 < aint.length; j1++)
/*      */     {
/*  593 */       aint[j1] = ((Integer)list.get(j1)).intValue();
/*      */     }
/*      */     
/*  596 */     return aint;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int parseBlockId(String p_parseBlockId_0_)
/*      */   {
/*  602 */     int i = Config.parseInt(p_parseBlockId_0_, -1);
/*      */     
/*  604 */     if (i >= 0)
/*      */     {
/*  606 */       return i;
/*      */     }
/*      */     
/*      */ 
/*  610 */     Block block = Block.getBlockFromName(p_parseBlockId_0_);
/*  611 */     return block != null ? Block.getIdFromBlock(block) : -1;
/*      */   }
/*      */   
/*      */ 
/*      */   private IBlockState parseBlockState(String p_parseBlockState_1_)
/*      */   {
/*  617 */     if (p_parseBlockState_1_ == null)
/*      */     {
/*  619 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  623 */     String[] astring = Config.tokenize(p_parseBlockState_1_, ":");
/*      */     
/*  625 */     if (astring.length < 2)
/*      */     {
/*  627 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  631 */     String s = astring[0];
/*  632 */     String s1 = astring[1];
/*  633 */     String s2 = s + ":" + s1;
/*  634 */     Block block = Block.getBlockFromName(s2);
/*      */     
/*  636 */     if (block == null)
/*      */     {
/*  638 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  642 */     int i = -1;
/*  643 */     IBlockState iblockstate = null;
/*      */     
/*  645 */     for (int j = 2; j < astring.length; j++)
/*      */     {
/*  647 */       String s3 = astring[j];
/*      */       
/*  649 */       if (s3.length() >= 1)
/*      */       {
/*  651 */         if (Character.isDigit(s3.charAt(0)))
/*      */         {
/*  653 */           if ((s3.indexOf('-') < 0) && (s3.indexOf(',') < 0))
/*      */           {
/*  655 */             int k = Config.parseInt(s3, -1);
/*      */             
/*  657 */             if (k >= 0)
/*      */             {
/*  659 */               i = k;
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  665 */           String[] astring1 = Config.tokenize(s3, "=");
/*      */           
/*  667 */           if (astring1.length >= 2)
/*      */           {
/*  669 */             String s4 = astring1[0];
/*  670 */             String s5 = astring1[1];
/*      */             
/*  672 */             if (s5.indexOf(',') < 0)
/*      */             {
/*  674 */               if (iblockstate == null)
/*      */               {
/*  676 */                 iblockstate = block.getDefaultState();
/*      */               }
/*      */               
/*  679 */               Collection collection = iblockstate.getPropertyNames();
/*  680 */               IProperty iproperty = getProperty(s4, collection);
/*      */               
/*  682 */               if (iproperty == null)
/*      */               {
/*  684 */                 String s6 = "\"";
/*  685 */                 Config.warn("Block " + s6 + s2 + s6 + " has no property " + s6 + s4 + s6);
/*      */               }
/*      */               else
/*      */               {
/*  689 */                 Class oclass = iproperty.getValueClass();
/*  690 */                 Object object = ConnectedParser.parseValue(s5, oclass);
/*      */                 
/*  692 */                 if (object == null)
/*      */                 {
/*  694 */                   Collection collection1 = iproperty.getAllowedValues();
/*  695 */                   object = ConnectedParser.getPropertyValue(s5, collection1);
/*      */                 }
/*      */                 
/*  698 */                 if (object == null)
/*      */                 {
/*  700 */                   Config.warn("Invalid value: " + s5 + ", for property: " + iproperty);
/*      */                 }
/*  702 */                 else if (!(object instanceof Comparable))
/*      */                 {
/*  704 */                   Config.warn("Value is not Comparable: " + s5 + ", for property: " + iproperty);
/*      */                 }
/*      */                 else
/*      */                 {
/*  708 */                   Comparable comparable = (Comparable)object;
/*  709 */                   iblockstate = iblockstate.withProperty(iproperty, comparable);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  718 */     if (iblockstate == null)
/*      */     {
/*  720 */       if (i < 0)
/*      */       {
/*  722 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  726 */       return block.getStateFromMeta(i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  731 */     return iblockstate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static IProperty getProperty(String p_getProperty_0_, Collection p_getProperty_1_)
/*      */   {
/*  740 */     for (Object iproperty : p_getProperty_1_)
/*      */     {
/*  742 */       if (p_getProperty_0_.equals(((IProperty)iproperty).getName()))
/*      */       {
/*  744 */         return (IProperty)iproperty;
/*      */       }
/*      */     }
/*      */     
/*  748 */     return null;
/*      */   }
/*      */   
/*      */   private static int parseMethod(String p_parseMethod_0_)
/*      */   {
/*  753 */     if (p_parseMethod_0_ == null)
/*      */     {
/*  755 */       return 1;
/*      */     }
/*  757 */     if ((!p_parseMethod_0_.equals("ctm")) && (!p_parseMethod_0_.equals("glass")))
/*      */     {
/*  759 */       if ((!p_parseMethod_0_.equals("horizontal")) && (!p_parseMethod_0_.equals("bookshelf")))
/*      */       {
/*  761 */         if (p_parseMethod_0_.equals("vertical"))
/*      */         {
/*  763 */           return 6;
/*      */         }
/*  765 */         if (p_parseMethod_0_.equals("top"))
/*      */         {
/*  767 */           return 3;
/*      */         }
/*  769 */         if (p_parseMethod_0_.equals("random"))
/*      */         {
/*  771 */           return 4;
/*      */         }
/*  773 */         if (p_parseMethod_0_.equals("repeat"))
/*      */         {
/*  775 */           return 5;
/*      */         }
/*  777 */         if (p_parseMethod_0_.equals("fixed"))
/*      */         {
/*  779 */           return 7;
/*      */         }
/*  781 */         if ((!p_parseMethod_0_.equals("horizontal+vertical")) && (!p_parseMethod_0_.equals("h+v")))
/*      */         {
/*  783 */           if ((!p_parseMethod_0_.equals("vertical+horizontal")) && (!p_parseMethod_0_.equals("v+h")))
/*      */           {
/*  785 */             Config.warn("Unknown method: " + p_parseMethod_0_);
/*  786 */             return 0;
/*      */           }
/*      */           
/*      */ 
/*  790 */           return 9;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  795 */         return 8;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  800 */       return 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  805 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isValid(String p_isValid_1_)
/*      */   {
/*  811 */     if ((this.name != null) && (this.name.length() > 0))
/*      */     {
/*  813 */       if (this.basePath == null)
/*      */       {
/*  815 */         Config.warn("No base path found: " + p_isValid_1_);
/*  816 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  820 */       if (this.matchBlocks == null)
/*      */       {
/*  822 */         this.matchBlocks = detectMatchBlocks();
/*      */       }
/*      */       
/*  825 */       if ((this.matchTiles == null) && (this.matchBlocks == null))
/*      */       {
/*  827 */         this.matchTiles = detectMatchTiles();
/*      */       }
/*      */       
/*  830 */       if ((this.matchBlocks == null) && (this.matchTiles == null))
/*      */       {
/*  832 */         Config.warn("No matchBlocks or matchTiles specified: " + p_isValid_1_);
/*  833 */         return false;
/*      */       }
/*  835 */       if (this.method == 0)
/*      */       {
/*  837 */         Config.warn("No method: " + p_isValid_1_);
/*  838 */         return false;
/*      */       }
/*  840 */       if ((this.tiles != null) && (this.tiles.length > 0))
/*      */       {
/*  842 */         if (this.connect == 0)
/*      */         {
/*  844 */           this.connect = detectConnect();
/*      */         }
/*      */         
/*  847 */         if (this.connect == 128)
/*      */         {
/*  849 */           Config.warn("Invalid connect in: " + p_isValid_1_);
/*  850 */           return false;
/*      */         }
/*  852 */         if (this.renderPass > 0)
/*      */         {
/*  854 */           Config.warn("Render pass not supported: " + this.renderPass);
/*  855 */           return false;
/*      */         }
/*  857 */         if ((this.faces & 0x80) != 0)
/*      */         {
/*  859 */           Config.warn("Invalid faces in: " + p_isValid_1_);
/*  860 */           return false;
/*      */         }
/*  862 */         if ((this.symmetry & 0x80) != 0)
/*      */         {
/*  864 */           Config.warn("Invalid symmetry in: " + p_isValid_1_);
/*  865 */           return false;
/*      */         }
/*      */         
/*      */ 
/*  869 */         switch (this.method)
/*      */         {
/*      */         case 1: 
/*  872 */           return isValidCtm(p_isValid_1_);
/*      */         
/*      */         case 2: 
/*  875 */           return isValidHorizontal(p_isValid_1_);
/*      */         
/*      */         case 3: 
/*  878 */           return isValidTop(p_isValid_1_);
/*      */         
/*      */         case 4: 
/*  881 */           return isValidRandom(p_isValid_1_);
/*      */         
/*      */         case 5: 
/*  884 */           return isValidRepeat(p_isValid_1_);
/*      */         
/*      */         case 6: 
/*  887 */           return isValidVertical(p_isValid_1_);
/*      */         
/*      */         case 7: 
/*  890 */           return isValidFixed(p_isValid_1_);
/*      */         
/*      */         case 8: 
/*  893 */           return isValidHorizontalVertical(p_isValid_1_);
/*      */         
/*      */         case 9: 
/*  896 */           return isValidVerticalHorizontal(p_isValid_1_);
/*      */         }
/*      */         
/*  899 */         Config.warn("Unknown method: " + p_isValid_1_);
/*  900 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  906 */       Config.warn("No tiles specified: " + p_isValid_1_);
/*  907 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  913 */     Config.warn("No name found: " + p_isValid_1_);
/*  914 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private int detectConnect()
/*      */   {
/*  920 */     return this.matchTiles != null ? 2 : this.matchBlocks != null ? 1 : 128;
/*      */   }
/*      */   
/*      */   private int[] detectMatchBlocks()
/*      */   {
/*  925 */     if (!this.name.startsWith("block"))
/*      */     {
/*  927 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  931 */     int i = "block".length();
/*      */     
/*      */ 
/*  934 */     for (int j = i; j < this.name.length(); j++)
/*      */     {
/*  936 */       char c0 = this.name.charAt(j);
/*      */       
/*  938 */       if ((c0 < '0') || (c0 > '9')) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  944 */     if (j == i)
/*      */     {
/*  946 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  950 */     String s = this.name.substring(i, j);
/*  951 */     int k = Config.parseInt(s, -1);
/*  952 */     return new int[] { k < 0 ? null : k };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String[] detectMatchTiles()
/*      */   {
/*  959 */     TextureAtlasSprite textureatlassprite = getIcon(this.name);
/*  960 */     return new String[] { textureatlassprite == null ? null : this.name };
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite getIcon(String p_getIcon_0_)
/*      */   {
/*  965 */     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
/*  966 */     TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(p_getIcon_0_);
/*      */     
/*  968 */     if (textureatlassprite != null)
/*      */     {
/*  970 */       return textureatlassprite;
/*      */     }
/*      */     
/*      */ 
/*  974 */     textureatlassprite = texturemap.getSpriteSafe("blocks/" + p_getIcon_0_);
/*  975 */     return textureatlassprite;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidCtm(String p_isValidCtm_1_)
/*      */   {
/*  981 */     if (this.tiles == null)
/*      */     {
/*  983 */       this.tiles = parseTileNames("0-11 16-27 32-43 48-58");
/*      */     }
/*      */     
/*  986 */     if (this.tiles.length < 47)
/*      */     {
/*  988 */       Config.warn("Invalid tiles, must be at least 47: " + p_isValidCtm_1_);
/*  989 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  993 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidHorizontal(String p_isValidHorizontal_1_)
/*      */   {
/*  999 */     if (this.tiles == null)
/*      */     {
/* 1001 */       this.tiles = parseTileNames("12-15");
/*      */     }
/*      */     
/* 1004 */     if (this.tiles.length != 4)
/*      */     {
/* 1006 */       Config.warn("Invalid tiles, must be exactly 4: " + p_isValidHorizontal_1_);
/* 1007 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1011 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidVertical(String p_isValidVertical_1_)
/*      */   {
/* 1017 */     if (this.tiles == null)
/*      */     {
/* 1019 */       Config.warn("No tiles defined for vertical: " + p_isValidVertical_1_);
/* 1020 */       return false;
/*      */     }
/* 1022 */     if (this.tiles.length != 4)
/*      */     {
/* 1024 */       Config.warn("Invalid tiles, must be exactly 4: " + p_isValidVertical_1_);
/* 1025 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1029 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidHorizontalVertical(String p_isValidHorizontalVertical_1_)
/*      */   {
/* 1035 */     if (this.tiles == null)
/*      */     {
/* 1037 */       Config.warn("No tiles defined for horizontal+vertical: " + p_isValidHorizontalVertical_1_);
/* 1038 */       return false;
/*      */     }
/* 1040 */     if (this.tiles.length != 7)
/*      */     {
/* 1042 */       Config.warn("Invalid tiles, must be exactly 7: " + p_isValidHorizontalVertical_1_);
/* 1043 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1047 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidVerticalHorizontal(String p_isValidVerticalHorizontal_1_)
/*      */   {
/* 1053 */     if (this.tiles == null)
/*      */     {
/* 1055 */       Config.warn("No tiles defined for vertical+horizontal: " + p_isValidVerticalHorizontal_1_);
/* 1056 */       return false;
/*      */     }
/* 1058 */     if (this.tiles.length != 7)
/*      */     {
/* 1060 */       Config.warn("Invalid tiles, must be exactly 7: " + p_isValidVerticalHorizontal_1_);
/* 1061 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1065 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidRandom(String p_isValidRandom_1_)
/*      */   {
/* 1071 */     if ((this.tiles != null) && (this.tiles.length > 0))
/*      */     {
/* 1073 */       if (this.weights != null)
/*      */       {
/* 1075 */         if (this.weights.length > this.tiles.length)
/*      */         {
/* 1077 */           Config.warn("More weights defined than tiles, trimming weights: " + p_isValidRandom_1_);
/* 1078 */           int[] aint = new int[this.tiles.length];
/* 1079 */           System.arraycopy(this.weights, 0, aint, 0, aint.length);
/* 1080 */           this.weights = aint;
/*      */         }
/*      */         
/* 1083 */         if (this.weights.length < this.tiles.length)
/*      */         {
/* 1085 */           Config.warn("Less weights defined than tiles, expanding weights: " + p_isValidRandom_1_);
/* 1086 */           int[] aint1 = new int[this.tiles.length];
/* 1087 */           System.arraycopy(this.weights, 0, aint1, 0, this.weights.length);
/* 1088 */           int i = ConnectedUtils.getAverage(this.weights);
/*      */           
/* 1090 */           for (int j = this.weights.length; j < aint1.length; j++)
/*      */           {
/* 1092 */             aint1[j] = i;
/*      */           }
/*      */           
/* 1095 */           this.weights = aint1;
/*      */         }
/*      */         
/* 1098 */         this.sumWeights = new int[this.weights.length];
/* 1099 */         int k = 0;
/*      */         
/* 1101 */         for (int l = 0; l < this.weights.length; l++)
/*      */         {
/* 1103 */           k += this.weights[l];
/* 1104 */           this.sumWeights[l] = k;
/*      */         }
/*      */         
/* 1107 */         this.sumAllWeights = k;
/*      */         
/* 1109 */         if (this.sumAllWeights <= 0)
/*      */         {
/* 1111 */           Config.warn("Invalid sum of all weights: " + k);
/* 1112 */           this.sumAllWeights = 1;
/*      */         }
/*      */       }
/*      */       
/* 1116 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1120 */     Config.warn("Tiles not defined: " + p_isValidRandom_1_);
/* 1121 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidRepeat(String p_isValidRepeat_1_)
/*      */   {
/* 1127 */     if (this.tiles == null)
/*      */     {
/* 1129 */       Config.warn("Tiles not defined: " + p_isValidRepeat_1_);
/* 1130 */       return false;
/*      */     }
/* 1132 */     if ((this.width > 0) && (this.width <= 16))
/*      */     {
/* 1134 */       if ((this.height > 0) && (this.height <= 16))
/*      */       {
/* 1136 */         if (this.tiles.length != this.width * this.height)
/*      */         {
/* 1138 */           Config.warn("Number of tiles does not equal width x height: " + p_isValidRepeat_1_);
/* 1139 */           return false;
/*      */         }
/*      */         
/*      */ 
/* 1143 */         return true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1148 */       Config.warn("Invalid height: " + p_isValidRepeat_1_);
/* 1149 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1154 */     Config.warn("Invalid width: " + p_isValidRepeat_1_);
/* 1155 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidFixed(String p_isValidFixed_1_)
/*      */   {
/* 1161 */     if (this.tiles == null)
/*      */     {
/* 1163 */       Config.warn("Tiles not defined: " + p_isValidFixed_1_);
/* 1164 */       return false;
/*      */     }
/* 1166 */     if (this.tiles.length != 1)
/*      */     {
/* 1168 */       Config.warn("Number of tiles should be 1 for method: fixed.");
/* 1169 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1173 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidTop(String p_isValidTop_1_)
/*      */   {
/* 1179 */     if (this.tiles == null)
/*      */     {
/* 1181 */       this.tiles = parseTileNames("66");
/*      */     }
/*      */     
/* 1184 */     if (this.tiles.length != 1)
/*      */     {
/* 1186 */       Config.warn("Invalid tiles, must be exactly 1: " + p_isValidTop_1_);
/* 1187 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1191 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateIcons(TextureMap p_updateIcons_1_)
/*      */   {
/* 1197 */     if (this.matchTiles != null)
/*      */     {
/* 1199 */       this.matchTileIcons = registerIcons(this.matchTiles, p_updateIcons_1_);
/*      */     }
/*      */     
/* 1202 */     if (this.tiles != null)
/*      */     {
/* 1204 */       this.tileIcons = registerIcons(this.tiles, p_updateIcons_1_);
/*      */     }
/*      */   }
/*      */   
/*      */   private static TextureAtlasSprite[] registerIcons(String[] p_registerIcons_0_, TextureMap p_registerIcons_1_)
/*      */   {
/* 1210 */     if (p_registerIcons_0_ == null)
/*      */     {
/* 1212 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1216 */     List list = new ArrayList();
/*      */     
/* 1218 */     for (int i = 0; i < p_registerIcons_0_.length; i++)
/*      */     {
/* 1220 */       String s = p_registerIcons_0_[i];
/* 1221 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/* 1222 */       String s1 = resourcelocation.getResourceDomain();
/* 1223 */       String s2 = resourcelocation.getResourcePath();
/*      */       
/* 1225 */       if (!s2.contains("/"))
/*      */       {
/* 1227 */         s2 = "textures/blocks/" + s2;
/*      */       }
/*      */       
/* 1230 */       String s3 = s2 + ".png";
/* 1231 */       ResourceLocation resourcelocation1 = new ResourceLocation(s1, s3);
/* 1232 */       boolean flag = Config.hasResource(resourcelocation1);
/*      */       
/* 1234 */       if (!flag)
/*      */       {
/* 1236 */         Config.warn("File not found: " + s3);
/*      */       }
/*      */       
/* 1239 */       String s4 = "textures/";
/* 1240 */       String s5 = s2;
/*      */       
/* 1242 */       if (s2.startsWith(s4))
/*      */       {
/* 1244 */         s5 = s2.substring(s4.length());
/*      */       }
/*      */       
/* 1247 */       ResourceLocation resourcelocation2 = new ResourceLocation(s1, s5);
/* 1248 */       TextureAtlasSprite textureatlassprite = p_registerIcons_1_.registerSprite(resourcelocation2);
/* 1249 */       list.add(textureatlassprite);
/*      */     }
/*      */     
/* 1252 */     TextureAtlasSprite[] atextureatlassprite = (TextureAtlasSprite[])list.toArray(new TextureAtlasSprite[list.size()]);
/* 1253 */     return atextureatlassprite;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean matchesBlock(int p_matchesBlock_1_)
/*      */   {
/* 1259 */     if ((this.matchBlocks != null) && (this.matchBlocks.length > 0))
/*      */     {
/* 1261 */       for (int i = 0; i < this.matchBlocks.length; i++)
/*      */       {
/* 1263 */         int j = this.matchBlocks[i];
/*      */         
/* 1265 */         if (j == p_matchesBlock_1_)
/*      */         {
/* 1267 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 1271 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1275 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean matchesIcon(TextureAtlasSprite p_matchesIcon_1_)
/*      */   {
/* 1281 */     if ((this.matchTileIcons != null) && (this.matchTileIcons.length > 0))
/*      */     {
/* 1283 */       for (int i = 0; i < this.matchTileIcons.length; i++)
/*      */       {
/* 1285 */         if (this.matchTileIcons[i] == p_matchesIcon_1_)
/*      */         {
/* 1287 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 1291 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1295 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1301 */     return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString(this.matchBlocks) + ", matchTiles: " + Config.arrayToString(this.matchTiles);
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ConnectedProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */