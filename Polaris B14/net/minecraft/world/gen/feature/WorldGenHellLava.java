/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenHellLava extends WorldGenerator
/*    */ {
/*    */   private final Block field_150553_a;
/*    */   private final boolean field_94524_b;
/*    */   
/*    */   public WorldGenHellLava(Block p_i45453_1_, boolean p_i45453_2_)
/*    */   {
/* 17 */     this.field_150553_a = p_i45453_1_;
/* 18 */     this.field_94524_b = p_i45453_2_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 23 */     if (worldIn.getBlockState(position.up()).getBlock() != Blocks.netherrack)
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if ((worldIn.getBlockState(position).getBlock().getMaterial() != net.minecraft.block.material.Material.air) && (worldIn.getBlockState(position).getBlock() != Blocks.netherrack))
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 33 */     int i = 0;
/*    */     
/* 35 */     if (worldIn.getBlockState(position.west()).getBlock() == Blocks.netherrack)
/*    */     {
/* 37 */       i++;
/*    */     }
/*    */     
/* 40 */     if (worldIn.getBlockState(position.east()).getBlock() == Blocks.netherrack)
/*    */     {
/* 42 */       i++;
/*    */     }
/*    */     
/* 45 */     if (worldIn.getBlockState(position.north()).getBlock() == Blocks.netherrack)
/*    */     {
/* 47 */       i++;
/*    */     }
/*    */     
/* 50 */     if (worldIn.getBlockState(position.south()).getBlock() == Blocks.netherrack)
/*    */     {
/* 52 */       i++;
/*    */     }
/*    */     
/* 55 */     if (worldIn.getBlockState(position.down()).getBlock() == Blocks.netherrack)
/*    */     {
/* 57 */       i++;
/*    */     }
/*    */     
/* 60 */     int j = 0;
/*    */     
/* 62 */     if (worldIn.isAirBlock(position.west()))
/*    */     {
/* 64 */       j++;
/*    */     }
/*    */     
/* 67 */     if (worldIn.isAirBlock(position.east()))
/*    */     {
/* 69 */       j++;
/*    */     }
/*    */     
/* 72 */     if (worldIn.isAirBlock(position.north()))
/*    */     {
/* 74 */       j++;
/*    */     }
/*    */     
/* 77 */     if (worldIn.isAirBlock(position.south()))
/*    */     {
/* 79 */       j++;
/*    */     }
/*    */     
/* 82 */     if (worldIn.isAirBlock(position.down()))
/*    */     {
/* 84 */       j++;
/*    */     }
/*    */     
/* 87 */     if (((!this.field_94524_b) && (i == 4) && (j == 1)) || (i == 5))
/*    */     {
/* 89 */       worldIn.setBlockState(position, this.field_150553_a.getDefaultState(), 2);
/* 90 */       worldIn.forceBlockUpdateTick(this.field_150553_a, position, rand);
/*    */     }
/*    */     
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenHellLava.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */