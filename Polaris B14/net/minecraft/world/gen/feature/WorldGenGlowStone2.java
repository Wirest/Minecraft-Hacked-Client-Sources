/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenGlowStone2 extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 14 */     if (!worldIn.isAirBlock(position))
/*    */     {
/* 16 */       return false;
/*    */     }
/* 18 */     if (worldIn.getBlockState(position.up()).getBlock() != Blocks.netherrack)
/*    */     {
/* 20 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 24 */     worldIn.setBlockState(position, Blocks.glowstone.getDefaultState(), 2);
/*    */     
/* 26 */     for (int i = 0; i < 1500; i++)
/*    */     {
/* 28 */       BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
/*    */       
/* 30 */       if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == net.minecraft.block.material.Material.air)
/*    */       {
/* 32 */         int j = 0;
/*    */         EnumFacing[] arrayOfEnumFacing;
/* 34 */         int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*    */           
/* 36 */           if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.glowstone)
/*    */           {
/* 38 */             j++;
/*    */           }
/*    */           
/* 41 */           if (j > 1) {
/*    */             break;
/*    */           }
/*    */         }
/*    */         
/*    */ 
/* 47 */         if (j == 1)
/*    */         {
/* 49 */           worldIn.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenGlowStone2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */