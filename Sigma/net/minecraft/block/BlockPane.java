package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    private final boolean field_150099_b;
    private static final String __OBFID = "CL_00000322";

    protected BlockPane(Material p_i45675_1_, boolean p_i45675_2_) {
        super(p_i45675_1_);
        setDefaultState(blockState.getBaseState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)));
        field_150099_b = p_i45675_2_;
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(BlockPane.NORTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.offsetNorth()).getBlock()))).withProperty(BlockPane.SOUTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.offsetSouth()).getBlock()))).withProperty(BlockPane.WEST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.offsetWest()).getBlock()))).withProperty(BlockPane.EAST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.offsetEast()).getBlock())));
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return !field_150099_b ? null : super.getItemDropped(state, rand, fortune);
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
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the
     * given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        boolean var7 = canPaneConnectToBlock(worldIn.getBlockState(pos.offsetNorth()).getBlock());
        boolean var8 = canPaneConnectToBlock(worldIn.getBlockState(pos.offsetSouth()).getBlock());
        boolean var9 = canPaneConnectToBlock(worldIn.getBlockState(pos.offsetWest()).getBlock());
        boolean var10 = canPaneConnectToBlock(worldIn.getBlockState(pos.offsetEast()).getBlock());

        if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
            if (var9) {
                setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            } else if (var10) {
                setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        } else {
            setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
            if (var7) {
                setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            } else if (var8) {
                setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        } else {
            setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.4375F;
        float var4 = 0.5625F;
        float var5 = 0.4375F;
        float var6 = 0.5625F;
        boolean var7 = canPaneConnectToBlock(access.getBlockState(pos.offsetNorth()).getBlock());
        boolean var8 = canPaneConnectToBlock(access.getBlockState(pos.offsetSouth()).getBlock());
        boolean var9 = canPaneConnectToBlock(access.getBlockState(pos.offsetWest()).getBlock());
        boolean var10 = canPaneConnectToBlock(access.getBlockState(pos.offsetEast()).getBlock());

        if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
            if (var9) {
                var3 = 0.0F;
            } else if (var10) {
                var4 = 1.0F;
            }
        } else {
            var3 = 0.0F;
            var4 = 1.0F;
        }

        if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
            if (var7) {
                var5 = 0.0F;
            } else if (var8) {
                var6 = 1.0F;
            }
        } else {
            var5 = 0.0F;
            var6 = 1.0F;
        }

        setBlockBounds(var3, 0.0F, var5, var4, 1.0F, var6);
    }

    public final boolean canPaneConnectToBlock(Block p_150098_1_) {
        return p_150098_1_.isFullBlock() || p_150098_1_ == this || p_150098_1_ == Blocks.glass || p_150098_1_ == Blocks.stained_glass || p_150098_1_ == Blocks.stained_glass_pane || p_150098_1_ instanceof BlockPane;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockPane.NORTH, BlockPane.EAST, BlockPane.WEST, BlockPane.SOUTH});
    }
}
