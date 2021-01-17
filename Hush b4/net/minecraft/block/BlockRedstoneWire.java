// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import java.util.EnumSet;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Set;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum<EnumAttachPosition> NORTH;
    public static final PropertyEnum<EnumAttachPosition> EAST;
    public static final PropertyEnum<EnumAttachPosition> SOUTH;
    public static final PropertyEnum<EnumAttachPosition> WEST;
    public static final PropertyInteger POWER;
    private boolean canProvidePower;
    private final Set<BlockPos> blocksNeedingUpdate;
    
    static {
        NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
        EAST = PropertyEnum.create("east", EnumAttachPosition.class);
        SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
        WEST = PropertyEnum.create("west", EnumAttachPosition.class);
        POWER = PropertyInteger.create("power", 0, 15);
    }
    
    public BlockRedstoneWire() {
        super(Material.circuits);
        this.canProvidePower = true;
        this.blocksNeedingUpdate = (Set<BlockPos>)Sets.newHashSet();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedstoneWire.NORTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.EAST, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.SOUTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.WEST, EnumAttachPosition.NONE).withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        state = state.withProperty(BlockRedstoneWire.WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(BlockRedstoneWire.EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(BlockRedstoneWire.NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(BlockRedstoneWire.SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }
    
    private EnumAttachPosition getAttachPosition(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing direction) {
        final BlockPos blockpos = pos.offset(direction);
        final Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
        if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (block.isBlockNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
            final Block block2 = worldIn.getBlockState(pos.up()).getBlock();
            return (!block2.isBlockNormalCube() && block.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
        }
        return EnumAttachPosition.SIDE;
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
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return (iblockstate.getBlock() != this) ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier(iblockstate.getValue((IProperty<Integer>)BlockRedstoneWire.POWER));
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
    }
    
    private IBlockState updateSurroundingRedstone(final World worldIn, final BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList((Iterable<?>)this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (final BlockPos blockpos : list) {
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }
        return state;
    }
    
    private IBlockState calculateCurrentChanges(final World worldIn, final BlockPos pos1, final BlockPos pos2, IBlockState state) {
        final IBlockState iblockstate = state;
        final int i = state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        int j = 0;
        j = this.getMaxCurrentStrength(worldIn, pos2, j);
        this.canProvidePower = false;
        final int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;
        if (k > 0 && k > j - 1) {
            j = k;
        }
        int l = 0;
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos1.offset((EnumFacing)enumfacing);
            final boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();
            if (flag) {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
                if (!flag || pos1.getY() < pos2.getY()) {
                    continue;
                }
                l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
            }
            else {
                if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() || !flag || pos1.getY() > pos2.getY()) {
                    continue;
                }
                l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
        }
        if (l > j) {
            j = l - 1;
        }
        else if (j > 0) {
            --j;
        }
        else {
            j = 0;
        }
        if (k > j - 1) {
            j = k;
        }
        if (i != j) {
            state = state.withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, j);
            if (worldIn.getBlockState(pos1) == iblockstate) {
                worldIn.setBlockState(pos1, state, 2);
            }
            this.blocksNeedingUpdate.add(pos1);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, n = 0; n < length; ++n) {
                final EnumFacing enumfacing2 = values[n];
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing2));
            }
        }
        return state;
    }
    
    private void notifyWireNeighborsOfStateChange(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final Object enumfacing : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)enumfacing), this);
            }
            for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                final EnumFacing enumfacing3 = (EnumFacing)enumfacing2;
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing3));
            }
            for (final Object enumfacing4 : EnumFacing.Plane.HORIZONTAL) {
                final EnumFacing enumfacing5 = (EnumFacing)enumfacing4;
                final BlockPos blockpos = pos.offset(enumfacing5);
                if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote) {
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset((EnumFacing)enumfacing2));
            }
            for (final Object enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos blockpos = pos.offset((EnumFacing)enumfacing3);
                if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }
    
    private int getMaxCurrentStrength(final World worldIn, final BlockPos pos, final int strength) {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return strength;
        }
        final int i = worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        return (i > strength) ? i : strength;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.redstone;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.canProvidePower ? this.getWeakPower(worldIn, pos, state, side) : 0;
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        final int i = state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        if (i == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return i;
        }
        final EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.func_176339_d(worldIn, pos, (EnumFacing)enumfacing)) {
                enumset.add((EnumFacing)enumfacing);
            }
        }
        if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
            return i;
        }
        if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
            return i;
        }
        return 0;
    }
    
    private boolean func_176339_d(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final BlockPos blockpos = pos.offset(side);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isNormalCube();
        final boolean flag2 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        return (!flag2 && flag && canConnectUpwardsTo(worldIn, blockpos.up())) || canConnectTo(iblockstate, side) || (block == Blocks.powered_repeater && iblockstate.getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) == side) || (!flag && canConnectUpwardsTo(worldIn, blockpos.down()));
    }
    
    protected static boolean canConnectUpwardsTo(final IBlockAccess worldIn, final BlockPos pos) {
        return canConnectUpwardsTo(worldIn.getBlockState(pos));
    }
    
    protected static boolean canConnectUpwardsTo(final IBlockState state) {
        return canConnectTo(state, null);
    }
    
    protected static boolean canConnectTo(final IBlockState blockState, final EnumFacing side) {
        final Block block = blockState.getBlock();
        if (block == Blocks.redstone_wire) {
            return true;
        }
        if (Blocks.unpowered_repeater.isAssociated(block)) {
            final EnumFacing enumfacing = blockState.getValue((IProperty<EnumFacing>)BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        return block.canProvidePower() && side != null;
    }
    
    @Override
    public boolean canProvidePower() {
        return this.canProvidePower;
    }
    
    private int colorMultiplier(final int powerLevel) {
        final float f = powerLevel / 15.0f;
        float f2 = f * 0.6f + 0.4f;
        if (powerLevel == 0) {
            f2 = 0.3f;
        }
        float f3 = f * f * 0.7f - 0.5f;
        float f4 = f * f * 0.6f - 0.7f;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        final int i = MathHelper.clamp_int((int)(f2 * 255.0f), 0, 255);
        final int j = MathHelper.clamp_int((int)(f3 * 255.0f), 0, 255);
        final int k = MathHelper.clamp_int((int)(f4 * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | j << 8 | k;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final int i = state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        if (i != 0) {
            final double d0 = pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final double d2 = pos.getY() + 0.0625f;
            final double d3 = pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final float f = i / 15.0f;
            final float f2 = f * 0.6f + 0.4f;
            final float f3 = Math.max(0.0f, f * f * 0.7f - 0.5f);
            final float f4 = Math.max(0.0f, f * f * 0.6f - 0.7f);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d2, d3, f2, f3, f4, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.redstone;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRedstoneWire.NORTH, BlockRedstoneWire.EAST, BlockRedstoneWire.SOUTH, BlockRedstoneWire.WEST, BlockRedstoneWire.POWER });
    }
    
    enum EnumAttachPosition implements IStringSerializable
    {
        UP("UP", 0, "up"), 
        SIDE("SIDE", 1, "side"), 
        NONE("NONE", 2, "none");
        
        private final String name;
        
        private EnumAttachPosition(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
