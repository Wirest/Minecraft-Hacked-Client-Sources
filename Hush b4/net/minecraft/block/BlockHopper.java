// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockHopper extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyBool ENABLED;
    
    static {
        FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
            @Override
            public boolean apply(final EnumFacing p_apply_1_) {
                return p_apply_1_ != EnumFacing.UP;
            }
        });
        ENABLED = PropertyBool.create("enabled");
    }
    
    public BlockHopper() {
        super(Material.iron, MapColor.stoneColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockHopper.FACING, EnumFacing.DOWN).withProperty((IProperty<Comparable>)BlockHopper.ENABLED, true));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        final float f = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        EnumFacing enumfacing = facing.getOpposite();
        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockHopper.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockHopper.ENABLED, true);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityHopper();
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityHopper) {
                ((TileEntityHopper)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.updateState(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityHopper) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.triggerAchievement(StatList.field_181732_P);
        }
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.updateState(worldIn, pos, state);
    }
    
    private void updateState(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean flag = !worldIn.isBlockPowered(pos);
        if (flag != state.getValue((IProperty<Boolean>)BlockHopper.ENABLED)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockHopper.ENABLED, flag), 4);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityHopper) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    public static EnumFacing getFacing(final int meta) {
        return EnumFacing.getFront(meta & 0x7);
    }
    
    public static boolean isEnabled(final int meta) {
        return (meta & 0x8) != 0x8;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockHopper.FACING, getFacing(meta)).withProperty((IProperty<Comparable>)BlockHopper.ENABLED, isEnabled(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockHopper.FACING).getIndex();
        if (!state.getValue((IProperty<Boolean>)BlockHopper.ENABLED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockHopper.FACING, BlockHopper.ENABLED });
    }
}
