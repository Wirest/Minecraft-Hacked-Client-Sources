package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator {
    private static final String __OBFID = "CL_00000189";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        for (int var4 = 0; var4 < 10; ++var4) {
            int var5 = p_180709_3_.getX() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);
            int var6 = p_180709_3_.getY() + p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4);
            int var7 = p_180709_3_.getZ() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);

            if (worldIn.isAirBlock(new BlockPos(var5, var6, var7)) && Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(var5, var6, var7))) {
                worldIn.setBlockState(new BlockPos(var5, var6, var7), Blocks.waterlily.getDefaultState(), 2);
            }
        }

        return true;
    }
}
