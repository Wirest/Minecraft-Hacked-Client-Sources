/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCocoa;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.BlockVine;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenTrees extends WorldGenAbstractTree
/*     */ {
/*  21 */   private static final IBlockState field_181653_a = Blocks.log.getDefaultState().withProperty(net.minecraft.block.BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
/*  22 */   private static final IBlockState field_181654_b = Blocks.leaves.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */ 
/*     */   private final int minTreeHeight;
/*     */   
/*     */ 
/*     */   private final boolean vinesGrow;
/*     */   
/*     */ 
/*     */   private final IBlockState metaWood;
/*     */   
/*     */   private final IBlockState metaLeaves;
/*     */   
/*     */ 
/*     */   public WorldGenTrees(boolean p_i2027_1_)
/*     */   {
/*  38 */     this(p_i2027_1_, 4, field_181653_a, field_181654_b, false);
/*     */   }
/*     */   
/*     */   public WorldGenTrees(boolean p_i46446_1_, int p_i46446_2_, IBlockState p_i46446_3_, IBlockState p_i46446_4_, boolean p_i46446_5_)
/*     */   {
/*  43 */     super(p_i46446_1_);
/*  44 */     this.minTreeHeight = p_i46446_2_;
/*  45 */     this.metaWood = p_i46446_3_;
/*  46 */     this.metaLeaves = p_i46446_4_;
/*  47 */     this.vinesGrow = p_i46446_5_;
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  52 */     int i = rand.nextInt(3) + this.minTreeHeight;
/*  53 */     boolean flag = true;
/*     */     
/*  55 */     if ((position.getY() >= 1) && (position.getY() + i + 1 <= 256))
/*     */     {
/*  57 */       for (int j = position.getY(); j <= position.getY() + 1 + i; j++)
/*     */       {
/*  59 */         int k = 1;
/*     */         
/*  61 */         if (j == position.getY())
/*     */         {
/*  63 */           k = 0;
/*     */         }
/*     */         
/*  66 */         if (j >= position.getY() + 1 + i - 2)
/*     */         {
/*  68 */           k = 2;
/*     */         }
/*     */         
/*  71 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  73 */         for (int l = position.getX() - k; (l <= position.getX() + k) && (flag); l++)
/*     */         {
/*  75 */           for (int i1 = position.getZ() - k; (i1 <= position.getZ() + k) && (flag); i1++)
/*     */           {
/*  77 */             if ((j >= 0) && (j < 256))
/*     */             {
/*  79 */               if (!func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i1)).getBlock()))
/*     */               {
/*  81 */                 flag = false;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/*  86 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  92 */       if (!flag)
/*     */       {
/*  94 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  98 */       Block block1 = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/* 100 */       if (((block1 == Blocks.grass) || (block1 == Blocks.dirt) || (block1 == Blocks.farmland)) && (position.getY() < 256 - i - 1))
/*     */       {
/* 102 */         func_175921_a(worldIn, position.down());
/* 103 */         int k2 = 3;
/* 104 */         int l2 = 0;
/*     */         
/* 106 */         for (int i3 = position.getY() - k2 + i; i3 <= position.getY() + i; i3++)
/*     */         {
/* 108 */           int i4 = i3 - (position.getY() + i);
/* 109 */           int j1 = l2 + 1 - i4 / 2;
/*     */           
/* 111 */           for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; k1++)
/*     */           {
/* 113 */             int l1 = k1 - position.getX();
/*     */             
/* 115 */             for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; i2++)
/*     */             {
/* 117 */               int j2 = i2 - position.getZ();
/*     */               
/* 119 */               if ((Math.abs(l1) != j1) || (Math.abs(j2) != j1) || ((rand.nextInt(2) != 0) && (i4 != 0)))
/*     */               {
/* 121 */                 BlockPos blockpos = new BlockPos(k1, i3, i2);
/* 122 */                 Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */                 
/* 124 */                 if ((block.getMaterial() == Material.air) || (block.getMaterial() == Material.leaves) || (block.getMaterial() == Material.vine))
/*     */                 {
/* 126 */                   setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 133 */         for (int j3 = 0; j3 < i; j3++)
/*     */         {
/* 135 */           Block block2 = worldIn.getBlockState(position.up(j3)).getBlock();
/*     */           
/* 137 */           if ((block2.getMaterial() == Material.air) || (block2.getMaterial() == Material.leaves) || (block2.getMaterial() == Material.vine))
/*     */           {
/* 139 */             setBlockAndNotifyAdequately(worldIn, position.up(j3), this.metaWood);
/*     */             
/* 141 */             if ((this.vinesGrow) && (j3 > 0))
/*     */             {
/* 143 */               if ((rand.nextInt(3) > 0) && (worldIn.isAirBlock(position.add(-1, j3, 0))))
/*     */               {
/* 145 */                 func_181651_a(worldIn, position.add(-1, j3, 0), BlockVine.EAST);
/*     */               }
/*     */               
/* 148 */               if ((rand.nextInt(3) > 0) && (worldIn.isAirBlock(position.add(1, j3, 0))))
/*     */               {
/* 150 */                 func_181651_a(worldIn, position.add(1, j3, 0), BlockVine.WEST);
/*     */               }
/*     */               
/* 153 */               if ((rand.nextInt(3) > 0) && (worldIn.isAirBlock(position.add(0, j3, -1))))
/*     */               {
/* 155 */                 func_181651_a(worldIn, position.add(0, j3, -1), BlockVine.SOUTH);
/*     */               }
/*     */               
/* 158 */               if ((rand.nextInt(3) > 0) && (worldIn.isAirBlock(position.add(0, j3, 1))))
/*     */               {
/* 160 */                 func_181651_a(worldIn, position.add(0, j3, 1), BlockVine.NORTH);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 166 */         if (this.vinesGrow) {
/*     */           int k4;
/* 168 */           for (int k3 = position.getY() - 3 + i; k3 <= position.getY() + i; k3++)
/*     */           {
/* 170 */             int j4 = k3 - (position.getY() + i);
/* 171 */             k4 = 2 - j4 / 2;
/* 172 */             BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
/*     */             
/* 174 */             for (int l4 = position.getX() - k4; l4 <= position.getX() + k4; l4++)
/*     */             {
/* 176 */               for (int i5 = position.getZ() - k4; i5 <= position.getZ() + k4; i5++)
/*     */               {
/* 178 */                 blockpos$mutableblockpos1.func_181079_c(l4, k3, i5);
/*     */                 
/* 180 */                 if (worldIn.getBlockState(blockpos$mutableblockpos1).getBlock().getMaterial() == Material.leaves)
/*     */                 {
/* 182 */                   BlockPos blockpos2 = blockpos$mutableblockpos1.west();
/* 183 */                   BlockPos blockpos3 = blockpos$mutableblockpos1.east();
/* 184 */                   BlockPos blockpos4 = blockpos$mutableblockpos1.north();
/* 185 */                   BlockPos blockpos1 = blockpos$mutableblockpos1.south();
/*     */                   
/* 187 */                   if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air))
/*     */                   {
/* 189 */                     func_181650_b(worldIn, blockpos2, BlockVine.EAST);
/*     */                   }
/*     */                   
/* 192 */                   if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air))
/*     */                   {
/* 194 */                     func_181650_b(worldIn, blockpos3, BlockVine.WEST);
/*     */                   }
/*     */                   
/* 197 */                   if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air))
/*     */                   {
/* 199 */                     func_181650_b(worldIn, blockpos4, BlockVine.SOUTH);
/*     */                   }
/*     */                   
/* 202 */                   if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air))
/*     */                   {
/* 204 */                     func_181650_b(worldIn, blockpos1, BlockVine.NORTH);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 211 */           if ((rand.nextInt(5) == 0) && (i > 5))
/*     */           {
/* 213 */             for (int l3 = 0; l3 < 2; l3++)
/*     */             {
/* 215 */               for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */               {
/* 217 */                 EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */                 
/* 219 */                 if (rand.nextInt(4 - l3) == 0)
/*     */                 {
/* 221 */                   EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 222 */                   func_181652_a(worldIn, rand.nextInt(3), position.add(enumfacing1.getFrontOffsetX(), i - 5 + l3, enumfacing1.getFrontOffsetZ()), enumfacing);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 229 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 233 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 239 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_181652_a(World p_181652_1_, int p_181652_2_, BlockPos p_181652_3_, EnumFacing p_181652_4_)
/*     */   {
/* 245 */     setBlockAndNotifyAdequately(p_181652_1_, p_181652_3_, Blocks.cocoa.getDefaultState().withProperty(BlockCocoa.AGE, Integer.valueOf(p_181652_2_)).withProperty(BlockCocoa.FACING, p_181652_4_));
/*     */   }
/*     */   
/*     */   private void func_181651_a(World p_181651_1_, BlockPos p_181651_2_, PropertyBool p_181651_3_)
/*     */   {
/* 250 */     setBlockAndNotifyAdequately(p_181651_1_, p_181651_2_, Blocks.vine.getDefaultState().withProperty(p_181651_3_, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   private void func_181650_b(World p_181650_1_, BlockPos p_181650_2_, PropertyBool p_181650_3_)
/*     */   {
/* 255 */     func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
/* 256 */     int i = 4;
/*     */     
/* 258 */     for (p_181650_2_ = p_181650_2_.down(); (p_181650_1_.getBlockState(p_181650_2_).getBlock().getMaterial() == Material.air) && (i > 0); i--)
/*     */     {
/* 260 */       func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
/* 261 */       p_181650_2_ = p_181650_2_.down();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */