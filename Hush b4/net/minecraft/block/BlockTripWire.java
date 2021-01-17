// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockTripWire extends Block
{
    public static final PropertyBool POWERED;
    public static final PropertyBool SUSPENDED;
    public static final PropertyBool ATTACHED;
    public static final PropertyBool DISARMED;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    
    static {
        POWERED = PropertyBool.create("powered");
        SUSPENDED = PropertyBool.create("suspended");
        ATTACHED = PropertyBool.create("attached");
        DISARMED = PropertyBool.create("disarmed");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }
    
    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTripWire.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWire.SUSPENDED, false).withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, false).withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, false).withProperty((IProperty<Comparable>)BlockTripWire.NORTH, false).withProperty((IProperty<Comparable>)BlockTripWire.EAST, false).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, false).withProperty((IProperty<Comparable>)BlockTripWire.WEST, false));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, isConnectedTo(worldIn, pos, state, EnumFacing.NORTH)).withProperty((IProperty<Comparable>)BlockTripWire.EAST, isConnectedTo(worldIn, pos, state, EnumFacing.EAST)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, isConnectedTo(worldIn, pos, state, EnumFacing.WEST));
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.string;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.string;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED);
        final boolean flag2 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        if (flag != flag2) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final boolean flag = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.ATTACHED);
        final boolean flag2 = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED);
        if (!flag2) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
        }
        else if (!flag) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, IBlockState state) {
        state = state.withProperty((IProperty<Comparable>)BlockTripWire.SUSPENDED, !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()));
        worldIn.setBlockState(pos, state, 3);
        this.notifyHook(worldIn, pos, state);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.notifyHook(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTripWire.POWERED, true));
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, true), 4);
        }
    }
    
    private void notifyHook(final World worldIn, final BlockPos pos, final IBlockState state) {
        EnumFacing[] array;
        for (int length = (array = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = array[j];
            int i = 1;
            while (i < 42) {
                final BlockPos blockpos = pos.offset(enumfacing, i);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                    if (iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
                        Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
                        break;
                    }
                    break;
                }
                else {
                    if (iblockstate.getBlock() != Blocks.tripwire) {
                        break;
                    }
                    ++i;
                }
            }
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !state.getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            this.updateState(worldIn, pos);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            this.updateState(worldIn, pos);
        }
    }
    
    private void updateState(final World worldIn, final BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        final boolean flag = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.POWERED);
        boolean flag2 = false;
        final List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
        if (!list.isEmpty()) {
            for (final Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    flag2 = true;
                    break;
                }
            }
        }
        if (flag2 != flag) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTripWire.POWERED, flag2);
            worldIn.setBlockState(pos, iblockstate, 3);
            this.notifyHook(worldIn, pos, iblockstate);
        }
        if (flag2) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    public static boolean isConnectedTo(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing direction) {
        final BlockPos blockpos = pos.offset(direction);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.tripwire_hook) {
            final EnumFacing enumfacing = direction.getOpposite();
            return iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing;
        }
        if (block == Blocks.tripwire) {
            final boolean flag = state.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED);
            final boolean flag2 = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED);
            return flag == flag2;
        }
        return false;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.POWERED, (meta & 0x1) > 0).withProperty((IProperty<Comparable>)BlockTripWire.SUSPENDED, (meta & 0x2) > 0).withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, (meta & 0x4) > 0).withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            i |= 0x1;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWire.SUSPENDED)) {
            i |= 0x2;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWire.ATTACHED)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWire.DISARMED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTripWire.POWERED, BlockTripWire.SUSPENDED, BlockTripWire.ATTACHED, BlockTripWire.DISARMED, BlockTripWire.NORTH, BlockTripWire.EAST, BlockTripWire.WEST, BlockTripWire.SOUTH });
    }
}
