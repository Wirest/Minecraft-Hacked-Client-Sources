// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockLadder extends Block
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockLadder() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockLadder.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            final float f = 0.125f;
            switch (iblockstate.getValue((IProperty<EnumFacing>)BlockLadder.FACING)) {
                case NORTH: {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                default: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.west()).getBlock().isNormalCube() || worldIn.getBlockState(pos.east()).getBlock().isNormalCube() || worldIn.getBlockState(pos.north()).getBlock().isNormalCube() || worldIn.getBlockState(pos.south()).getBlock().isNormalCube();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal() && this.canBlockStay(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, facing);
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.canBlockStay(worldIn, pos, (EnumFacing)enumfacing)) {
                return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, enumfacing);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockLadder.FACING);
        if (!this.canBlockStay(worldIn, pos, enumfacing)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    protected boolean canBlockStay(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube();
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLadder.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockLadder.FACING).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLadder.FACING });
    }
}
