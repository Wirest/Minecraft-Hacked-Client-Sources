/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenWaterlily extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 12 */     for (int i = 0; i < 10; i++)
/*    */     {
/* 14 */       int j = position.getX() + rand.nextInt(8) - rand.nextInt(8);
/* 15 */       int k = position.getY() + rand.nextInt(4) - rand.nextInt(4);
/* 16 */       int l = position.getZ() + rand.nextInt(8) - rand.nextInt(8);
/*    */       
/* 18 */       if ((worldIn.isAirBlock(new BlockPos(j, k, l))) && (net.minecraft.init.Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(j, k, l))))
/*    */       {
/* 20 */         worldIn.setBlockState(new BlockPos(j, k, l), net.minecraft.init.Blocks.waterlily.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */     
/* 24 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenWaterlily.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */