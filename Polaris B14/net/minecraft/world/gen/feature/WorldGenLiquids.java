/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenLiquids extends WorldGenerator
/*    */ {
/*    */   private Block block;
/*    */   
/*    */   public WorldGenLiquids(Block p_i45465_1_)
/*    */   {
/* 16 */     this.block = p_i45465_1_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 21 */     if (worldIn.getBlockState(position.up()).getBlock() != Blocks.stone)
/*    */     {
/* 23 */       return false;
/*    */     }
/* 25 */     if (worldIn.getBlockState(position.down()).getBlock() != Blocks.stone)
/*    */     {
/* 27 */       return false;
/*    */     }
/* 29 */     if ((worldIn.getBlockState(position).getBlock().getMaterial() != net.minecraft.block.material.Material.air) && (worldIn.getBlockState(position).getBlock() != Blocks.stone))
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 35 */     int i = 0;
/*    */     
/* 37 */     if (worldIn.getBlockState(position.west()).getBlock() == Blocks.stone)
/*    */     {
/* 39 */       i++;
/*    */     }
/*    */     
/* 42 */     if (worldIn.getBlockState(position.east()).getBlock() == Blocks.stone)
/*    */     {
/* 44 */       i++;
/*    */     }
/*    */     
/* 47 */     if (worldIn.getBlockState(position.north()).getBlock() == Blocks.stone)
/*    */     {
/* 49 */       i++;
/*    */     }
/*    */     
/* 52 */     if (worldIn.getBlockState(position.south()).getBlock() == Blocks.stone)
/*    */     {
/* 54 */       i++;
/*    */     }
/*    */     
/* 57 */     int j = 0;
/*    */     
/* 59 */     if (worldIn.isAirBlock(position.west()))
/*    */     {
/* 61 */       j++;
/*    */     }
/*    */     
/* 64 */     if (worldIn.isAirBlock(position.east()))
/*    */     {
/* 66 */       j++;
/*    */     }
/*    */     
/* 69 */     if (worldIn.isAirBlock(position.north()))
/*    */     {
/* 71 */       j++;
/*    */     }
/*    */     
/* 74 */     if (worldIn.isAirBlock(position.south()))
/*    */     {
/* 76 */       j++;
/*    */     }
/*    */     
/* 79 */     if ((i == 3) && (j == 1))
/*    */     {
/* 81 */       worldIn.setBlockState(position, this.block.getDefaultState(), 2);
/* 82 */       worldIn.forceBlockUpdateTick(this.block, position, rand);
/*    */     }
/*    */     
/* 85 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenLiquids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */