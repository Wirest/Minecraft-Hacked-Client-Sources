package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign {
    public static final PropertyInteger ROTATION_PROP = PropertyInteger.create("rotation", 0, 15);
    private static final String __OBFID = "CL_00002060";

    public BlockStandingSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION_PROP, Integer.valueOf(0)));
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ROTATION_PROP, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(ROTATION_PROP)).intValue();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{ROTATION_PROP});
    }
}
