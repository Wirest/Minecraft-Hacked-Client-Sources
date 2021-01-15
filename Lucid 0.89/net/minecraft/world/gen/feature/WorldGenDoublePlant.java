package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator
{
    private BlockDoublePlant.EnumPlantType field_150549_a;

    public void func_180710_a(BlockDoublePlant.EnumPlantType p_180710_1_)
    {
        this.field_150549_a = p_180710_1_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        boolean var4 = false;

        for (int var5 = 0; var5 < 64; ++var5)
        {
            BlockPos var6 = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(var6) && (!worldIn.provider.getHasNoSky() || var6.getY() < 254) && Blocks.double_plant.canPlaceBlockAt(worldIn, var6))
            {
                Blocks.double_plant.placeAt(worldIn, var6, this.field_150549_a, 2);
                var4 = true;
            }
        }

        return var4;
    }
}
