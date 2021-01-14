package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
    public static final PropertyEnum NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;
    private final Set field_150179_b = Sets.newHashSet();
    private static final String __OBFID = "CL_00000295";

    public BlockRedstoneWire() {
        super(Material.circuits);
        setDefaultState(blockState.getBaseState().withProperty(BlockRedstoneWire.NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.EAST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.POWER, Integer.valueOf(0)));
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(BlockRedstoneWire.WEST, getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(BlockRedstoneWire.EAST, getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(BlockRedstoneWire.NORTH, getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(BlockRedstoneWire.SOUTH, getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess p_176341_1_, BlockPos p_176341_2_, EnumFacing p_176341_3_) {
        BlockPos var4 = p_176341_2_.offset(p_176341_3_);
        Block var5 = p_176341_1_.getBlockState(p_176341_2_.offset(p_176341_3_)).getBlock();

        if (!BlockRedstoneWire.func_176343_a(p_176341_1_.getBlockState(var4), p_176341_3_) && (var5.isSolidFullCube() || !BlockRedstoneWire.func_176346_d(p_176341_1_.getBlockState(var4.offsetDown())))) {
            Block var6 = p_176341_1_.getBlockState(p_176341_2_.offsetUp()).getBlock();
            return !var6.isSolidFullCube() && var5.isSolidFullCube() && BlockRedstoneWire.func_176346_d(p_176341_1_.getBlockState(var4.offsetUp())) ? BlockRedstoneWire.EnumAttachPosition.UP : BlockRedstoneWire.EnumAttachPosition.NONE;
        } else {
            return BlockRedstoneWire.EnumAttachPosition.SIDE;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
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
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        IBlockState var4 = worldIn.getBlockState(pos);
        return var4.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : func_176337_b(((Integer) var4.getValue(BlockRedstoneWire.POWER)).intValue());
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || worldIn.getBlockState(pos.offsetDown()).getBlock() == Blocks.glowstone;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos p_176338_2_, IBlockState p_176338_3_) {
        p_176338_3_ = func_176345_a(worldIn, p_176338_2_, p_176338_2_, p_176338_3_);
        ArrayList var4 = Lists.newArrayList(field_150179_b);
        field_150179_b.clear();
        Iterator var5 = var4.iterator();

        while (var5.hasNext()) {
            BlockPos var6 = (BlockPos) var5.next();
            worldIn.notifyNeighborsOfStateChange(var6, this);
        }

        return p_176338_3_;
    }

    private IBlockState func_176345_a(World worldIn, BlockPos p_176345_2_, BlockPos p_176345_3_, IBlockState p_176345_4_) {
        IBlockState var5 = p_176345_4_;
        int var6 = ((Integer) p_176345_4_.getValue(BlockRedstoneWire.POWER)).intValue();
        byte var7 = 0;
        int var14 = func_176342_a(worldIn, p_176345_3_, var7);
        canProvidePower = false;
        int var8 = worldIn.func_175687_A(p_176345_2_);
        canProvidePower = true;

        if (var8 > 0 && var8 > var14 - 1) {
            var14 = var8;
        }

        int var9 = 0;
        Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var10.hasNext()) {
            EnumFacing var11 = (EnumFacing) var10.next();
            BlockPos var12 = p_176345_2_.offset(var11);
            boolean var13 = var12.getX() != p_176345_3_.getX() || var12.getZ() != p_176345_3_.getZ();

            if (var13) {
                var9 = func_176342_a(worldIn, var12, var9);
            }

            if (worldIn.getBlockState(var12).getBlock().isNormalCube() && !worldIn.getBlockState(p_176345_2_.offsetUp()).getBlock().isNormalCube()) {
                if (var13 && p_176345_2_.getY() >= p_176345_3_.getY()) {
                    var9 = func_176342_a(worldIn, var12.offsetUp(), var9);
                }
            } else if (!worldIn.getBlockState(var12).getBlock().isNormalCube() && var13 && p_176345_2_.getY() <= p_176345_3_.getY()) {
                var9 = func_176342_a(worldIn, var12.offsetDown(), var9);
            }
        }

        if (var9 > var14) {
            var14 = var9 - 1;
        } else if (var14 > 0) {
            --var14;
        } else {
            var14 = 0;
        }

        if (var8 > var14 - 1) {
            var14 = var8;
        }

        if (var6 != var14) {
            p_176345_4_ = p_176345_4_.withProperty(BlockRedstoneWire.POWER, Integer.valueOf(var14));

            if (worldIn.getBlockState(p_176345_2_) == var5) {
                worldIn.setBlockState(p_176345_2_, p_176345_4_, 2);
            }

            field_150179_b.add(p_176345_2_);
            EnumFacing[] var15 = EnumFacing.values();
            int var16 = var15.length;

            for (int var17 = 0; var17 < var16; ++var17) {
                EnumFacing var18 = var15[var17];
                field_150179_b.add(p_176345_2_.offset(var18));
            }
        }

        return p_176345_4_;
    }

    private void func_176344_d(World worldIn, BlockPos p_176344_2_) {
        if (worldIn.getBlockState(p_176344_2_).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(p_176344_2_, this);
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                EnumFacing var6 = var3[var5];
                worldIn.notifyNeighborsOfStateChange(p_176344_2_.offset(var6), this);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            updateSurroundingRedstone(worldIn, pos, state);
            Iterator var4 = EnumFacing.Plane.VERTICAL.iterator();
            EnumFacing var5;

            while (var4.hasNext()) {
                var5 = (EnumFacing) var4.next();
                worldIn.notifyNeighborsOfStateChange(pos.offset(var5), this);
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var4.hasNext()) {
                var5 = (EnumFacing) var4.next();
                func_176344_d(worldIn, pos.offset(var5));
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var4.hasNext()) {
                var5 = (EnumFacing) var4.next();
                BlockPos var6 = pos.offset(var5);

                if (worldIn.getBlockState(var6).getBlock().isNormalCube()) {
                    func_176344_d(worldIn, var6.offsetUp());
                } else {
                    func_176344_d(worldIn, var6.offsetDown());
                }
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote) {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }

            updateSurroundingRedstone(worldIn, pos, state);
            Iterator var8 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var9;

            while (var8.hasNext()) {
                var9 = (EnumFacing) var8.next();
                func_176344_d(worldIn, pos.offset(var9));
            }

            var8 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var8.hasNext()) {
                var9 = (EnumFacing) var8.next();
                BlockPos var10 = pos.offset(var9);

                if (worldIn.getBlockState(var10).getBlock().isNormalCube()) {
                    func_176344_d(worldIn, var10.offsetUp());
                } else {
                    func_176344_d(worldIn, var10.offsetDown());
                }
            }
        }
    }

    private int func_176342_a(World worldIn, BlockPos p_176342_2_, int p_176342_3_) {
        if (worldIn.getBlockState(p_176342_2_).getBlock() != this) {
            return p_176342_3_;
        } else {
            int var4 = ((Integer) worldIn.getBlockState(p_176342_2_).getValue(BlockRedstoneWire.POWER)).intValue();
            return var4 > p_176342_3_ ? var4 : p_176342_3_;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            if (canPlaceBlockAt(worldIn, pos)) {
                updateSurroundingRedstone(worldIn, pos, state);
            } else {
                dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.redstone;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !canProvidePower ? 0 : isProvidingWeakPower(worldIn, pos, state, side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!canProvidePower) {
            return 0;
        } else {
            int var5 = ((Integer) state.getValue(BlockRedstoneWire.POWER)).intValue();

            if (var5 == 0) {
                return 0;
            } else if (side == EnumFacing.UP) {
                return var5;
            } else {
                EnumSet var6 = EnumSet.noneOf(EnumFacing.class);
                Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

                while (var7.hasNext()) {
                    EnumFacing var8 = (EnumFacing) var7.next();

                    if (func_176339_d(worldIn, pos, var8)) {
                        var6.add(var8);
                    }
                }

                if (side.getAxis().isHorizontal() && var6.isEmpty()) {
                    return var5;
                } else if (var6.contains(side) && !var6.contains(side.rotateYCCW()) && !var6.contains(side.rotateY())) {
                    return var5;
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean func_176339_d(IBlockAccess p_176339_1_, BlockPos p_176339_2_, EnumFacing p_176339_3_) {
        BlockPos var4 = p_176339_2_.offset(p_176339_3_);
        IBlockState var5 = p_176339_1_.getBlockState(var4);
        Block var6 = var5.getBlock();
        boolean var7 = var6.isNormalCube();
        boolean var8 = p_176339_1_.getBlockState(p_176339_2_.offsetUp()).getBlock().isNormalCube();
        return !var8 && var7 && BlockRedstoneWire.func_176340_e(p_176339_1_, var4.offsetUp()) ? true : (BlockRedstoneWire.func_176343_a(var5, p_176339_3_) ? true : (var6 == Blocks.powered_repeater && var5.getValue(BlockDirectional.AGE) == p_176339_3_ ? true : !var7 && BlockRedstoneWire.func_176340_e(p_176339_1_, var4.offsetDown())));
    }

    protected static boolean func_176340_e(IBlockAccess p_176340_0_, BlockPos p_176340_1_) {
        return BlockRedstoneWire.func_176346_d(p_176340_0_.getBlockState(p_176340_1_));
    }

    protected static boolean func_176346_d(IBlockState p_176346_0_) {
        return BlockRedstoneWire.func_176343_a(p_176346_0_, (EnumFacing) null);
    }

    protected static boolean func_176343_a(IBlockState p_176343_0_, EnumFacing p_176343_1_) {
        Block var2 = p_176343_0_.getBlock();

        if (var2 == Blocks.redstone_wire) {
            return true;
        } else if (Blocks.unpowered_repeater.func_149907_e(var2)) {
            EnumFacing var3 = (EnumFacing) p_176343_0_.getValue(BlockDirectional.AGE);
            return var3 == p_176343_1_ || var3.getOpposite() == p_176343_1_;
        } else {
            return var2.canProvidePower() && p_176343_1_ != null;
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return canProvidePower;
    }

    private int func_176337_b(int p_176337_1_) {
        float var2 = p_176337_1_ / 15.0F;
        float var3 = var2 * 0.6F + 0.4F;

        if (p_176337_1_ == 0) {
            var3 = 0.3F;
        }

        float var4 = var2 * var2 * 0.7F - 0.5F;
        float var5 = var2 * var2 * 0.6F - 0.7F;

        if (var4 < 0.0F) {
            var4 = 0.0F;
        }

        if (var5 < 0.0F) {
            var5 = 0.0F;
        }

        int var6 = MathHelper.clamp_int((int) (var3 * 255.0F), 0, 255);
        int var7 = MathHelper.clamp_int((int) (var4 * 255.0F), 0, 255);
        int var8 = MathHelper.clamp_int((int) (var5 * 255.0F), 0, 255);
        return -16777216 | var6 << 16 | var7 << 8 | var8;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5 = ((Integer) state.getValue(BlockRedstoneWire.POWER)).intValue();

        if (var5 != 0) {
            double var6 = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            double var8 = pos.getY() + 0.0625F;
            double var10 = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            float var12 = var5 / 15.0F;
            float var13 = var12 * 0.6F + 0.4F;
            float var14 = Math.max(0.0F, var12 * var12 * 0.7F - 0.5F);
            float var15 = Math.max(0.0F, var12 * var12 * 0.6F - 0.7F);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6, var8, var10, var13, var14, var15, new int[0]);
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.redstone;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockRedstoneWire.POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(BlockRedstoneWire.POWER)).intValue();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockRedstoneWire.NORTH, BlockRedstoneWire.EAST, BlockRedstoneWire.SOUTH, BlockRedstoneWire.WEST, BlockRedstoneWire.POWER});
    }

    static enum EnumAttachPosition implements IStringSerializable {
        UP("UP", 0, "up"), SIDE("SIDE", 1, "side"), NONE("NONE", 2, "none");
        private final String field_176820_d;

        private static final BlockRedstoneWire.EnumAttachPosition[] $VALUES = new BlockRedstoneWire.EnumAttachPosition[]{UP, SIDE, NONE};
        private static final String __OBFID = "CL_00002070";

        private EnumAttachPosition(String p_i45689_1_, int p_i45689_2_, String p_i45689_3_) {
            field_176820_d = p_i45689_3_;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return field_176820_d;
        }
    }
}
