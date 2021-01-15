/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoiseGeneratorOctaves
/*    */   extends NoiseGenerator
/*    */ {
/*    */   private NoiseGeneratorImproved[] generatorCollection;
/*    */   private int octaves;
/*    */   
/*    */   public NoiseGeneratorOctaves(Random p_i2111_1_, int p_i2111_2_)
/*    */   {
/* 16 */     this.octaves = p_i2111_2_;
/* 17 */     this.generatorCollection = new NoiseGeneratorImproved[p_i2111_2_];
/*    */     
/* 19 */     for (int i = 0; i < p_i2111_2_; i++)
/*    */     {
/* 21 */       this.generatorCollection[i] = new NoiseGeneratorImproved(p_i2111_1_);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public double[] generateNoiseOctaves(double[] p_76304_1_, int p_76304_2_, int p_76304_3_, int p_76304_4_, int p_76304_5_, int p_76304_6_, int p_76304_7_, double p_76304_8_, double p_76304_10_, double p_76304_12_)
/*    */   {
/* 31 */     if (p_76304_1_ == null)
/*    */     {
/* 33 */       p_76304_1_ = new double[p_76304_5_ * p_76304_6_ * p_76304_7_];
/*    */     }
/*    */     else
/*    */     {
/* 37 */       for (int i = 0; i < p_76304_1_.length; i++)
/*    */       {
/* 39 */         p_76304_1_[i] = 0.0D;
/*    */       }
/*    */     }
/*    */     
/* 43 */     double d3 = 1.0D;
/*    */     
/* 45 */     for (int j = 0; j < this.octaves; j++)
/*    */     {
/* 47 */       double d0 = p_76304_2_ * d3 * p_76304_8_;
/* 48 */       double d1 = p_76304_3_ * d3 * p_76304_10_;
/* 49 */       double d2 = p_76304_4_ * d3 * p_76304_12_;
/* 50 */       long k = MathHelper.floor_double_long(d0);
/* 51 */       long l = MathHelper.floor_double_long(d2);
/* 52 */       d0 -= k;
/* 53 */       d2 -= l;
/* 54 */       k %= 16777216L;
/* 55 */       l %= 16777216L;
/* 56 */       d0 += k;
/* 57 */       d2 += l;
/* 58 */       this.generatorCollection[j].populateNoiseArray(p_76304_1_, d0, d1, d2, p_76304_5_, p_76304_6_, p_76304_7_, p_76304_8_ * d3, p_76304_10_ * d3, p_76304_12_ * d3, d3);
/* 59 */       d3 /= 2.0D;
/*    */     }
/*    */     
/* 62 */     return p_76304_1_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double[] generateNoiseOctaves(double[] p_76305_1_, int p_76305_2_, int p_76305_3_, int p_76305_4_, int p_76305_5_, double p_76305_6_, double p_76305_8_, double p_76305_10_)
/*    */   {
/* 70 */     return generateNoiseOctaves(p_76305_1_, p_76305_2_, 10, p_76305_3_, p_76305_4_, 1, p_76305_5_, p_76305_6_, 1.0D, p_76305_8_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\NoiseGeneratorOctaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */