package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenGlowStone1 extends WorldGenerator {
    private static final String __OBFID = "CL_00000419";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        if (!worldIn.isAirBlock(p_180709_3_)) {
            return false;
        } else if (worldIn.getBlockState(p_180709_3_.offsetUp()).getBlock() != Blocks.netherrack) {
            return false;
        } else {
            worldIn.setBlockState(p_180709_3_, Blocks.glowstone.getDefaultState(), 2);

            for (int var4 = 0; var4 < 1500; ++var4) {
                BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), -p_180709_2_.nextInt(12), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

                if (worldIn.getBlockState(var5).getBlock().getMaterial() == Material.air) {
                    int var6 = 0;
                    EnumFacing[] var7 = EnumFacing.values();
                    int var8 = var7.length;

                    for (int var9 = 0; var9 < var8; ++var9) {
                        EnumFacing var10 = var7[var9];

                        if (worldIn.getBlockState(var5.offset(var10)).getBlock() == Blocks.glowstone) {
                            ++var6;
                        }

                        if (var6 > 1) {
                            break;
                        }
                    }

                    if (var6 == 1) {
                        worldIn.setBlockState(var5, Blocks.glowstone.getDefaultState(), 2);
                    }
                }
            }

            return true;
        }
    }
}
