/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ 
/*     */ public class MapGenCavesHell extends MapGenBase
/*     */ {
/*     */   protected void func_180705_a(long p_180705_1_, int p_180705_3_, int p_180705_4_, ChunkPrimer p_180705_5_, double p_180705_6_, double p_180705_8_, double p_180705_10_)
/*     */   {
/*  14 */     func_180704_a(p_180705_1_, p_180705_3_, p_180705_4_, p_180705_5_, p_180705_6_, p_180705_8_, p_180705_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
/*     */   }
/*     */   
/*     */   protected void func_180704_a(long p_180704_1_, int p_180704_3_, int p_180704_4_, ChunkPrimer p_180704_5_, double p_180704_6_, double p_180704_8_, double p_180704_10_, float p_180704_12_, float p_180704_13_, float p_180704_14_, int p_180704_15_, int p_180704_16_, double p_180704_17_)
/*     */   {
/*  19 */     double d0 = p_180704_3_ * 16 + 8;
/*  20 */     double d1 = p_180704_4_ * 16 + 8;
/*  21 */     float f = 0.0F;
/*  22 */     float f1 = 0.0F;
/*  23 */     Random random = new Random(p_180704_1_);
/*     */     
/*  25 */     if (p_180704_16_ <= 0)
/*     */     {
/*  27 */       int i = this.range * 16 - 16;
/*  28 */       p_180704_16_ = i - random.nextInt(i / 4);
/*     */     }
/*     */     
/*  31 */     boolean flag1 = false;
/*     */     
/*  33 */     if (p_180704_15_ == -1)
/*     */     {
/*  35 */       p_180704_15_ = p_180704_16_ / 2;
/*  36 */       flag1 = true;
/*     */     }
/*     */     
/*  39 */     int j = random.nextInt(p_180704_16_ / 2) + p_180704_16_ / 4;
/*     */     
/*  41 */     for (boolean flag = random.nextInt(6) == 0; p_180704_15_ < p_180704_16_; p_180704_15_++)
/*     */     {
/*  43 */       double d2 = 1.5D + MathHelper.sin(p_180704_15_ * 3.1415927F / p_180704_16_) * p_180704_12_ * 1.0F;
/*  44 */       double d3 = d2 * p_180704_17_;
/*  45 */       float f2 = MathHelper.cos(p_180704_14_);
/*  46 */       float f3 = MathHelper.sin(p_180704_14_);
/*  47 */       p_180704_6_ += MathHelper.cos(p_180704_13_) * f2;
/*  48 */       p_180704_8_ += f3;
/*  49 */       p_180704_10_ += MathHelper.sin(p_180704_13_) * f2;
/*     */       
/*  51 */       if (flag)
/*     */       {
/*  53 */         p_180704_14_ *= 0.92F;
/*     */       }
/*     */       else
/*     */       {
/*  57 */         p_180704_14_ *= 0.7F;
/*     */       }
/*     */       
/*  60 */       p_180704_14_ += f1 * 0.1F;
/*  61 */       p_180704_13_ += f * 0.1F;
/*  62 */       f1 *= 0.9F;
/*  63 */       f *= 0.75F;
/*  64 */       f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
/*  65 */       f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
/*     */       
/*  67 */       if ((!flag1) && (p_180704_15_ == j) && (p_180704_12_ > 1.0F))
/*     */       {
/*  69 */         func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5F + 0.5F, p_180704_13_ - 1.5707964F, p_180704_14_ / 3.0F, p_180704_15_, p_180704_16_, 1.0D);
/*  70 */         func_180704_a(random.nextLong(), p_180704_3_, p_180704_4_, p_180704_5_, p_180704_6_, p_180704_8_, p_180704_10_, random.nextFloat() * 0.5F + 0.5F, p_180704_13_ + 1.5707964F, p_180704_14_ / 3.0F, p_180704_15_, p_180704_16_, 1.0D);
/*  71 */         return;
/*     */       }
/*     */       
/*  74 */       if ((flag1) || (random.nextInt(4) != 0))
/*     */       {
/*  76 */         double d4 = p_180704_6_ - d0;
/*  77 */         double d5 = p_180704_10_ - d1;
/*  78 */         double d6 = p_180704_16_ - p_180704_15_;
/*  79 */         double d7 = p_180704_12_ + 2.0F + 16.0F;
/*     */         
/*  81 */         if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7)
/*     */         {
/*  83 */           return;
/*     */         }
/*     */         
/*  86 */         if ((p_180704_6_ >= d0 - 16.0D - d2 * 2.0D) && (p_180704_10_ >= d1 - 16.0D - d2 * 2.0D) && (p_180704_6_ <= d0 + 16.0D + d2 * 2.0D) && (p_180704_10_ <= d1 + 16.0D + d2 * 2.0D))
/*     */         {
/*  88 */           int j2 = MathHelper.floor_double(p_180704_6_ - d2) - p_180704_3_ * 16 - 1;
/*  89 */           int k = MathHelper.floor_double(p_180704_6_ + d2) - p_180704_3_ * 16 + 1;
/*  90 */           int k2 = MathHelper.floor_double(p_180704_8_ - d3) - 1;
/*  91 */           int l = MathHelper.floor_double(p_180704_8_ + d3) + 1;
/*  92 */           int l2 = MathHelper.floor_double(p_180704_10_ - d2) - p_180704_4_ * 16 - 1;
/*  93 */           int i1 = MathHelper.floor_double(p_180704_10_ + d2) - p_180704_4_ * 16 + 1;
/*     */           
/*  95 */           if (j2 < 0)
/*     */           {
/*  97 */             j2 = 0;
/*     */           }
/*     */           
/* 100 */           if (k > 16)
/*     */           {
/* 102 */             k = 16;
/*     */           }
/*     */           
/* 105 */           if (k2 < 1)
/*     */           {
/* 107 */             k2 = 1;
/*     */           }
/*     */           
/* 110 */           if (l > 120)
/*     */           {
/* 112 */             l = 120;
/*     */           }
/*     */           
/* 115 */           if (l2 < 0)
/*     */           {
/* 117 */             l2 = 0;
/*     */           }
/*     */           
/* 120 */           if (i1 > 16)
/*     */           {
/* 122 */             i1 = 16;
/*     */           }
/*     */           
/* 125 */           boolean flag2 = false;
/*     */           
/* 127 */           for (int j1 = j2; (!flag2) && (j1 < k); j1++)
/*     */           {
/* 129 */             for (int k1 = l2; (!flag2) && (k1 < i1); k1++)
/*     */             {
/* 131 */               for (int l1 = l + 1; (!flag2) && (l1 >= k2 - 1); l1--)
/*     */               {
/* 133 */                 if ((l1 >= 0) && (l1 < 128))
/*     */                 {
/* 135 */                   IBlockState iblockstate = p_180704_5_.getBlockState(j1, l1, k1);
/*     */                   
/* 137 */                   if ((iblockstate.getBlock() == Blocks.flowing_lava) || (iblockstate.getBlock() == Blocks.lava))
/*     */                   {
/* 139 */                     flag2 = true;
/*     */                   }
/*     */                   
/* 142 */                   if ((l1 != k2 - 1) && (j1 != j2) && (j1 != k - 1) && (k1 != l2) && (k1 != i1 - 1))
/*     */                   {
/* 144 */                     l1 = k2;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 151 */           if (!flag2)
/*     */           {
/* 153 */             for (int i3 = j2; i3 < k; i3++)
/*     */             {
/* 155 */               double d10 = (i3 + p_180704_3_ * 16 + 0.5D - p_180704_6_) / d2;
/*     */               
/* 157 */               for (int j3 = l2; j3 < i1; j3++)
/*     */               {
/* 159 */                 double d8 = (j3 + p_180704_4_ * 16 + 0.5D - p_180704_10_) / d2;
/*     */                 
/* 161 */                 for (int i2 = l; i2 > k2; i2--)
/*     */                 {
/* 163 */                   double d9 = (i2 - 1 + 0.5D - p_180704_8_) / d3;
/*     */                   
/* 165 */                   if ((d9 > -0.7D) && (d10 * d10 + d9 * d9 + d8 * d8 < 1.0D))
/*     */                   {
/* 167 */                     IBlockState iblockstate1 = p_180704_5_.getBlockState(i3, i2, j3);
/*     */                     
/* 169 */                     if ((iblockstate1.getBlock() == Blocks.netherrack) || (iblockstate1.getBlock() == Blocks.dirt) || (iblockstate1.getBlock() == Blocks.grass))
/*     */                     {
/* 171 */                       p_180704_5_.setBlockState(i3, i2, j3, Blocks.air.getDefaultState());
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 178 */             if (flag1) {
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
/* 193 */     int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
/*     */     
/* 195 */     if (this.rand.nextInt(5) != 0)
/*     */     {
/* 197 */       i = 0;
/*     */     }
/*     */     
/* 200 */     for (int j = 0; j < i; j++)
/*     */     {
/* 202 */       double d0 = chunkX * 16 + this.rand.nextInt(16);
/* 203 */       double d1 = this.rand.nextInt(128);
/* 204 */       double d2 = chunkZ * 16 + this.rand.nextInt(16);
/* 205 */       int k = 1;
/*     */       
/* 207 */       if (this.rand.nextInt(4) == 0)
/*     */       {
/* 209 */         func_180705_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2);
/* 210 */         k += this.rand.nextInt(4);
/*     */       }
/*     */       
/* 213 */       for (int l = 0; l < k; l++)
/*     */       {
/* 215 */         float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
/* 216 */         float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
/* 217 */         float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
/* 218 */         func_180704_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2 * 2.0F, f, f1, 0, 0, 0.5D);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\MapGenCavesHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */