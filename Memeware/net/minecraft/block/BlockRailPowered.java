package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase {
    public static final PropertyEnum field_176568_b = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        private static final String __OBFID = "CL_00002080";

        public boolean func_180133_a(BlockRailBase.EnumRailDirection p_180133_1_) {
            return p_180133_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_180133_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_180133_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_180133_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_180133_a((BlockRailBase.EnumRailDirection) p_apply_1_);
        }
    });
    public static final PropertyBool field_176569_M = PropertyBool.create("powered");
    private static final String __OBFID = "CL_00000288";

    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176568_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(field_176569_M, Boolean.valueOf(false)));
    }

    protected boolean func_176566_a(World worldIn, BlockPos p_176566_2_, IBlockState p_176566_3_, boolean p_176566_4_, int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        } else {
            int var6 = p_176566_2_.getX();
            int var7 = p_176566_2_.getY();
            int var8 = p_176566_2_.getZ();
            boolean var9 = true;
            BlockRailBase.EnumRailDirection var10 = (BlockRailBase.EnumRailDirection) p_176566_3_.getValue(field_176568_b);

            switch (BlockRailPowered.SwitchEnumRailDirection.field_180121_a[var10.ordinal()]) {
                case 1:
                    if (p_176566_4_) {
                        ++var8;
                    } else {
                        --var8;
                    }

                    break;

                case 2:
                    if (p_176566_4_) {
                        --var6;
                    } else {
                        ++var6;
                    }

                    break;

                case 3:
                    if (p_176566_4_) {
                        --var6;
                    } else {
                        ++var6;
                        ++var7;
                        var9 = false;
                    }

                    var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;

                case 4:
                    if (p_176566_4_) {
                        --var6;
                        ++var7;
                        var9 = false;
                    } else {
                        ++var6;
                    }

                    var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;

                case 5:
                    if (p_176566_4_) {
                        ++var8;
                    } else {
                        --var8;
                        ++var7;
                        var9 = false;
                    }

                    var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                    break;

                case 6:
                    if (p_176566_4_) {
                        ++var8;
                        ++var7;
                        var9 = false;
                    } else {
                        --var8;
                    }

                    var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            return this.func_176567_a(worldIn, new BlockPos(var6, var7, var8), p_176566_4_, p_176566_5_, var10) ? true : var9 && this.func_176567_a(worldIn, new BlockPos(var6, var7 - 1, var8), p_176566_4_, p_176566_5_, var10);
        }
    }

    protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int p_176567_4_, BlockRailBase.EnumRailDirection p_176567_5_) {
        IBlockState var6 = worldIn.getBlockState(p_176567_2_);

        if (var6.getBlock() != this) {
            return false;
        } else {
            BlockRailBase.EnumRailDirection var7 = (BlockRailBase.EnumRailDirection) var6.getValue(field_176568_b);
            return p_176567_5_ == BlockRailBase.EnumRailDirection.EAST_WEST && (var7 == BlockRailBase.EnumRailDirection.NORTH_SOUTH || var7 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || var7 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) ? false : (p_176567_5_ == BlockRailBase.EnumRailDirection.NORTH_SOUTH && (var7 == BlockRailBase.EnumRailDirection.EAST_WEST || var7 == BlockRailBase.EnumRailDirection.ASCENDING_EAST || var7 == BlockRailBase.EnumRailDirection.ASCENDING_WEST) ? false : (((Boolean) var6.getValue(field_176569_M)).booleanValue() ? (worldIn.isBlockPowered(p_176567_2_) ? true : this.func_176566_a(worldIn, p_176567_2_, var6, p_176567_3_, p_176567_4_ + 1)) : false));
        }
    }

    protected void func_176561_b(World worldIn, BlockPos p_176561_2_, IBlockState p_176561_3_, Block p_176561_4_) {
        boolean var5 = ((Boolean) p_176561_3_.getValue(field_176569_M)).booleanValue();
        boolean var6 = worldIn.isBlockPowered(p_176561_2_) || this.func_176566_a(worldIn, p_176561_2_, p_176561_3_, true, 0) || this.func_176566_a(worldIn, p_176561_2_, p_176561_3_, false, 0);

        if (var6 != var5) {
            worldIn.setBlockState(p_176561_2_, p_176561_3_.withProperty(field_176569_M, Boolean.valueOf(var6)), 3);
            worldIn.notifyNeighborsOfStateChange(p_176561_2_.offsetDown(), this);

            if (((BlockRailBase.EnumRailDirection) p_176561_3_.getValue(field_176568_b)).func_177018_c()) {
                worldIn.notifyNeighborsOfStateChange(p_176561_2_.offsetUp(), this);
            }
        }
    }

    public IProperty func_176560_l() {
        return field_176568_b;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176568_b, BlockRailBase.EnumRailDirection.func_177016_a(meta & 7)).withProperty(field_176569_M, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockRailBase.EnumRailDirection) state.getValue(field_176568_b)).func_177015_a();

        if (((Boolean) state.getValue(field_176569_M)).booleanValue()) {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176568_b, field_176569_M});
    }

    static final class SwitchEnumRailDirection {
        static final int[] field_180121_a = new int[BlockRailBase.EnumRailDirection.values().length];
        private static final String __OBFID = "CL_00002079";

        static {
            try {
                field_180121_a[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                field_180121_a[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_180121_a[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_180121_a[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_180121_a[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_180121_a[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
