package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockDirectional {
    public static final PropertyBool field_176466_a = PropertyBool.create("open");
    public static final PropertyBool field_176465_b = PropertyBool.create("powered");
    public static final PropertyBool field_176467_M = PropertyBool.create("in_wall");
    private static final String __OBFID = "CL_00000243";

    public BlockFenceGate() {
        super(Material.wood);
        setDefaultState(blockState.getBaseState().withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(false)).withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf(false)).withProperty(BlockFenceGate.field_176467_M, Boolean.valueOf(false)));
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis var4 = ((EnumFacing) state.getValue(BlockDirectional.AGE)).getAxis();

        if (var4 == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.offsetWest()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetEast()).getBlock() == Blocks.cobblestone_wall) || var4 == EnumFacing.Axis.X && (worldIn.getBlockState(pos.offsetNorth()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetSouth()).getBlock() == Blocks.cobblestone_wall)) {
            state = state.withProperty(BlockFenceGate.field_176467_M, Boolean.valueOf(true));
        }

        return state;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue()) {
            return null;
        } else {
            EnumFacing.Axis var4 = ((EnumFacing) state.getValue(BlockDirectional.AGE)).getAxis();
            return var4 == EnumFacing.Axis.Z ? new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + 0.375F, pos.getX() + 1, pos.getY() + 1.5F, pos.getZ() + 0.625F) : new AxisAlignedBB(pos.getX() + 0.375F, pos.getY(), pos.getZ(), pos.getX() + 0.625F, pos.getY() + 1.5F, pos.getZ() + 1);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        EnumFacing.Axis var3 = ((EnumFacing) access.getBlockState(pos).getValue(BlockDirectional.AGE)).getAxis();

        if (var3 == EnumFacing.Axis.Z) {
            setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        } else {
            setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }
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
        return ((Boolean) blockAccess.getBlockState(pos).getValue(BlockFenceGate.field_176466_a)).booleanValue();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO()).withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(false)).withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf(false)).withProperty(BlockFenceGate.field_176467_M, Boolean.valueOf(false));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue()) {
            state = state.withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(false));
            worldIn.setBlockState(pos, state, 2);
        } else {
            EnumFacing var9 = EnumFacing.fromAngle(playerIn.rotationYaw);

            if (state.getValue(BlockDirectional.AGE) == var9.getOpposite()) {
                state = state.withProperty(BlockDirectional.AGE, var9);
            }

            state = state.withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 2);
        }

        worldIn.playAuxSFXAtEntity(playerIn, ((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue() ? 1003 : 1006, pos, 0);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            boolean var5 = worldIn.isBlockPowered(pos);

            if (var5 || neighborBlock.canProvidePower()) {
                if (var5 && !((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue() && !((Boolean) state.getValue(BlockFenceGate.field_176465_b)).booleanValue()) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(true)).withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf(true)), 2);
                    worldIn.playAuxSFXAtEntity((EntityPlayer) null, 1003, pos, 0);
                } else if (!var5 && ((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue() && ((Boolean) state.getValue(BlockFenceGate.field_176465_b)).booleanValue()) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf(false)).withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf(false)), 2);
                    worldIn.playAuxSFXAtEntity((EntityPlayer) null, 1006, pos, 0);
                } else if (var5 != ((Boolean) state.getValue(BlockFenceGate.field_176465_b)).booleanValue()) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf(var5)), 2);
                }
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockFenceGate.field_176466_a, Boolean.valueOf((meta & 4) != 0)).withProperty(BlockFenceGate.field_176465_b, Boolean.valueOf((meta & 8) != 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing) state.getValue(BlockDirectional.AGE)).getHorizontalIndex();

        if (((Boolean) state.getValue(BlockFenceGate.field_176465_b)).booleanValue()) {
            var3 |= 8;
        }

        if (((Boolean) state.getValue(BlockFenceGate.field_176466_a)).booleanValue()) {
            var3 |= 4;
        }

        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDirectional.AGE, BlockFenceGate.field_176466_a, BlockFenceGate.field_176465_b, BlockFenceGate.field_176467_M});
    }
}
