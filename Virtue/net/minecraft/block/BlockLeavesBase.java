package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block
{
    protected boolean field_150121_P;
    private static final String __OBFID = "CL_00000326";

    protected BlockLeavesBase(Material p_i45433_1_, boolean p_i45433_2_)
    {
        super(p_i45433_1_);
        this.field_150121_P = p_i45433_2_;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return !this.field_150121_P && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
}
