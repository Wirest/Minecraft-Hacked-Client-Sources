package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator
{

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int var4 = 0; var4 < 10; ++var4)
        {
            BlockPos var5 = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(var5))
            {
                int var6 = 1 + rand.nextInt(rand.nextInt(3) + 1);

                for (int var7 = 0; var7 < var6; ++var7)
                {
                    if (Blocks.cactus.canBlockStay(worldIn, var5))
                    {
                        worldIn.setBlockState(var5.up(var7), Blocks.cactus.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}
