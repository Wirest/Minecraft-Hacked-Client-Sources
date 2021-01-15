package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection field_176284_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool field_176283_b = PropertyBool.create("open");
    public static final PropertyEnum field_176285_M = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
    private static final String __OBFID = "CL_00000327";

    protected BlockTrapDoor(Material p_i45434_1_)
    {
        super(p_i45434_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176284_a, EnumFacing.NORTH).withProperty(field_176283_b, Boolean.valueOf(false)).withProperty(field_176285_M, BlockTrapDoor.DoorHalf.BOTTOM));
        float var2 = 0.5F;
        float var3 = 1.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return !((Boolean)blockAccess.getBlockState(pos).getValue(field_176283_b)).booleanValue();
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        this.func_180693_d(access.getBlockState(pos));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.1875F;
        this.setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
    }

    public void func_180693_d(IBlockState p_180693_1_)
    {
        if (p_180693_1_.getBlock() == this)
        {
            boolean var2 = p_180693_1_.getValue(field_176285_M) == BlockTrapDoor.DoorHalf.TOP;
            Boolean var3 = (Boolean)p_180693_1_.getValue(field_176283_b);
            EnumFacing var4 = (EnumFacing)p_180693_1_.getValue(field_176284_a);
            float var5 = 0.1875F;

            if (var2)
            {
                this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
            }

            if (var3.booleanValue())
            {
                if (var4 == EnumFacing.NORTH)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                }

                if (var4 == EnumFacing.SOUTH)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                }

                if (var4 == EnumFacing.WEST)
                {
                    this.setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }

                if (var4 == EnumFacing.EAST)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            state = state.cycleProperty(field_176283_b);
            worldIn.setBlockState(pos, state, 2);
            worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue(field_176283_b)).booleanValue() ? 1003 : 1006, pos, 0);
            return true;
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            BlockPos var5 = pos.offset(((EnumFacing)state.getValue(field_176284_a)).getOpposite());

            if (!isValidSupportBlock(worldIn.getBlockState(var5).getBlock()))
            {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            else
            {
                boolean var6 = worldIn.isBlockPowered(pos);

                if (var6 || neighborBlock.canProvidePower())
                {
                    boolean var7 = ((Boolean)state.getValue(field_176283_b)).booleanValue();

                    if (var7 != var6)
                    {
                        worldIn.setBlockState(pos, state.withProperty(field_176283_b, Boolean.valueOf(var6)), 2);
                        worldIn.playAuxSFXAtEntity((EntityPlayer)null, var6 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState var9 = this.getDefaultState();

        if (facing.getAxis().isHorizontal())
        {
            var9 = var9.withProperty(field_176284_a, facing).withProperty(field_176283_b, Boolean.valueOf(false));
            var9 = var9.withProperty(field_176285_M, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        }

        return var9;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return !side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }

    protected static EnumFacing func_176281_b(int p_176281_0_)
    {
        switch (p_176281_0_ & 3)
        {
            case 0:
                return EnumFacing.NORTH;

            case 1:
                return EnumFacing.SOUTH;

            case 2:
                return EnumFacing.WEST;

            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    protected static int func_176282_a(EnumFacing p_176282_0_)
    {
        switch (BlockTrapDoor.SwitchEnumFacing.field_177058_a[p_176282_0_.ordinal()])
        {
            case 1:
                return 0;

            case 2:
                return 1;

            case 3:
                return 2;

            case 4:
            default:
                return 3;
        }
    }

    private static boolean isValidSupportBlock(Block p_150119_0_)
    {
        return p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.isFullCube() || p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab || p_150119_0_ instanceof BlockStairs;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(field_176284_a, func_176281_b(meta)).withProperty(field_176283_b, Boolean.valueOf((meta & 4) != 0)).withProperty(field_176285_M, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | func_176282_a((EnumFacing)state.getValue(field_176284_a));

        if (((Boolean)state.getValue(field_176283_b)).booleanValue())
        {
            var3 |= 4;
        }

        if (state.getValue(field_176285_M) == BlockTrapDoor.DoorHalf.TOP)
        {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {field_176284_a, field_176283_b, field_176285_M});
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"),
        BOTTOM("BOTTOM", 1, "bottom");
        private final String field_176671_c;

        private static final BlockTrapDoor.DoorHalf[] $VALUES = new BlockTrapDoor.DoorHalf[]{TOP, BOTTOM};
        private static final String __OBFID = "CL_00002051";

        private DoorHalf(String p_i45674_1_, int p_i45674_2_, String p_i45674_3_)
        {
            this.field_176671_c = p_i45674_3_;
        }

        public String toString()
        {
            return this.field_176671_c;
        }

        public String getName()
        {
            return this.field_176671_c;
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_177058_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002052";

        static
        {
            try
            {
                field_177058_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_177058_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177058_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177058_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
