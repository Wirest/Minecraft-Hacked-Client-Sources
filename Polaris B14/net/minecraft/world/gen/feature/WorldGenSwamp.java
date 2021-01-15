/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockOldLeaf;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.BlockVine;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenSwamp extends WorldGenAbstractTree
/*     */ {
/*  18 */   private static final IBlockState field_181648_a = Blocks.log.getDefaultState().withProperty(net.minecraft.block.BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
/*  19 */   private static final IBlockState field_181649_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */   public WorldGenSwamp()
/*     */   {
/*  23 */     super(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  30 */     for (int i = rand.nextInt(4) + 5; worldIn.getBlockState(position.down()).getBlock().getMaterial() == Material.water; position = position.down()) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  35 */     boolean flag = true;
/*     */     
/*  37 */     if ((position.getY() >= 1) && (position.getY() + i + 1 <= 256))
/*     */     {
/*  39 */       for (int j = position.getY(); j <= position.getY() + 1 + i; j++)
/*     */       {
/*  41 */         int k = 1;
/*     */         
/*  43 */         if (j == position.getY())
/*     */         {
/*  45 */           k = 0;
/*     */         }
/*     */         
/*  48 */         if (j >= position.getY() + 1 + i - 2)
/*     */         {
/*  50 */           k = 3;
/*     */         }
/*     */         
/*  53 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  55 */         for (int l = position.getX() - k; (l <= position.getX() + k) && (flag); l++)
/*     */         {
/*  57 */           for (int i1 = position.getZ() - k; (i1 <= position.getZ() + k) && (flag); i1++)
/*     */           {
/*  59 */             if ((j >= 0) && (j < 256))
/*     */             {
/*  61 */               Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i1)).getBlock();
/*     */               
/*  63 */               if ((block.getMaterial() != Material.air) && (block.getMaterial() != Material.leaves))
/*     */               {
/*  65 */                 if ((block != Blocks.water) && (block != Blocks.flowing_water))
/*     */                 {
/*  67 */                   flag = false;
/*     */                 }
/*  69 */                 else if (j > position.getY())
/*     */                 {
/*  71 */                   flag = false;
/*     */                 }
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/*  77 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  83 */       if (!flag)
/*     */       {
/*  85 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  89 */       Block block1 = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/*  91 */       if (((block1 == Blocks.grass) || (block1 == Blocks.dirt)) && (position.getY() < 256 - i - 1))
/*     */       {
/*  93 */         func_175921_a(worldIn, position.down());
/*     */         
/*  95 */         for (int l1 = position.getY() - 3 + i; l1 <= position.getY() + i; l1++)
/*     */         {
/*  97 */           int k2 = l1 - (position.getY() + i);
/*  98 */           int i3 = 2 - k2 / 2;
/*     */           
/* 100 */           for (int k3 = position.getX() - i3; k3 <= position.getX() + i3; k3++)
/*     */           {
/* 102 */             int l3 = k3 - position.getX();
/*     */             
/* 104 */             for (int j1 = position.getZ() - i3; j1 <= position.getZ() + i3; j1++)
/*     */             {
/* 106 */               int k1 = j1 - position.getZ();
/*     */               
/* 108 */               if ((Math.abs(l3) != i3) || (Math.abs(k1) != i3) || ((rand.nextInt(2) != 0) && (k2 != 0)))
/*     */               {
/* 110 */                 BlockPos blockpos = new BlockPos(k3, l1, j1);
/*     */                 
/* 112 */                 if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*     */                 {
/* 114 */                   setBlockAndNotifyAdequately(worldIn, blockpos, field_181649_b);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 121 */         for (int i2 = 0; i2 < i; i2++)
/*     */         {
/* 123 */           Block block2 = worldIn.getBlockState(position.up(i2)).getBlock();
/*     */           
/* 125 */           if ((block2.getMaterial() == Material.air) || (block2.getMaterial() == Material.leaves) || (block2 == Blocks.flowing_water) || (block2 == Blocks.water))
/*     */           {
/* 127 */             setBlockAndNotifyAdequately(worldIn, position.up(i2), field_181648_a);
/*     */           }
/*     */         }
/*     */         
/* 131 */         for (int j2 = position.getY() - 3 + i; j2 <= position.getY() + i; j2++)
/*     */         {
/* 133 */           int l2 = j2 - (position.getY() + i);
/* 134 */           int j3 = 2 - l2 / 2;
/* 135 */           BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
/*     */           
/* 137 */           for (int i4 = position.getX() - j3; i4 <= position.getX() + j3; i4++)
/*     */           {
/* 139 */             for (int j4 = position.getZ() - j3; j4 <= position.getZ() + j3; j4++)
/*     */             {
/* 141 */               blockpos$mutableblockpos1.func_181079_c(i4, j2, j4);
/*     */               
/* 143 */               if (worldIn.getBlockState(blockpos$mutableblockpos1).getBlock().getMaterial() == Material.leaves)
/*     */               {
/* 145 */                 BlockPos blockpos3 = blockpos$mutableblockpos1.west();
/* 146 */                 BlockPos blockpos4 = blockpos$mutableblockpos1.east();
/* 147 */                 BlockPos blockpos1 = blockpos$mutableblockpos1.north();
/* 148 */                 BlockPos blockpos2 = blockpos$mutableblockpos1.south();
/*     */                 
/* 150 */                 if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air))
/*     */                 {
/* 152 */                   func_181647_a(worldIn, blockpos3, BlockVine.EAST);
/*     */                 }
/*     */                 
/* 155 */                 if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air))
/*     */                 {
/* 157 */                   func_181647_a(worldIn, blockpos4, BlockVine.WEST);
/*     */                 }
/*     */                 
/* 160 */                 if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air))
/*     */                 {
/* 162 */                   func_181647_a(worldIn, blockpos1, BlockVine.SOUTH);
/*     */                 }
/*     */                 
/* 165 */                 if ((rand.nextInt(4) == 0) && (worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air))
/*     */                 {
/* 167 */                   func_181647_a(worldIn, blockpos2, BlockVine.NORTH);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 174 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 178 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 184 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_181647_a(World p_181647_1_, BlockPos p_181647_2_, PropertyBool p_181647_3_)
/*     */   {
/* 190 */     IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty(p_181647_3_, Boolean.valueOf(true));
/* 191 */     setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
/* 192 */     int i = 4;
/*     */     
/* 194 */     for (p_181647_2_ = p_181647_2_.down(); (p_181647_1_.getBlockState(p_181647_2_).getBlock().getMaterial() == Material.air) && (i > 0); i--)
/*     */     {
/* 196 */       setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
/* 197 */       p_181647_2_ = p_181647_2_.down();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenSwamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */