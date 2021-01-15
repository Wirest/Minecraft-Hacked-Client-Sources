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
/*     */ public class WorldGenCanopyTree extends WorldGenAbstractTree
/*     */ {
/*  18 */   private static final IBlockState field_181640_a = Blocks.log2.getDefaultState().withProperty(net.minecraft.block.BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
/*  19 */   private static final IBlockState field_181641_b = Blocks.leaves2.getDefaultState().withProperty(net.minecraft.block.BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */   public WorldGenCanopyTree(boolean p_i45461_1_)
/*     */   {
/*  23 */     super(p_i45461_1_);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  28 */     int i = rand.nextInt(3) + rand.nextInt(2) + 6;
/*  29 */     int j = position.getX();
/*  30 */     int k = position.getY();
/*  31 */     int l = position.getZ();
/*     */     
/*  33 */     if ((k >= 1) && (k + i + 1 < 256))
/*     */     {
/*  35 */       BlockPos blockpos = position.down();
/*  36 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/*  38 */       if ((block != Blocks.grass) && (block != Blocks.dirt))
/*     */       {
/*  40 */         return false;
/*     */       }
/*  42 */       if (!func_181638_a(worldIn, position, i))
/*     */       {
/*  44 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  48 */       func_175921_a(worldIn, blockpos);
/*  49 */       func_175921_a(worldIn, blockpos.east());
/*  50 */       func_175921_a(worldIn, blockpos.south());
/*  51 */       func_175921_a(worldIn, blockpos.south().east());
/*  52 */       EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
/*  53 */       int i1 = i - rand.nextInt(4);
/*  54 */       int j1 = 2 - rand.nextInt(3);
/*  55 */       int k1 = j;
/*  56 */       int l1 = l;
/*  57 */       int i2 = k + i - 1;
/*     */       
/*  59 */       for (int j2 = 0; j2 < i; j2++)
/*     */       {
/*  61 */         if ((j2 >= i1) && (j1 > 0))
/*     */         {
/*  63 */           k1 += enumfacing.getFrontOffsetX();
/*  64 */           l1 += enumfacing.getFrontOffsetZ();
/*  65 */           j1--;
/*     */         }
/*     */         
/*  68 */         int k2 = k + j2;
/*  69 */         BlockPos blockpos1 = new BlockPos(k1, k2, l1);
/*  70 */         Material material = worldIn.getBlockState(blockpos1).getBlock().getMaterial();
/*     */         
/*  72 */         if ((material == Material.air) || (material == Material.leaves))
/*     */         {
/*  74 */           func_181639_b(worldIn, blockpos1);
/*  75 */           func_181639_b(worldIn, blockpos1.east());
/*  76 */           func_181639_b(worldIn, blockpos1.south());
/*  77 */           func_181639_b(worldIn, blockpos1.east().south());
/*     */         }
/*     */       }
/*     */       
/*  81 */       for (int i3 = -2; i3 <= 0; i3++)
/*     */       {
/*  83 */         for (int l3 = -2; l3 <= 0; l3++)
/*     */         {
/*  85 */           int k4 = -1;
/*  86 */           func_150526_a(worldIn, k1 + i3, i2 + k4, l1 + l3);
/*  87 */           func_150526_a(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
/*  88 */           func_150526_a(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
/*  89 */           func_150526_a(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
/*     */           
/*  91 */           if (((i3 > -2) || (l3 > -1)) && ((i3 != -1) || (l3 != -2)))
/*     */           {
/*  93 */             k4 = 1;
/*  94 */             func_150526_a(worldIn, k1 + i3, i2 + k4, l1 + l3);
/*  95 */             func_150526_a(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
/*  96 */             func_150526_a(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
/*  97 */             func_150526_a(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 102 */       if (rand.nextBoolean())
/*     */       {
/* 104 */         func_150526_a(worldIn, k1, i2 + 2, l1);
/* 105 */         func_150526_a(worldIn, k1 + 1, i2 + 2, l1);
/* 106 */         func_150526_a(worldIn, k1 + 1, i2 + 2, l1 + 1);
/* 107 */         func_150526_a(worldIn, k1, i2 + 2, l1 + 1);
/*     */       }
/*     */       
/* 110 */       for (int j3 = -3; j3 <= 4; j3++)
/*     */       {
/* 112 */         for (int i4 = -3; i4 <= 4; i4++)
/*     */         {
/* 114 */           if (((j3 != -3) || (i4 != -3)) && ((j3 != -3) || (i4 != 4)) && ((j3 != 4) || (i4 != -3)) && ((j3 != 4) || (i4 != 4)) && ((Math.abs(j3) < 3) || (Math.abs(i4) < 3)))
/*     */           {
/* 116 */             func_150526_a(worldIn, k1 + j3, i2, l1 + i4);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 121 */       for (int k3 = -1; k3 <= 2; k3++)
/*     */       {
/* 123 */         for (int j4 = -1; j4 <= 2; j4++)
/*     */         {
/* 125 */           if (((k3 < 0) || (k3 > 1) || (j4 < 0) || (j4 > 1)) && (rand.nextInt(3) <= 0))
/*     */           {
/* 127 */             int l4 = rand.nextInt(3) + 2;
/*     */             
/* 129 */             for (int i5 = 0; i5 < l4; i5++)
/*     */             {
/* 131 */               func_181639_b(worldIn, new BlockPos(j + k3, i2 - i5 - 1, l + j4));
/*     */             }
/*     */             
/* 134 */             for (int j5 = -1; j5 <= 1; j5++)
/*     */             {
/* 136 */               for (int l2 = -1; l2 <= 1; l2++)
/*     */               {
/* 138 */                 func_150526_a(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2);
/*     */               }
/*     */             }
/*     */             
/* 142 */             for (int k5 = -2; k5 <= 2; k5++)
/*     */             {
/* 144 */               for (int l5 = -2; l5 <= 2; l5++)
/*     */               {
/* 146 */                 if ((Math.abs(k5) != 2) || (Math.abs(l5) != 2))
/*     */                 {
/* 148 */                   func_150526_a(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 156 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 161 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean func_181638_a(World p_181638_1_, BlockPos p_181638_2_, int p_181638_3_)
/*     */   {
/* 167 */     int i = p_181638_2_.getX();
/* 168 */     int j = p_181638_2_.getY();
/* 169 */     int k = p_181638_2_.getZ();
/* 170 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 172 */     for (int l = 0; l <= p_181638_3_ + 1; l++)
/*     */     {
/* 174 */       int i1 = 1;
/*     */       
/* 176 */       if (l == 0)
/*     */       {
/* 178 */         i1 = 0;
/*     */       }
/*     */       
/* 181 */       if (l >= p_181638_3_ - 1)
/*     */       {
/* 183 */         i1 = 2;
/*     */       }
/*     */       
/* 186 */       for (int j1 = -i1; j1 <= i1; j1++)
/*     */       {
/* 188 */         for (int k1 = -i1; k1 <= i1; k1++)
/*     */         {
/* 190 */           if (!func_150523_a(p_181638_1_.getBlockState(blockpos$mutableblockpos.func_181079_c(i + j1, j + l, k + k1)).getBlock()))
/*     */           {
/* 192 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     return true;
/*     */   }
/*     */   
/*     */   private void func_181639_b(World p_181639_1_, BlockPos p_181639_2_)
/*     */   {
/* 203 */     if (func_150523_a(p_181639_1_.getBlockState(p_181639_2_).getBlock()))
/*     */     {
/* 205 */       setBlockAndNotifyAdequately(p_181639_1_, p_181639_2_, field_181640_a);
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_150526_a(World worldIn, int p_150526_2_, int p_150526_3_, int p_150526_4_)
/*     */   {
/* 211 */     BlockPos blockpos = new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_);
/* 212 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/* 214 */     if (block.getMaterial() == Material.air)
/*     */     {
/* 216 */       setBlockAndNotifyAdequately(worldIn, blockpos, field_181641_b);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenCanopyTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */