/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenIceSpike extends WorldGenerator
/*     */ {
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  15 */     while ((worldIn.isAirBlock(position)) && (position.getY() > 2))
/*     */     {
/*  17 */       position = position.down();
/*     */     }
/*     */     
/*  20 */     if (worldIn.getBlockState(position).getBlock() != Blocks.snow)
/*     */     {
/*  22 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  26 */     position = position.up(rand.nextInt(4));
/*  27 */     int i = rand.nextInt(4) + 7;
/*  28 */     int j = i / 4 + rand.nextInt(2);
/*     */     
/*  30 */     if ((j > 1) && (rand.nextInt(60) == 0))
/*     */     {
/*  32 */       position = position.up(10 + rand.nextInt(30));
/*     */     }
/*     */     
/*  35 */     for (int k = 0; k < i; k++)
/*     */     {
/*  37 */       float f = (1.0F - k / i) * j;
/*  38 */       int l = MathHelper.ceiling_float_int(f);
/*     */       
/*  40 */       for (int i1 = -l; i1 <= l; i1++)
/*     */       {
/*  42 */         float f1 = MathHelper.abs_int(i1) - 0.25F;
/*     */         
/*  44 */         for (int j1 = -l; j1 <= l; j1++)
/*     */         {
/*  46 */           float f2 = MathHelper.abs_int(j1) - 0.25F;
/*     */           
/*  48 */           if (((i1 == 0) && (j1 == 0)) || ((f1 * f1 + f2 * f2 <= f * f) && (((i1 != -l) && (i1 != l) && (j1 != -l) && (j1 != l)) || (rand.nextFloat() <= 0.75F))))
/*     */           {
/*  50 */             Block block = worldIn.getBlockState(position.add(i1, k, j1)).getBlock();
/*     */             
/*  52 */             if ((block.getMaterial() == net.minecraft.block.material.Material.air) || (block == Blocks.dirt) || (block == Blocks.snow) || (block == Blocks.ice))
/*     */             {
/*  54 */               setBlockAndNotifyAdequately(worldIn, position.add(i1, k, j1), Blocks.packed_ice.getDefaultState());
/*     */             }
/*     */             
/*  57 */             if ((k != 0) && (l > 1))
/*     */             {
/*  59 */               block = worldIn.getBlockState(position.add(i1, -k, j1)).getBlock();
/*     */               
/*  61 */               if ((block.getMaterial() == net.minecraft.block.material.Material.air) || (block == Blocks.dirt) || (block == Blocks.snow) || (block == Blocks.ice))
/*     */               {
/*  63 */                 setBlockAndNotifyAdequately(worldIn, position.add(i1, -k, j1), Blocks.packed_ice.getDefaultState());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  71 */     int k1 = j - 1;
/*     */     
/*  73 */     if (k1 < 0)
/*     */     {
/*  75 */       k1 = 0;
/*     */     }
/*  77 */     else if (k1 > 1)
/*     */     {
/*  79 */       k1 = 1;
/*     */     }
/*     */     
/*  82 */     for (int l1 = -k1; l1 <= k1; l1++)
/*     */     {
/*  84 */       for (int i2 = -k1; i2 <= k1; i2++)
/*     */       {
/*  86 */         BlockPos blockpos = position.add(l1, -1, i2);
/*  87 */         int j2 = 50;
/*     */         
/*  89 */         if ((Math.abs(l1) == 1) && (Math.abs(i2) == 1))
/*     */         {
/*  91 */           j2 = rand.nextInt(5);
/*     */         }
/*     */         
/*  94 */         while (blockpos.getY() > 50)
/*     */         {
/*  96 */           Block block1 = worldIn.getBlockState(blockpos).getBlock();
/*     */           
/*  98 */           if ((block1.getMaterial() != net.minecraft.block.material.Material.air) && (block1 != Blocks.dirt) && (block1 != Blocks.snow) && (block1 != Blocks.ice) && (block1 != Blocks.packed_ice)) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 103 */           setBlockAndNotifyAdequately(worldIn, blockpos, Blocks.packed_ice.getDefaultState());
/* 104 */           blockpos = blockpos.down();
/* 105 */           j2--;
/*     */           
/* 107 */           if (j2 <= 0)
/*     */           {
/* 109 */             blockpos = blockpos.down(rand.nextInt(5) + 1);
/* 110 */             j2 = rand.nextInt(5);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 116 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenIceSpike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */