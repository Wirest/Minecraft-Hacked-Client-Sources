package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator {
   public boolean generate(World worldIn, Random rand, BlockPos position) {
      if (!worldIn.isAirBlock(position)) {
         return false;
      } else if (worldIn.getBlockState(position.up()).getBlock() != Blocks.netherrack) {
         return false;
      } else {
         worldIn.setBlockState(position, Blocks.glowstone.getDefaultState(), 2);

         for(int i = 0; i < 1500; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
               int j = 0;
               EnumFacing[] var7 = EnumFacing.values();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  EnumFacing enumfacing = var7[var9];
                  if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.glowstone) {
                     ++j;
                  }

                  if (j > 1) {
                     break;
                  }
               }

               if (j == 1) {
                  worldIn.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);
               }
            }
         }

         return true;
      }
   }
}
