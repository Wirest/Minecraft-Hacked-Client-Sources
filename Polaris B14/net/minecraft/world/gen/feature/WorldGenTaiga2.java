/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockOldLog;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenTaiga2 extends WorldGenAbstractTree
/*     */ {
/*  17 */   private static final IBlockState field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
/*  18 */   private static final IBlockState field_181646_b = Blocks.leaves.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */   public WorldGenTaiga2(boolean p_i2025_1_)
/*     */   {
/*  22 */     super(p_i2025_1_);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  27 */     int i = rand.nextInt(4) + 6;
/*  28 */     int j = 1 + rand.nextInt(2);
/*  29 */     int k = i - j;
/*  30 */     int l = 2 + rand.nextInt(2);
/*  31 */     boolean flag = true;
/*     */     
/*  33 */     if ((position.getY() >= 1) && (position.getY() + i + 1 <= 256))
/*     */     {
/*  35 */       for (int i1 = position.getY(); (i1 <= position.getY() + 1 + i) && (flag); i1++)
/*     */       {
/*  37 */         int j1 = 1;
/*     */         
/*  39 */         if (i1 - position.getY() < j)
/*     */         {
/*  41 */           j1 = 0;
/*     */         }
/*     */         else
/*     */         {
/*  45 */           j1 = l;
/*     */         }
/*     */         
/*  48 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  50 */         for (int k1 = position.getX() - j1; (k1 <= position.getX() + j1) && (flag); k1++)
/*     */         {
/*  52 */           for (int l1 = position.getZ() - j1; (l1 <= position.getZ() + j1) && (flag); l1++)
/*     */           {
/*  54 */             if ((i1 >= 0) && (i1 < 256))
/*     */             {
/*  56 */               Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock();
/*     */               
/*  58 */               if ((block.getMaterial() != Material.air) && (block.getMaterial() != Material.leaves))
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
/*  79 */       if (((block1 == Blocks.grass) || (block1 == Blocks.dirt) || (block1 == Blocks.farmland)) && (position.getY() < 256 - i - 1))
/*     */       {
/*  81 */         func_175921_a(worldIn, position.down());
/*  82 */         int i3 = rand.nextInt(2);
/*  83 */         int j3 = 1;
/*  84 */         int k3 = 0;
/*     */         
/*  86 */         for (int l3 = 0; l3 <= k; l3++)
/*     */         {
/*  88 */           int j4 = position.getY() + i - l3;
/*     */           
/*  90 */           for (int i2 = position.getX() - i3; i2 <= position.getX() + i3; i2++)
/*     */           {
/*  92 */             int j2 = i2 - position.getX();
/*     */             
/*  94 */             for (int k2 = position.getZ() - i3; k2 <= position.getZ() + i3; k2++)
/*     */             {
/*  96 */               int l2 = k2 - position.getZ();
/*     */               
/*  98 */               if ((Math.abs(j2) != i3) || (Math.abs(l2) != i3) || (i3 <= 0))
/*     */               {
/* 100 */                 BlockPos blockpos = new BlockPos(i2, j4, k2);
/*     */                 
/* 102 */                 if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*     */                 {
/* 104 */                   setBlockAndNotifyAdequately(worldIn, blockpos, field_181646_b);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 110 */           if (i3 >= j3)
/*     */           {
/* 112 */             i3 = k3;
/* 113 */             k3 = 1;
/* 114 */             j3++;
/*     */             
/* 116 */             if (j3 > l)
/*     */             {
/* 118 */               j3 = l;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 123 */             i3++;
/*     */           }
/*     */         }
/*     */         
/* 127 */         int i4 = rand.nextInt(3);
/*     */         
/* 129 */         for (int k4 = 0; k4 < i - i4; k4++)
/*     */         {
/* 131 */           Block block2 = worldIn.getBlockState(position.up(k4)).getBlock();
/*     */           
/* 133 */           if ((block2.getMaterial() == Material.air) || (block2.getMaterial() == Material.leaves))
/*     */           {
/* 135 */             setBlockAndNotifyAdequately(worldIn, position.up(k4), field_181645_a);
/*     */           }
/*     */         }
/*     */         
/* 139 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 143 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 149 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenTaiga2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */