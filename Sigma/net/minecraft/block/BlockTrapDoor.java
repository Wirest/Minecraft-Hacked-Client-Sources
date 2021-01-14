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

public class BlockTrapDoor extends Block {
    public static final PropertyDirection field_176284_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool field_176283_b = PropertyBool.create("open");
    public static final PropertyEnum field_176285_M = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
    private static final String __OBFID = "CL_00000327";

    protected BlockTrapDoor(Material p_i45434_1_) {
        super(p_i45434_1_);
        setDefaultState(blockState.getBaseState().withProperty(BlockTrapDoor.field_176284_a, EnumFacing.NORTH).withProperty(BlockTrapDoor.field_176283_b, Boolean.valueOf(false)).withProperty(BlockTrapDoor.field_176285_M, BlockTrapDoor.DoorHalf.BOTTOM));
        float var2 = 0.5F;
        float var3 = 1.0F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setCreativeTab(CreativeTabs.tabRedstone);
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
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return !((Boolean) blockAccess.getBlockState(pos).getValue(BlockTrapDoor.field_176283_b)).booleanValue();
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        func_180693_d(access.getBlockState(pos));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.1875F;
        setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
    }

    public void func_180693_d(IBlockState p_180693_1_) {
        if (p_180693_1_.getBlock() == this) {
            boolean var2 = p_180693_1_.getValue(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.TOP;
            Boolean var3 = (Boolean) p_180693_1_.getValue(BlockTrapDoor.field_176283_b);
            EnumFacing var4 = (EnumFacing) p_180693_1_.getValue(BlockTrapDoor.field_176284_a);
            float var5 = 0.1875F;

            if (var2) {
                setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
            }

            if (var3.booleanValue()) {
                if (var4 == EnumFacing.NORTH) {
                    setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                }

                if (var4 == EnumFacing.SOUTH) {
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                }

                if (var4 == EnumFacing.WEST) {
                    setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }

                if (var4 == EnumFacing.EAST) {
                    setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (blockMaterial == Material.iron) {
            return true;
        } else {
            state = state.cycleProperty(BlockTrapDoor.field_176283_b);
            worldIn.setBlockState(pos, state, 2);
            worldIn.playAuxSFXAtEntity(playerIn, ((Boolean) state.getValue(BlockTrapDoor.field_176283_b)).booleanValue() ? 1003 : 1006, pos, 0);
            return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            BlockPos var5 = pos.offset(((EnumFacing) state.getValue(BlockTrapDoor.field_176284_a)).getOpposite());

            if (!BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(var5).getBlock())) {
                worldIn.setBlockToAir(pos);
                dropBlockAsItem(worldIn, pos, state, 0);
            } else {
                boolean var6 = worldIn.isBlockPowered(pos);

                if (var6 || neighborBlock.canProvidePower()) {
                    boolean var7 = ((Boolean) state.getValue(BlockTrapDoor.field_176283_b)).booleanValue();

                    if (var7 != var6) {
                        worldIn.setBlockState(pos, state.withProperty(BlockTrapDoor.field_176283_b, Boolean.valueOf(var6)), 2);
                        worldIn.playAuxSFXAtEntity((EntityPlayer) null, var6 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit.
     *
     * @param start The start vector
     * @param end   The end vector
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = getDefaultState();

        if (facing.getAxis().isHorizontal()) {
            var9 = var9.withProperty(BlockTrapDoor.field_176284_a, facing).withProperty(BlockTrapDoor.field_176283_b, Boolean.valueOf(false));
            var9 = var9.withProperty(BlockTrapDoor.field_176285_M, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        }

        return var9;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return !side.getAxis().isVertical() && BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }

    protected static EnumFacing func_176281_b(int p_176281_0_) {
        switch (p_176281_0_ & 3) {
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

    protected static int func_176282_a(EnumFacing p_176282_0_) {
        switch (BlockTrapDoor.SwitchEnumFacing.field_177058_a[p_176282_0_.ordinal()]) {
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

    private static boolean isValidSupportBlock(Block p_150119_0_) {
        return p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.isFullCube() || p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab || p_150119_0_ instanceof BlockStairs;
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
        return getDefaultState().withProperty(BlockTrapDoor.field_176284_a, BlockTrapDoor.func_176281_b(meta)).withProperty(BlockTrapDoor.field_176283_b, Boolean.valueOf((meta & 4) != 0)).withProperty(BlockTrapDoor.field_176285_M, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | BlockTrapDoor.func_176282_a((EnumFacing) state.getValue(BlockTrapDoor.field_176284_a));

        if (((Boolean) state.getValue(BlockTrapDoor.field_176283_b)).booleanValue()) {
            var3 |= 4;
        }

        if (state.getValue(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.TOP) {
            var3 |= 8;
        }

        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockTrapDoor.field_176284_a, BlockTrapDoor.field_176283_b, BlockTrapDoor.field_176285_M});
    }

    public static enum DoorHalf implements IStringSerializable {
        TOP("TOP", 0, "top"), BOTTOM("BOTTOM", 1, "bottom");
        private final String field_176671_c;

        private static final BlockTrapDoor.DoorHalf[] $VALUES = new BlockTrapDoor.DoorHalf[]{TOP, BOTTOM};
        private static final String __OBFID = "CL_00002051";

        private DoorHalf(String p_i45674_1_, int p_i45674_2_, String p_i45674_3_) {
            field_176671_c = p_i45674_3_;
        }

        @Override
        public String toString() {
            return field_176671_c;
        }

        @Override
        public String getName() {
            return field_176671_c;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_177058_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002052";

        static {
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
