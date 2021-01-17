// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockPane extends Block
{
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    private final boolean canDrop;
    
    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }
    
    protected BlockPane(final Material materialIn, final boolean canDrop) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPane.NORTH, false).withProperty((IProperty<Comparable>)BlockPane.EAST, false).withProperty((IProperty<Comparable>)BlockPane.SOUTH, false).withProperty((IProperty<Comparable>)BlockPane.WEST, false));
        this.canDrop = canDrop;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockPane.NORTH, this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock())).withProperty((IProperty<Comparable>)BlockPane.SOUTH, this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock())).withProperty((IProperty<Comparable>)BlockPane.WEST, this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock())).withProperty((IProperty<Comparable>)BlockPane.EAST, this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock()));
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return this.canDrop ? super.getItemDropped(state, rand, fortune) : null;
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
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock() != this && super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        final boolean flag = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
        final boolean flag2 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
        final boolean flag3 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
        final boolean flag4 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
        if ((!flag3 || !flag4) && (flag3 || flag4 || flag || flag2)) {
            if (flag3) {
                this.setBlockBounds(0.0f, 0.0f, 0.4375f, 0.5f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (flag4) {
                this.setBlockBounds(0.5f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        if ((!flag || !flag2) && (flag3 || flag4 || flag || flag2)) {
            if (flag) {
                this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 0.5f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (flag2) {
                this.setBlockBounds(0.4375f, 0.0f, 0.5f, 0.5625f, 1.0f, 1.0f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else {
            this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 1.0f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        float f = 0.4375f;
        float f2 = 0.5625f;
        float f3 = 0.4375f;
        float f4 = 0.5625f;
        final boolean flag = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
        final boolean flag2 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
        final boolean flag3 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
        final boolean flag4 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
        if ((!flag3 || !flag4) && (flag3 || flag4 || flag || flag2)) {
            if (flag3) {
                f = 0.0f;
            }
            else if (flag4) {
                f2 = 1.0f;
            }
        }
        else {
            f = 0.0f;
            f2 = 1.0f;
        }
        if ((!flag || !flag2) && (flag3 || flag4 || flag || flag2)) {
            if (flag) {
                f3 = 0.0f;
            }
            else if (flag2) {
                f4 = 1.0f;
            }
        }
        else {
            f3 = 0.0f;
            f4 = 1.0f;
        }
        this.setBlockBounds(f, 0.0f, f3, f2, 1.0f, f4);
    }
    
    public final boolean canPaneConnectToBlock(final Block blockIn) {
        return blockIn.isFullBlock() || blockIn == this || blockIn == Blocks.glass || blockIn == Blocks.stained_glass || blockIn == Blocks.stained_glass_pane || blockIn instanceof BlockPane;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPane.NORTH, BlockPane.EAST, BlockPane.WEST, BlockPane.SOUTH });
    }
}
