/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenClay extends WorldGenerator
/*    */ {
/* 12 */   private Block field_150546_a = Blocks.clay;
/*    */   
/*    */   private int numberOfBlocks;
/*    */   
/*    */ 
/*    */   public WorldGenClay(int p_i2011_1_)
/*    */   {
/* 19 */     this.numberOfBlocks = p_i2011_1_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 24 */     if (worldIn.getBlockState(position).getBlock().getMaterial() != net.minecraft.block.material.Material.water)
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 30 */     int i = rand.nextInt(this.numberOfBlocks - 2) + 2;
/* 31 */     int j = 1;
/*    */     
/* 33 */     for (int k = position.getX() - i; k <= position.getX() + i; k++)
/*    */     {
/* 35 */       for (int l = position.getZ() - i; l <= position.getZ() + i; l++)
/*    */       {
/* 37 */         int i1 = k - position.getX();
/* 38 */         int j1 = l - position.getZ();
/*    */         
/* 40 */         if (i1 * i1 + j1 * j1 <= i * i)
/*    */         {
/* 42 */           for (int k1 = position.getY() - j; k1 <= position.getY() + j; k1++)
/*    */           {
/* 44 */             BlockPos blockpos = new BlockPos(k, k1, l);
/* 45 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*    */             
/* 47 */             if ((block == Blocks.dirt) || (block == Blocks.clay))
/*    */             {
/* 49 */               worldIn.setBlockState(blockpos, this.field_150546_a.getDefaultState(), 2);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenClay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */