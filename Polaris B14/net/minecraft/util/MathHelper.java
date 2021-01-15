/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MathHelper
/*     */ {
/*  10 */   public static final float SQRT_2 = sqrt_float(2.0F);
/*     */   private static final int SIN_BITS = 12;
/*     */   private static final int SIN_MASK = 4095;
/*     */   private static final int SIN_COUNT = 4096;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float PI2 = 6.2831855F;
/*     */   public static final float PId2 = 1.5707964F;
/*     */   private static final float radFull = 6.2831855F;
/*     */   private static final float degFull = 360.0F;
/*     */   private static final float radToIndex = 651.8986F;
/*     */   private static final float degToIndex = 11.377778F;
/*     */   public static final float deg2Rad = 0.017453292F;
/*  22 */   private static final float[] SIN_TABLE_FAST = new float['က'];
/*  23 */   public static boolean fastMath = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  28 */   private static final float[] SIN_TABLE = new float[65536];
/*     */   
/*     */ 
/*     */   private static final int[] multiplyDeBruijnBitPosition;
/*     */   
/*     */ 
/*     */   private static final double field_181163_d;
/*     */   
/*     */   private static final double[] field_181164_e;
/*     */   
/*     */   private static final double[] field_181165_f;
/*     */   
/*     */   private static final String __OBFID = "CL_00001496";
/*     */   
/*  42 */   private static Random random = new Random();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float sin(float p_76126_0_)
/*     */   {
/*  49 */     return fastMath ? SIN_TABLE_FAST[((int)(p_76126_0_ * 651.8986F) & 0xFFF)] : SIN_TABLE[((int)(p_76126_0_ * 10430.378F) & 0xFFFF)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float cos(float value)
/*     */   {
/*  57 */     return fastMath ? SIN_TABLE_FAST[((int)((value + 1.5707964F) * 651.8986F) & 0xFFF)] : SIN_TABLE[((int)(value * 10430.378F + 16384.0F) & 0xFFFF)];
/*     */   }
/*     */   
/*     */   public static float sqrt_float(float value)
/*     */   {
/*  62 */     return (float)Math.sqrt(value);
/*     */   }
/*     */   
/*     */   public static float sqrt_double(double value)
/*     */   {
/*  67 */     return (float)Math.sqrt(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int floor_float(float value)
/*     */   {
/*  75 */     int i = (int)value;
/*  76 */     return value < i ? i - 1 : i;
/*     */   }
/*     */   
/*     */   public static double getRandom(double min, double max) {
/*  80 */     return clamp_double(min + random.nextDouble() * max, min, max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int truncateDoubleToInt(double value)
/*     */   {
/*  88 */     return (int)(value + 1024.0D) - 1024;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int floor_double(double value)
/*     */   {
/*  96 */     int i = (int)value;
/*  97 */     return value < i ? i - 1 : i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long floor_double_long(double value)
/*     */   {
/* 105 */     long i = value;
/* 106 */     return value < i ? i - 1L : i;
/*     */   }
/*     */   
/*     */   public static int func_154353_e(double value)
/*     */   {
/* 111 */     return (int)(value >= 0.0D ? value : -value + 1.0D);
/*     */   }
/*     */   
/*     */   public static float abs(float value)
/*     */   {
/* 116 */     return value >= 0.0F ? value : -value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int abs_int(int value)
/*     */   {
/* 124 */     return value >= 0 ? value : -value;
/*     */   }
/*     */   
/*     */   public static int ceiling_float_int(float value)
/*     */   {
/* 129 */     int i = (int)value;
/* 130 */     return value > i ? i + 1 : i;
/*     */   }
/*     */   
/*     */   public static int ceiling_double_int(double value)
/*     */   {
/* 135 */     int i = (int)value;
/* 136 */     return value > i ? i + 1 : i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int clamp_int(int num, int min, int max)
/*     */   {
/* 145 */     return num > max ? max : num < min ? min : num;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float clamp_float(float num, float min, float max)
/*     */   {
/* 154 */     return num > max ? max : num < min ? min : num;
/*     */   }
/*     */   
/*     */   public static double clamp_double(double num, double min, double max)
/*     */   {
/* 159 */     return num > max ? max : num < min ? min : num;
/*     */   }
/*     */   
/*     */   public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_)
/*     */   {
/* 164 */     return p_151238_4_ > 1.0D ? p_151238_2_ : p_151238_4_ < 0.0D ? p_151238_0_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double abs_max(double p_76132_0_, double p_76132_2_)
/*     */   {
/* 172 */     if (p_76132_0_ < 0.0D)
/*     */     {
/* 174 */       p_76132_0_ = -p_76132_0_;
/*     */     }
/*     */     
/* 177 */     if (p_76132_2_ < 0.0D)
/*     */     {
/* 179 */       p_76132_2_ = -p_76132_2_;
/*     */     }
/*     */     
/* 182 */     return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int bucketInt(int p_76137_0_, int p_76137_1_)
/*     */   {
/* 190 */     return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
/*     */   }
/*     */   
/*     */   public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_)
/*     */   {
/* 195 */     return p_76136_1_ >= p_76136_2_ ? p_76136_1_ : p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
/*     */   }
/*     */   
/*     */   public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_)
/*     */   {
/* 200 */     return p_151240_1_ >= p_151240_2_ ? p_151240_1_ : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
/*     */   }
/*     */   
/*     */   public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_)
/*     */   {
/* 205 */     return p_82716_1_ >= p_82716_3_ ? p_82716_1_ : p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
/*     */   }
/*     */   
/*     */   public static double average(long[] values)
/*     */   {
/* 210 */     long i = 0L;
/*     */     
/* 212 */     long[] arrayOfLong = values;int j = values.length; for (int i = 0; i < j; i++) { long j = arrayOfLong[i];
/*     */       
/* 214 */       i += j;
/*     */     }
/*     */     
/* 217 */     return i / values.length;
/*     */   }
/*     */   
/*     */   public static boolean epsilonEquals(float p_180185_0_, float p_180185_1_)
/*     */   {
/* 222 */     return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
/*     */   }
/*     */   
/*     */   public static int normalizeAngle(int p_180184_0_, int p_180184_1_)
/*     */   {
/* 227 */     return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float wrapAngleTo180_float(float value)
/*     */   {
/* 235 */     value %= 360.0F;
/*     */     
/* 237 */     if (value >= 180.0F)
/*     */     {
/* 239 */       value -= 360.0F;
/*     */     }
/*     */     
/* 242 */     if (value < -180.0F)
/*     */     {
/* 244 */       value += 360.0F;
/*     */     }
/*     */     
/* 247 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double wrapAngleTo180_double(double value)
/*     */   {
/* 255 */     value %= 360.0D;
/*     */     
/* 257 */     if (value >= 180.0D)
/*     */     {
/* 259 */       value -= 360.0D;
/*     */     }
/*     */     
/* 262 */     if (value < -180.0D)
/*     */     {
/* 264 */       value += 360.0D;
/*     */     }
/*     */     
/* 267 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int parseIntWithDefault(String p_82715_0_, int p_82715_1_)
/*     */   {
/*     */     try
/*     */     {
/* 277 */       return Integer.parseInt(p_82715_0_);
/*     */     }
/*     */     catch (Throwable var3) {}
/*     */     
/* 281 */     return p_82715_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_)
/*     */   {
/* 290 */     return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_)
/*     */   {
/*     */     try
/*     */     {
/* 300 */       return Double.parseDouble(p_82712_0_);
/*     */     }
/*     */     catch (Throwable var4) {}
/*     */     
/* 304 */     return p_82712_1_;
/*     */   }
/*     */   
/*     */ 
/*     */   public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_)
/*     */   {
/* 310 */     return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int roundUpToPowerOfTwo(int value)
/*     */   {
/* 318 */     int i = value - 1;
/* 319 */     i |= i >> 1;
/* 320 */     i |= i >> 2;
/* 321 */     i |= i >> 4;
/* 322 */     i |= i >> 8;
/* 323 */     i |= i >> 16;
/* 324 */     return i + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isPowerOfTwo(int value)
/*     */   {
/* 332 */     return (value != 0) && ((value & value - 1) == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int calculateLogBaseTwoDeBruijn(int value)
/*     */   {
/* 342 */     value = isPowerOfTwo(value) ? value : roundUpToPowerOfTwo(value);
/* 343 */     return multiplyDeBruijnBitPosition[((int)(value * 125613361L >> 27) & 0x1F)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateLogBaseTwo(int value)
/*     */   {
/* 352 */     return calculateLogBaseTwoDeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
/*     */   }
/*     */   
/*     */   public static int func_154354_b(int p_154354_0_, int p_154354_1_)
/*     */   {
/* 357 */     if (p_154354_1_ == 0)
/*     */     {
/* 359 */       return 0;
/*     */     }
/* 361 */     if (p_154354_0_ == 0)
/*     */     {
/* 363 */       return p_154354_1_;
/*     */     }
/*     */     
/*     */ 
/* 367 */     if (p_154354_0_ < 0)
/*     */     {
/* 369 */       p_154354_1_ *= -1;
/*     */     }
/*     */     
/* 372 */     int i = p_154354_0_ % p_154354_1_;
/* 373 */     return i == 0 ? p_154354_0_ : p_154354_0_ + p_154354_1_ - i;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int func_180183_b(float p_180183_0_, float p_180183_1_, float p_180183_2_)
/*     */   {
/* 379 */     return func_180181_b(floor_float(p_180183_0_ * 255.0F), floor_float(p_180183_1_ * 255.0F), floor_float(p_180183_2_ * 255.0F));
/*     */   }
/*     */   
/*     */   public static int func_180181_b(int p_180181_0_, int p_180181_1_, int p_180181_2_)
/*     */   {
/* 384 */     int i = (p_180181_0_ << 8) + p_180181_1_;
/* 385 */     i = (i << 8) + p_180181_2_;
/* 386 */     return i;
/*     */   }
/*     */   
/*     */   public static int func_180188_d(int p_180188_0_, int p_180188_1_)
/*     */   {
/* 391 */     int i = (p_180188_0_ & 0xFF0000) >> 16;
/* 392 */     int j = (p_180188_1_ & 0xFF0000) >> 16;
/* 393 */     int k = (p_180188_0_ & 0xFF00) >> 8;
/* 394 */     int l = (p_180188_1_ & 0xFF00) >> 8;
/* 395 */     int i1 = (p_180188_0_ & 0xFF) >> 0;
/* 396 */     int j1 = (p_180188_1_ & 0xFF) >> 0;
/* 397 */     int k1 = (int)(i * j / 255.0F);
/* 398 */     int l1 = (int)(k * l / 255.0F);
/* 399 */     int i2 = (int)(i1 * j1 / 255.0F);
/* 400 */     return p_180188_0_ & 0xFF000000 | k1 << 16 | l1 << 8 | i2;
/*     */   }
/*     */   
/*     */   public static double func_181162_h(double p_181162_0_)
/*     */   {
/* 405 */     return p_181162_0_ - Math.floor(p_181162_0_);
/*     */   }
/*     */   
/*     */   public static long getPositionRandom(Vec3i pos)
/*     */   {
/* 410 */     return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public static long getCoordinateRandom(int x, int y, int z)
/*     */   {
/* 415 */     long i = x * 3129871 ^ z * 116129781L ^ y;
/* 416 */     i = i * i * 42317861L + i * 11L;
/* 417 */     return i;
/*     */   }
/*     */   
/*     */   public static UUID getRandomUuid(Random rand)
/*     */   {
/* 422 */     long i = rand.nextLong() & 0xFFFFFFFFFFFF0FFF | 0x4000;
/* 423 */     long j = rand.nextLong() & 0x3FFFFFFFFFFFFFFF | 0x8000000000000000;
/* 424 */     return new UUID(i, j);
/*     */   }
/*     */   
/*     */   public static double func_181160_c(double p_181160_0_, double p_181160_2_, double p_181160_4_)
/*     */   {
/* 429 */     return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
/*     */   }
/*     */   
/*     */   public static double func_181159_b(double p_181159_0_, double p_181159_2_)
/*     */   {
/* 434 */     double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
/*     */     
/* 436 */     if (Double.isNaN(d0))
/*     */     {
/* 438 */       return NaN.0D;
/*     */     }
/*     */     
/*     */ 
/* 442 */     boolean flag = p_181159_0_ < 0.0D;
/*     */     
/* 444 */     if (flag)
/*     */     {
/* 446 */       p_181159_0_ = -p_181159_0_;
/*     */     }
/*     */     
/* 449 */     boolean flag1 = p_181159_2_ < 0.0D;
/*     */     
/* 451 */     if (flag1)
/*     */     {
/* 453 */       p_181159_2_ = -p_181159_2_;
/*     */     }
/*     */     
/* 456 */     boolean flag2 = p_181159_0_ > p_181159_2_;
/*     */     
/* 458 */     if (flag2)
/*     */     {
/* 460 */       double d1 = p_181159_2_;
/* 461 */       p_181159_2_ = p_181159_0_;
/* 462 */       p_181159_0_ = d1;
/*     */     }
/*     */     
/* 465 */     double d9 = func_181161_i(d0);
/* 466 */     p_181159_2_ *= d9;
/* 467 */     p_181159_0_ *= d9;
/* 468 */     double d2 = field_181163_d + p_181159_0_;
/* 469 */     int i = (int)Double.doubleToRawLongBits(d2);
/* 470 */     double d3 = field_181164_e[i];
/* 471 */     double d4 = field_181165_f[i];
/* 472 */     double d5 = d2 - field_181163_d;
/* 473 */     double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
/* 474 */     double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
/* 475 */     double d8 = d3 + d7;
/*     */     
/* 477 */     if (flag2)
/*     */     {
/* 479 */       d8 = 1.5707963267948966D - d8;
/*     */     }
/*     */     
/* 482 */     if (flag1)
/*     */     {
/* 484 */       d8 = 3.141592653589793D - d8;
/*     */     }
/*     */     
/* 487 */     if (flag)
/*     */     {
/* 489 */       d8 = -d8;
/*     */     }
/*     */     
/* 492 */     return d8;
/*     */   }
/*     */   
/*     */ 
/*     */   public static double func_181161_i(double p_181161_0_)
/*     */   {
/* 498 */     double d0 = 0.5D * p_181161_0_;
/* 499 */     long i = Double.doubleToRawLongBits(p_181161_0_);
/* 500 */     i = 6910469410427058090L - (i >> 1);
/* 501 */     p_181161_0_ = Double.longBitsToDouble(i);
/* 502 */     p_181161_0_ *= (1.5D - d0 * p_181161_0_ * p_181161_0_);
/* 503 */     return p_181161_0_;
/*     */   }
/*     */   
/*     */   public static int func_181758_c(float p_181758_0_, float p_181758_1_, float p_181758_2_)
/*     */   {
/* 508 */     int i = (int)(p_181758_0_ * 6.0F) % 6;
/* 509 */     float f = p_181758_0_ * 6.0F - i;
/* 510 */     float f1 = p_181758_2_ * (1.0F - p_181758_1_);
/* 511 */     float f2 = p_181758_2_ * (1.0F - f * p_181758_1_);
/* 512 */     float f3 = p_181758_2_ * (1.0F - (1.0F - f) * p_181758_1_);
/*     */     float f6;
/*     */     float f6;
/*     */     float f6;
/*     */     float f6;
/* 517 */     float f6; float f6; switch (i)
/*     */     {
/*     */     case 0: 
/* 520 */       float f4 = p_181758_2_;
/* 521 */       float f5 = f3;
/* 522 */       f6 = f1;
/* 523 */       break;
/*     */     
/*     */     case 1: 
/* 526 */       float f4 = f2;
/* 527 */       float f5 = p_181758_2_;
/* 528 */       f6 = f1;
/* 529 */       break;
/*     */     
/*     */     case 2: 
/* 532 */       float f4 = f1;
/* 533 */       float f5 = p_181758_2_;
/* 534 */       f6 = f3;
/* 535 */       break;
/*     */     
/*     */     case 3: 
/* 538 */       float f4 = f1;
/* 539 */       float f5 = f2;
/* 540 */       f6 = p_181758_2_;
/* 541 */       break;
/*     */     
/*     */     case 4: 
/* 544 */       float f4 = f3;
/* 545 */       float f5 = f1;
/* 546 */       f6 = p_181758_2_;
/* 547 */       break;
/*     */     
/*     */     case 5: 
/* 550 */       float f4 = p_181758_2_;
/* 551 */       float f5 = f1;
/* 552 */       f6 = f2;
/* 553 */       break;
/*     */     
/*     */     default: 
/* 556 */       throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + p_181758_0_ + ", " + p_181758_1_ + ", " + p_181758_2_); }
/*     */     float f6;
/*     */     float f5;
/* 559 */     float f4; int j = clamp_int((int)(f4 * 255.0F), 0, 255);
/* 560 */     int k = clamp_int((int)(f5 * 255.0F), 0, 255);
/* 561 */     int l = clamp_int((int)(f6 * 255.0F), 0, 255);
/* 562 */     return j << 16 | k << 8 | l;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 567 */     for (int i = 0; i < 65536; i++)
/*     */     {
/* 569 */       SIN_TABLE[i] = ((float)Math.sin(i * 3.141592653589793D * 2.0D / 65536.0D));
/*     */     }
/*     */     
/* 572 */     for (int j = 0; j < 4096; j++)
/*     */     {
/* 574 */       SIN_TABLE_FAST[j] = ((float)Math.sin((j + 0.5F) / 4096.0F * 6.2831855F));
/*     */     }
/*     */     
/* 577 */     for (int l = 0; l < 360; l += 90)
/*     */     {
/* 579 */       SIN_TABLE_FAST[((int)(l * 11.377778F) & 0xFFF)] = ((float)Math.sin(l * 0.017453292F));
/*     */     }
/*     */     
/* 582 */     multiplyDeBruijnBitPosition = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
/* 583 */     field_181163_d = Double.longBitsToDouble(4805340802404319232L);
/* 584 */     field_181164_e = new double['ā'];
/* 585 */     field_181165_f = new double['ā'];
/*     */     
/* 587 */     for (int k = 0; k < 257; k++)
/*     */     {
/* 589 */       double d1 = k / 256.0D;
/* 590 */       double d0 = Math.asin(d1);
/* 591 */       field_181165_f[k] = Math.cos(d0);
/* 592 */       field_181164_e[k] = d0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MathHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */