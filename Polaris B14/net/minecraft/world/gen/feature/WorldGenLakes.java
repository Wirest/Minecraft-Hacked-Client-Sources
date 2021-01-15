/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class WorldGenLakes extends WorldGenerator
/*     */ {
/*     */   private Block block;
/*     */   
/*     */   public WorldGenLakes(Block blockIn)
/*     */   {
/*  18 */     this.block = blockIn;
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  23 */     for (position = position.add(-8, 0, -8); (position.getY() > 5) && (worldIn.isAirBlock(position)); position = position.down()) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  28 */     if (position.getY() <= 4)
/*     */     {
/*  30 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  34 */     position = position.down(4);
/*  35 */     boolean[] aboolean = new boolean['ࠀ'];
/*  36 */     int i = rand.nextInt(4) + 4;
/*     */     
/*  38 */     for (int j = 0; j < i; j++)
/*     */     {
/*  40 */       double d0 = rand.nextDouble() * 6.0D + 3.0D;
/*  41 */       double d1 = rand.nextDouble() * 4.0D + 2.0D;
/*  42 */       double d2 = rand.nextDouble() * 6.0D + 3.0D;
/*  43 */       double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
/*  44 */       double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
/*  45 */       double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;
/*     */       
/*  47 */       for (int l = 1; l < 15; l++)
/*     */       {
/*  49 */         for (int i1 = 1; i1 < 15; i1++)
/*     */         {
/*  51 */           for (int j1 = 1; j1 < 7; j1++)
/*     */           {
/*  53 */             double d6 = (l - d3) / (d0 / 2.0D);
/*  54 */             double d7 = (j1 - d4) / (d1 / 2.0D);
/*  55 */             double d8 = (i1 - d5) / (d2 / 2.0D);
/*  56 */             double d9 = d6 * d6 + d7 * d7 + d8 * d8;
/*     */             
/*  58 */             if (d9 < 1.0D)
/*     */             {
/*  60 */               aboolean[((l * 16 + i1) * 8 + j1)] = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  67 */     for (int k1 = 0; k1 < 16; k1++)
/*     */     {
/*  69 */       for (int l2 = 0; l2 < 16; l2++)
/*     */       {
/*  71 */         for (int k = 0; k < 8; k++)
/*     */         {
/*  73 */           boolean flag = (aboolean[((k1 * 16 + l2) * 8 + k)] == 0) && (((k1 < 15) && (aboolean[(((k1 + 1) * 16 + l2) * 8 + k)] != 0)) || ((k1 > 0) && (aboolean[(((k1 - 1) * 16 + l2) * 8 + k)] != 0)) || ((l2 < 15) && (aboolean[((k1 * 16 + l2 + 1) * 8 + k)] != 0)) || ((l2 > 0) && (aboolean[((k1 * 16 + (l2 - 1)) * 8 + k)] != 0)) || ((k < 7) && (aboolean[((k1 * 16 + l2) * 8 + k + 1)] != 0)) || ((k > 0) && (aboolean[((k1 * 16 + l2) * 8 + (k - 1))] != 0)));
/*     */           
/*  75 */           if (flag)
/*     */           {
/*  77 */             Material material = worldIn.getBlockState(position.add(k1, k, l2)).getBlock().getMaterial();
/*     */             
/*  79 */             if ((k >= 4) && (material.isLiquid()))
/*     */             {
/*  81 */               return false;
/*     */             }
/*     */             
/*  84 */             if ((k < 4) && (!material.isSolid()) && (worldIn.getBlockState(position.add(k1, k, l2)).getBlock() != this.block))
/*     */             {
/*  86 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     for (int l1 = 0; l1 < 16; l1++)
/*     */     {
/*  95 */       for (int i3 = 0; i3 < 16; i3++)
/*     */       {
/*  97 */         for (int i4 = 0; i4 < 8; i4++)
/*     */         {
/*  99 */           if (aboolean[((l1 * 16 + i3) * 8 + i4)] != 0)
/*     */           {
/* 101 */             worldIn.setBlockState(position.add(l1, i4, i3), i4 >= 4 ? Blocks.air.getDefaultState() : this.block.getDefaultState(), 2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 107 */     for (int i2 = 0; i2 < 16; i2++)
/*     */     {
/* 109 */       for (int j3 = 0; j3 < 16; j3++)
/*     */       {
/* 111 */         for (int j4 = 4; j4 < 8; j4++)
/*     */         {
/* 113 */           if (aboolean[((i2 * 16 + j3) * 8 + j4)] != 0)
/*     */           {
/* 115 */             BlockPos blockpos = position.add(i2, j4 - 1, j3);
/*     */             
/* 117 */             if ((worldIn.getBlockState(blockpos).getBlock() == Blocks.dirt) && (worldIn.getLightFor(net.minecraft.world.EnumSkyBlock.SKY, position.add(i2, j4, j3)) > 0))
/*     */             {
/* 119 */               BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(blockpos);
/*     */               
/* 121 */               if (biomegenbase.topBlock.getBlock() == Blocks.mycelium)
/*     */               {
/* 123 */                 worldIn.setBlockState(blockpos, Blocks.mycelium.getDefaultState(), 2);
/*     */               }
/*     */               else
/*     */               {
/* 127 */                 worldIn.setBlockState(blockpos, Blocks.grass.getDefaultState(), 2);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 135 */     if (this.block.getMaterial() == Material.lava)
/*     */     {
/* 137 */       for (int j2 = 0; j2 < 16; j2++)
/*     */       {
/* 139 */         for (int k3 = 0; k3 < 16; k3++)
/*     */         {
/* 141 */           for (int k4 = 0; k4 < 8; k4++)
/*     */           {
/* 143 */             boolean flag1 = (aboolean[((j2 * 16 + k3) * 8 + k4)] == 0) && (((j2 < 15) && (aboolean[(((j2 + 1) * 16 + k3) * 8 + k4)] != 0)) || ((j2 > 0) && (aboolean[(((j2 - 1) * 16 + k3) * 8 + k4)] != 0)) || ((k3 < 15) && (aboolean[((j2 * 16 + k3 + 1) * 8 + k4)] != 0)) || ((k3 > 0) && (aboolean[((j2 * 16 + (k3 - 1)) * 8 + k4)] != 0)) || ((k4 < 7) && (aboolean[((j2 * 16 + k3) * 8 + k4 + 1)] != 0)) || ((k4 > 0) && (aboolean[((j2 * 16 + k3) * 8 + (k4 - 1))] != 0)));
/*     */             
/* 145 */             if ((flag1) && ((k4 < 4) || (rand.nextInt(2) != 0)) && (worldIn.getBlockState(position.add(j2, k4, k3)).getBlock().getMaterial().isSolid()))
/*     */             {
/* 147 */               worldIn.setBlockState(position.add(j2, k4, k3), Blocks.stone.getDefaultState(), 2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 154 */     if (this.block.getMaterial() == Material.water)
/*     */     {
/* 156 */       for (int k2 = 0; k2 < 16; k2++)
/*     */       {
/* 158 */         for (int l3 = 0; l3 < 16; l3++)
/*     */         {
/* 160 */           int l4 = 4;
/*     */           
/* 162 */           if (worldIn.canBlockFreezeWater(position.add(k2, l4, l3)))
/*     */           {
/* 164 */             worldIn.setBlockState(position.add(k2, l4, l3), Blocks.ice.getDefaultState(), 2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 170 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenLakes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */