/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenSand
/*    */   extends WorldGenerator
/*    */ {
/*    */   private Block block;
/*    */   private int radius;
/*    */   
/*    */   public WorldGenSand(Block p_i45462_1_, int p_i45462_2_)
/*    */   {
/* 19 */     this.block = p_i45462_1_;
/* 20 */     this.radius = p_i45462_2_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 25 */     if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water)
/*    */     {
/* 27 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 31 */     int i = rand.nextInt(this.radius - 2) + 2;
/* 32 */     int j = 2;
/*    */     
/* 34 */     for (int k = position.getX() - i; k <= position.getX() + i; k++)
/*    */     {
/* 36 */       for (int l = position.getZ() - i; l <= position.getZ() + i; l++)
/*    */       {
/* 38 */         int i1 = k - position.getX();
/* 39 */         int j1 = l - position.getZ();
/*    */         
/* 41 */         if (i1 * i1 + j1 * j1 <= i * i)
/*    */         {
/* 43 */           for (int k1 = position.getY() - j; k1 <= position.getY() + j; k1++)
/*    */           {
/* 45 */             BlockPos blockpos = new BlockPos(k, k1, l);
/* 46 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*    */             
/* 48 */             if ((block == Blocks.dirt) || (block == Blocks.grass))
/*    */             {
/* 50 */               worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */