/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockDeadBush;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenDeadBush extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/*    */     Block block;
/* 16 */     while ((((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air) || (block.getMaterial() == Material.leaves)) && (position.getY() > 0)) {
/*    */       Block block;
/* 18 */       position = position.down();
/*    */     }
/*    */     
/* 21 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 23 */       BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
/*    */       
/* 25 */       if ((worldIn.isAirBlock(blockpos)) && (Blocks.deadbush.canBlockStay(worldIn, blockpos, Blocks.deadbush.getDefaultState())))
/*    */       {
/* 27 */         worldIn.setBlockState(blockpos, Blocks.deadbush.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */     
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenDeadBush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */