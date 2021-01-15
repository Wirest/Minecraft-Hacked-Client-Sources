/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WorldGenHugeTrees
/*     */   extends WorldGenAbstractTree
/*     */ {
/*     */   protected final int baseHeight;
/*     */   protected final IBlockState woodMetadata;
/*     */   protected final IBlockState leavesMetadata;
/*     */   protected int extraRandomHeight;
/*     */   
/*     */   public WorldGenHugeTrees(boolean p_i46447_1_, int p_i46447_2_, int p_i46447_3_, IBlockState p_i46447_4_, IBlockState p_i46447_5_)
/*     */   {
/*  25 */     super(p_i46447_1_);
/*  26 */     this.baseHeight = p_i46447_2_;
/*  27 */     this.extraRandomHeight = p_i46447_3_;
/*  28 */     this.woodMetadata = p_i46447_4_;
/*  29 */     this.leavesMetadata = p_i46447_5_;
/*     */   }
/*     */   
/*     */   protected int func_150533_a(Random p_150533_1_)
/*     */   {
/*  34 */     int i = p_150533_1_.nextInt(3) + this.baseHeight;
/*     */     
/*  36 */     if (this.extraRandomHeight > 1)
/*     */     {
/*  38 */       i += p_150533_1_.nextInt(this.extraRandomHeight);
/*     */     }
/*     */     
/*  41 */     return i;
/*     */   }
/*     */   
/*     */   private boolean func_175926_c(World worldIn, BlockPos p_175926_2_, int p_175926_3_)
/*     */   {
/*  46 */     boolean flag = true;
/*     */     
/*  48 */     if ((p_175926_2_.getY() >= 1) && (p_175926_2_.getY() + p_175926_3_ + 1 <= 256))
/*     */     {
/*  50 */       for (int i = 0; i <= 1 + p_175926_3_; i++)
/*     */       {
/*  52 */         int j = 2;
/*     */         
/*  54 */         if (i == 0)
/*     */         {
/*  56 */           j = 1;
/*     */         }
/*  58 */         else if (i >= 1 + p_175926_3_ - 2)
/*     */         {
/*  60 */           j = 2;
/*     */         }
/*     */         
/*  63 */         for (int k = -j; (k <= j) && (flag); k++)
/*     */         {
/*  65 */           for (int l = -j; (l <= j) && (flag); l++)
/*     */           {
/*  67 */             if ((p_175926_2_.getY() + i < 0) || (p_175926_2_.getY() + i >= 256) || (!func_150523_a(worldIn.getBlockState(p_175926_2_.add(k, i, l)).getBlock())))
/*     */             {
/*  69 */               flag = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  75 */       return flag;
/*     */     }
/*     */     
/*     */ 
/*  79 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean func_175927_a(BlockPos p_175927_1_, World worldIn)
/*     */   {
/*  85 */     BlockPos blockpos = p_175927_1_.down();
/*  86 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/*  88 */     if (((block == Blocks.grass) || (block == Blocks.dirt)) && (p_175927_1_.getY() >= 2))
/*     */     {
/*  90 */       func_175921_a(worldIn, blockpos);
/*  91 */       func_175921_a(worldIn, blockpos.east());
/*  92 */       func_175921_a(worldIn, blockpos.south());
/*  93 */       func_175921_a(worldIn, blockpos.south().east());
/*  94 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  98 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean func_175929_a(World worldIn, Random p_175929_2_, BlockPos p_175929_3_, int p_175929_4_)
/*     */   {
/* 104 */     return (func_175926_c(worldIn, p_175929_3_, p_175929_4_)) && (func_175927_a(p_175929_3_, worldIn));
/*     */   }
/*     */   
/*     */   protected void func_175925_a(World worldIn, BlockPos p_175925_2_, int p_175925_3_)
/*     */   {
/* 109 */     int i = p_175925_3_ * p_175925_3_;
/*     */     
/* 111 */     for (int j = -p_175925_3_; j <= p_175925_3_ + 1; j++)
/*     */     {
/* 113 */       for (int k = -p_175925_3_; k <= p_175925_3_ + 1; k++)
/*     */       {
/* 115 */         int l = j - 1;
/* 116 */         int i1 = k - 1;
/*     */         
/* 118 */         if ((j * j + k * k <= i) || (l * l + i1 * i1 <= i) || (j * j + i1 * i1 <= i) || (l * l + k * k <= i))
/*     */         {
/* 120 */           BlockPos blockpos = p_175925_2_.add(j, 0, k);
/* 121 */           Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 123 */           if ((material == Material.air) || (material == Material.leaves))
/*     */           {
/* 125 */             setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_175928_b(World worldIn, BlockPos p_175928_2_, int p_175928_3_)
/*     */   {
/* 134 */     int i = p_175928_3_ * p_175928_3_;
/*     */     
/* 136 */     for (int j = -p_175928_3_; j <= p_175928_3_; j++)
/*     */     {
/* 138 */       for (int k = -p_175928_3_; k <= p_175928_3_; k++)
/*     */       {
/* 140 */         if (j * j + k * k <= i)
/*     */         {
/* 142 */           BlockPos blockpos = p_175928_2_.add(j, 0, k);
/* 143 */           Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 145 */           if ((material == Material.air) || (material == Material.leaves))
/*     */           {
/* 147 */             setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenHugeTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */