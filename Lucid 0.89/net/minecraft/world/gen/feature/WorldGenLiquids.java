package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator
{
    private Block field_150521_a;

    public WorldGenLiquids(Block p_i45465_1_)
    {
        this.field_150521_a = p_i45465_1_;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        if (worldIn.getBlockState(position.up()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(position.down()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.air && worldIn.getBlockState(position).getBlock() != Blocks.stone)
        {
            return false;
        }
        else
        {
            int var4 = 0;

            if (worldIn.getBlockState(position.west()).getBlock() == Blocks.stone)
            {
                ++var4;
            }

            if (worldIn.getBlockState(position.east()).getBlock() == Blocks.stone)
            {
                ++var4;
            }

            if (worldIn.getBlockState(position.north()).getBlock() == Blocks.stone)
            {
                ++var4;
            }

            if (worldIn.getBlockState(position.south()).getBlock() == Blocks.stone)
            {
                ++var4;
            }

            int var5 = 0;

            if (worldIn.isAirBlock(position.west()))
            {
                ++var5;
            }

            if (worldIn.isAirBlock(position.east()))
            {
                ++var5;
            }

            if (worldIn.isAirBlock(position.north()))
            {
                ++var5;
            }

            if (worldIn.isAirBlock(position.south()))
            {
                ++var5;
            }

            if (var4 == 3 && var5 == 1)
            {
                worldIn.setBlockState(position, this.field_150521_a.getDefaultState(), 2);
                worldIn.forceBlockUpdateTick(this.field_150521_a, position, rand);
            }

            return true;
        }
    }
}
