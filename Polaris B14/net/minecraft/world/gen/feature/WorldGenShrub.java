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
/*    */ public class WorldGenShrub extends WorldGenTrees
/*    */ {
/*    */   private final IBlockState leavesMetadata;
/*    */   private final IBlockState woodMetadata;
/*    */   
/*    */   public WorldGenShrub(IBlockState p_i46450_1_, IBlockState p_i46450_2_)
/*    */   {
/* 18 */     super(false);
/* 19 */     this.woodMetadata = p_i46450_1_;
/* 20 */     this.leavesMetadata = p_i46450_2_;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/*    */     Block block;
/* 27 */     while ((((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air) || (block.getMaterial() == Material.leaves)) && (position.getY() > 0)) {
/*    */       Block block;
/* 29 */       position = position.down();
/*    */     }
/*    */     
/* 32 */     Block block1 = worldIn.getBlockState(position).getBlock();
/*    */     
/* 34 */     if ((block1 == Blocks.dirt) || (block1 == Blocks.grass))
/*    */     {
/* 36 */       position = position.up();
/* 37 */       setBlockAndNotifyAdequately(worldIn, position, this.woodMetadata);
/*    */       
/* 39 */       for (int i = position.getY(); i <= position.getY() + 2; i++)
/*    */       {
/* 41 */         int j = i - position.getY();
/* 42 */         int k = 2 - j;
/*    */         
/* 44 */         for (int l = position.getX() - k; l <= position.getX() + k; l++)
/*    */         {
/* 46 */           int i1 = l - position.getX();
/*    */           
/* 48 */           for (int j1 = position.getZ() - k; j1 <= position.getZ() + k; j1++)
/*    */           {
/* 50 */             int k1 = j1 - position.getZ();
/*    */             
/* 52 */             if ((Math.abs(i1) != k) || (Math.abs(k1) != k) || (rand.nextInt(2) != 0))
/*    */             {
/* 54 */               BlockPos blockpos = new BlockPos(l, i, j1);
/*    */               
/* 56 */               if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*    */               {
/* 58 */                 setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 66 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenShrub.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */