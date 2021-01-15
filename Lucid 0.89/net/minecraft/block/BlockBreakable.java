package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable extends Block
{
    private boolean ignoreSimilarity;

    protected BlockBreakable(Material materialIn, boolean ignoreSimilarityIn)
    {
        super(materialIn);
        this.ignoreSimilarity = ignoreSimilarityIn;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState var4 = worldIn.getBlockState(pos);
        Block var5 = var4.getBlock();

        if (this == Blocks.glass || this == Blocks.stained_glass)
        {
            if (worldIn.getBlockState(pos.offset(side.getOpposite())) != var4)
            {
                return true;
            }

            if (var5 == this)
            {
                return false;
            }
        }

        return !this.ignoreSimilarity && var5 == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
}
