/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigMushroom;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ 
/*     */ public class BlockMushroom extends BlockBush implements IGrowable
/*     */ {
/*     */   protected BlockMushroom()
/*     */   {
/*  15 */     float f = 0.2F;
/*  16 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
/*  17 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  22 */     if (rand.nextInt(25) == 0)
/*     */     {
/*  24 */       int i = 5;
/*  25 */       int j = 4;
/*     */       
/*  27 */       for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4)))
/*     */       {
/*  29 */         if (worldIn.getBlockState(blockpos).getBlock() == this)
/*     */         {
/*  31 */           i--;
/*     */           
/*  33 */           if (i <= 0)
/*     */           {
/*  35 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  40 */       BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
/*     */       
/*  42 */       for (int k = 0; k < 4; k++)
/*     */       {
/*  44 */         if ((worldIn.isAirBlock(blockpos1)) && (canBlockStay(worldIn, blockpos1, getDefaultState())))
/*     */         {
/*  46 */           pos = blockpos1;
/*     */         }
/*     */         
/*  49 */         blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
/*     */       }
/*     */       
/*  52 */       if ((worldIn.isAirBlock(blockpos1)) && (canBlockStay(worldIn, blockpos1, getDefaultState())))
/*     */       {
/*  54 */         worldIn.setBlockState(blockpos1, getDefaultState(), 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  61 */     return (super.canPlaceBlockAt(worldIn, pos)) && (canBlockStay(worldIn, pos, getDefaultState()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canPlaceBlockOn(Block ground)
/*     */   {
/*  69 */     return ground.isFullBlock();
/*     */   }
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  74 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*     */     {
/*  76 */       IBlockState iblockstate = worldIn.getBlockState(pos.down());
/*  77 */       return iblockstate.getBlock() == Blocks.mycelium;
/*     */     }
/*     */     
/*     */ 
/*  81 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean generateBigMushroom(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  87 */     worldIn.setBlockToAir(pos);
/*  88 */     WorldGenerator worldgenerator = null;
/*     */     
/*  90 */     if (this == Blocks.brown_mushroom)
/*     */     {
/*  92 */       worldgenerator = new WorldGenBigMushroom(Blocks.brown_mushroom_block);
/*     */     }
/*  94 */     else if (this == Blocks.red_mushroom)
/*     */     {
/*  96 */       worldgenerator = new WorldGenBigMushroom(Blocks.red_mushroom_block);
/*     */     }
/*     */     
/*  99 */     if ((worldgenerator != null) && (worldgenerator.generate(worldIn, rand, pos)))
/*     */     {
/* 101 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 105 */     worldIn.setBlockState(pos, state, 3);
/* 106 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 120 */     return rand.nextFloat() < 0.4D;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 125 */     generateBigMushroom(worldIn, pos, state, rand);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */