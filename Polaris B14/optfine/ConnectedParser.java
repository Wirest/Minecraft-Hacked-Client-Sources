/*     */ package optfine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class ConnectedParser
/*     */ {
/*  19 */   private String context = null;
/*     */   
/*     */   public ConnectedParser(String p_i28_1_)
/*     */   {
/*  23 */     this.context = p_i28_1_;
/*     */   }
/*     */   
/*     */   public String parseName(String p_parseName_1_)
/*     */   {
/*  28 */     String s = p_parseName_1_;
/*  29 */     int i = p_parseName_1_.lastIndexOf('/');
/*     */     
/*  31 */     if (i >= 0)
/*     */     {
/*  33 */       s = p_parseName_1_.substring(i + 1);
/*     */     }
/*     */     
/*  36 */     int j = s.lastIndexOf('.');
/*     */     
/*  38 */     if (j >= 0)
/*     */     {
/*  40 */       s = s.substring(0, j);
/*     */     }
/*     */     
/*  43 */     return s;
/*     */   }
/*     */   
/*     */   public String parseBasePath(String p_parseBasePath_1_)
/*     */   {
/*  48 */     int i = p_parseBasePath_1_.lastIndexOf('/');
/*  49 */     return i < 0 ? "" : p_parseBasePath_1_.substring(0, i);
/*     */   }
/*     */   
/*     */   public MatchBlock[] parseMatchBlocks(String p_parseMatchBlocks_1_)
/*     */   {
/*  54 */     if (p_parseMatchBlocks_1_ == null)
/*     */     {
/*  56 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  60 */     List list = new ArrayList();
/*  61 */     String[] astring = Config.tokenize(p_parseMatchBlocks_1_, " ");
/*     */     
/*  63 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/*  65 */       String s = astring[i];
/*  66 */       MatchBlock[] amatchblock = parseMatchBlock(s);
/*     */       
/*  68 */       if (amatchblock == null)
/*     */       {
/*  70 */         return null;
/*     */       }
/*     */       
/*  73 */       list.addAll(Arrays.asList(amatchblock));
/*     */     }
/*     */     
/*  76 */     MatchBlock[] amatchblock1 = (MatchBlock[])list.toArray(new MatchBlock[list.size()]);
/*  77 */     return amatchblock1;
/*     */   }
/*     */   
/*     */ 
/*     */   public MatchBlock[] parseMatchBlock(String p_parseMatchBlock_1_)
/*     */   {
/*  83 */     if (p_parseMatchBlock_1_ == null)
/*     */     {
/*  85 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  89 */     p_parseMatchBlock_1_ = p_parseMatchBlock_1_.trim();
/*     */     
/*  91 */     if (p_parseMatchBlock_1_.length() <= 0)
/*     */     {
/*  93 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  97 */     String[] astring = Config.tokenize(p_parseMatchBlock_1_, ":");
/*  98 */     String s = "minecraft";
/*  99 */     int i = 0;
/*     */     
/* 101 */     if ((astring.length > 1) && (isFullBlockName(astring)))
/*     */     {
/* 103 */       s = astring[0];
/* 104 */       i = 1;
/*     */     }
/*     */     else
/*     */     {
/* 108 */       s = "minecraft";
/* 109 */       i = 0;
/*     */     }
/*     */     
/* 112 */     String s1 = astring[i];
/* 113 */     String[] astring1 = (String[])Arrays.copyOfRange(astring, i + 1, astring.length);
/* 114 */     Block[] ablock = parseBlockPart(s, s1);
/* 115 */     MatchBlock[] amatchblock = new MatchBlock[ablock.length];
/*     */     
/* 117 */     for (int j = 0; j < ablock.length; j++)
/*     */     {
/* 119 */       Block block = ablock[j];
/* 120 */       int k = Block.getIdFromBlock(block);
/* 121 */       int[] aint = parseBlockMetadatas(block, astring1);
/* 122 */       MatchBlock matchblock = new MatchBlock(k, aint);
/* 123 */       amatchblock[j] = matchblock;
/*     */     }
/*     */     
/* 126 */     return amatchblock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isFullBlockName(String[] p_isFullBlockName_1_)
/*     */   {
/* 133 */     if (p_isFullBlockName_1_.length < 2)
/*     */     {
/* 135 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 139 */     String s = p_isFullBlockName_1_[1];
/* 140 */     return s.length() >= 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean startsWithDigit(String p_startsWithDigit_1_)
/*     */   {
/* 146 */     if (p_startsWithDigit_1_ == null)
/*     */     {
/* 148 */       return false;
/*     */     }
/* 150 */     if (p_startsWithDigit_1_.length() < 1)
/*     */     {
/* 152 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 156 */     char c0 = p_startsWithDigit_1_.charAt(0);
/* 157 */     return Character.isDigit(c0);
/*     */   }
/*     */   
/*     */ 
/*     */   public Block[] parseBlockPart(String p_parseBlockPart_1_, String p_parseBlockPart_2_)
/*     */   {
/* 163 */     if (startsWithDigit(p_parseBlockPart_2_))
/*     */     {
/* 165 */       int[] aint = parseIntList(p_parseBlockPart_2_);
/*     */       
/* 167 */       if (aint == null)
/*     */       {
/* 169 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 173 */       Block[] ablock1 = new Block[aint.length];
/*     */       
/* 175 */       for (int j = 0; j < aint.length; j++)
/*     */       {
/* 177 */         int i = aint[j];
/* 178 */         Block block1 = Block.getBlockById(i);
/*     */         
/* 180 */         if (block1 == null)
/*     */         {
/* 182 */           warn("Block not found for id: " + i);
/* 183 */           return null;
/*     */         }
/*     */         
/* 186 */         ablock1[j] = block1;
/*     */       }
/*     */       
/* 189 */       return ablock1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 194 */     String s = p_parseBlockPart_1_ + ":" + p_parseBlockPart_2_;
/* 195 */     Block block = Block.getBlockFromName(s);
/*     */     
/* 197 */     if (block == null)
/*     */     {
/* 199 */       warn("Block not found for name: " + s);
/* 200 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 204 */     Block[] ablock = { block };
/* 205 */     return ablock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int[] parseBlockMetadatas(Block p_parseBlockMetadatas_1_, String[] p_parseBlockMetadatas_2_)
/*     */   {
/* 212 */     if (p_parseBlockMetadatas_2_.length <= 0)
/*     */     {
/* 214 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 218 */     String s = p_parseBlockMetadatas_2_[0];
/*     */     
/* 220 */     if (startsWithDigit(s))
/*     */     {
/* 222 */       int[] aint = parseIntList(s);
/* 223 */       return aint;
/*     */     }
/*     */     
/*     */ 
/* 227 */     IBlockState iblockstate = p_parseBlockMetadatas_1_.getDefaultState();
/* 228 */     Collection collection = iblockstate.getPropertyNames();
/* 229 */     Map map = new HashMap();
/*     */     
/* 231 */     for (int i = 0; i < p_parseBlockMetadatas_2_.length; i++)
/*     */     {
/* 233 */       String s1 = p_parseBlockMetadatas_2_[i];
/*     */       
/* 235 */       if (s1.length() > 0)
/*     */       {
/* 237 */         String[] astring = Config.tokenize(s1, "=");
/*     */         
/* 239 */         if (astring.length != 2)
/*     */         {
/* 241 */           warn("Invalid block property: " + s1);
/* 242 */           return null;
/*     */         }
/*     */         
/* 245 */         String s2 = astring[0];
/* 246 */         String s3 = astring[1];
/* 247 */         IProperty iproperty = ConnectedProperties.getProperty(s2, collection);
/*     */         
/* 249 */         if (iproperty == null)
/*     */         {
/* 251 */           warn("Property not found: " + s2 + ", block: " + p_parseBlockMetadatas_1_);
/* 252 */           return null;
/*     */         }
/*     */         
/* 255 */         List list = (List)map.get(s2);
/*     */         
/* 257 */         if (list == null)
/*     */         {
/* 259 */           list = new ArrayList();
/* 260 */           map.put(iproperty, list);
/*     */         }
/*     */         
/* 263 */         String[] astring1 = Config.tokenize(s3, ",");
/*     */         
/* 265 */         for (int j = 0; j < astring1.length; j++)
/*     */         {
/* 267 */           String s4 = astring1[j];
/* 268 */           Comparable comparable = parsePropertyValue(iproperty, s4);
/*     */           
/* 270 */           if (comparable == null)
/*     */           {
/* 272 */             warn("Property value not found: " + s4 + ", property: " + s2 + ", block: " + p_parseBlockMetadatas_1_);
/* 273 */             return null;
/*     */           }
/*     */           
/* 276 */           list.add(comparable);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 281 */     if (map.isEmpty())
/*     */     {
/* 283 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 287 */     List list1 = new ArrayList();
/*     */     
/* 289 */     for (int k = 0; k < 16; k++)
/*     */     {
/* 291 */       IBlockState iblockstate1 = p_parseBlockMetadatas_1_.getStateFromMeta(k);
/*     */       
/* 293 */       if (matchState(iblockstate1, map))
/*     */       {
/* 295 */         list1.add(Integer.valueOf(k));
/*     */       }
/*     */     }
/*     */     
/* 299 */     if (list1.size() == 16)
/*     */     {
/* 301 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 305 */     int[] aint1 = new int[list1.size()];
/*     */     
/* 307 */     for (int l = 0; l < aint1.length; l++)
/*     */     {
/* 309 */       aint1[l] = ((Integer)list1.get(l)).intValue();
/*     */     }
/*     */     
/* 312 */     return aint1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Comparable parsePropertyValue(IProperty p_parsePropertyValue_0_, String p_parsePropertyValue_1_)
/*     */   {
/* 321 */     Class oclass = p_parsePropertyValue_0_.getValueClass();
/* 322 */     Comparable comparable = parseValue(p_parsePropertyValue_1_, oclass);
/*     */     
/* 324 */     if (comparable == null)
/*     */     {
/* 326 */       Collection collection = p_parsePropertyValue_0_.getAllowedValues();
/* 327 */       comparable = getPropertyValue(p_parsePropertyValue_1_, collection);
/*     */     }
/*     */     
/* 330 */     return comparable;
/*     */   }
/*     */   
/*     */   public static Comparable getPropertyValue(String p_getPropertyValue_0_, Collection p_getPropertyValue_1_)
/*     */   {
/* 335 */     for (Object comparable : p_getPropertyValue_1_)
/*     */     {
/* 337 */       if (String.valueOf(comparable).equals(p_getPropertyValue_0_))
/*     */       {
/* 339 */         return (Comparable)comparable;
/*     */       }
/*     */     }
/*     */     
/* 343 */     return null;
/*     */   }
/*     */   
/*     */   public static Comparable parseValue(String p_parseValue_0_, Class p_parseValue_1_)
/*     */   {
/* 348 */     return p_parseValue_1_ == Boolean.class ? Boolean.valueOf(p_parseValue_0_) : p_parseValue_1_ == String.class ? p_parseValue_0_ : Double.valueOf(p_parseValue_1_ == Double.class ? Double.valueOf(p_parseValue_0_).doubleValue() : p_parseValue_1_ == Float.class ? Float.valueOf(p_parseValue_0_).floatValue() : p_parseValue_1_ == Integer.class ? Integer.valueOf(p_parseValue_0_).intValue() : (p_parseValue_1_ == Long.class ? Long.valueOf(p_parseValue_0_) : null).longValue());
/*     */   }
/*     */   
/*     */   public boolean matchState(IBlockState p_matchState_1_, Map p_matchState_2_)
/*     */   {
/* 353 */     for (Object entry : p_matchState_2_.entrySet())
/*     */     {
/* 355 */       IProperty iproperty = (IProperty)((Map.Entry)entry).getKey();
/* 356 */       List list = (List)((Map.Entry)entry).getValue();
/* 357 */       Comparable comparable = p_matchState_1_.getValue(iproperty);
/*     */       
/* 359 */       if (comparable == null)
/*     */       {
/* 361 */         return false;
/*     */       }
/*     */       
/* 364 */       if (!list.contains(comparable))
/*     */       {
/* 366 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 370 */     return true;
/*     */   }
/*     */   
/*     */   public BiomeGenBase[] parseBiomes(String p_parseBiomes_1_)
/*     */   {
/* 375 */     if (p_parseBiomes_1_ == null)
/*     */     {
/* 377 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 381 */     String[] astring = Config.tokenize(p_parseBiomes_1_, " ");
/* 382 */     List list = new ArrayList();
/*     */     
/* 384 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 386 */       String s = astring[i];
/* 387 */       BiomeGenBase biomegenbase = findBiome(s);
/*     */       
/* 389 */       if (biomegenbase == null)
/*     */       {
/* 391 */         warn("Biome not found: " + s);
/*     */       }
/*     */       else
/*     */       {
/* 395 */         list.add(biomegenbase);
/*     */       }
/*     */     }
/*     */     
/* 399 */     BiomeGenBase[] abiomegenbase = (BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]);
/* 400 */     return abiomegenbase;
/*     */   }
/*     */   
/*     */ 
/*     */   public BiomeGenBase findBiome(String p_findBiome_1_)
/*     */   {
/* 406 */     p_findBiome_1_ = p_findBiome_1_.toLowerCase();
/* 407 */     BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
/*     */     
/* 409 */     for (int i = 0; i < abiomegenbase.length; i++)
/*     */     {
/* 411 */       BiomeGenBase biomegenbase = abiomegenbase[i];
/*     */       
/* 413 */       if (biomegenbase != null)
/*     */       {
/* 415 */         String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();
/*     */         
/* 417 */         if (s.equals(p_findBiome_1_))
/*     */         {
/* 419 */           return biomegenbase;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 424 */     return null;
/*     */   }
/*     */   
/*     */   public int parseInt(String p_parseInt_1_)
/*     */   {
/* 429 */     if (p_parseInt_1_ == null)
/*     */     {
/* 431 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 435 */     int i = Config.parseInt(p_parseInt_1_, -1);
/*     */     
/* 437 */     if (i < 0)
/*     */     {
/* 439 */       warn("Invalid number: " + p_parseInt_1_);
/*     */     }
/*     */     
/* 442 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public int parseInt(String p_parseInt_1_, int p_parseInt_2_)
/*     */   {
/* 448 */     if (p_parseInt_1_ == null)
/*     */     {
/* 450 */       return p_parseInt_2_;
/*     */     }
/*     */     
/*     */ 
/* 454 */     int i = Config.parseInt(p_parseInt_1_, -1);
/*     */     
/* 456 */     if (i < 0)
/*     */     {
/* 458 */       warn("Invalid number: " + p_parseInt_1_);
/* 459 */       return p_parseInt_2_;
/*     */     }
/*     */     
/*     */ 
/* 463 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int[] parseIntList(String p_parseIntList_1_)
/*     */   {
/* 470 */     if (p_parseIntList_1_ == null)
/*     */     {
/* 472 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 476 */     List list = new ArrayList();
/* 477 */     String[] astring = Config.tokenize(p_parseIntList_1_, " ,");
/*     */     
/* 479 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 481 */       String s = astring[i];
/*     */       
/* 483 */       if (s.contains("-"))
/*     */       {
/* 485 */         String[] astring1 = Config.tokenize(s, "-");
/*     */         
/* 487 */         if (astring1.length != 2)
/*     */         {
/* 489 */           warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
/*     */         }
/*     */         else
/*     */         {
/* 493 */           int k = Config.parseInt(astring1[0], -1);
/* 494 */           int l = Config.parseInt(astring1[1], -1);
/*     */           
/* 496 */           if ((k >= 0) && (l >= 0) && (k <= l))
/*     */           {
/* 498 */             for (int i1 = k; i1 <= l; i1++)
/*     */             {
/* 500 */               list.add(Integer.valueOf(i1));
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 505 */             warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 511 */         int j = Config.parseInt(s, -1);
/*     */         
/* 513 */         if (j < 0)
/*     */         {
/* 515 */           warn("Invalid number: " + s + ", when parsing: " + p_parseIntList_1_);
/*     */         }
/*     */         else
/*     */         {
/* 519 */           list.add(Integer.valueOf(j));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 524 */     int[] aint = new int[list.size()];
/*     */     
/* 526 */     for (int j1 = 0; j1 < aint.length; j1++)
/*     */     {
/* 528 */       aint[j1] = ((Integer)list.get(j1)).intValue();
/*     */     }
/*     */     
/* 531 */     return aint;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean[] parseFaces(String p_parseFaces_1_, boolean[] p_parseFaces_2_)
/*     */   {
/* 537 */     if (p_parseFaces_1_ == null)
/*     */     {
/* 539 */       return p_parseFaces_2_;
/*     */     }
/*     */     
/*     */ 
/* 543 */     EnumSet enumset = EnumSet.allOf(EnumFacing.class);
/* 544 */     String[] astring = Config.tokenize(p_parseFaces_1_, " ,");
/*     */     
/* 546 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 548 */       String s = astring[i];
/*     */       
/* 550 */       if (s.equals("sides"))
/*     */       {
/* 552 */         enumset.add(EnumFacing.NORTH);
/* 553 */         enumset.add(EnumFacing.SOUTH);
/* 554 */         enumset.add(EnumFacing.WEST);
/* 555 */         enumset.add(EnumFacing.EAST);
/*     */       }
/* 557 */       else if (s.equals("all"))
/*     */       {
/* 559 */         enumset.addAll(Arrays.asList(EnumFacing.VALUES));
/*     */       }
/*     */       else
/*     */       {
/* 563 */         EnumFacing enumfacing = parseFace(s);
/*     */         
/* 565 */         if (enumfacing != null)
/*     */         {
/* 567 */           enumset.add(enumfacing);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 572 */     boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
/*     */     
/* 574 */     for (int j = 0; j < aboolean.length; j++)
/*     */     {
/* 576 */       aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
/*     */     }
/*     */     
/* 579 */     return aboolean;
/*     */   }
/*     */   
/*     */ 
/*     */   public EnumFacing parseFace(String p_parseFace_1_)
/*     */   {
/* 585 */     p_parseFace_1_ = p_parseFace_1_.toLowerCase();
/*     */     
/* 587 */     if ((!p_parseFace_1_.equals("bottom")) && (!p_parseFace_1_.equals("down")))
/*     */     {
/* 589 */       if ((!p_parseFace_1_.equals("top")) && (!p_parseFace_1_.equals("up")))
/*     */       {
/* 591 */         if (p_parseFace_1_.equals("north"))
/*     */         {
/* 593 */           return EnumFacing.NORTH;
/*     */         }
/* 595 */         if (p_parseFace_1_.equals("south"))
/*     */         {
/* 597 */           return EnumFacing.SOUTH;
/*     */         }
/* 599 */         if (p_parseFace_1_.equals("east"))
/*     */         {
/* 601 */           return EnumFacing.EAST;
/*     */         }
/* 603 */         if (p_parseFace_1_.equals("west"))
/*     */         {
/* 605 */           return EnumFacing.WEST;
/*     */         }
/*     */         
/*     */ 
/* 609 */         Config.warn("Unknown face: " + p_parseFace_1_);
/* 610 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 615 */       return EnumFacing.UP;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 620 */     return EnumFacing.DOWN;
/*     */   }
/*     */   
/*     */ 
/*     */   public void dbg(String p_dbg_1_)
/*     */   {
/* 626 */     Config.dbg(this.context + ": " + p_dbg_1_);
/*     */   }
/*     */   
/*     */   public void warn(String p_warn_1_)
/*     */   {
/* 631 */     Config.warn(this.context + ": " + p_warn_1_);
/*     */   }
/*     */   
/*     */   public RangeListInt parseRangeListInt(String p_parseRangeListInt_1_)
/*     */   {
/* 636 */     if (p_parseRangeListInt_1_ == null)
/*     */     {
/* 638 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 642 */     RangeListInt rangelistint = new RangeListInt();
/* 643 */     String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ,");
/*     */     
/* 645 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 647 */       String s = astring[i];
/* 648 */       RangeInt rangeint = parseRangeInt(s);
/*     */       
/* 650 */       if (rangeint == null)
/*     */       {
/* 652 */         return null;
/*     */       }
/*     */       
/* 655 */       rangelistint.addRange(rangeint);
/*     */     }
/*     */     
/* 658 */     return rangelistint;
/*     */   }
/*     */   
/*     */ 
/*     */   private RangeInt parseRangeInt(String p_parseRangeInt_1_)
/*     */   {
/* 664 */     if (p_parseRangeInt_1_ == null)
/*     */     {
/* 666 */       return null;
/*     */     }
/* 668 */     if (p_parseRangeInt_1_.indexOf('-') >= 0)
/*     */     {
/* 670 */       String[] astring = Config.tokenize(p_parseRangeInt_1_, "-");
/*     */       
/* 672 */       if (astring.length != 2)
/*     */       {
/* 674 */         warn("Invalid range: " + p_parseRangeInt_1_);
/* 675 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 679 */       int j = Config.parseInt(astring[0], -1);
/* 680 */       int k = Config.parseInt(astring[1], -1);
/*     */       
/* 682 */       if ((j >= 0) && (k >= 0))
/*     */       {
/* 684 */         return new RangeInt(j, k);
/*     */       }
/*     */       
/*     */ 
/* 688 */       warn("Invalid range: " + p_parseRangeInt_1_);
/* 689 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 695 */     int i = Config.parseInt(p_parseRangeInt_1_, -1);
/*     */     
/* 697 */     if (i < 0)
/*     */     {
/* 699 */       warn("Invalid integer: " + p_parseRangeInt_1_);
/* 700 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 704 */     return new RangeInt(i, i);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ConnectedParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */