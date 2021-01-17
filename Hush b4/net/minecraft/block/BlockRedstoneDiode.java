// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public abstract class BlockRedstoneDiode extends BlockDirectional
{
    protected final boolean isRepeaterPowered;
    
    protected BlockRedstoneDiode(final boolean powered) {
        super(Material.circuits);
        this.isRepeaterPowered = powered;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && super.canPlaceBlockAt(worldIn, pos);
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.isLocked(worldIn, pos, state)) {
            final boolean flag = this.shouldBePowered(worldIn, pos, state);
            if (this.isRepeaterPowered && !flag) {
                worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
            }
            else if (!this.isRepeaterPowered) {
                worldIn.setBlockState(pos, this.getPoweredState(state), 2);
                if (!flag) {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                }
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }
    
    protected boolean isPowered(final IBlockState state) {
        return this.isRepeaterPowered;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.getWeakPower(worldIn, pos, state, side);
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.isPowered(state) ? ((state.getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) == side) ? this.getActiveSignal(worldIn, pos, state) : 0) : 0;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.canBlockStay(worldIn, pos)) {
            this.updateState(worldIn, pos, state);
        }
        else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }
    
    protected void updateState(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.isLocked(worldIn, pos, state)) {
            final boolean flag = this.shouldBePowered(worldIn, pos, state);
            if (((this.isRepeaterPowered && !flag) || (!this.isRepeaterPowered && flag)) && !worldIn.isBlockTickPending(pos, this)) {
                int i = -1;
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    i = -3;
                }
                else if (this.isRepeaterPowered) {
                    i = -2;
                }
                worldIn.updateBlockTick(pos, this, this.getDelay(state), i);
            }
        }
    }
    
    public boolean isLocked(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        return false;
    }
    
    protected boolean shouldBePowered(final World worldIn, final BlockPos pos, final IBlockState state) {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }
    
    protected int calculateInputStrength(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final BlockPos blockpos = pos.offset(enumfacing);
        final int i = worldIn.getRedstonePower(blockpos, enumfacing);
        if (i >= 15) {
            return i;
        }
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        return Math.max(i, (iblockstate.getBlock() == Blocks.redstone_wire) ? ((int)iblockstate.getValue((IProperty<Integer>)BlockRedstoneWire.POWER)) : 0);
    }
    
    protected int getPowerOnSides(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final EnumFacing enumfacing2 = enumfacing.rotateY();
        final EnumFacing enumfacing3 = enumfacing.rotateYCCW();
        return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2), this.getPowerOnSide(worldIn, pos.offset(enumfacing3), enumfacing3));
    }
    
    protected int getPowerOnSide(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return this.canPowerSide(block) ? ((block == Blocks.redstone_wire) ? iblockstate.getValue((IProperty<Integer>)BlockRedstoneWire.POWER) : worldIn.getStrongPower(pos, side)) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneDiode.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (this.shouldBePowered(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.notifyNeighbors(worldIn, pos, state);
    }
    
    protected void notifyNeighbors(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING);
        final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.notifyBlockOfStateChange(blockpos, this);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isRepeaterPowered) {
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    protected boolean canPowerSide(final Block blockIn) {
        return blockIn.canProvidePower();
    }
    
    protected int getActiveSignal(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        return 15;
    }
    
    public static boolean isRedstoneRepeaterBlockID(final Block blockIn) {
        return Blocks.unpowered_repeater.isAssociated(blockIn) || Blocks.unpowered_comparator.isAssociated(blockIn);
    }
    
    public boolean isAssociated(final Block other) {
        return other == this.getPoweredState(this.getDefaultState()).getBlock() || other == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }
    
    public boolean isFacingTowardsRepeater(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneDiode.FACING).getOpposite();
        final BlockPos blockpos = pos.offset(enumfacing);
        return isRedstoneRepeaterBlockID(worldIn.getBlockState(blockpos).getBlock()) && worldIn.getBlockState(blockpos).getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) != enumfacing;
    }
    
    protected int getTickDelay(final IBlockState state) {
        return this.getDelay(state);
    }
    
    protected abstract int getDelay(final IBlockState p0);
    
    protected abstract IBlockState getPoweredState(final IBlockState p0);
    
    protected abstract IBlockState getUnpoweredState(final IBlockState p0);
    
    @Override
    public boolean isAssociatedBlock(final Block other) {
        return this.isAssociated(other);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
