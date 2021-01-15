package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTallGrass extends WorldGenerator
{
    private final IBlockState field_175907_a;
    private static final String __OBFID = "CL_00000437";

    public WorldGenTallGrass(BlockTallGrass.EnumType p_i45629_1_)
    {
        this.field_175907_a = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.field_176497_a, p_i45629_1_);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Block var4;

        while (((var4 = worldIn.getBlockState(p_180709_3_).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && p_180709_3_.getY() > 0)
        {
            p_180709_3_ = p_180709_3_.offsetDown();
        }

        for (int var5 = 0; var5 < 128; ++var5)
        {
            BlockPos var6 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(var6) && Blocks.tallgrass.canBlockStay(worldIn, var6, this.field_175907_a))
            {
                worldIn.setBlockState(var6, this.field_175907_a, 2);
            }
        }

        return true;
    }
}
