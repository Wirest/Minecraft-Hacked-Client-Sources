// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.EntityMinecartCommandBlock;
import java.util.List;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailDetector extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE;
    public static final PropertyBool POWERED;
    
    static {
        SHAPE = PropertyEnum.create("shape", EnumRailDirection.class, new Predicate<EnumRailDirection>() {
            @Override
            public boolean apply(final EnumRailDirection p_apply_1_) {
                return p_apply_1_ != EnumRailDirection.NORTH_EAST && p_apply_1_ != EnumRailDirection.NORTH_WEST && p_apply_1_ != EnumRailDirection.SOUTH_EAST && p_apply_1_ != EnumRailDirection.SOUTH_WEST;
            }
        });
        POWERED = PropertyBool.create("powered");
    }
    
    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, false).withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 20;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED) ? ((side == EnumFacing.UP) ? 15 : 0) : 0;
    }
    
    private void updatePoweredState(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED);
        boolean flag2 = false;
        final List<EntityMinecart> list = this.findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[0]);
        if (!list.isEmpty()) {
            flag2 = true;
        }
        if (flag2 && !flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, true), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!flag2 && flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, false), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (flag2) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
        worldIn.updateComparatorOutputLevel(pos, this);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePoweredState(worldIn, pos, state);
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRailDetector.SHAPE;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            final List<EntityMinecartCommandBlock> list = this.findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, (Predicate<Entity>[])new Predicate[0]);
            if (!list.isEmpty()) {
                return list.get(0).getCommandBlockLogic().getSuccessCount();
            }
            final List<EntityMinecart> list2 = this.findMinecarts(worldIn, pos, EntityMinecart.class, EntitySelectors.selectInventories);
            if (!list2.isEmpty()) {
                return Container.calcRedstoneFromInventory((IInventory)list2.get(0));
            }
        }
        return 0;
    }
    
    protected <T extends EntityMinecart> List<T> findMinecarts(final World worldIn, final BlockPos pos, final Class<T> clazz, final Predicate<Entity>... filter) {
        final AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
        return (filter.length != 1) ? worldIn.getEntitiesWithinAABB((Class<? extends T>)clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB((Class<? extends T>)clazz, axisalignedbb, (Predicate<? super T>)filter[0]);
    }
    
    private AxisAlignedBB getDectectionBox(final BlockPos pos) {
        final float f = 0.2f;
        return new AxisAlignedBB(pos.getX() + 0.2f, pos.getY(), pos.getZ() + 0.2f, pos.getX() + 1 - 0.2f, pos.getY() + 1 - 0.2f, pos.getZ() + 1 - 0.2f);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailDetector.SHAPE, EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockRailDetector.SHAPE).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRailDetector.SHAPE, BlockRailDetector.POWERED });
    }
}
