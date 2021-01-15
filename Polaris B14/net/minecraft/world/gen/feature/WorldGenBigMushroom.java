/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockHugeMushroom.EnumType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenBigMushroom extends WorldGenerator
/*     */ {
/*     */   private Block mushroomType;
/*     */   
/*     */   public WorldGenBigMushroom(Block p_i46449_1_)
/*     */   {
/*  18 */     super(true);
/*  19 */     this.mushroomType = p_i46449_1_;
/*     */   }
/*     */   
/*     */   public WorldGenBigMushroom()
/*     */   {
/*  24 */     super(false);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  29 */     if (this.mushroomType == null)
/*     */     {
/*  31 */       this.mushroomType = (rand.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block);
/*     */     }
/*     */     
/*  34 */     int i = rand.nextInt(3) + 4;
/*  35 */     boolean flag = true;
/*     */     
/*  37 */     if ((position.getY() >= 1) && (position.getY() + i + 1 < 256))
/*     */     {
/*  39 */       for (int j = position.getY(); j <= position.getY() + 1 + i; j++)
/*     */       {
/*  41 */         int k = 3;
/*     */         
/*  43 */         if (j <= position.getY() + 3)
/*     */         {
/*  45 */           k = 0;
/*     */         }
/*     */         
/*  48 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  50 */         for (int l = position.getX() - k; (l <= position.getX() + k) && (flag); l++)
/*     */         {
/*  52 */           for (int i1 = position.getZ() - k; (i1 <= position.getZ() + k) && (flag); i1++)
/*     */           {
/*  54 */             if ((j >= 0) && (j < 256))
/*     */             {
/*  56 */               Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i1)).getBlock();
/*     */               
/*  58 */               if ((block.getMaterial() != net.minecraft.block.material.Material.air) && (block.getMaterial() != net.minecraft.block.material.Material.leaves))
/*     */               {
/*  60 */                 flag = false;
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/*  65 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  71 */       if (!flag)
/*     */       {
/*  73 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  77 */       Block block1 = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/*  79 */       if ((block1 != Blocks.dirt) && (block1 != Blocks.grass) && (block1 != Blocks.mycelium))
/*     */       {
/*  81 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  85 */       int k2 = position.getY() + i;
/*     */       
/*  87 */       if (this.mushroomType == Blocks.red_mushroom_block)
/*     */       {
/*  89 */         k2 = position.getY() + i - 3;
/*     */       }
/*     */       
/*  92 */       for (int l2 = k2; l2 <= position.getY() + i; l2++)
/*     */       {
/*  94 */         int j3 = 1;
/*     */         
/*  96 */         if (l2 < position.getY() + i)
/*     */         {
/*  98 */           j3++;
/*     */         }
/*     */         
/* 101 */         if (this.mushroomType == Blocks.brown_mushroom_block)
/*     */         {
/* 103 */           j3 = 3;
/*     */         }
/*     */         
/* 106 */         int k3 = position.getX() - j3;
/* 107 */         int l3 = position.getX() + j3;
/* 108 */         int j1 = position.getZ() - j3;
/* 109 */         int k1 = position.getZ() + j3;
/*     */         
/* 111 */         for (int l1 = k3; l1 <= l3; l1++)
/*     */         {
/* 113 */           for (int i2 = j1; i2 <= k1; i2++)
/*     */           {
/* 115 */             int j2 = 5;
/*     */             
/* 117 */             if (l1 == k3)
/*     */             {
/* 119 */               j2--;
/*     */             }
/* 121 */             else if (l1 == l3)
/*     */             {
/* 123 */               j2++;
/*     */             }
/*     */             
/* 126 */             if (i2 == j1)
/*     */             {
/* 128 */               j2 -= 3;
/*     */             }
/* 130 */             else if (i2 == k1)
/*     */             {
/* 132 */               j2 += 3;
/*     */             }
/*     */             
/* 135 */             BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);
/*     */             
/* 137 */             if ((this.mushroomType == Blocks.brown_mushroom_block) || (l2 < position.getY() + i))
/*     */             {
/* 139 */               if (((l1 == k3) || (l1 == l3)) && ((i2 == j1) || (i2 == k1))) {
/*     */                 continue;
/*     */               }
/*     */               
/*     */ 
/* 144 */               if ((l1 == position.getX() - (j3 - 1)) && (i2 == j1))
/*     */               {
/* 146 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
/*     */               }
/*     */               
/* 149 */               if ((l1 == k3) && (i2 == position.getZ() - (j3 - 1)))
/*     */               {
/* 151 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
/*     */               }
/*     */               
/* 154 */               if ((l1 == position.getX() + (j3 - 1)) && (i2 == j1))
/*     */               {
/* 156 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
/*     */               }
/*     */               
/* 159 */               if ((l1 == l3) && (i2 == position.getZ() - (j3 - 1)))
/*     */               {
/* 161 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
/*     */               }
/*     */               
/* 164 */               if ((l1 == position.getX() - (j3 - 1)) && (i2 == k1))
/*     */               {
/* 166 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
/*     */               }
/*     */               
/* 169 */               if ((l1 == k3) && (i2 == position.getZ() + (j3 - 1)))
/*     */               {
/* 171 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
/*     */               }
/*     */               
/* 174 */               if ((l1 == position.getX() + (j3 - 1)) && (i2 == k1))
/*     */               {
/* 176 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
/*     */               }
/*     */               
/* 179 */               if ((l1 == l3) && (i2 == position.getZ() + (j3 - 1)))
/*     */               {
/* 181 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
/*     */               }
/*     */             }
/*     */             
/* 185 */             if ((blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER) && (l2 < position.getY() + i))
/*     */             {
/* 187 */               blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
/*     */             }
/*     */             
/* 190 */             if ((position.getY() >= position.getY() + i - 1) || (blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE))
/*     */             {
/* 192 */               BlockPos blockpos = new BlockPos(l1, l2, i2);
/*     */               
/* 194 */               if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*     */               {
/* 196 */                 setBlockAndNotifyAdequately(worldIn, blockpos, this.mushroomType.getDefaultState().withProperty(net.minecraft.block.BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 203 */       for (int i3 = 0; i3 < i; i3++)
/*     */       {
/* 205 */         Block block2 = worldIn.getBlockState(position.up(i3)).getBlock();
/*     */         
/* 207 */         if (!block2.isFullBlock())
/*     */         {
/* 209 */           setBlockAndNotifyAdequately(worldIn, position.up(i3), this.mushroomType.getDefaultState().withProperty(net.minecraft.block.BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
/*     */         }
/*     */       }
/*     */       
/* 213 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 219 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenBigMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */