/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class NoiseGeneratorPerlin extends NoiseGenerator
/*    */ {
/*    */   private NoiseGeneratorSimplex[] field_151603_a;
/*    */   private int field_151602_b;
/*    */   
/*    */   public NoiseGeneratorPerlin(Random p_i45470_1_, int p_i45470_2_)
/*    */   {
/* 12 */     this.field_151602_b = p_i45470_2_;
/* 13 */     this.field_151603_a = new NoiseGeneratorSimplex[p_i45470_2_];
/*    */     
/* 15 */     for (int i = 0; i < p_i45470_2_; i++)
/*    */     {
/* 17 */       this.field_151603_a[i] = new NoiseGeneratorSimplex(p_i45470_1_);
/*    */     }
/*    */   }
/*    */   
/*    */   public double func_151601_a(double p_151601_1_, double p_151601_3_)
/*    */   {
/* 23 */     double d0 = 0.0D;
/* 24 */     double d1 = 1.0D;
/*    */     
/* 26 */     for (int i = 0; i < this.field_151602_b; i++)
/*    */     {
/* 28 */       d0 += this.field_151603_a[i].func_151605_a(p_151601_1_ * d1, p_151601_3_ * d1) / d1;
/* 29 */       d1 /= 2.0D;
/*    */     }
/*    */     
/* 32 */     return d0;
/*    */   }
/*    */   
/*    */   public double[] func_151599_a(double[] p_151599_1_, double p_151599_2_, double p_151599_4_, int p_151599_6_, int p_151599_7_, double p_151599_8_, double p_151599_10_, double p_151599_12_)
/*    */   {
/* 37 */     return func_151600_a(p_151599_1_, p_151599_2_, p_151599_4_, p_151599_6_, p_151599_7_, p_151599_8_, p_151599_10_, p_151599_12_, 0.5D);
/*    */   }
/*    */   
/*    */   public double[] func_151600_a(double[] p_151600_1_, double p_151600_2_, double p_151600_4_, int p_151600_6_, int p_151600_7_, double p_151600_8_, double p_151600_10_, double p_151600_12_, double p_151600_14_)
/*    */   {
/* 42 */     if ((p_151600_1_ != null) && (p_151600_1_.length >= p_151600_6_ * p_151600_7_))
/*    */     {
/* 44 */       for (int i = 0; i < p_151600_1_.length; i++)
/*    */       {
/* 46 */         p_151600_1_[i] = 0.0D;
/*    */       }
/*    */       
/*    */     }
/*    */     else {
/* 51 */       p_151600_1_ = new double[p_151600_6_ * p_151600_7_];
/*    */     }
/*    */     
/* 54 */     double d1 = 1.0D;
/* 55 */     double d0 = 1.0D;
/*    */     
/* 57 */     for (int j = 0; j < this.field_151602_b; j++)
/*    */     {
/* 59 */       this.field_151603_a[j].func_151606_a(p_151600_1_, p_151600_2_, p_151600_4_, p_151600_6_, p_151600_7_, p_151600_8_ * d0 * d1, p_151600_10_ * d0 * d1, 0.55D / d1);
/* 60 */       d0 *= p_151600_12_;
/* 61 */       d1 *= p_151600_14_;
/*    */     }
/*    */     
/* 64 */     return p_151600_1_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\NoiseGeneratorPerlin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */