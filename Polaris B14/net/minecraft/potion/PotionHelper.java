/*     */ package net.minecraft.potion;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.IntegerCache;
/*     */ 
/*     */ public class PotionHelper
/*     */ {
/*  12 */   public static final String field_77924_a = null;
/*     */   public static final String sugarEffect = "-0+1-2-3&4-4+13";
/*     */   public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
/*     */   public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
/*     */   public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
/*     */   public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
/*     */   public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
/*     */   public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
/*     */   public static final String redstoneEffect = "-5+6-7";
/*     */   public static final String glowstoneEffect = "+5-6-7";
/*     */   public static final String gunpowderEffect = "+14&13-13";
/*     */   public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
/*     */   public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
/*     */   public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
/*  26 */   private static final Map<Integer, String> potionRequirements = Maps.newHashMap();
/*  27 */   private static final Map<Integer, String> potionAmplifiers = Maps.newHashMap();
/*  28 */   private static final Map<Integer, Integer> DATAVALUE_COLORS = Maps.newHashMap();
/*     */   
/*     */ 
/*  31 */   private static final String[] potionPrefixes = { "potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkFlag(int p_77914_0_, int p_77914_1_)
/*     */   {
/*  38 */     return (p_77914_0_ & 1 << p_77914_1_) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int isFlagSet(int p_77910_0_, int p_77910_1_)
/*     */   {
/*  46 */     return checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int isFlagUnset(int p_77916_0_, int p_77916_1_)
/*     */   {
/*  54 */     return checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getPotionPrefixIndex(int dataValue)
/*     */   {
/*  62 */     return func_77908_a(dataValue, 5, 4, 3, 2, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calcPotionLiquidColor(Collection<PotionEffect> p_77911_0_)
/*     */   {
/*  70 */     int i = 3694022;
/*     */     
/*  72 */     if ((p_77911_0_ != null) && (!p_77911_0_.isEmpty()))
/*     */     {
/*  74 */       float f = 0.0F;
/*  75 */       float f1 = 0.0F;
/*  76 */       float f2 = 0.0F;
/*  77 */       float f3 = 0.0F;
/*     */       
/*  79 */       for (PotionEffect potioneffect : p_77911_0_)
/*     */       {
/*  81 */         if (potioneffect.getIsShowParticles())
/*     */         {
/*  83 */           int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();
/*     */           
/*  85 */           for (int k = 0; k <= potioneffect.getAmplifier(); k++)
/*     */           {
/*  87 */             f += (j >> 16 & 0xFF) / 255.0F;
/*  88 */             f1 += (j >> 8 & 0xFF) / 255.0F;
/*  89 */             f2 += (j >> 0 & 0xFF) / 255.0F;
/*  90 */             f3 += 1.0F;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  95 */       if (f3 == 0.0F)
/*     */       {
/*  97 */         return 0;
/*     */       }
/*     */       
/*     */ 
/* 101 */       f = f / f3 * 255.0F;
/* 102 */       f1 = f1 / f3 * 255.0F;
/* 103 */       f2 = f2 / f3 * 255.0F;
/* 104 */       return (int)f << 16 | (int)f1 << 8 | (int)f2;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean getAreAmbient(Collection<PotionEffect> potionEffects)
/*     */   {
/* 118 */     for (PotionEffect potioneffect : potionEffects)
/*     */     {
/* 120 */       if (!potioneffect.getIsAmbient())
/*     */       {
/* 122 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 126 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getLiquidColor(int dataValue, boolean bypassCache)
/*     */   {
/* 134 */     Integer integer = IntegerCache.func_181756_a(dataValue);
/*     */     
/* 136 */     if (!bypassCache)
/*     */     {
/* 138 */       if (DATAVALUE_COLORS.containsKey(integer))
/*     */       {
/* 140 */         return ((Integer)DATAVALUE_COLORS.get(integer)).intValue();
/*     */       }
/*     */       
/*     */ 
/* 144 */       int i = calcPotionLiquidColor(getPotionEffects(integer.intValue(), false));
/* 145 */       DATAVALUE_COLORS.put(integer, Integer.valueOf(i));
/* 146 */       return i;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 151 */     return calcPotionLiquidColor(getPotionEffects(integer.intValue(), true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getPotionPrefix(int dataValue)
/*     */   {
/* 160 */     int i = getPotionPrefixIndex(dataValue);
/* 161 */     return potionPrefixes[i];
/*     */   }
/*     */   
/*     */   private static int func_77904_a(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_)
/*     */   {
/* 166 */     int i = 0;
/*     */     
/* 168 */     if (p_77904_0_)
/*     */     {
/* 170 */       i = isFlagUnset(p_77904_6_, p_77904_4_);
/*     */     }
/* 172 */     else if (p_77904_3_ != -1)
/*     */     {
/* 174 */       if ((p_77904_3_ == 0) && (countSetFlags(p_77904_6_) == p_77904_4_))
/*     */       {
/* 176 */         i = 1;
/*     */       }
/* 178 */       else if ((p_77904_3_ == 1) && (countSetFlags(p_77904_6_) > p_77904_4_))
/*     */       {
/* 180 */         i = 1;
/*     */       }
/* 182 */       else if ((p_77904_3_ == 2) && (countSetFlags(p_77904_6_) < p_77904_4_))
/*     */       {
/* 184 */         i = 1;
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 189 */       i = isFlagSet(p_77904_6_, p_77904_4_);
/*     */     }
/*     */     
/* 192 */     if (p_77904_1_)
/*     */     {
/* 194 */       i *= p_77904_5_;
/*     */     }
/*     */     
/* 197 */     if (p_77904_2_)
/*     */     {
/* 199 */       i *= -1;
/*     */     }
/*     */     
/* 202 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int countSetFlags(int p_77907_0_)
/*     */   {
/* 212 */     for (int i = 0; p_77907_0_ > 0; i++)
/*     */     {
/* 214 */       p_77907_0_ &= p_77907_0_ - 1;
/*     */     }
/*     */     
/* 217 */     return i;
/*     */   }
/*     */   
/*     */   private static int parsePotionEffects(String p_77912_0_, int p_77912_1_, int p_77912_2_, int p_77912_3_)
/*     */   {
/* 222 */     if ((p_77912_1_ < p_77912_0_.length()) && (p_77912_2_ >= 0) && (p_77912_1_ < p_77912_2_))
/*     */     {
/* 224 */       int i = p_77912_0_.indexOf('|', p_77912_1_);
/*     */       
/* 226 */       if ((i >= 0) && (i < p_77912_2_))
/*     */       {
/* 228 */         int l1 = parsePotionEffects(p_77912_0_, p_77912_1_, i - 1, p_77912_3_);
/*     */         
/* 230 */         if (l1 > 0)
/*     */         {
/* 232 */           return l1;
/*     */         }
/*     */         
/*     */ 
/* 236 */         int j2 = parsePotionEffects(p_77912_0_, i + 1, p_77912_2_, p_77912_3_);
/* 237 */         return j2 > 0 ? j2 : 0;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 242 */       int j = p_77912_0_.indexOf('&', p_77912_1_);
/*     */       
/* 244 */       if ((j >= 0) && (j < p_77912_2_))
/*     */       {
/* 246 */         int i2 = parsePotionEffects(p_77912_0_, p_77912_1_, j - 1, p_77912_3_);
/*     */         
/* 248 */         if (i2 <= 0)
/*     */         {
/* 250 */           return 0;
/*     */         }
/*     */         
/*     */ 
/* 254 */         int k2 = parsePotionEffects(p_77912_0_, j + 1, p_77912_2_, p_77912_3_);
/* 255 */         return i2 > k2 ? i2 : k2 <= 0 ? 0 : k2;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 260 */       boolean flag = false;
/* 261 */       boolean flag1 = false;
/* 262 */       boolean flag2 = false;
/* 263 */       boolean flag3 = false;
/* 264 */       boolean flag4 = false;
/* 265 */       int k = -1;
/* 266 */       int l = 0;
/* 267 */       int i1 = 0;
/* 268 */       int j1 = 0;
/*     */       
/* 270 */       for (int k1 = p_77912_1_; k1 < p_77912_2_; k1++)
/*     */       {
/* 272 */         char c0 = p_77912_0_.charAt(k1);
/*     */         
/* 274 */         if ((c0 >= '0') && (c0 <= '9'))
/*     */         {
/* 276 */           if (flag)
/*     */           {
/* 278 */             i1 = c0 - '0';
/* 279 */             flag1 = true;
/*     */           }
/*     */           else
/*     */           {
/* 283 */             l *= 10;
/* 284 */             l += c0 - '0';
/* 285 */             flag2 = true;
/*     */           }
/*     */         }
/* 288 */         else if (c0 == '*')
/*     */         {
/* 290 */           flag = true;
/*     */         }
/* 292 */         else if (c0 == '!')
/*     */         {
/* 294 */           if (flag2)
/*     */           {
/* 296 */             j1 += func_77904_a(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 297 */             flag3 = false;
/* 298 */             flag4 = false;
/* 299 */             flag = false;
/* 300 */             flag1 = false;
/* 301 */             flag2 = false;
/* 302 */             i1 = 0;
/* 303 */             l = 0;
/* 304 */             k = -1;
/*     */           }
/*     */           
/* 307 */           flag3 = true;
/*     */         }
/* 309 */         else if (c0 == '-')
/*     */         {
/* 311 */           if (flag2)
/*     */           {
/* 313 */             j1 += func_77904_a(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 314 */             flag3 = false;
/* 315 */             flag4 = false;
/* 316 */             flag = false;
/* 317 */             flag1 = false;
/* 318 */             flag2 = false;
/* 319 */             i1 = 0;
/* 320 */             l = 0;
/* 321 */             k = -1;
/*     */           }
/*     */           
/* 324 */           flag4 = true;
/*     */         }
/* 326 */         else if ((c0 != '=') && (c0 != '<') && (c0 != '>'))
/*     */         {
/* 328 */           if ((c0 == '+') && (flag2))
/*     */           {
/* 330 */             j1 += func_77904_a(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 331 */             flag3 = false;
/* 332 */             flag4 = false;
/* 333 */             flag = false;
/* 334 */             flag1 = false;
/* 335 */             flag2 = false;
/* 336 */             i1 = 0;
/* 337 */             l = 0;
/* 338 */             k = -1;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 343 */           if (flag2)
/*     */           {
/* 345 */             j1 += func_77904_a(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 346 */             flag3 = false;
/* 347 */             flag4 = false;
/* 348 */             flag = false;
/* 349 */             flag1 = false;
/* 350 */             flag2 = false;
/* 351 */             i1 = 0;
/* 352 */             l = 0;
/* 353 */             k = -1;
/*     */           }
/*     */           
/* 356 */           if (c0 == '=')
/*     */           {
/* 358 */             k = 0;
/*     */           }
/* 360 */           else if (c0 == '<')
/*     */           {
/* 362 */             k = 2;
/*     */           }
/* 364 */           else if (c0 == '>')
/*     */           {
/* 366 */             k = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 371 */       if (flag2)
/*     */       {
/* 373 */         j1 += func_77904_a(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/*     */       }
/*     */       
/* 376 */       return j1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 382 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static List<PotionEffect> getPotionEffects(int p_77917_0_, boolean p_77917_1_)
/*     */   {
/* 388 */     List<PotionEffect> list = null;
/*     */     Potion[] arrayOfPotion;
/* 390 */     int j = (arrayOfPotion = Potion.potionTypes).length; for (int i = 0; i < j; i++) { Potion potion = arrayOfPotion[i];
/*     */       
/* 392 */       if ((potion != null) && ((!potion.isUsable()) || (p_77917_1_)))
/*     */       {
/* 394 */         String s = (String)potionRequirements.get(Integer.valueOf(potion.getId()));
/*     */         
/* 396 */         if (s != null)
/*     */         {
/* 398 */           int i = parsePotionEffects(s, 0, s.length(), p_77917_0_);
/*     */           
/* 400 */           if (i > 0)
/*     */           {
/* 402 */             int j = 0;
/* 403 */             String s1 = (String)potionAmplifiers.get(Integer.valueOf(potion.getId()));
/*     */             
/* 405 */             if (s1 != null)
/*     */             {
/* 407 */               j = parsePotionEffects(s1, 0, s1.length(), p_77917_0_);
/*     */               
/* 409 */               if (j < 0)
/*     */               {
/* 411 */                 j = 0;
/*     */               }
/*     */             }
/*     */             
/* 415 */             if (potion.isInstant())
/*     */             {
/* 417 */               i = 1;
/*     */             }
/*     */             else
/*     */             {
/* 421 */               i = 1200 * (i * 3 + (i - 1) * 2);
/* 422 */               i >>= j;
/* 423 */               i = (int)Math.round(i * potion.getEffectiveness());
/*     */               
/* 425 */               if ((p_77917_0_ & 0x4000) != 0)
/*     */               {
/* 427 */                 i = (int)Math.round(i * 0.75D + 0.5D);
/*     */               }
/*     */             }
/*     */             
/* 431 */             if (list == null)
/*     */             {
/* 433 */               list = Lists.newArrayList();
/*     */             }
/*     */             
/* 436 */             PotionEffect potioneffect = new PotionEffect(potion.getId(), i, j);
/*     */             
/* 438 */             if ((p_77917_0_ & 0x4000) != 0)
/*     */             {
/* 440 */               potioneffect.setSplashPotion(true);
/*     */             }
/*     */             
/* 443 */             list.add(potioneffect);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 449 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int brewBitOperations(int p_77906_0_, int p_77906_1_, boolean p_77906_2_, boolean p_77906_3_, boolean p_77906_4_)
/*     */   {
/* 457 */     if (p_77906_4_)
/*     */     {
/* 459 */       if (!checkFlag(p_77906_0_, p_77906_1_))
/*     */       {
/* 461 */         return 0;
/*     */       }
/*     */     }
/* 464 */     else if (p_77906_2_)
/*     */     {
/* 466 */       p_77906_0_ &= (1 << p_77906_1_ ^ 0xFFFFFFFF);
/*     */     }
/* 468 */     else if (p_77906_3_)
/*     */     {
/* 470 */       if ((p_77906_0_ & 1 << p_77906_1_) == 0)
/*     */       {
/* 472 */         p_77906_0_ |= 1 << p_77906_1_;
/*     */       }
/*     */       else
/*     */       {
/* 476 */         p_77906_0_ &= (1 << p_77906_1_ ^ 0xFFFFFFFF);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 481 */       p_77906_0_ |= 1 << p_77906_1_;
/*     */     }
/*     */     
/* 484 */     return p_77906_0_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int applyIngredient(int p_77913_0_, String p_77913_1_)
/*     */   {
/* 492 */     int i = 0;
/* 493 */     int j = p_77913_1_.length();
/* 494 */     boolean flag = false;
/* 495 */     boolean flag1 = false;
/* 496 */     boolean flag2 = false;
/* 497 */     boolean flag3 = false;
/* 498 */     int k = 0;
/*     */     
/* 500 */     for (int l = i; l < j; l++)
/*     */     {
/* 502 */       char c0 = p_77913_1_.charAt(l);
/*     */       
/* 504 */       if ((c0 >= '0') && (c0 <= '9'))
/*     */       {
/* 506 */         k *= 10;
/* 507 */         k += c0 - '0';
/* 508 */         flag = true;
/*     */       }
/* 510 */       else if (c0 == '!')
/*     */       {
/* 512 */         if (flag)
/*     */         {
/* 514 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 515 */           flag3 = false;
/* 516 */           flag1 = false;
/* 517 */           flag2 = false;
/* 518 */           flag = false;
/* 519 */           k = 0;
/*     */         }
/*     */         
/* 522 */         flag1 = true;
/*     */       }
/* 524 */       else if (c0 == '-')
/*     */       {
/* 526 */         if (flag)
/*     */         {
/* 528 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 529 */           flag3 = false;
/* 530 */           flag1 = false;
/* 531 */           flag2 = false;
/* 532 */           flag = false;
/* 533 */           k = 0;
/*     */         }
/*     */         
/* 536 */         flag2 = true;
/*     */       }
/* 538 */       else if (c0 == '+')
/*     */       {
/* 540 */         if (flag)
/*     */         {
/* 542 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 543 */           flag3 = false;
/* 544 */           flag1 = false;
/* 545 */           flag2 = false;
/* 546 */           flag = false;
/* 547 */           k = 0;
/*     */         }
/*     */       }
/* 550 */       else if (c0 == '&')
/*     */       {
/* 552 */         if (flag)
/*     */         {
/* 554 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 555 */           flag3 = false;
/* 556 */           flag1 = false;
/* 557 */           flag2 = false;
/* 558 */           flag = false;
/* 559 */           k = 0;
/*     */         }
/*     */         
/* 562 */         flag3 = true;
/*     */       }
/*     */     }
/*     */     
/* 566 */     if (flag)
/*     */     {
/* 568 */       p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/*     */     }
/*     */     
/* 571 */     return p_77913_0_ & 0x7FFF;
/*     */   }
/*     */   
/*     */   public static int func_77908_a(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_)
/*     */   {
/* 576 */     return (checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 581 */     potionRequirements.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
/* 582 */     potionRequirements.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
/* 583 */     potionRequirements.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
/* 584 */     potionRequirements.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
/* 585 */     potionRequirements.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
/* 586 */     potionRequirements.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
/* 587 */     potionRequirements.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
/* 588 */     potionRequirements.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
/* 589 */     potionRequirements.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
/* 590 */     potionRequirements.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
/* 591 */     potionRequirements.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
/* 592 */     potionRequirements.put(Integer.valueOf(Potion.waterBreathing.getId()), "0 & !1 & 2 & 3 & 2+6");
/* 593 */     potionRequirements.put(Integer.valueOf(Potion.jump.getId()), "0 & 1 & !2 & 3 & 3+6");
/* 594 */     potionAmplifiers.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
/* 595 */     potionAmplifiers.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
/* 596 */     potionAmplifiers.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
/* 597 */     potionAmplifiers.put(Integer.valueOf(Potion.regeneration.getId()), "5");
/* 598 */     potionAmplifiers.put(Integer.valueOf(Potion.harm.getId()), "5");
/* 599 */     potionAmplifiers.put(Integer.valueOf(Potion.heal.getId()), "5");
/* 600 */     potionAmplifiers.put(Integer.valueOf(Potion.resistance.getId()), "5");
/* 601 */     potionAmplifiers.put(Integer.valueOf(Potion.poison.getId()), "5");
/* 602 */     potionAmplifiers.put(Integer.valueOf(Potion.jump.getId()), "5");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\potion\PotionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */