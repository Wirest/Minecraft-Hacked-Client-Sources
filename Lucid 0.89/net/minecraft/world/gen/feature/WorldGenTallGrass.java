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

    public WorldGenTallGrass(BlockTallGrass.EnumType p_i45629_1_)
    {
        this.field_175907_a = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, p_i45629_1_);
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        Block var4;

        while (((var4 = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && position.getY() > 0)
        {
            position = position.down();
        }

        for (int var5 = 0; var5 < 128; ++var5)
        {
            BlockPos var6 = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(var6) && Blocks.tallgrass.canBlockStay(worldIn, var6, this.field_175907_a))
            {
                worldIn.setBlockState(var6, this.field_175907_a, 2);
            }
        }

        return true;
    }
}
