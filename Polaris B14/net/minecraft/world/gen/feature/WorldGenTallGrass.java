/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockTallGrass;
/*    */ import net.minecraft.block.BlockTallGrass.EnumType;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenTallGrass extends WorldGenerator
/*    */ {
/*    */   private final IBlockState tallGrassState;
/*    */   
/*    */   public WorldGenTallGrass(BlockTallGrass.EnumType p_i45629_1_)
/*    */   {
/* 18 */     this.tallGrassState = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, p_i45629_1_);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/*    */     Block block;
/* 25 */     while ((((block = worldIn.getBlockState(position).getBlock()).getMaterial() == net.minecraft.block.material.Material.air) || (block.getMaterial() == net.minecraft.block.material.Material.leaves)) && (position.getY() > 0)) {
/*    */       Block block;
/* 27 */       position = position.down();
/*    */     }
/*    */     
/* 30 */     for (int i = 0; i < 128; i++)
/*    */     {
/* 32 */       BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
/*    */       
/* 34 */       if ((worldIn.isAirBlock(blockpos)) && (Blocks.tallgrass.canBlockStay(worldIn, blockpos, this.tallGrassState)))
/*    */       {
/* 36 */         worldIn.setBlockState(blockpos, this.tallGrassState, 2);
/*    */       }
/*    */     }
/*    */     
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenTallGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */