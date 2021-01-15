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
/*     */ public class WorldGenTaiga1 extends WorldGenAbstractTree
/*     */ {
/*  17 */   private static final IBlockState field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
/*  18 */   private static final IBlockState field_181637_b = Blocks.leaves.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*     */   
/*     */   public WorldGenTaiga1()
/*     */   {
/*  22 */     super(false);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  27 */     int i = rand.nextInt(5) + 7;
/*  28 */     int j = i - rand.nextInt(2) - 3;
/*  29 */     int k = i - j;
/*  30 */     int l = 1 + rand.nextInt(k + 1);
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
/*  56 */               if (!func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock()))
/*     */               {
/*  58 */                 flag = false;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/*  63 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  69 */       if (!flag)
/*     */       {
/*  71 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  75 */       Block block = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/*  77 */       if (((block == Blocks.grass) || (block == Blocks.dirt)) && (position.getY() < 256 - i - 1))
/*     */       {
/*  79 */         func_175921_a(worldIn, position.down());
/*  80 */         int k2 = 0;
/*     */         
/*  82 */         for (int l2 = position.getY() + i; l2 >= position.getY() + j; l2--)
/*     */         {
/*  84 */           for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; j3++)
/*     */           {
/*  86 */             int k3 = j3 - position.getX();
/*     */             
/*  88 */             for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; i2++)
/*     */             {
/*  90 */               int j2 = i2 - position.getZ();
/*     */               
/*  92 */               if ((Math.abs(k3) != k2) || (Math.abs(j2) != k2) || (k2 <= 0))
/*     */               {
/*  94 */                 BlockPos blockpos = new BlockPos(j3, l2, i2);
/*     */                 
/*  96 */                 if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*     */                 {
/*  98 */                   setBlockAndNotifyAdequately(worldIn, blockpos, field_181637_b);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 104 */           if ((k2 >= 1) && (l2 == position.getY() + j + 1))
/*     */           {
/* 106 */             k2--;
/*     */           }
/* 108 */           else if (k2 < l)
/*     */           {
/* 110 */             k2++;
/*     */           }
/*     */         }
/*     */         
/* 114 */         for (int i3 = 0; i3 < i - 1; i3++)
/*     */         {
/* 116 */           Block block1 = worldIn.getBlockState(position.up(i3)).getBlock();
/*     */           
/* 118 */           if ((block1.getMaterial() == Material.air) || (block1.getMaterial() == Material.leaves))
/*     */           {
/* 120 */             setBlockAndNotifyAdequately(worldIn, position.up(i3), field_181636_a);
/*     */           }
/*     */         }
/*     */         
/* 124 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 128 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 134 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenTaiga1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */