/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockVine;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.EnumFacing.Plane;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenVines extends WorldGenerator
/*    */ {
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 15 */     for (; position.getY() < 128; position = position.up())
/*    */     {
/* 17 */       if (worldIn.isAirBlock(position)) {
/*    */         EnumFacing[] arrayOfEnumFacing;
/* 19 */         int j = (arrayOfEnumFacing = EnumFacing.Plane.HORIZONTAL.facings()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*    */           
/* 21 */           if (net.minecraft.init.Blocks.vine.canPlaceBlockOnSide(worldIn, position, enumfacing))
/*    */           {
/* 23 */             IBlockState iblockstate = net.minecraft.init.Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(enumfacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumfacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(enumfacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(enumfacing == EnumFacing.WEST));
/* 24 */             worldIn.setBlockState(position, iblockstate, 2);
/* 25 */             break;
/*    */           }
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 31 */         position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
/*    */       }
/*    */     }
/*    */     
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenVines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */