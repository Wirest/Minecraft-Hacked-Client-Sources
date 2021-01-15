/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ public class NoiseGeneratorImproved extends NoiseGenerator
/*     */ {
/*     */   private int[] permutations;
/*     */   public double xCoord;
/*     */   public double yCoord;
/*     */   public double zCoord;
/*  11 */   private static final double[] field_152381_e = { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
/*  12 */   private static final double[] field_152382_f = { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
/*  13 */   private static final double[] field_152383_g = { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
/*  14 */   private static final double[] field_152384_h = { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
/*  15 */   private static final double[] field_152385_i = { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
/*     */   
/*     */   public NoiseGeneratorImproved()
/*     */   {
/*  19 */     this(new Random());
/*     */   }
/*     */   
/*     */   public NoiseGeneratorImproved(Random p_i45469_1_)
/*     */   {
/*  24 */     this.permutations = new int['Ȁ'];
/*  25 */     this.xCoord = (p_i45469_1_.nextDouble() * 256.0D);
/*  26 */     this.yCoord = (p_i45469_1_.nextDouble() * 256.0D);
/*  27 */     this.zCoord = (p_i45469_1_.nextDouble() * 256.0D);
/*     */     
/*  29 */     for (int i = 0; i < 256; this.permutations[i] = (i++)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  34 */     for (int l = 0; l < 256; l++)
/*     */     {
/*  36 */       int j = p_i45469_1_.nextInt(256 - l) + l;
/*  37 */       int k = this.permutations[l];
/*  38 */       this.permutations[l] = this.permutations[j];
/*  39 */       this.permutations[j] = k;
/*  40 */       this.permutations[(l + 256)] = this.permutations[l];
/*     */     }
/*     */   }
/*     */   
/*     */   public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_)
/*     */   {
/*  46 */     return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
/*     */   }
/*     */   
/*     */   public final double func_76309_a(int p_76309_1_, double p_76309_2_, double p_76309_4_)
/*     */   {
/*  51 */     int i = p_76309_1_ & 0xF;
/*  52 */     return field_152384_h[i] * p_76309_2_ + field_152385_i[i] * p_76309_4_;
/*     */   }
/*     */   
/*     */   public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_)
/*     */   {
/*  57 */     int i = p_76310_1_ & 0xF;
/*  58 */     return field_152381_e[i] * p_76310_2_ + field_152382_f[i] * p_76310_4_ + field_152383_g[i] * p_76310_6_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populateNoiseArray(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_, int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_, double p_76308_17_)
/*     */   {
/*  67 */     if (p_76308_9_ == 1)
/*     */     {
/*  69 */       int i5 = 0;
/*  70 */       int j5 = 0;
/*  71 */       int j = 0;
/*  72 */       int k5 = 0;
/*  73 */       double d14 = 0.0D;
/*  74 */       double d15 = 0.0D;
/*  75 */       int l5 = 0;
/*  76 */       double d16 = 1.0D / p_76308_17_;
/*     */       
/*  78 */       for (int j2 = 0; j2 < p_76308_8_; j2++)
/*     */       {
/*  80 */         double d17 = p_76308_2_ + j2 * p_76308_11_ + this.xCoord;
/*  81 */         int i6 = (int)d17;
/*     */         
/*  83 */         if (d17 < i6)
/*     */         {
/*  85 */           i6--;
/*     */         }
/*     */         
/*  88 */         int k2 = i6 & 0xFF;
/*  89 */         d17 -= i6;
/*  90 */         double d18 = d17 * d17 * d17 * (d17 * (d17 * 6.0D - 15.0D) + 10.0D);
/*     */         
/*  92 */         for (int j6 = 0; j6 < p_76308_10_; j6++)
/*     */         {
/*  94 */           double d19 = p_76308_6_ + j6 * p_76308_15_ + this.zCoord;
/*  95 */           int k6 = (int)d19;
/*     */           
/*  97 */           if (d19 < k6)
/*     */           {
/*  99 */             k6--;
/*     */           }
/*     */           
/* 102 */           int l6 = k6 & 0xFF;
/* 103 */           d19 -= k6;
/* 104 */           double d20 = d19 * d19 * d19 * (d19 * (d19 * 6.0D - 15.0D) + 10.0D);
/* 105 */           i5 = this.permutations[k2] + 0;
/* 106 */           j5 = this.permutations[i5] + l6;
/* 107 */           j = this.permutations[(k2 + 1)] + 0;
/* 108 */           k5 = this.permutations[j] + l6;
/* 109 */           d14 = lerp(d18, func_76309_a(this.permutations[j5], d17, d19), grad(this.permutations[k5], d17 - 1.0D, 0.0D, d19));
/* 110 */           d15 = lerp(d18, grad(this.permutations[(j5 + 1)], d17, 0.0D, d19 - 1.0D), grad(this.permutations[(k5 + 1)], d17 - 1.0D, 0.0D, d19 - 1.0D));
/* 111 */           double d21 = lerp(d20, d14, d15);
/* 112 */           int i7 = l5++;
/* 113 */           p_76308_1_[i7] += d21 * d16;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 119 */       int i = 0;
/* 120 */       double d0 = 1.0D / p_76308_17_;
/* 121 */       int k = -1;
/* 122 */       int l = 0;
/* 123 */       int i1 = 0;
/* 124 */       int j1 = 0;
/* 125 */       int k1 = 0;
/* 126 */       int l1 = 0;
/* 127 */       int i2 = 0;
/* 128 */       double d1 = 0.0D;
/* 129 */       double d2 = 0.0D;
/* 130 */       double d3 = 0.0D;
/* 131 */       double d4 = 0.0D;
/*     */       
/* 133 */       for (int l2 = 0; l2 < p_76308_8_; l2++)
/*     */       {
/* 135 */         double d5 = p_76308_2_ + l2 * p_76308_11_ + this.xCoord;
/* 136 */         int i3 = (int)d5;
/*     */         
/* 138 */         if (d5 < i3)
/*     */         {
/* 140 */           i3--;
/*     */         }
/*     */         
/* 143 */         int j3 = i3 & 0xFF;
/* 144 */         d5 -= i3;
/* 145 */         double d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);
/*     */         
/* 147 */         for (int k3 = 0; k3 < p_76308_10_; k3++)
/*     */         {
/* 149 */           double d7 = p_76308_6_ + k3 * p_76308_15_ + this.zCoord;
/* 150 */           int l3 = (int)d7;
/*     */           
/* 152 */           if (d7 < l3)
/*     */           {
/* 154 */             l3--;
/*     */           }
/*     */           
/* 157 */           int i4 = l3 & 0xFF;
/* 158 */           d7 -= l3;
/* 159 */           double d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);
/*     */           
/* 161 */           for (int j4 = 0; j4 < p_76308_9_; j4++)
/*     */           {
/* 163 */             double d9 = p_76308_4_ + j4 * p_76308_13_ + this.yCoord;
/* 164 */             int k4 = (int)d9;
/*     */             
/* 166 */             if (d9 < k4)
/*     */             {
/* 168 */               k4--;
/*     */             }
/*     */             
/* 171 */             int l4 = k4 & 0xFF;
/* 172 */             d9 -= k4;
/* 173 */             double d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
/*     */             
/* 175 */             if ((j4 == 0) || (l4 != k))
/*     */             {
/* 177 */               k = l4;
/* 178 */               l = this.permutations[j3] + l4;
/* 179 */               i1 = this.permutations[l] + i4;
/* 180 */               j1 = this.permutations[(l + 1)] + i4;
/* 181 */               k1 = this.permutations[(j3 + 1)] + l4;
/* 182 */               l1 = this.permutations[k1] + i4;
/* 183 */               i2 = this.permutations[(k1 + 1)] + i4;
/* 184 */               d1 = lerp(d6, grad(this.permutations[i1], d5, d9, d7), grad(this.permutations[l1], d5 - 1.0D, d9, d7));
/* 185 */               d2 = lerp(d6, grad(this.permutations[j1], d5, d9 - 1.0D, d7), grad(this.permutations[i2], d5 - 1.0D, d9 - 1.0D, d7));
/* 186 */               d3 = lerp(d6, grad(this.permutations[(i1 + 1)], d5, d9, d7 - 1.0D), grad(this.permutations[(l1 + 1)], d5 - 1.0D, d9, d7 - 1.0D));
/* 187 */               d4 = lerp(d6, grad(this.permutations[(j1 + 1)], d5, d9 - 1.0D, d7 - 1.0D), grad(this.permutations[(i2 + 1)], d5 - 1.0D, d9 - 1.0D, d7 - 1.0D));
/*     */             }
/*     */             
/* 190 */             double d11 = lerp(d10, d1, d2);
/* 191 */             double d12 = lerp(d10, d3, d4);
/* 192 */             double d13 = lerp(d8, d11, d12);
/* 193 */             int j7 = i++;
/* 194 */             p_76308_1_[j7] += d13 * d0;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\NoiseGeneratorImproved.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */