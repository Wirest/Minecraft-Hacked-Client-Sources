/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ public class NoiseGeneratorSimplex
/*     */ {
/*   7 */   private static int[][] field_151611_e = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
/*   8 */   public static final double field_151614_a = Math.sqrt(3.0D);
/*     */   private int[] field_151608_f;
/*     */   public double field_151612_b;
/*     */   public double field_151613_c;
/*     */   public double field_151610_d;
/*  13 */   private static final double field_151609_g = 0.5D * (field_151614_a - 1.0D);
/*  14 */   private static final double field_151615_h = (3.0D - field_151614_a) / 6.0D;
/*     */   
/*     */   public NoiseGeneratorSimplex()
/*     */   {
/*  18 */     this(new Random());
/*     */   }
/*     */   
/*     */   public NoiseGeneratorSimplex(Random p_i45471_1_)
/*     */   {
/*  23 */     this.field_151608_f = new int['Ȁ'];
/*  24 */     this.field_151612_b = (p_i45471_1_.nextDouble() * 256.0D);
/*  25 */     this.field_151613_c = (p_i45471_1_.nextDouble() * 256.0D);
/*  26 */     this.field_151610_d = (p_i45471_1_.nextDouble() * 256.0D);
/*     */     
/*  28 */     for (int i = 0; i < 256; this.field_151608_f[i] = (i++)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  33 */     for (int l = 0; l < 256; l++)
/*     */     {
/*  35 */       int j = p_i45471_1_.nextInt(256 - l) + l;
/*  36 */       int k = this.field_151608_f[l];
/*  37 */       this.field_151608_f[l] = this.field_151608_f[j];
/*  38 */       this.field_151608_f[j] = k;
/*  39 */       this.field_151608_f[(l + 256)] = this.field_151608_f[l];
/*     */     }
/*     */   }
/*     */   
/*     */   private static int func_151607_a(double p_151607_0_)
/*     */   {
/*  45 */     return p_151607_0_ > 0.0D ? (int)p_151607_0_ : (int)p_151607_0_ - 1;
/*     */   }
/*     */   
/*     */   private static double func_151604_a(int[] p_151604_0_, double p_151604_1_, double p_151604_3_)
/*     */   {
/*  50 */     return p_151604_0_[0] * p_151604_1_ + p_151604_0_[1] * p_151604_3_;
/*     */   }
/*     */   
/*     */   public double func_151605_a(double p_151605_1_, double p_151605_3_)
/*     */   {
/*  55 */     double d3 = 0.5D * (field_151614_a - 1.0D);
/*  56 */     double d4 = (p_151605_1_ + p_151605_3_) * d3;
/*  57 */     int i = func_151607_a(p_151605_1_ + d4);
/*  58 */     int j = func_151607_a(p_151605_3_ + d4);
/*  59 */     double d5 = (3.0D - field_151614_a) / 6.0D;
/*  60 */     double d6 = (i + j) * d5;
/*  61 */     double d7 = i - d6;
/*  62 */     double d8 = j - d6;
/*  63 */     double d9 = p_151605_1_ - d7;
/*  64 */     double d10 = p_151605_3_ - d8;
/*     */     int l;
/*     */     int k;
/*     */     int l;
/*  68 */     if (d9 > d10)
/*     */     {
/*  70 */       int k = 1;
/*  71 */       l = 0;
/*     */     }
/*     */     else
/*     */     {
/*  75 */       k = 0;
/*  76 */       l = 1;
/*     */     }
/*     */     
/*  79 */     double d11 = d9 - k + d5;
/*  80 */     double d12 = d10 - l + d5;
/*  81 */     double d13 = d9 - 1.0D + 2.0D * d5;
/*  82 */     double d14 = d10 - 1.0D + 2.0D * d5;
/*  83 */     int i1 = i & 0xFF;
/*  84 */     int j1 = j & 0xFF;
/*  85 */     int k1 = this.field_151608_f[(i1 + this.field_151608_f[j1])] % 12;
/*  86 */     int l1 = this.field_151608_f[(i1 + k + this.field_151608_f[(j1 + l)])] % 12;
/*  87 */     int i2 = this.field_151608_f[(i1 + 1 + this.field_151608_f[(j1 + 1)])] % 12;
/*  88 */     double d15 = 0.5D - d9 * d9 - d10 * d10;
/*     */     double d0;
/*     */     double d0;
/*  91 */     if (d15 < 0.0D)
/*     */     {
/*  93 */       d0 = 0.0D;
/*     */     }
/*     */     else
/*     */     {
/*  97 */       d15 *= d15;
/*  98 */       d0 = d15 * d15 * func_151604_a(field_151611_e[k1], d9, d10);
/*     */     }
/*     */     
/* 101 */     double d16 = 0.5D - d11 * d11 - d12 * d12;
/*     */     double d1;
/*     */     double d1;
/* 104 */     if (d16 < 0.0D)
/*     */     {
/* 106 */       d1 = 0.0D;
/*     */     }
/*     */     else
/*     */     {
/* 110 */       d16 *= d16;
/* 111 */       d1 = d16 * d16 * func_151604_a(field_151611_e[l1], d11, d12);
/*     */     }
/*     */     
/* 114 */     double d17 = 0.5D - d13 * d13 - d14 * d14;
/*     */     double d2;
/*     */     double d2;
/* 117 */     if (d17 < 0.0D)
/*     */     {
/* 119 */       d2 = 0.0D;
/*     */     }
/*     */     else
/*     */     {
/* 123 */       d17 *= d17;
/* 124 */       d2 = d17 * d17 * func_151604_a(field_151611_e[i2], d13, d14);
/*     */     }
/*     */     
/* 127 */     return 70.0D * (d0 + d1 + d2);
/*     */   }
/*     */   
/*     */   public void func_151606_a(double[] p_151606_1_, double p_151606_2_, double p_151606_4_, int p_151606_6_, int p_151606_7_, double p_151606_8_, double p_151606_10_, double p_151606_12_)
/*     */   {
/* 132 */     int i = 0;
/*     */     
/* 134 */     for (int j = 0; j < p_151606_7_; j++)
/*     */     {
/* 136 */       double d0 = (p_151606_4_ + j) * p_151606_10_ + this.field_151613_c;
/*     */       
/* 138 */       for (int k = 0; k < p_151606_6_; k++)
/*     */       {
/* 140 */         double d1 = (p_151606_2_ + k) * p_151606_8_ + this.field_151612_b;
/* 141 */         double d5 = (d1 + d0) * field_151609_g;
/* 142 */         int l = func_151607_a(d1 + d5);
/* 143 */         int i1 = func_151607_a(d0 + d5);
/* 144 */         double d6 = (l + i1) * field_151615_h;
/* 145 */         double d7 = l - d6;
/* 146 */         double d8 = i1 - d6;
/* 147 */         double d9 = d1 - d7;
/* 148 */         double d10 = d0 - d8;
/*     */         int k1;
/*     */         int j1;
/*     */         int k1;
/* 152 */         if (d9 > d10)
/*     */         {
/* 154 */           int j1 = 1;
/* 155 */           k1 = 0;
/*     */         }
/*     */         else
/*     */         {
/* 159 */           j1 = 0;
/* 160 */           k1 = 1;
/*     */         }
/*     */         
/* 163 */         double d11 = d9 - j1 + field_151615_h;
/* 164 */         double d12 = d10 - k1 + field_151615_h;
/* 165 */         double d13 = d9 - 1.0D + 2.0D * field_151615_h;
/* 166 */         double d14 = d10 - 1.0D + 2.0D * field_151615_h;
/* 167 */         int l1 = l & 0xFF;
/* 168 */         int i2 = i1 & 0xFF;
/* 169 */         int j2 = this.field_151608_f[(l1 + this.field_151608_f[i2])] % 12;
/* 170 */         int k2 = this.field_151608_f[(l1 + j1 + this.field_151608_f[(i2 + k1)])] % 12;
/* 171 */         int l2 = this.field_151608_f[(l1 + 1 + this.field_151608_f[(i2 + 1)])] % 12;
/* 172 */         double d15 = 0.5D - d9 * d9 - d10 * d10;
/*     */         double d2;
/*     */         double d2;
/* 175 */         if (d15 < 0.0D)
/*     */         {
/* 177 */           d2 = 0.0D;
/*     */         }
/*     */         else
/*     */         {
/* 181 */           d15 *= d15;
/* 182 */           d2 = d15 * d15 * func_151604_a(field_151611_e[j2], d9, d10);
/*     */         }
/*     */         
/* 185 */         double d16 = 0.5D - d11 * d11 - d12 * d12;
/*     */         double d3;
/*     */         double d3;
/* 188 */         if (d16 < 0.0D)
/*     */         {
/* 190 */           d3 = 0.0D;
/*     */         }
/*     */         else
/*     */         {
/* 194 */           d16 *= d16;
/* 195 */           d3 = d16 * d16 * func_151604_a(field_151611_e[k2], d11, d12);
/*     */         }
/*     */         
/* 198 */         double d17 = 0.5D - d13 * d13 - d14 * d14;
/*     */         double d4;
/*     */         double d4;
/* 201 */         if (d17 < 0.0D)
/*     */         {
/* 203 */           d4 = 0.0D;
/*     */         }
/*     */         else
/*     */         {
/* 207 */           d17 *= d17;
/* 208 */           d4 = d17 * d17 * func_151604_a(field_151611_e[l2], d13, d14);
/*     */         }
/*     */         
/* 211 */         int i3 = i++;
/* 212 */         p_151606_1_[i3] += 70.0D * (d2 + d3 + d4) * p_151606_12_;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\NoiseGeneratorSimplex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */