/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSand.EnumType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ 
/*     */ public class MapGenCaves extends MapGenBase
/*     */ {
/*     */   protected void func_180703_a(long p_180703_1_, int p_180703_3_, int p_180703_4_, ChunkPrimer p_180703_5_, double p_180703_6_, double p_180703_8_, double p_180703_10_)
/*     */   {
/*  18 */     func_180702_a(p_180703_1_, p_180703_3_, p_180703_4_, p_180703_5_, p_180703_6_, p_180703_8_, p_180703_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
/*     */   }
/*     */   
/*     */   protected void func_180702_a(long p_180702_1_, int p_180702_3_, int p_180702_4_, ChunkPrimer p_180702_5_, double p_180702_6_, double p_180702_8_, double p_180702_10_, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_)
/*     */   {
/*  23 */     double d0 = p_180702_3_ * 16 + 8;
/*  24 */     double d1 = p_180702_4_ * 16 + 8;
/*  25 */     float f = 0.0F;
/*  26 */     float f1 = 0.0F;
/*  27 */     Random random = new Random(p_180702_1_);
/*     */     
/*  29 */     if (p_180702_16_ <= 0)
/*     */     {
/*  31 */       int i = this.range * 16 - 16;
/*  32 */       p_180702_16_ = i - random.nextInt(i / 4);
/*     */     }
/*     */     
/*  35 */     boolean flag2 = false;
/*     */     
/*  37 */     if (p_180702_15_ == -1)
/*     */     {
/*  39 */       p_180702_15_ = p_180702_16_ / 2;
/*  40 */       flag2 = true;
/*     */     }
/*     */     
/*  43 */     int j = random.nextInt(p_180702_16_ / 2) + p_180702_16_ / 4;
/*     */     
/*  45 */     for (boolean flag = random.nextInt(6) == 0; p_180702_15_ < p_180702_16_; p_180702_15_++)
/*     */     {
/*  47 */       double d2 = 1.5D + MathHelper.sin(p_180702_15_ * 3.1415927F / p_180702_16_) * p_180702_12_ * 1.0F;
/*  48 */       double d3 = d2 * p_180702_17_;
/*  49 */       float f2 = MathHelper.cos(p_180702_14_);
/*  50 */       float f3 = MathHelper.sin(p_180702_14_);
/*  51 */       p_180702_6_ += MathHelper.cos(p_180702_13_) * f2;
/*  52 */       p_180702_8_ += f3;
/*  53 */       p_180702_10_ += MathHelper.sin(p_180702_13_) * f2;
/*     */       
/*  55 */       if (flag)
/*     */       {
/*  57 */         p_180702_14_ *= 0.92F;
/*     */       }
/*     */       else
/*     */       {
/*  61 */         p_180702_14_ *= 0.7F;
/*     */       }
/*     */       
/*  64 */       p_180702_14_ += f1 * 0.1F;
/*  65 */       p_180702_13_ += f * 0.1F;
/*  66 */       f1 *= 0.9F;
/*  67 */       f *= 0.75F;
/*  68 */       f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
/*  69 */       f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
/*     */       
/*  71 */       if ((!flag2) && (p_180702_15_ == j) && (p_180702_12_ > 1.0F) && (p_180702_16_ > 0))
/*     */       {
/*  73 */         func_180702_a(random.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ - 1.5707964F, p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
/*  74 */         func_180702_a(random.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ + 1.5707964F, p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
/*  75 */         return;
/*     */       }
/*     */       
/*  78 */       if ((flag2) || (random.nextInt(4) != 0))
/*     */       {
/*  80 */         double d4 = p_180702_6_ - d0;
/*  81 */         double d5 = p_180702_10_ - d1;
/*  82 */         double d6 = p_180702_16_ - p_180702_15_;
/*  83 */         double d7 = p_180702_12_ + 2.0F + 16.0F;
/*     */         
/*  85 */         if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7)
/*     */         {
/*  87 */           return;
/*     */         }
/*     */         
/*  90 */         if ((p_180702_6_ >= d0 - 16.0D - d2 * 2.0D) && (p_180702_10_ >= d1 - 16.0D - d2 * 2.0D) && (p_180702_6_ <= d0 + 16.0D + d2 * 2.0D) && (p_180702_10_ <= d1 + 16.0D + d2 * 2.0D))
/*     */         {
/*  92 */           int k2 = MathHelper.floor_double(p_180702_6_ - d2) - p_180702_3_ * 16 - 1;
/*  93 */           int k = MathHelper.floor_double(p_180702_6_ + d2) - p_180702_3_ * 16 + 1;
/*  94 */           int l2 = MathHelper.floor_double(p_180702_8_ - d3) - 1;
/*  95 */           int l = MathHelper.floor_double(p_180702_8_ + d3) + 1;
/*  96 */           int i3 = MathHelper.floor_double(p_180702_10_ - d2) - p_180702_4_ * 16 - 1;
/*  97 */           int i1 = MathHelper.floor_double(p_180702_10_ + d2) - p_180702_4_ * 16 + 1;
/*     */           
/*  99 */           if (k2 < 0)
/*     */           {
/* 101 */             k2 = 0;
/*     */           }
/*     */           
/* 104 */           if (k > 16)
/*     */           {
/* 106 */             k = 16;
/*     */           }
/*     */           
/* 109 */           if (l2 < 1)
/*     */           {
/* 111 */             l2 = 1;
/*     */           }
/*     */           
/* 114 */           if (l > 248)
/*     */           {
/* 116 */             l = 248;
/*     */           }
/*     */           
/* 119 */           if (i3 < 0)
/*     */           {
/* 121 */             i3 = 0;
/*     */           }
/*     */           
/* 124 */           if (i1 > 16)
/*     */           {
/* 126 */             i1 = 16;
/*     */           }
/*     */           
/* 129 */           boolean flag3 = false;
/*     */           
/* 131 */           for (int j1 = k2; (!flag3) && (j1 < k); j1++)
/*     */           {
/* 133 */             for (int k1 = i3; (!flag3) && (k1 < i1); k1++)
/*     */             {
/* 135 */               for (int l1 = l + 1; (!flag3) && (l1 >= l2 - 1); l1--)
/*     */               {
/* 137 */                 if ((l1 >= 0) && (l1 < 256))
/*     */                 {
/* 139 */                   IBlockState iblockstate = p_180702_5_.getBlockState(j1, l1, k1);
/*     */                   
/* 141 */                   if ((iblockstate.getBlock() == Blocks.flowing_water) || (iblockstate.getBlock() == Blocks.water))
/*     */                   {
/* 143 */                     flag3 = true;
/*     */                   }
/*     */                   
/* 146 */                   if ((l1 != l2 - 1) && (j1 != k2) && (j1 != k - 1) && (k1 != i3) && (k1 != i1 - 1))
/*     */                   {
/* 148 */                     l1 = l2;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 155 */           if (!flag3)
/*     */           {
/* 157 */             BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */             
/* 159 */             for (int j3 = k2; j3 < k; j3++)
/*     */             {
/* 161 */               double d10 = (j3 + p_180702_3_ * 16 + 0.5D - p_180702_6_) / d2;
/*     */               
/* 163 */               for (int i2 = i3; i2 < i1; i2++)
/*     */               {
/* 165 */                 double d8 = (i2 + p_180702_4_ * 16 + 0.5D - p_180702_10_) / d2;
/* 166 */                 boolean flag1 = false;
/*     */                 
/* 168 */                 if (d10 * d10 + d8 * d8 < 1.0D)
/*     */                 {
/* 170 */                   for (int j2 = l; j2 > l2; j2--)
/*     */                   {
/* 172 */                     double d9 = (j2 - 1 + 0.5D - p_180702_8_) / d3;
/*     */                     
/* 174 */                     if ((d9 > -0.7D) && (d10 * d10 + d9 * d9 + d8 * d8 < 1.0D))
/*     */                     {
/* 176 */                       IBlockState iblockstate1 = p_180702_5_.getBlockState(j3, j2, i2);
/* 177 */                       IBlockState iblockstate2 = (IBlockState)com.google.common.base.Objects.firstNonNull(p_180702_5_.getBlockState(j3, j2 + 1, i2), Blocks.air.getDefaultState());
/*     */                       
/* 179 */                       if ((iblockstate1.getBlock() == Blocks.grass) || (iblockstate1.getBlock() == Blocks.mycelium))
/*     */                       {
/* 181 */                         flag1 = true;
/*     */                       }
/*     */                       
/* 184 */                       if (func_175793_a(iblockstate1, iblockstate2))
/*     */                       {
/* 186 */                         if (j2 - 1 < 10)
/*     */                         {
/* 188 */                           p_180702_5_.setBlockState(j3, j2, i2, Blocks.lava.getDefaultState());
/*     */                         }
/*     */                         else
/*     */                         {
/* 192 */                           p_180702_5_.setBlockState(j3, j2, i2, Blocks.air.getDefaultState());
/*     */                           
/* 194 */                           if (iblockstate2.getBlock() == Blocks.sand)
/*     */                           {
/* 196 */                             p_180702_5_.setBlockState(j3, j2 + 1, i2, iblockstate2.getValue(net.minecraft.block.BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
/*     */                           }
/*     */                           
/* 199 */                           if ((flag1) && (p_180702_5_.getBlockState(j3, j2 - 1, i2).getBlock() == Blocks.dirt))
/*     */                           {
/* 201 */                             blockpos$mutableblockpos.func_181079_c(j3 + p_180702_3_ * 16, 0, i2 + p_180702_4_ * 16);
/* 202 */                             p_180702_5_.setBlockState(j3, j2 - 1, i2, this.worldObj.getBiomeGenForCoords(blockpos$mutableblockpos).topBlock.getBlock().getDefaultState());
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 212 */             if (flag2) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean func_175793_a(IBlockState p_175793_1_, IBlockState p_175793_2_)
/*     */   {
/* 224 */     return p_175793_1_.getBlock() == Blocks.stone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn)
/*     */   {
/* 232 */     int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
/*     */     
/* 234 */     if (this.rand.nextInt(7) != 0)
/*     */     {
/* 236 */       i = 0;
/*     */     }
/*     */     
/* 239 */     for (int j = 0; j < i; j++)
/*     */     {
/* 241 */       double d0 = chunkX * 16 + this.rand.nextInt(16);
/* 242 */       double d1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
/* 243 */       double d2 = chunkZ * 16 + this.rand.nextInt(16);
/* 244 */       int k = 1;
/*     */       
/* 246 */       if (this.rand.nextInt(4) == 0)
/*     */       {
/* 248 */         func_180703_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2);
/* 249 */         k += this.rand.nextInt(4);
/*     */       }
/*     */       
/* 252 */       for (int l = 0; l < k; l++)
/*     */       {
/* 254 */         float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
/* 255 */         float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
/* 256 */         float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
/*     */         
/* 258 */         if (this.rand.nextInt(10) == 0)
/*     */         {
/* 260 */           f2 *= (this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F);
/*     */         }
/*     */         
/* 263 */         func_180702_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\MapGenCaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */