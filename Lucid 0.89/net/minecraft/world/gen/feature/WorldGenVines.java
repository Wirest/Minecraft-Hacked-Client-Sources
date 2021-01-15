package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator
{

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (; position.getY() < 128; position = position.up())
        {
            if (worldIn.isAirBlock(position))
            {
                EnumFacing[] var4 = EnumFacing.Plane.HORIZONTAL.facings();
                int var5 = var4.length;

                for (int var6 = 0; var6 < var5; ++var6)
                {
                    EnumFacing var7 = var4[var6];

                    if (Blocks.vine.canPlaceBlockOnSide(worldIn, position, var7))
                    {
                        IBlockState var8 = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(var7 == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(var7 == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(var7 == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(var7 == EnumFacing.WEST));
                        worldIn.setBlockState(position, var8, 2);
                        break;
                    }
                }
            }
            else
            {
                position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            }
        }

        return true;
    }
}
