package net.minecraft.block;

import com.google.common.base.Predicate;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate()
    {
        public boolean apply(BlockRailBase.EnumRailDirection direction)
        {
            return direction != BlockRailBase.EnumRailDirection.NORTH_EAST && direction != BlockRailBase.EnumRailDirection.NORTH_WEST && direction != BlockRailBase.EnumRailDirection.SOUTH_EAST && direction != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((BlockRailBase.EnumRailDirection)p_apply_1_);
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered()
    {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
    }

    protected boolean func_176566_a(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_)
    {
        if (p_176566_5_ >= 8)
        {
            return false;
        }
        else
        {
            int var6 = pos.getX();
            int var7 = pos.getY();
            int var8 = pos.getZ();
            boolean var9 = true;
            BlockRailBase.EnumRailDirection var10 = (BlockRailBase.EnumRailDirection)state.getValue(SHAPE);

            switch (BlockRailPowered.SwitchEnumRailDirection.DIRECTION_LOOKUP[var10.ordinal()])
            {
                case 1:
                    if (p_176566_4_)
                    {
                        ++var8;
                    }
                    else
                    {
                        --var8;
                    }

                    break;

                case 2:
                    if (p_176566_4_)
                    {
                        --var6;
                    }
                    else
                    {
                        ++var6;
                    }

                    break;

                case 3:
                    if (p_176566_4_)
                    {
                        --var6;
                    }
                    else
                    {
                        ++var6;
                        ++var7;
                        var9 = false;
                    }

                    var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;

                case 4:
                    if (p_176566_4_)
                    {
                        --var6;
                        ++var7;
                        var9 = false;
                    }
                    else
                    {
                        ++var6;
                    }

                    var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;

                case 5:
                    if (p_176566_4_)
                    {
                        ++var8;
                    }
                    else
                    {
                        --var8;
                        ++var7;
                        var9 = false;
                    }

                    var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                    break;

                case 6:
                    if (p_176566_4_)
                    {
                        ++var8;
                        ++var7;
                        var9 = false;
                    }
                    else
                    {
                        --var8;
                    }

                    var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            return this.func_176567_a(worldIn, new BlockPos(var6, var7, var8), p_176566_4_, p_176566_5_, var10) ? true : var9 && this.func_176567_a(worldIn, new BlockPos(var6, var7 - 1, var8), p_176566_4_, p_176566_5_, var10);
        }
    }

    protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_)
    {
        IBlockState var6 = worldIn.getBlockState(p_176567_2_);

        if (var6.getBlock() != this)
        {
            return false;
        }
        else
        {
            BlockRailBase.EnumRailDirection var7 = (BlockRailBase.EnumRailDirection)var6.getValue(SHAPE);
            return p_176567_5_ == BlockRailBase.EnumRailDirection.EAST_WEST && (var7 == BlockRailBase.EnumRailDirection.NORTH_SOUTH || var7 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || var7 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) ? false : (p_176567_5_ == BlockRailBase.EnumRailDirection.NORTH_SOUTH && (var7 == BlockRailBase.EnumRailDirection.EAST_WEST || var7 == BlockRailBase.EnumRailDirection.ASCENDING_EAST || var7 == BlockRailBase.EnumRailDirection.ASCENDING_WEST) ? false : (((Boolean)var6.getValue(POWERED)).booleanValue() ? (worldIn.isBlockPowered(p_176567_2_) ? true : this.func_176566_a(worldIn, p_176567_2_, var6, p_176567_3_, distance + 1)) : false));
        }
    }

    @Override
	protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean var5 = ((Boolean)state.getValue(POWERED)).booleanValue();
        boolean var6 = worldIn.isBlockPowered(pos) || this.func_176566_a(worldIn, pos, state, true, 0) || this.func_176566_a(worldIn, pos, state, false, 0);

        if (var6 != var5)
        {
            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(var6)), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);

            if (((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).isAscending())
            {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
            }
        }
    }

    @Override
	public IProperty getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).getMetadata();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            var3 |= 8;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {SHAPE, POWERED});
    }

    static final class SwitchEnumRailDirection
    {
        static final int[] DIRECTION_LOOKUP = new int[BlockRailBase.EnumRailDirection.values().length];

        static
        {
            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
