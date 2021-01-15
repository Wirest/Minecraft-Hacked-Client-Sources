package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator
{

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int var4 = 0; var4 < 10; ++var4)
        {
            int var5 = position.getX() + rand.nextInt(8) - rand.nextInt(8);
            int var6 = position.getY() + rand.nextInt(4) - rand.nextInt(4);
            int var7 = position.getZ() + rand.nextInt(8) - rand.nextInt(8);

            if (worldIn.isAirBlock(new BlockPos(var5, var6, var7)) && Blocks.waterlily.canPlaceBlockAt(worldIn, new BlockPos(var5, var6, var7)))
            {
                worldIn.setBlockState(new BlockPos(var5, var6, var7), Blocks.waterlily.getDefaultState(), 2);
            }
        }

        return true;
    }
}
