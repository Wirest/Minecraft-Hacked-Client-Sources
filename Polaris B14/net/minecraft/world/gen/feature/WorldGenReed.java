/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenReed extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 13 */     for (int i = 0; i < 20; i++)
/*    */     {
/* 15 */       BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
/*    */       
/* 17 */       if (worldIn.isAirBlock(blockpos))
/*    */       {
/* 19 */         BlockPos blockpos1 = blockpos.down();
/*    */         
/* 21 */         if ((worldIn.getBlockState(blockpos1.west()).getBlock().getMaterial() == net.minecraft.block.material.Material.water) || (worldIn.getBlockState(blockpos1.east()).getBlock().getMaterial() == net.minecraft.block.material.Material.water) || (worldIn.getBlockState(blockpos1.north()).getBlock().getMaterial() == net.minecraft.block.material.Material.water) || (worldIn.getBlockState(blockpos1.south()).getBlock().getMaterial() == net.minecraft.block.material.Material.water))
/*    */         {
/* 23 */           int j = 2 + rand.nextInt(rand.nextInt(3) + 1);
/*    */           
/* 25 */           for (int k = 0; k < j; k++)
/*    */           {
/* 27 */             if (net.minecraft.init.Blocks.reeds.canBlockStay(worldIn, blockpos))
/*    */             {
/* 29 */               worldIn.setBlockState(blockpos.up(k), net.minecraft.init.Blocks.reeds.getDefaultState(), 2);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenReed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */