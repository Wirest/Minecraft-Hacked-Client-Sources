package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator
{
    private BlockDoublePlant.EnumPlantType field_150549_a;
    private static final String __OBFID = "CL_00000408";

    public void func_180710_a(BlockDoublePlant.EnumPlantType p_180710_1_)
    {
        this.field_150549_a = p_180710_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        boolean var4 = false;

        for (int var5 = 0; var5 < 64; ++var5)
        {
            BlockPos var6 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(var6) && (!worldIn.provider.getHasNoSky() || var6.getY() < 254) && Blocks.double_plant.canPlaceBlockAt(worldIn, var6))
            {
                Blocks.double_plant.func_176491_a(worldIn, var6, this.field_150549_a, 2);
                var4 = true;
            }
        }

        return var4;
    }
}
