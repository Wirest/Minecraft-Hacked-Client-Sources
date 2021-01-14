package net.minecraft.block;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
    public static final PropertyDirection field_176382_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final String __OBFID = "CL_00000262";

    protected BlockLadder() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176382_a, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        IBlockState var3 = access.getBlockState(pos);

        if (var3.getBlock() == this) {
            float var4 = 0.125F;

            switch (BlockLadder.SwitchEnumFacing.field_180190_a[((EnumFacing) var3.getValue(field_176382_a)).ordinal()]) {
                case 1:
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var4, 1.0F, 1.0F, 1.0F);
                    break;

                case 2:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var4);
                    break;

                case 3:
                    this.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 4:
                default:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var4, 1.0F, 1.0F);
            }
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.offsetWest()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.offsetEast()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.offsetNorth()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.offsetSouth()).getBlock().isNormalCube()));
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal() && this.func_176381_b(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(field_176382_a, facing);
        } else {
            Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var10;

            do {
                if (!var9.hasNext()) {
                    return this.getDefaultState();
                }

                var10 = (EnumFacing) var9.next();
            }
            while (!this.func_176381_b(worldIn, pos, var10));

            return this.getDefaultState().withProperty(field_176382_a, var10);
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing) state.getValue(field_176382_a);

        if (!this.func_176381_b(worldIn, pos, var5)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected boolean func_176381_b(World worldIn, BlockPos p_176381_2_, EnumFacing p_176381_3_) {
        return worldIn.getBlockState(p_176381_2_.offset(p_176381_3_.getOpposite())).getBlock().isNormalCube();
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);

        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(field_176382_a, var2);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(field_176382_a)).getIndex();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176382_a});
    }

    static final class SwitchEnumFacing {
        static final int[] field_180190_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002104";

        static {
            try {
                field_180190_a[EnumFacing.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_180190_a[EnumFacing.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_180190_a[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_180190_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
