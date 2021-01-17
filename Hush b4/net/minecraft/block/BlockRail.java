// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRail extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE;
    
    static {
        SHAPE = PropertyEnum.create("shape", EnumRailDirection.class);
    }
    
    protected BlockRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_SOUTH));
    }
    
    @Override
    protected void onNeighborChangedInternal(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (neighborBlock.canProvidePower() && new Rail(worldIn, pos, state).countAdjacentRails() == 3) {
            this.func_176564_a(worldIn, pos, state, false);
        }
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRail.SHAPE;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRail.SHAPE, EnumRailDirection.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockRail.SHAPE).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRail.SHAPE });
    }
}
