// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Random;
import com.google.common.base.Objects;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool POWERED;
    public static final PropertyBool ATTACHED;
    public static final PropertyBool SUSPENDED;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        POWERED = PropertyBool.create("powered");
        ATTACHED = PropertyBool.create("attached");
        SUSPENDED = PropertyBool.create("suspended");
    }
    
    public BlockTripWireHook() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.SUSPENDED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockTripWireHook.SUSPENDED, !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()));
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
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock().isNormalCube()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.SUSPENDED, false);
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, facing);
        }
        return iblockstate;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        this.func_176260_a(worldIn, pos, state, false, false, -1, null);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (neighborBlock != this && this.checkForDrop(worldIn, pos, state)) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING);
            if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().isNormalCube()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    public void func_176260_a(final World worldIn, final BlockPos pos, final IBlockState hookState, final boolean p_176260_4_, final boolean p_176260_5_, final int p_176260_6_, final IBlockState p_176260_7_) {
        final EnumFacing enumfacing = hookState.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING);
        final boolean flag = hookState.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED);
        final boolean flag2 = hookState.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED);
        final boolean flag3 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        boolean flag4 = !p_176260_4_;
        boolean flag5 = false;
        int i = 0;
        final IBlockState[] aiblockstate = new IBlockState[42];
        int j = 1;
        while (j < 42) {
            final BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                if (iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
                    i = j;
                    break;
                }
                break;
            }
            else {
                if (iblockstate.getBlock() != Blocks.tripwire && j != p_176260_6_) {
                    aiblockstate[j] = null;
                    flag4 = false;
                }
                else {
                    if (j == p_176260_6_) {
                        iblockstate = Objects.firstNonNull(p_176260_7_, iblockstate);
                    }
                    final boolean flag6 = !iblockstate.getValue((IProperty<Boolean>)BlockTripWire.DISARMED);
                    final boolean flag7 = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.POWERED);
                    final boolean flag8 = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED);
                    flag4 &= (flag8 == flag3);
                    flag5 |= (flag6 && flag7);
                    aiblockstate[j] = iblockstate;
                    if (j == p_176260_6_) {
                        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                        flag4 &= flag6;
                    }
                }
                ++j;
            }
        }
        flag4 &= (i > 1);
        flag5 &= flag4;
        final IBlockState iblockstate2 = this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, flag4).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, flag5);
        if (i > 0) {
            final BlockPos blockpos2 = pos.offset(enumfacing, i);
            final EnumFacing enumfacing2 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, enumfacing2), 3);
            this.func_176262_b(worldIn, blockpos2, enumfacing2);
            this.func_180694_a(worldIn, blockpos2, flag4, flag5, flag, flag2);
        }
        this.func_180694_a(worldIn, pos, flag4, flag5, flag, flag2);
        if (!p_176260_4_) {
            worldIn.setBlockState(pos, iblockstate2.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, enumfacing), 3);
            if (p_176260_5_) {
                this.func_176262_b(worldIn, pos, enumfacing);
            }
        }
        if (flag != flag4) {
            for (int k = 1; k < i; ++k) {
                final BlockPos blockpos3 = pos.offset(enumfacing, k);
                final IBlockState iblockstate3 = aiblockstate[k];
                if (iblockstate3 != null && worldIn.getBlockState(blockpos3).getBlock() != Blocks.air) {
                    worldIn.setBlockState(blockpos3, iblockstate3.withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, flag4), 3);
                }
            }
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.func_176260_a(worldIn, pos, state, false, true, -1, null);
    }
    
    private void func_180694_a(final World worldIn, final BlockPos pos, final boolean p_180694_3_, final boolean p_180694_4_, final boolean p_180694_5_, final boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.click", 0.4f, 0.6f);
        }
        else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.click", 0.4f, 0.5f);
        }
        else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.click", 0.4f, 0.7f);
        }
        else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.bowhit", 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }
    
    private void func_176262_b(final World worldIn, final BlockPos p_176262_2_, final EnumFacing p_176262_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }
    
    private boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final float f = 0.1875f;
        switch (worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING)) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                break;
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED);
        final boolean flag2 = state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED);
        if (flag || flag2) {
            this.func_176260_a(worldIn, pos, state, true, false, -1, null);
        }
        if (flag2) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING).getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED) ? ((state.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == side) ? 15 : 0) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, (meta & 0x8) > 0).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, (meta & 0x4) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED)) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTripWireHook.FACING, BlockTripWireHook.POWERED, BlockTripWireHook.ATTACHED, BlockTripWireHook.SUSPENDED });
    }
}
