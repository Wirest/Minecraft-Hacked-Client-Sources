// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailPowered extends BlockRailBase
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
    
    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, false));
    }
    
    protected boolean func_176566_a(final World worldIn, final BlockPos pos, final IBlockState state, final boolean p_176566_4_, final int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRailPowered.SHAPE);
        switch (blockrailbase$enumraildirection) {
            case NORTH_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    break;
                }
                --k;
                break;
            }
            case EAST_WEST: {
                if (p_176566_4_) {
                    --i;
                    break;
                }
                ++i;
                break;
            }
            case ASCENDING_EAST: {
                if (p_176566_4_) {
                    --i;
                }
                else {
                    ++i;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (p_176566_4_) {
                    --i;
                    ++j;
                    flag = false;
                }
                else {
                    ++i;
                }
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (p_176566_4_) {
                    ++k;
                }
                else {
                    --k;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    ++j;
                    flag = false;
                }
                else {
                    --k;
                }
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                break;
            }
        }
        return this.func_176567_a(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection) || (flag && this.func_176567_a(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection));
    }
    
    protected boolean func_176567_a(final World worldIn, final BlockPos p_176567_2_, final boolean p_176567_3_, final int distance, final EnumRailDirection p_176567_5_) {
        final IBlockState iblockstate = worldIn.getBlockState(p_176567_2_);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        final EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(BlockRailPowered.SHAPE);
        return (p_176567_5_ != EnumRailDirection.EAST_WEST || (blockrailbase$enumraildirection != EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_SOUTH)) && (p_176567_5_ != EnumRailDirection.NORTH_SOUTH || (blockrailbase$enumraildirection != EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_WEST)) && iblockstate.getValue((IProperty<Boolean>)BlockRailPowered.POWERED) && (worldIn.isBlockPowered(p_176567_2_) || this.func_176566_a(worldIn, p_176567_2_, iblockstate, p_176567_3_, distance + 1));
    }
    
    @Override
    protected void onNeighborChangedInternal(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockRailPowered.POWERED);
        final boolean flag2 = worldIn.isBlockPowered(pos) || this.func_176566_a(worldIn, pos, state, true, 0) || this.func_176566_a(worldIn, pos, state, false, 0);
        if (flag2 != flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, flag2), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            if (state.getValue(BlockRailPowered.SHAPE).isAscending()) {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
            }
        }
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRailPowered.SHAPE;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailPowered.SHAPE, EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockRailPowered.SHAPE).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockRailPowered.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRailPowered.SHAPE, BlockRailPowered.POWERED });
    }
}
