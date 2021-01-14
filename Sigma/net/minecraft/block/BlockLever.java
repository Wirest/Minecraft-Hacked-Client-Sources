package net.minecraft.block;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
    public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private static final String __OBFID = "CL_00000264";

    protected BlockLever() {
        super(Material.circuits);
        setDefaultState(blockState.getBaseState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH).withProperty(BlockLever.POWERED, Boolean.valueOf(false)));
        setCreativeTab(CreativeTabs.tabRedstone);
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

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : func_176358_d(worldIn, pos.offset(side.getOpposite()));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return func_176358_d(worldIn, pos.offsetWest()) ? true : (func_176358_d(worldIn, pos.offsetEast()) ? true : (func_176358_d(worldIn, pos.offsetNorth()) ? true : (func_176358_d(worldIn, pos.offsetSouth()) ? true : (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : func_176358_d(worldIn, pos.offsetUp())))));
    }

    protected boolean func_176358_d(World worldIn, BlockPos p_176358_2_) {
        return worldIn.getBlockState(p_176358_2_).getBlock().isNormalCube();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = getDefaultState().withProperty(BlockLever.POWERED, Boolean.valueOf(false));

        if (func_176358_d(worldIn, pos.offset(facing.getOpposite()))) {
            return var9.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.func_176856_a(facing, placer.func_174811_aO()));
        } else {
            Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var11;

            do {
                if (!var10.hasNext()) {
                    if (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                        return var9.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.func_176856_a(EnumFacing.UP, placer.func_174811_aO()));
                    }

                    return var9;
                }

                var11 = (EnumFacing) var10.next();
            } while (var11 == facing || !func_176358_d(worldIn, pos.offset(var11.getOpposite())));

            return var9.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.func_176856_a(var11, placer.func_174811_aO()));
        }
    }

    public static int func_176357_a(EnumFacing p_176357_0_) {
        switch (BlockLever.SwitchEnumFacing.FACING_LOOKUP[p_176357_0_.ordinal()]) {
            case 1:
                return 0;

            case 2:
                return 5;

            case 3:
                return 4;

            case 4:
                return 3;

            case 5:
                return 2;

            case 6:
                return 1;

            default:
                return -1;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (func_176356_e(worldIn, pos) && !func_176358_d(worldIn, pos.offset(((BlockLever.EnumOrientation) state.getValue(BlockLever.FACING)).func_176852_c().getOpposite()))) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean func_176356_e(World worldIn, BlockPos p_176356_2_) {
        if (canPlaceBlockAt(worldIn, p_176356_2_)) {
            return true;
        } else {
            dropBlockAsItem(worldIn, p_176356_2_, worldIn.getBlockState(p_176356_2_), 0);
            worldIn.setBlockToAir(p_176356_2_);
            return false;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.1875F;

        switch (BlockLever.SwitchEnumFacing.ORIENTATION_LOOKUP[((BlockLever.EnumOrientation) access.getBlockState(pos).getValue(BlockLever.FACING)).ordinal()]) {
            case 1:
                setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
                break;

            case 2:
                setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
                break;

            case 3:
                setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
                break;

            case 4:
                setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
                break;

            case 5:
            case 6:
                var3 = 0.25F;
                setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
                break;

            case 7:
            case 8:
                var3 = 0.25F;
                setBlockBounds(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            state = state.cycleProperty(BlockLever.POWERED);
            worldIn.setBlockState(pos, state, 3);
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean) state.getValue(BlockLever.POWERED)).booleanValue() ? 0.6F : 0.5F);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing var9 = ((BlockLever.EnumOrientation) state.getValue(BlockLever.FACING)).func_176852_c();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
            return true;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean) state.getValue(BlockLever.POWERED)).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing var4 = ((BlockLever.EnumOrientation) state.getValue(BlockLever.FACING)).func_176852_c();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return ((Boolean) state.getValue(BlockLever.POWERED)).booleanValue() ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !((Boolean) state.getValue(BlockLever.POWERED)).booleanValue() ? 0 : (((BlockLever.EnumOrientation) state.getValue(BlockLever.FACING)).func_176852_c() == side ? 15 : 0);
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
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.func_176853_a(meta & 7)).withProperty(BlockLever.POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockLever.EnumOrientation) state.getValue(BlockLever.FACING)).func_176855_a();

        if (((Boolean) state.getValue(BlockLever.POWERED)).booleanValue()) {
            var3 |= 8;
        }

        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockLever.FACING, BlockLever.POWERED});
    }

    public static enum EnumOrientation implements IStringSerializable {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN), EAST("EAST", 1, 1, "east", EnumFacing.EAST), WEST("WEST", 2, 2, "west", EnumFacing.WEST), SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH), NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH), UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP), UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP), DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);
        private static final BlockLever.EnumOrientation[] field_176869_i = new BlockLever.EnumOrientation[EnumOrientation.values().length];
        private final int field_176866_j;
        private final String field_176867_k;
        private final EnumFacing field_176864_l;

        private static final BlockLever.EnumOrientation[] $VALUES = new BlockLever.EnumOrientation[]{DOWN_X, EAST, WEST, SOUTH, NORTH, UP_Z, UP_X, DOWN_Z};
        private static final String __OBFID = "CL_00002102";

        private EnumOrientation(String p_i45709_1_, int p_i45709_2_, int p_i45709_3_, String p_i45709_4_, EnumFacing p_i45709_5_) {
            field_176866_j = p_i45709_3_;
            field_176867_k = p_i45709_4_;
            field_176864_l = p_i45709_5_;
        }

        public int func_176855_a() {
            return field_176866_j;
        }

        public EnumFacing func_176852_c() {
            return field_176864_l;
        }

        @Override
        public String toString() {
            return field_176867_k;
        }

        public static BlockLever.EnumOrientation func_176853_a(int p_176853_0_) {
            if (p_176853_0_ < 0 || p_176853_0_ >= EnumOrientation.field_176869_i.length) {
                p_176853_0_ = 0;
            }

            return EnumOrientation.field_176869_i[p_176853_0_];
        }

        public static BlockLever.EnumOrientation func_176856_a(EnumFacing p_176856_0_, EnumFacing p_176856_1_) {
            switch (BlockLever.SwitchEnumFacing.FACING_LOOKUP[p_176856_0_.ordinal()]) {
                case 1:
                    switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()]) {
                        case 1:
                            return DOWN_X;

                        case 2:
                            return DOWN_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                    }

                case 2:
                    switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()]) {
                        case 1:
                            return UP_X;

                        case 2:
                            return UP_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                    }

                case 3:
                    return NORTH;

                case 4:
                    return SOUTH;

                case 5:
                    return WEST;

                case 6:
                    return EAST;

                default:
                    throw new IllegalArgumentException("Invalid facing: " + p_176856_0_);
            }
        }

        @Override
        public String getName() {
            return field_176867_k;
        }

        static {
            BlockLever.EnumOrientation[] var0 = EnumOrientation.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockLever.EnumOrientation var3 = var0[var2];
                EnumOrientation.field_176869_i[var3.func_176855_a()] = var3;
            }
        }
    }

    static final class SwitchEnumFacing {
        static final int[] FACING_LOOKUP;

        static final int[] ORIENTATION_LOOKUP;

        static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];
        private static final String __OBFID = "CL_00002103";

        static {
            try {
                SwitchEnumFacing.AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
            } catch (NoSuchFieldError var16) {
                ;
            }

            try {
                SwitchEnumFacing.AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
            } catch (NoSuchFieldError var15) {
                ;
            }

            ORIENTATION_LOOKUP = new int[BlockLever.EnumOrientation.values().length];

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.EAST.ordinal()] = 1;
            } catch (NoSuchFieldError var14) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.WEST.ordinal()] = 2;
            } catch (NoSuchFieldError var13) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError var12) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.NORTH.ordinal()] = 4;
            } catch (NoSuchFieldError var11) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_Z.ordinal()] = 5;
            } catch (NoSuchFieldError var10) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_X.ordinal()] = 6;
            } catch (NoSuchFieldError var9) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 7;
            } catch (NoSuchFieldError var8) {
                ;
            }

            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
            } catch (NoSuchFieldError var7) {
                ;
            }

            FACING_LOOKUP = new int[EnumFacing.values().length];

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
