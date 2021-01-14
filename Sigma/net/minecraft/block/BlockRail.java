package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase {
    public static final PropertyEnum field_176565_b = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);
    private static final String __OBFID = "CL_00000293";

    protected BlockRail() {
        super(false);
        setDefaultState(blockState.getBaseState().withProperty(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
    }

    @Override
    protected void func_176561_b(World worldIn, BlockPos p_176561_2_, IBlockState p_176561_3_, Block p_176561_4_) {
        if (p_176561_4_.canProvidePower() && (new BlockRailBase.Rail(worldIn, p_176561_2_, p_176561_3_)).countAdjacentRails() == 3) {
            func_176564_a(worldIn, p_176561_2_, p_176561_3_, false);
        }
    }

    @Override
    public IProperty func_176560_l() {
        return BlockRail.field_176565_b;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.func_177016_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockRailBase.EnumRailDirection) state.getValue(BlockRail.field_176565_b)).func_177015_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockRail.field_176565_b});
    }
}
