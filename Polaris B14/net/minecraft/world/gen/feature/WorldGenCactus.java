/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockCactus;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenCactus extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 12 */     for (int i = 0; i < 10; i++)
/*    */     {
/* 14 */       BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
/*    */       
/* 16 */       if (worldIn.isAirBlock(blockpos))
/*    */       {
/* 18 */         int j = 1 + rand.nextInt(rand.nextInt(3) + 1);
/*    */         
/* 20 */         for (int k = 0; k < j; k++)
/*    */         {
/* 22 */           if (net.minecraft.init.Blocks.cactus.canBlockStay(worldIn, blockpos))
/*    */           {
/* 24 */             worldIn.setBlockState(blockpos.up(k), net.minecraft.init.Blocks.cactus.getDefaultState(), 2);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenCactus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */