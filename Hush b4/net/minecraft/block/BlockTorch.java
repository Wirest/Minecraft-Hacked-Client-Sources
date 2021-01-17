// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTorch extends Block
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
            @Override
            public boolean apply(final EnumFacing p_apply_1_) {
                return p_apply_1_ != EnumFacing.DOWN;
            }
        });
    }
    
    protected BlockTorch() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    private boolean canPlaceOn(final World worldIn, final BlockPos pos) {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos)) {
            return true;
        }
        final Block block = worldIn.getBlockState(pos).getBlock();
        return block instanceof BlockFence || block == Blocks.glass || block == Blocks.cobblestone_wall || block == Blocks.stained_glass;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : BlockTorch.FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canPlaceAt(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        final BlockPos blockpos = pos.offset(facing.getOpposite());
        final boolean flag = facing.getAxis().isHorizontal();
        return (flag && worldIn.isBlockNormalCube(blockpos, true)) || (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos));
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (this.canPlaceAt(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, facing);
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.isBlockNormalCube(pos.offset(((EnumFacing)enumfacing).getOpposite()), true)) {
                return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, enumfacing);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }
    
    protected boolean onNeighborChangeInternal(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.checkForDrop(worldIn, pos, state)) {
            return true;
        }
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTorch.FACING);
        final EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
        final EnumFacing enumfacing2 = enumfacing.getOpposite();
        boolean flag = false;
        if (enumfacing$axis.isHorizontal() && !worldIn.isBlockNormalCube(pos.offset(enumfacing2), true)) {
            flag = true;
        }
        else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing2))) {
            flag = true;
        }
        if (flag) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    protected boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockTorch.FACING))) {
            return true;
        }
        if (worldIn.getBlockState(pos).getBlock() == this) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockTorch.FACING);
        float f = 0.15f;
        if (enumfacing == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
        }
        else if (enumfacing == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
        }
        else if (enumfacing == EnumFacing.SOUTH) {
            this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
        }
        else if (enumfacing == EnumFacing.NORTH) {
            this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
        }
        else {
            f = 0.1f;
            this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
        }
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTorch.FACING);
        final double d0 = pos.getX() + 0.5;
        final double d2 = pos.getY() + 0.7;
        final double d3 = pos.getZ() + 0.5;
        final double d4 = 0.22;
        final double d5 = 0.27;
        if (enumfacing.getAxis().isHorizontal()) {
            final EnumFacing enumfacing2 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d5 * enumfacing2.getFrontOffsetX(), d2 + d4, d3 + d5 * enumfacing2.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5 * enumfacing2.getFrontOffsetX(), d2 + d4, d3 + d5 * enumfacing2.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        switch (meta) {
            case 1: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.EAST);
                break;
            }
            case 2: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.WEST);
                break;
            }
            case 3: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.SOUTH);
                break;
            }
            case 4: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.NORTH);
                break;
            }
            default: {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.UP);
                break;
            }
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        switch (state.getValue((IProperty<EnumFacing>)BlockTorch.FACING)) {
            case EAST: {
                i |= 0x1;
                break;
            }
            case WEST: {
                i |= 0x2;
                break;
            }
            case SOUTH: {
                i |= 0x3;
                break;
            }
            case NORTH: {
                i |= 0x4;
                break;
            }
            default: {
                i |= 0x5;
                break;
            }
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTorch.FACING });
    }
}
