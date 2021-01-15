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

public class BlockLever extends Block
{
    public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockLever()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) ? true : this.canSustainLever(worldIn, pos.offset(side.getOpposite()));
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return this.canSustainLever(worldIn, pos.west()) ? true : (this.canSustainLever(worldIn, pos.east()) ? true : (this.canSustainLever(worldIn, pos.north()) ? true : (this.canSustainLever(worldIn, pos.south()) ? true : (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) ? true : this.canSustainLever(worldIn, pos.up())))));
    }

    protected boolean canSustainLever(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isNormalCube();
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState var9 = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));

        if (this.canSustainLever(worldIn, pos.offset(facing.getOpposite())))
        {
            return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        else
        {
            Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var11;

            do
            {
                if (!var10.hasNext())
                {
                    if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
                    {
                        return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
                    }

                    return var9;
                }

                var11 = (EnumFacing)var10.next();
            }
            while (var11 == facing || !this.canSustainLever(worldIn, pos.offset(var11.getOpposite())));

            return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings(var11, placer.getHorizontalFacing()));
        }
    }

    public static int getMetadataForFacing(EnumFacing facing)
    {
        switch (BlockLever.SwitchEnumFacing.FACING_LOOKUP[facing.ordinal()])
        {
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

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (this.checkForDrop(worldIn, pos) && !this.canSustainLever(worldIn, pos.offset(((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing().getOpposite())))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkForDrop(World worldIn, BlockPos pos)
    {
        if (this.canPlaceBlockAt(worldIn, pos))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float var3 = 0.1875F;

        switch (BlockLever.SwitchEnumFacing.ORIENTATION_LOOKUP[((BlockLever.EnumOrientation)worldIn.getBlockState(pos).getValue(FACING)).ordinal()])
        {
            case 1:
                this.setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
                break;

            case 2:
                this.setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
                break;

            case 3:
                this.setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
                break;

            case 4:
                this.setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
                break;

            case 5:
            case 6:
                var3 = 0.25F;
                this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
                break;

            case 7:
            case 8:
                var3 = 0.25F;
                this.setBlockBounds(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        }
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            state = state.cycleProperty(POWERED);
            worldIn.setBlockState(pos, state, 3);
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing var9 = ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
            return true;
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing var4 = ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    @Override
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : (((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing() == side ? 15 : 0);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
	public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockLever.EnumOrientation)state.getValue(FACING)).getMetadata();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            var3 |= 8;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, POWERED});
    }

    public static enum EnumOrientation implements IStringSerializable
    {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN),
        EAST("EAST", 1, 1, "east", EnumFacing.EAST),
        WEST("WEST", 2, 2, "west", EnumFacing.WEST),
        SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH),
        NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH),
        UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP),
        UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP),
        DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);
        private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[values().length];
        private final int meta;
        private final String name;
        private final EnumFacing facing; 

        private EnumOrientation(String p_i45709_1_, int p_i45709_2_, int meta, String name, EnumFacing facing)
        {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public EnumFacing getFacing()
        {
            return this.facing;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        public static BlockLever.EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static BlockLever.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing)
        {
            switch (BlockLever.SwitchEnumFacing.FACING_LOOKUP[clickedSide.ordinal()])
            {
                case 1:
                    switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[entityFacing.getAxis().ordinal()])
                    {
                        case 1:
                            return DOWN_X;

                        case 2:
                            return DOWN_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case 2:
                    switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[entityFacing.getAxis().ordinal()])
                    {
                        case 1:
                            return UP_X;

                        case 2:
                            return UP_Z;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
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
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        static {
            BlockLever.EnumOrientation[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockLever.EnumOrientation var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP;

        static final int[] ORIENTATION_LOOKUP;

        static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

        static
        {
            try
            {
                AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError var16)
            {
                ;
            }

            try
            {
                AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
            }
            catch (NoSuchFieldError var15)
            {
                ;
            }

            ORIENTATION_LOOKUP = new int[BlockLever.EnumOrientation.values().length];

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var14)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var13)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var12)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var11)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_Z.ordinal()] = 5;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_X.ordinal()] = 6;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 7;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            FACING_LOOKUP = new int[EnumFacing.values().length];

            try
            {
                FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
