/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockVine;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenMegaJungle extends WorldGenHugeTrees
/*     */ {
/*     */   public WorldGenMegaJungle(boolean p_i46448_1_, int p_i46448_2_, int p_i46448_3_, IBlockState p_i46448_4_, IBlockState p_i46448_5_)
/*     */   {
/*  16 */     super(p_i46448_1_, p_i46448_2_, p_i46448_3_, p_i46448_4_, p_i46448_5_);
/*     */   }
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*     */   {
/*  21 */     int i = func_150533_a(rand);
/*     */     
/*  23 */     if (!func_175929_a(worldIn, rand, position, i))
/*     */     {
/*  25 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  29 */     func_175930_c(worldIn, position.up(i), 2);
/*     */     
/*  31 */     for (int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4))
/*     */     {
/*  33 */       float f = rand.nextFloat() * 3.1415927F * 2.0F;
/*  34 */       int k = position.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
/*  35 */       int l = position.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);
/*     */       
/*  37 */       for (int i1 = 0; i1 < 5; i1++)
/*     */       {
/*  39 */         k = position.getX() + (int)(1.5F + MathHelper.cos(f) * i1);
/*  40 */         l = position.getZ() + (int)(1.5F + MathHelper.sin(f) * i1);
/*  41 */         setBlockAndNotifyAdequately(worldIn, new BlockPos(k, j - 3 + i1 / 2, l), this.woodMetadata);
/*     */       }
/*     */       
/*  44 */       int j2 = 1 + rand.nextInt(2);
/*  45 */       int j1 = j;
/*     */       
/*  47 */       for (int k1 = j - j2; k1 <= j1; k1++)
/*     */       {
/*  49 */         int l1 = k1 - j1;
/*  50 */         func_175928_b(worldIn, new BlockPos(k, k1, l), 1 - l1);
/*     */       }
/*     */     }
/*     */     
/*  54 */     for (int i2 = 0; i2 < i; i2++)
/*     */     {
/*  56 */       BlockPos blockpos = position.up(i2);
/*     */       
/*  58 */       if (func_150523_a(worldIn.getBlockState(blockpos).getBlock()))
/*     */       {
/*  60 */         setBlockAndNotifyAdequately(worldIn, blockpos, this.woodMetadata);
/*     */         
/*  62 */         if (i2 > 0)
/*     */         {
/*  64 */           func_181632_a(worldIn, rand, blockpos.west(), BlockVine.EAST);
/*  65 */           func_181632_a(worldIn, rand, blockpos.north(), BlockVine.SOUTH);
/*     */         }
/*     */       }
/*     */       
/*  69 */       if (i2 < i - 1)
/*     */       {
/*  71 */         BlockPos blockpos1 = blockpos.east();
/*     */         
/*  73 */         if (func_150523_a(worldIn.getBlockState(blockpos1).getBlock()))
/*     */         {
/*  75 */           setBlockAndNotifyAdequately(worldIn, blockpos1, this.woodMetadata);
/*     */           
/*  77 */           if (i2 > 0)
/*     */           {
/*  79 */             func_181632_a(worldIn, rand, blockpos1.east(), BlockVine.WEST);
/*  80 */             func_181632_a(worldIn, rand, blockpos1.north(), BlockVine.SOUTH);
/*     */           }
/*     */         }
/*     */         
/*  84 */         BlockPos blockpos2 = blockpos.south().east();
/*     */         
/*  86 */         if (func_150523_a(worldIn.getBlockState(blockpos2).getBlock()))
/*     */         {
/*  88 */           setBlockAndNotifyAdequately(worldIn, blockpos2, this.woodMetadata);
/*     */           
/*  90 */           if (i2 > 0)
/*     */           {
/*  92 */             func_181632_a(worldIn, rand, blockpos2.east(), BlockVine.WEST);
/*  93 */             func_181632_a(worldIn, rand, blockpos2.south(), BlockVine.NORTH);
/*     */           }
/*     */         }
/*     */         
/*  97 */         BlockPos blockpos3 = blockpos.south();
/*     */         
/*  99 */         if (func_150523_a(worldIn.getBlockState(blockpos3).getBlock()))
/*     */         {
/* 101 */           setBlockAndNotifyAdequately(worldIn, blockpos3, this.woodMetadata);
/*     */           
/* 103 */           if (i2 > 0)
/*     */           {
/* 105 */             func_181632_a(worldIn, rand, blockpos3.west(), BlockVine.EAST);
/* 106 */             func_181632_a(worldIn, rand, blockpos3.south(), BlockVine.NORTH);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 112 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_181632_a(World p_181632_1_, Random p_181632_2_, BlockPos p_181632_3_, PropertyBool p_181632_4_)
/*     */   {
/* 118 */     if ((p_181632_2_.nextInt(3) > 0) && (p_181632_1_.isAirBlock(p_181632_3_)))
/*     */     {
/* 120 */       setBlockAndNotifyAdequately(p_181632_1_, p_181632_3_, net.minecraft.init.Blocks.vine.getDefaultState().withProperty(p_181632_4_, Boolean.valueOf(true)));
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_175930_c(World worldIn, BlockPos p_175930_2_, int p_175930_3_)
/*     */   {
/* 126 */     int i = 2;
/*     */     
/* 128 */     for (int j = -i; j <= 0; j++)
/*     */     {
/* 130 */       func_175925_a(worldIn, p_175930_2_.up(j), p_175930_3_ + 1 - j);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenMegaJungle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */