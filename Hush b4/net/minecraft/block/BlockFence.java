// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemLead;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockFence extends Block
{
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    
    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }
    
    public BlockFence(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    public BlockFence(final Material p_i46395_1_, final MapColor p_i46395_2_) {
        super(p_i46395_1_, p_i46395_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFence.NORTH, false).withProperty((IProperty<Comparable>)BlockFence.EAST, false).withProperty((IProperty<Comparable>)BlockFence.SOUTH, false).withProperty((IProperty<Comparable>)BlockFence.WEST, false));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        final boolean flag = this.canConnectTo(worldIn, pos.north());
        final boolean flag2 = this.canConnectTo(worldIn, pos.south());
        final boolean flag3 = this.canConnectTo(worldIn, pos.west());
        final boolean flag4 = this.canConnectTo(worldIn, pos.east());
        float f = 0.375f;
        float f2 = 0.625f;
        float f3 = 0.375f;
        float f4 = 0.625f;
        if (flag) {
            f3 = 0.0f;
        }
        if (flag2) {
            f4 = 1.0f;
        }
        if (flag || flag2) {
            this.setBlockBounds(f, 0.0f, f3, f2, 1.5f, f4);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        f3 = 0.375f;
        f4 = 0.625f;
        if (flag3) {
            f = 0.0f;
        }
        if (flag4) {
            f2 = 1.0f;
        }
        if (flag3 || flag4 || (!flag && !flag2)) {
            this.setBlockBounds(f, 0.0f, f3, f2, 1.5f, f4);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        if (flag) {
            f3 = 0.0f;
        }
        if (flag2) {
            f4 = 1.0f;
        }
        this.setBlockBounds(f, 0.0f, f3, f2, 1.0f, f4);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final boolean flag = this.canConnectTo(worldIn, pos.north());
        final boolean flag2 = this.canConnectTo(worldIn, pos.south());
        final boolean flag3 = this.canConnectTo(worldIn, pos.west());
        final boolean flag4 = this.canConnectTo(worldIn, pos.east());
        float f = 0.375f;
        float f2 = 0.625f;
        float f3 = 0.375f;
        float f4 = 0.625f;
        if (flag) {
            f3 = 0.0f;
        }
        if (flag2) {
            f4 = 1.0f;
        }
        if (flag3) {
            f = 0.0f;
        }
        if (flag4) {
            f2 = 1.0f;
        }
        this.setBlockBounds(f, 0.0f, f3, f2, 1.0f, f4);
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
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }
    
    public boolean canConnectTo(final IBlockAccess worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        return block != Blocks.barrier && ((block instanceof BlockFence && block.blockMaterial == this.blockMaterial) || block instanceof BlockFenceGate || (block.blockMaterial.isOpaque() && block.isFullCube() && block.blockMaterial != Material.gourd));
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return worldIn.isRemote || ItemLead.attachToFence(playerIn, worldIn, pos);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockFence.NORTH, this.canConnectTo(worldIn, pos.north())).withProperty((IProperty<Comparable>)BlockFence.EAST, this.canConnectTo(worldIn, pos.east())).withProperty((IProperty<Comparable>)BlockFence.SOUTH, this.canConnectTo(worldIn, pos.south())).withProperty((IProperty<Comparable>)BlockFence.WEST, this.canConnectTo(worldIn, pos.west()));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFence.NORTH, BlockFence.EAST, BlockFence.WEST, BlockFence.SOUTH });
    }
}
