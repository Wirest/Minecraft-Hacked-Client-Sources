package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenFire extends WorldGenerator
{
    private static final String __OBFID = "CL_00000412";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int var4 = 0; var4 < 64; ++var4)
        {
            BlockPos var5 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(var5) && worldIn.getBlockState(var5.offsetDown()).getBlock() == Blocks.netherrack)
            {
                worldIn.setBlockState(var5, Blocks.fire.getDefaultState(), 2);
            }
        }

        return true;
    }
}
