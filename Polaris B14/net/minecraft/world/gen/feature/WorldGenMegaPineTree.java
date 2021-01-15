/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDirt.DirtType;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockOldLeaf;
/*     */ import net.minecraft.block.BlockOldLog;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenMegaPineTree extends WorldGenHugeTrees
/*     */ {
/*  19 */   private static final IBlockState field_181633_e = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
/*  20 */   private static final IBlockState field_181634_f = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*  21 */   private static final IBlockState field_181635_g = Blocks.dirt.getDefaultState().withProperty(net.minecraft.block.BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
/*     */   private boolean useBaseHeight;
/*     */   
/*     */   public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_)
/*     */   {
/*  26 */     super(p_i45457_1_, 13, 15, field_181633_e, field_181634_f);
/*  27 */     this.useBaseHeight = p_i45457_2_;
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  32 */     int i = func_150533_a(rand);
/*     */     
/*  34 */     if (!func_175929_a(worldIn, rand, position, i))
/*     */     {
/*  36 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  40 */     func_150541_c(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);
/*     */     
/*  42 */     for (int j = 0; j < i; j++)
/*     */     {
/*  44 */       Block block = worldIn.getBlockState(position.up(j)).getBlock();
/*     */       
/*  46 */       if ((block.getMaterial() == Material.air) || (block.getMaterial() == Material.leaves))
/*     */       {
/*  48 */         setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
/*     */       }
/*     */       
/*  51 */       if (j < i - 1)
/*     */       {
/*  53 */         block = worldIn.getBlockState(position.add(1, j, 0)).getBlock();
/*     */         
/*  55 */         if ((block.getMaterial() == Material.air) || (block.getMaterial() == Material.leaves))
/*     */         {
/*  57 */           setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
/*     */         }
/*     */         
/*  60 */         block = worldIn.getBlockState(position.add(1, j, 1)).getBlock();
/*     */         
/*  62 */         if ((block.getMaterial() == Material.air) || (block.getMaterial() == Material.leaves))
/*     */         {
/*  64 */           setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
/*     */         }
/*     */         
/*  67 */         block = worldIn.getBlockState(position.add(0, j, 1)).getBlock();
/*     */         
/*  69 */         if ((block.getMaterial() == Material.air) || (block.getMaterial() == Material.leaves))
/*     */         {
/*  71 */           setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  76 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_150541_c(World worldIn, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_)
/*     */   {
/*  82 */     int i = p_150541_6_.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
/*  83 */     int j = 0;
/*     */     
/*  85 */     for (int k = p_150541_4_ - i; k <= p_150541_4_; k++)
/*     */     {
/*  87 */       int l = p_150541_4_ - k;
/*  88 */       int i1 = p_150541_5_ + MathHelper.floor_float(l / i * 3.5F);
/*  89 */       func_175925_a(worldIn, new BlockPos(p_150541_2_, k, p_150541_3_), i1 + ((l > 0) && (i1 == j) && ((k & 0x1) == 0) ? 1 : 0));
/*  90 */       j = i1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_180711_a(World worldIn, Random p_180711_2_, BlockPos p_180711_3_)
/*     */   {
/*  96 */     func_175933_b(worldIn, p_180711_3_.west().north());
/*  97 */     func_175933_b(worldIn, p_180711_3_.east(2).north());
/*  98 */     func_175933_b(worldIn, p_180711_3_.west().south(2));
/*  99 */     func_175933_b(worldIn, p_180711_3_.east(2).south(2));
/*     */     
/* 101 */     for (int i = 0; i < 5; i++)
/*     */     {
/* 103 */       int j = p_180711_2_.nextInt(64);
/* 104 */       int k = j % 8;
/* 105 */       int l = j / 8;
/*     */       
/* 107 */       if ((k == 0) || (k == 7) || (l == 0) || (l == 7))
/*     */       {
/* 109 */         func_175933_b(worldIn, p_180711_3_.add(-3 + k, 0, -3 + l));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_175933_b(World worldIn, BlockPos p_175933_2_)
/*     */   {
/* 116 */     for (int i = -2; i <= 2; i++)
/*     */     {
/* 118 */       for (int j = -2; j <= 2; j++)
/*     */       {
/* 120 */         if ((Math.abs(i) != 2) || (Math.abs(j) != 2))
/*     */         {
/* 122 */           func_175934_c(worldIn, p_175933_2_.add(i, 0, j));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_175934_c(World worldIn, BlockPos p_175934_2_)
/*     */   {
/* 130 */     for (int i = 2; i >= -3; i--)
/*     */     {
/* 132 */       BlockPos blockpos = p_175934_2_.up(i);
/* 133 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/* 135 */       if ((block == Blocks.grass) || (block == Blocks.dirt))
/*     */       {
/* 137 */         setBlockAndNotifyAdequately(worldIn, blockpos, field_181635_g);
/*     */       }
/*     */       else
/*     */       {
/* 141 */         if ((block.getMaterial() != Material.air) && (i < 0)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenMegaPineTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */