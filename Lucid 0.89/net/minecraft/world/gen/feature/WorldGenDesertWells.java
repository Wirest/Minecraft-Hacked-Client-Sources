package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Predicates;

import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState field_175911_b;
    private final IBlockState field_175912_c;
    private final IBlockState field_175910_d;

    public WorldGenDesertWells()
    {
        this.field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.field_175912_c = Blocks.sandstone.getDefaultState();
        this.field_175910_d = Blocks.flowing_water.getDefaultState();
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }

        if (!field_175913_a.matchesState(worldIn.getBlockState(position)))
        {
            return false;
        }
        else
        {
            int var4;
            int var5;

            for (var4 = -2; var4 <= 2; ++var4)
            {
                for (var5 = -2; var5 <= 2; ++var5)
                {
                    if (worldIn.isAirBlock(position.add(var4, -1, var5)) && worldIn.isAirBlock(position.add(var4, -2, var5)))
                    {
                        return false;
                    }
                }
            }

            for (var4 = -1; var4 <= 0; ++var4)
            {
                for (var5 = -2; var5 <= 2; ++var5)
                {
                    for (int var6 = -2; var6 <= 2; ++var6)
                    {
                        worldIn.setBlockState(position.add(var5, var4, var6), this.field_175912_c, 2);
                    }
                }
            }

            worldIn.setBlockState(position, this.field_175910_d, 2);
            Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var7.hasNext())
            {
                EnumFacing var8 = (EnumFacing)var7.next();
                worldIn.setBlockState(position.offset(var8), this.field_175910_d, 2);
            }

            for (var4 = -2; var4 <= 2; ++var4)
            {
                for (var5 = -2; var5 <= 2; ++var5)
                {
                    if (var4 == -2 || var4 == 2 || var5 == -2 || var5 == 2)
                    {
                        worldIn.setBlockState(position.add(var4, 1, var5), this.field_175912_c, 2);
                    }
                }
            }

            worldIn.setBlockState(position.add(2, 1, 0), this.field_175911_b, 2);
            worldIn.setBlockState(position.add(-2, 1, 0), this.field_175911_b, 2);
            worldIn.setBlockState(position.add(0, 1, 2), this.field_175911_b, 2);
            worldIn.setBlockState(position.add(0, 1, -2), this.field_175911_b, 2);

            for (var4 = -1; var4 <= 1; ++var4)
            {
                for (var5 = -1; var5 <= 1; ++var5)
                {
                    if (var4 == 0 && var5 == 0)
                    {
                        worldIn.setBlockState(position.add(var4, 4, var5), this.field_175912_c, 2);
                    }
                    else
                    {
                        worldIn.setBlockState(position.add(var4, 4, var5), this.field_175911_b, 2);
                    }
                }
            }

            for (var4 = 1; var4 <= 3; ++var4)
            {
                worldIn.setBlockState(position.add(-1, var4, -1), this.field_175912_c, 2);
                worldIn.setBlockState(position.add(-1, var4, 1), this.field_175912_c, 2);
                worldIn.setBlockState(position.add(1, var4, -1), this.field_175912_c, 2);
                worldIn.setBlockState(position.add(1, var4, 1), this.field_175912_c, 2);
            }

            return true;
        }
    }
}
