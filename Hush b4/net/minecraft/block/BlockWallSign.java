// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockWallSign extends BlockSign
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, EnumFacing.NORTH));
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockWallSign.FACING);
        final float f = 0.28125f;
        final float f2 = 0.78125f;
        final float f3 = 0.0f;
        final float f4 = 1.0f;
        final float f5 = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        switch (enumfacing) {
            case NORTH: {
                this.setBlockBounds(f3, f, 1.0f - f5, f4, f2, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(f3, f, 0.0f, f4, f2, f5);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f5, f, f3, 1.0f, f2, f4);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, f, f3, f5, f2, f4);
                break;
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING);
        if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockWallSign.FACING).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockWallSign.FACING });
    }
}
