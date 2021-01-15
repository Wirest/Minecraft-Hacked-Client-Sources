package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator
{

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        Block var4;

        while (((var4 = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && position.getY() > 0)
        {
            position = position.down();
        }

        for (int var5 = 0; var5 < 4; ++var5)
        {
            BlockPos var6 = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(var6) && Blocks.deadbush.canBlockStay(worldIn, var6, Blocks.deadbush.getDefaultState()))
            {
                worldIn.setBlockState(var6, Blocks.deadbush.getDefaultState(), 2);
            }
        }

        return true;
    }
}
