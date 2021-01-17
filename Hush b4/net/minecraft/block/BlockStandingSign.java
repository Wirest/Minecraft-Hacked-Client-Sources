// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockStandingSign extends BlockSign
{
    public static final PropertyInteger ROTATION;
    
    static {
        ROTATION = PropertyInteger.create("rotation", 0, 15);
    }
    
    public BlockStandingSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, 0));
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockStandingSign.ROTATION);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStandingSign.ROTATION });
    }
}
