/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenSavannaTree extends WorldGenAbstractTree
/*     */ {
/*  18 */   private static final IBlockState field_181643_a = Blocks.log2.getDefaultState().withProperty(net.minecraft.block.BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
/*  19 */   private static final IBlockState field_181644_b = Blocks.leaves2.getDefaultState().withProperty(net.minecraft.block.BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */   public WorldGenSavannaTree(boolean p_i45463_1_)
/*     */   {
/*  23 */     super(p_i45463_1_);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  28 */     int i = rand.nextInt(3) + rand.nextInt(3) + 5;
/*  29 */     boolean flag = true;
/*     */     
/*  31 */     if ((position.getY() >= 1) && (position.getY() + i + 1 <= 256))
/*     */     {
/*  33 */       for (int j = position.getY(); j <= position.getY() + 1 + i; j++)
/*     */       {
/*  35 */         int k = 1;
/*     */         
/*  37 */         if (j == position.getY())
/*     */         {
/*  39 */           k = 0;
/*     */         }
/*     */         
/*  42 */         if (j >= position.getY() + 1 + i - 2)
/*     */         {
/*  44 */           k = 2;
/*     */         }
/*     */         
/*  47 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  49 */         for (int l = position.getX() - k; (l <= position.getX() + k) && (flag); l++)
/*     */         {
/*  51 */           for (int i1 = position.getZ() - k; (i1 <= position.getZ() + k) && (flag); i1++)
/*     */           {
/*  53 */             if ((j >= 0) && (j < 256))
/*     */             {
/*  55 */               if (!func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i1)).getBlock()))
/*     */               {
/*  57 */                 flag = false;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/*  62 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  68 */       if (!flag)
/*     */       {
/*  70 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  74 */       Block block = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/*  76 */       if (((block == Blocks.grass) || (block == Blocks.dirt)) && (position.getY() < 256 - i - 1))
/*     */       {
/*  78 */         func_175921_a(worldIn, position.down());
/*  79 */         EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
/*  80 */         int k2 = i - rand.nextInt(4) - 1;
/*  81 */         int l2 = 3 - rand.nextInt(3);
/*  82 */         int i3 = position.getX();
/*  83 */         int j1 = position.getZ();
/*  84 */         int k1 = 0;
/*     */         
/*  86 */         for (int l1 = 0; l1 < i; l1++)
/*     */         {
/*  88 */           int i2 = position.getY() + l1;
/*     */           
/*  90 */           if ((l1 >= k2) && (l2 > 0))
/*     */           {
/*  92 */             i3 += enumfacing.getFrontOffsetX();
/*  93 */             j1 += enumfacing.getFrontOffsetZ();
/*  94 */             l2--;
/*     */           }
/*     */           
/*  97 */           BlockPos blockpos = new BlockPos(i3, i2, j1);
/*  98 */           Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 100 */           if ((material == Material.air) || (material == Material.leaves))
/*     */           {
/* 102 */             func_181642_b(worldIn, blockpos);
/* 103 */             k1 = i2;
/*     */           }
/*     */         }
/*     */         
/* 107 */         BlockPos blockpos2 = new BlockPos(i3, k1, j1);
/*     */         
/* 109 */         for (int j3 = -3; j3 <= 3; j3++)
/*     */         {
/* 111 */           for (int i4 = -3; i4 <= 3; i4++)
/*     */           {
/* 113 */             if ((Math.abs(j3) != 3) || (Math.abs(i4) != 3))
/*     */             {
/* 115 */               func_175924_b(worldIn, blockpos2.add(j3, 0, i4));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 120 */         blockpos2 = blockpos2.up();
/*     */         
/* 122 */         for (int k3 = -1; k3 <= 1; k3++)
/*     */         {
/* 124 */           for (int j4 = -1; j4 <= 1; j4++)
/*     */           {
/* 126 */             func_175924_b(worldIn, blockpos2.add(k3, 0, j4));
/*     */           }
/*     */         }
/*     */         
/* 130 */         func_175924_b(worldIn, blockpos2.east(2));
/* 131 */         func_175924_b(worldIn, blockpos2.west(2));
/* 132 */         func_175924_b(worldIn, blockpos2.south(2));
/* 133 */         func_175924_b(worldIn, blockpos2.north(2));
/* 134 */         i3 = position.getX();
/* 135 */         j1 = position.getZ();
/* 136 */         EnumFacing enumfacing1 = EnumFacing.Plane.HORIZONTAL.random(rand);
/*     */         
/* 138 */         if (enumfacing1 != enumfacing)
/*     */         {
/* 140 */           int l3 = k2 - rand.nextInt(2) - 1;
/* 141 */           int k4 = 1 + rand.nextInt(3);
/* 142 */           k1 = 0;
/*     */           
/* 144 */           for (int l4 = l3; (l4 < i) && (k4 > 0); k4--)
/*     */           {
/* 146 */             if (l4 >= 1)
/*     */             {
/* 148 */               int j2 = position.getY() + l4;
/* 149 */               i3 += enumfacing1.getFrontOffsetX();
/* 150 */               j1 += enumfacing1.getFrontOffsetZ();
/* 151 */               BlockPos blockpos1 = new BlockPos(i3, j2, j1);
/* 152 */               Material material1 = worldIn.getBlockState(blockpos1).getBlock().getMaterial();
/*     */               
/* 154 */               if ((material1 == Material.air) || (material1 == Material.leaves))
/*     */               {
/* 156 */                 func_181642_b(worldIn, blockpos1);
/* 157 */                 k1 = j2;
/*     */               }
/*     */             }
/*     */             
/* 161 */             l4++;
/*     */           }
/*     */           
/* 164 */           if (k1 > 0)
/*     */           {
/* 166 */             BlockPos blockpos3 = new BlockPos(i3, k1, j1);
/*     */             
/* 168 */             for (int i5 = -2; i5 <= 2; i5++)
/*     */             {
/* 170 */               for (int k5 = -2; k5 <= 2; k5++)
/*     */               {
/* 172 */                 if ((Math.abs(i5) != 2) || (Math.abs(k5) != 2))
/*     */                 {
/* 174 */                   func_175924_b(worldIn, blockpos3.add(i5, 0, k5));
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 179 */             blockpos3 = blockpos3.up();
/*     */             
/* 181 */             for (int j5 = -1; j5 <= 1; j5++)
/*     */             {
/* 183 */               for (int l5 = -1; l5 <= 1; l5++)
/*     */               {
/* 185 */                 func_175924_b(worldIn, blockpos3.add(j5, 0, l5));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 191 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 195 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 201 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_181642_b(World p_181642_1_, BlockPos p_181642_2_)
/*     */   {
/* 207 */     setBlockAndNotifyAdequately(p_181642_1_, p_181642_2_, field_181643_a);
/*     */   }
/*     */   
/*     */   private void func_175924_b(World worldIn, BlockPos p_175924_2_)
/*     */   {
/* 212 */     Material material = worldIn.getBlockState(p_175924_2_).getBlock().getMaterial();
/*     */     
/* 214 */     if ((material == Material.air) || (material == Material.leaves))
/*     */     {
/* 216 */       setBlockAndNotifyAdequately(worldIn, p_175924_2_, field_181644_b);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenSavannaTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */