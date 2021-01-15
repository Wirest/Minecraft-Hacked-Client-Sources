/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ 
/*     */ public class MapGenRavine extends MapGenBase
/*     */ {
/*  13 */   private float[] field_75046_d = new float['Ѐ'];
/*     */   
/*     */   protected void func_180707_a(long p_180707_1_, int p_180707_3_, int p_180707_4_, ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_)
/*     */   {
/*  17 */     Random random = new Random(p_180707_1_);
/*  18 */     double d0 = p_180707_3_ * 16 + 8;
/*  19 */     double d1 = p_180707_4_ * 16 + 8;
/*  20 */     float f = 0.0F;
/*  21 */     float f1 = 0.0F;
/*     */     
/*  23 */     if (p_180707_16_ <= 0)
/*     */     {
/*  25 */       int i = this.range * 16 - 16;
/*  26 */       p_180707_16_ = i - random.nextInt(i / 4);
/*     */     }
/*     */     
/*  29 */     boolean flag1 = false;
/*     */     
/*  31 */     if (p_180707_15_ == -1)
/*     */     {
/*  33 */       p_180707_15_ = p_180707_16_ / 2;
/*  34 */       flag1 = true;
/*     */     }
/*     */     
/*  37 */     float f2 = 1.0F;
/*     */     
/*  39 */     for (int j = 0; j < 256; j++)
/*     */     {
/*  41 */       if ((j == 0) || (random.nextInt(3) == 0))
/*     */       {
/*  43 */         f2 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
/*     */       }
/*     */       
/*  46 */       this.field_75046_d[j] = (f2 * f2);
/*     */     }
/*  49 */     for (; 
/*  49 */         p_180707_15_ < p_180707_16_; p_180707_15_++)
/*     */     {
/*  51 */       double d9 = 1.5D + MathHelper.sin(p_180707_15_ * 3.1415927F / p_180707_16_) * p_180707_12_ * 1.0F;
/*  52 */       double d2 = d9 * p_180707_17_;
/*  53 */       d9 *= (random.nextFloat() * 0.25D + 0.75D);
/*  54 */       d2 *= (random.nextFloat() * 0.25D + 0.75D);
/*  55 */       float f3 = MathHelper.cos(p_180707_14_);
/*  56 */       float f4 = MathHelper.sin(p_180707_14_);
/*  57 */       p_180707_6_ += MathHelper.cos(p_180707_13_) * f3;
/*  58 */       p_180707_8_ += f4;
/*  59 */       p_180707_10_ += MathHelper.sin(p_180707_13_) * f3;
/*  60 */       p_180707_14_ *= 0.7F;
/*  61 */       p_180707_14_ += f1 * 0.05F;
/*  62 */       p_180707_13_ += f * 0.05F;
/*  63 */       f1 *= 0.8F;
/*  64 */       f *= 0.5F;
/*  65 */       f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
/*  66 */       f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
/*     */       
/*  68 */       if ((flag1) || (random.nextInt(4) != 0))
/*     */       {
/*  70 */         double d3 = p_180707_6_ - d0;
/*  71 */         double d4 = p_180707_10_ - d1;
/*  72 */         double d5 = p_180707_16_ - p_180707_15_;
/*  73 */         double d6 = p_180707_12_ + 2.0F + 16.0F;
/*     */         
/*  75 */         if (d3 * d3 + d4 * d4 - d5 * d5 > d6 * d6)
/*     */         {
/*  77 */           return;
/*     */         }
/*     */         
/*  80 */         if ((p_180707_6_ >= d0 - 16.0D - d9 * 2.0D) && (p_180707_10_ >= d1 - 16.0D - d9 * 2.0D) && (p_180707_6_ <= d0 + 16.0D + d9 * 2.0D) && (p_180707_10_ <= d1 + 16.0D + d9 * 2.0D))
/*     */         {
/*  82 */           int k2 = MathHelper.floor_double(p_180707_6_ - d9) - p_180707_3_ * 16 - 1;
/*  83 */           int k = MathHelper.floor_double(p_180707_6_ + d9) - p_180707_3_ * 16 + 1;
/*  84 */           int l2 = MathHelper.floor_double(p_180707_8_ - d2) - 1;
/*  85 */           int l = MathHelper.floor_double(p_180707_8_ + d2) + 1;
/*  86 */           int i3 = MathHelper.floor_double(p_180707_10_ - d9) - p_180707_4_ * 16 - 1;
/*  87 */           int i1 = MathHelper.floor_double(p_180707_10_ + d9) - p_180707_4_ * 16 + 1;
/*     */           
/*  89 */           if (k2 < 0)
/*     */           {
/*  91 */             k2 = 0;
/*     */           }
/*     */           
/*  94 */           if (k > 16)
/*     */           {
/*  96 */             k = 16;
/*     */           }
/*     */           
/*  99 */           if (l2 < 1)
/*     */           {
/* 101 */             l2 = 1;
/*     */           }
/*     */           
/* 104 */           if (l > 248)
/*     */           {
/* 106 */             l = 248;
/*     */           }
/*     */           
/* 109 */           if (i3 < 0)
/*     */           {
/* 111 */             i3 = 0;
/*     */           }
/*     */           
/* 114 */           if (i1 > 16)
/*     */           {
/* 116 */             i1 = 16;
/*     */           }
/*     */           
/* 119 */           boolean flag2 = false;
/*     */           
/* 121 */           for (int j1 = k2; (!flag2) && (j1 < k); j1++)
/*     */           {
/* 123 */             for (int k1 = i3; (!flag2) && (k1 < i1); k1++)
/*     */             {
/* 125 */               for (int l1 = l + 1; (!flag2) && (l1 >= l2 - 1); l1--)
/*     */               {
/* 127 */                 if ((l1 >= 0) && (l1 < 256))
/*     */                 {
/* 129 */                   IBlockState iblockstate = p_180707_5_.getBlockState(j1, l1, k1);
/*     */                   
/* 131 */                   if ((iblockstate.getBlock() == Blocks.flowing_water) || (iblockstate.getBlock() == Blocks.water))
/*     */                   {
/* 133 */                     flag2 = true;
/*     */                   }
/*     */                   
/* 136 */                   if ((l1 != l2 - 1) && (j1 != k2) && (j1 != k - 1) && (k1 != i3) && (k1 != i1 - 1))
/*     */                   {
/* 138 */                     l1 = l2;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 145 */           if (!flag2)
/*     */           {
/* 147 */             BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */             
/* 149 */             for (int j3 = k2; j3 < k; j3++)
/*     */             {
/* 151 */               double d10 = (j3 + p_180707_3_ * 16 + 0.5D - p_180707_6_) / d9;
/*     */               
/* 153 */               for (int i2 = i3; i2 < i1; i2++)
/*     */               {
/* 155 */                 double d7 = (i2 + p_180707_4_ * 16 + 0.5D - p_180707_10_) / d9;
/* 156 */                 boolean flag = false;
/*     */                 
/* 158 */                 if (d10 * d10 + d7 * d7 < 1.0D)
/*     */                 {
/* 160 */                   for (int j2 = l; j2 > l2; j2--)
/*     */                   {
/* 162 */                     double d8 = (j2 - 1 + 0.5D - p_180707_8_) / d2;
/*     */                     
/* 164 */                     if ((d10 * d10 + d7 * d7) * this.field_75046_d[(j2 - 1)] + d8 * d8 / 6.0D < 1.0D)
/*     */                     {
/* 166 */                       IBlockState iblockstate1 = p_180707_5_.getBlockState(j3, j2, i2);
/*     */                       
/* 168 */                       if (iblockstate1.getBlock() == Blocks.grass)
/*     */                       {
/* 170 */                         flag = true;
/*     */                       }
/*     */                       
/* 173 */                       if ((iblockstate1.getBlock() == Blocks.stone) || (iblockstate1.getBlock() == Blocks.dirt) || (iblockstate1.getBlock() == Blocks.grass))
/*     */                       {
/* 175 */                         if (j2 - 1 < 10)
/*     */                         {
/* 177 */                           p_180707_5_.setBlockState(j3, j2, i2, Blocks.flowing_lava.getDefaultState());
/*     */                         }
/*     */                         else
/*     */                         {
/* 181 */                           p_180707_5_.setBlockState(j3, j2, i2, Blocks.air.getDefaultState());
/*     */                           
/* 183 */                           if ((flag) && (p_180707_5_.getBlockState(j3, j2 - 1, i2).getBlock() == Blocks.dirt))
/*     */                           {
/* 185 */                             blockpos$mutableblockpos.func_181079_c(j3 + p_180707_3_ * 16, 0, i2 + p_180707_4_ * 16);
/* 186 */                             p_180707_5_.setBlockState(j3, j2 - 1, i2, this.worldObj.getBiomeGenForCoords(blockpos$mutableblockpos).topBlock);
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 196 */             if (flag1) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn)
/*     */   {
/* 211 */     if (this.rand.nextInt(50) == 0)
/*     */     {
/* 213 */       double d0 = chunkX * 16 + this.rand.nextInt(16);
/* 214 */       double d1 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
/* 215 */       double d2 = chunkZ * 16 + this.rand.nextInt(16);
/* 216 */       int i = 1;
/*     */       
/* 218 */       for (int j = 0; j < i; j++)
/*     */       {
/* 220 */         float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
/* 221 */         float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
/* 222 */         float f2 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
/* 223 */         func_180707_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 3.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\MapGenRavine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */