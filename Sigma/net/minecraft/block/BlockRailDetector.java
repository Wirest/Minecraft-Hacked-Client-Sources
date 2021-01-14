package net.minecraft.block;

import com.google.common.base.Predicate;

import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase {
    public static final PropertyEnum field_176573_b = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        private static final String __OBFID = "CL_00002126";

        public boolean func_180344_a(BlockRailBase.EnumRailDirection p_180344_1_) {
            return p_180344_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_180344_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_180344_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_180344_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        @Override
        public boolean apply(Object p_apply_1_) {
            return func_180344_a((BlockRailBase.EnumRailDirection) p_apply_1_);
        }
    });
    public static final PropertyBool field_176574_M = PropertyBool.create("powered");
    private static final String __OBFID = "CL_00000225";

    public BlockRailDetector() {
        super(true);
        setDefaultState(blockState.getBaseState().withProperty(BlockRailDetector.field_176574_M, Boolean.valueOf(false)).withProperty(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        setTickRandomly(true);
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return true;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            if (!((Boolean) state.getValue(BlockRailDetector.field_176574_M)).booleanValue()) {
                func_176570_e(worldIn, pos, state);
            }
        }
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops
     * to grow, etc.)
     */
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean) state.getValue(BlockRailDetector.field_176574_M)).booleanValue()) {
            func_176570_e(worldIn, pos, state);
        }
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return ((Boolean) state.getValue(BlockRailDetector.field_176574_M)).booleanValue() ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !((Boolean) state.getValue(BlockRailDetector.field_176574_M)).booleanValue() ? 0 : (side == EnumFacing.UP ? 15 : 0);
    }

    private void func_176570_e(World worldIn, BlockPos p_176570_2_, IBlockState p_176570_3_) {
        boolean var4 = ((Boolean) p_176570_3_.getValue(BlockRailDetector.field_176574_M)).booleanValue();
        boolean var5 = false;
        List var6 = func_176571_a(worldIn, p_176570_2_, EntityMinecart.class, new Predicate[0]);

        if (!var6.isEmpty()) {
            var5 = true;
        }

        if (var5 && !var4) {
            worldIn.setBlockState(p_176570_2_, p_176570_3_.withProperty(BlockRailDetector.field_176574_M, Boolean.valueOf(true)), 3);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_, this);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_.offsetDown(), this);
            worldIn.markBlockRangeForRenderUpdate(p_176570_2_, p_176570_2_);
        }

        if (!var5 && var4) {
            worldIn.setBlockState(p_176570_2_, p_176570_3_.withProperty(BlockRailDetector.field_176574_M, Boolean.valueOf(false)), 3);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_, this);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_.offsetDown(), this);
            worldIn.markBlockRangeForRenderUpdate(p_176570_2_, p_176570_2_);
        }

        if (var5) {
            worldIn.scheduleUpdate(p_176570_2_, this, tickRate(worldIn));
        }

        worldIn.updateComparatorOutputLevel(p_176570_2_, this);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        func_176570_e(worldIn, pos, state);
    }

    @Override
    public IProperty func_176560_l() {
        return BlockRailDetector.field_176573_b;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        if (((Boolean) worldIn.getBlockState(pos).getValue(BlockRailDetector.field_176574_M)).booleanValue()) {
            List var3 = func_176571_a(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);

            if (!var3.isEmpty()) {
                return ((EntityMinecartCommandBlock) var3.get(0)).func_145822_e().getSuccessCount();
            }

            List var4 = func_176571_a(worldIn, pos, EntityMinecart.class, new Predicate[]{IEntitySelector.selectInventories});

            if (!var4.isEmpty()) {
                return Container.calcRedstoneFromInventory((IInventory) var4.get(0));
            }
        }

        return 0;
    }

    protected List func_176571_a(World worldIn, BlockPos p_176571_2_, Class p_176571_3_, Predicate... p_176571_4_) {
        AxisAlignedBB var5 = func_176572_a(p_176571_2_);
        return p_176571_4_.length != 1 ? worldIn.getEntitiesWithinAABB(p_176571_3_, var5) : worldIn.func_175647_a(p_176571_3_, var5, p_176571_4_[0]);
    }

    private AxisAlignedBB func_176572_a(BlockPos p_176572_1_) {
        float var2 = 0.2F;
        return new AxisAlignedBB(p_176572_1_.getX() + 0.2F, p_176572_1_.getY(), p_176572_1_.getZ() + 0.2F, p_176572_1_.getX() + 1 - 0.2F, p_176572_1_.getY() + 1 - 0.2F, p_176572_1_.getZ() + 1 - 0.2F);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.func_177016_a(meta & 7)).withProperty(BlockRailDetector.field_176574_M, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockRailBase.EnumRailDirection) state.getValue(BlockRailDetector.field_176573_b)).func_177015_a();

        if (((Boolean) state.getValue(BlockRailDetector.field_176574_M)).booleanValue()) {
            var3 |= 8;
        }

        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockRailDetector.field_176573_b, BlockRailDetector.field_176574_M});
    }
}
