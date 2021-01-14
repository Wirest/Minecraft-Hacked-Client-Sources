package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenPumpkin extends WorldGenerator {
    private static final String __OBFID = "CL_00000428";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 64; ++var4) {
            BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(var5) && worldIn.getBlockState(var5.offsetDown()).getBlock() == Blocks.grass && Blocks.pumpkin.canPlaceBlockAt(worldIn, var5)) {
                worldIn.setBlockState(var5, Blocks.pumpkin.getDefaultState().withProperty(BlockPumpkin.AGE, EnumFacing.Plane.HORIZONTAL.random(p_180709_2_)), 2);
            }
        }

        return true;
    }
}
