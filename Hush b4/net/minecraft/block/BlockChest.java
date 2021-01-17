// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.inventory.Container;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;
import net.minecraft.stats.StatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockChest extends BlockContainer
{
    public static final PropertyDirection FACING;
    public final int chestType;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockChest(final int type) {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockChest.FACING, EnumFacing.NORTH));
        this.chestType = type;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
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
    public int getRenderType() {
        return 2;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos.north()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0f, 0.9375f, 0.875f, 0.9375f);
        }
        else if (worldIn.getBlockState(pos.south()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 1.0f);
        }
        else if (worldIn.getBlockState(pos.west()).getBlock() == this) {
            this.setBlockBounds(0.0f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
        else if (worldIn.getBlockState(pos.east()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 1.0f, 0.875f, 0.9375f);
        }
        else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForSurroundingChests(worldIn, pos, state);
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == this) {
                this.checkForSurroundingChests(worldIn, blockpos, iblockstate);
            }
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, placer.getHorizontalFacing());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double(placer.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3).getOpposite();
        state = state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
        final BlockPos blockpos = pos.north();
        final BlockPos blockpos2 = pos.south();
        final BlockPos blockpos3 = pos.west();
        final BlockPos blockpos4 = pos.east();
        final boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
        final boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
        final boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();
        final boolean flag4 = this == worldIn.getBlockState(blockpos4).getBlock();
        if (!flag && !flag2 && !flag3 && !flag4) {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || (!flag && !flag2)) {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag3 || flag4)) {
                if (flag3) {
                    worldIn.setBlockState(blockpos3, state, 3);
                }
                else {
                    worldIn.setBlockState(blockpos4, state, 3);
                }
                worldIn.setBlockState(pos, state, 3);
            }
        }
        else {
            if (flag) {
                worldIn.setBlockState(blockpos, state, 3);
            }
            else {
                worldIn.setBlockState(blockpos2, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        }
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    public IBlockState checkForSurroundingChests(final World worldIn, final BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) {
            return state;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos.north());
        final IBlockState iblockstate2 = worldIn.getBlockState(pos.south());
        final IBlockState iblockstate3 = worldIn.getBlockState(pos.west());
        final IBlockState iblockstate4 = worldIn.getBlockState(pos.east());
        EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockChest.FACING);
        final Block block = iblockstate.getBlock();
        final Block block2 = iblockstate2.getBlock();
        final Block block3 = iblockstate3.getBlock();
        final Block block4 = iblockstate4.getBlock();
        if (block != this && block2 != this) {
            final boolean flag = block.isFullBlock();
            final boolean flag2 = block2.isFullBlock();
            if (block3 == this || block4 == this) {
                final BlockPos blockpos1 = (block3 == this) ? pos.west() : pos.east();
                final IBlockState iblockstate5 = worldIn.getBlockState(blockpos1.north());
                final IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.south());
                enumfacing = EnumFacing.SOUTH;
                EnumFacing enumfacing2;
                if (block3 == this) {
                    enumfacing2 = iblockstate3.getValue((IProperty<EnumFacing>)BlockChest.FACING);
                }
                else {
                    enumfacing2 = iblockstate4.getValue((IProperty<EnumFacing>)BlockChest.FACING);
                }
                if (enumfacing2 == EnumFacing.NORTH) {
                    enumfacing = EnumFacing.NORTH;
                }
                final Block block5 = iblockstate5.getBlock();
                final Block block6 = iblockstate6.getBlock();
                if ((flag || block5.isFullBlock()) && !flag2 && !block6.isFullBlock()) {
                    enumfacing = EnumFacing.SOUTH;
                }
                if ((flag2 || block6.isFullBlock()) && !flag && !block5.isFullBlock()) {
                    enumfacing = EnumFacing.NORTH;
                }
            }
        }
        else {
            final BlockPos blockpos2 = (block == this) ? pos.north() : pos.south();
            final IBlockState iblockstate7 = worldIn.getBlockState(blockpos2.west());
            final IBlockState iblockstate8 = worldIn.getBlockState(blockpos2.east());
            enumfacing = EnumFacing.EAST;
            EnumFacing enumfacing3;
            if (block == this) {
                enumfacing3 = iblockstate.getValue((IProperty<EnumFacing>)BlockChest.FACING);
            }
            else {
                enumfacing3 = iblockstate2.getValue((IProperty<EnumFacing>)BlockChest.FACING);
            }
            if (enumfacing3 == EnumFacing.WEST) {
                enumfacing = EnumFacing.WEST;
            }
            final Block block7 = iblockstate7.getBlock();
            final Block block8 = iblockstate8.getBlock();
            if ((block3.isFullBlock() || block7.isFullBlock()) && !block4.isFullBlock() && !block8.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            }
            if ((block4.isFullBlock() || block8.isFullBlock()) && !block3.isFullBlock() && !block7.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
        }
        state = state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
        worldIn.setBlockState(pos, state, 3);
        return state;
    }
    
    public IBlockState correctFacing(final World worldIn, final BlockPos pos, final IBlockState state) {
        EnumFacing enumfacing = null;
        for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing)enumfacing2));
            if (iblockstate.getBlock() == this) {
                return state;
            }
            if (!iblockstate.getBlock().isFullBlock()) {
                continue;
            }
            if (enumfacing != null) {
                enumfacing = null;
                break;
            }
            enumfacing = (EnumFacing)enumfacing2;
        }
        if (enumfacing != null) {
            return state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing.getOpposite());
        }
        EnumFacing enumfacing3 = state.getValue((IProperty<EnumFacing>)BlockChest.FACING);
        if (worldIn.getBlockState(pos.offset(enumfacing3)).getBlock().isFullBlock()) {
            enumfacing3 = enumfacing3.getOpposite();
        }
        if (worldIn.getBlockState(pos.offset(enumfacing3)).getBlock().isFullBlock()) {
            enumfacing3 = enumfacing3.rotateY();
        }
        if (worldIn.getBlockState(pos.offset(enumfacing3)).getBlock().isFullBlock()) {
            enumfacing3 = enumfacing3.getOpposite();
        }
        return state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing3);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        int i = 0;
        final BlockPos blockpos = pos.west();
        final BlockPos blockpos2 = pos.east();
        final BlockPos blockpos3 = pos.north();
        final BlockPos blockpos4 = pos.south();
        if (worldIn.getBlockState(blockpos).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos2).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos2)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos3).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos3)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos4).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos4)) {
                return false;
            }
            ++i;
        }
        return i <= 1;
    }
    
    private boolean isDoubleChest(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return false;
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock() == this) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityChest) {
            tileentity.updateContainingBlockInfo();
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);
        if (ilockablecontainer != null) {
            playerIn.displayGUIChest(ilockablecontainer);
            if (this.chestType == 0) {
                playerIn.triggerAchievement(StatList.field_181723_aa);
            }
            else if (this.chestType == 1) {
                playerIn.triggerAchievement(StatList.field_181737_U);
            }
        }
        return true;
    }
    
    public ILockableContainer getLockableContainer(final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityChest)) {
            return null;
        }
        ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
        if (this.isBlocked(worldIn, pos)) {
            return null;
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
            final Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block == this) {
                if (this.isBlocked(worldIn, blockpos)) {
                    return null;
                }
                final TileEntity tileentity2 = worldIn.getTileEntity(blockpos);
                if (!(tileentity2 instanceof TileEntityChest)) {
                    continue;
                }
                if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
                    ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (ILockableContainer)tileentity2);
                }
                else {
                    ilockablecontainer = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileentity2, ilockablecontainer);
                }
            }
        }
        return ilockablecontainer;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityChest();
    }
    
    @Override
    public boolean canProvidePower() {
        return this.chestType == 1;
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        if (!this.canProvidePower()) {
            return 0;
        }
        int i = 0;
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityChest) {
            i = ((TileEntityChest)tileentity).numPlayersUsing;
        }
        return MathHelper.clamp_int(i, 0, 15);
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (side == EnumFacing.UP) ? this.getWeakPower(worldIn, pos, state, side) : 0;
    }
    
    private boolean isBlocked(final World worldIn, final BlockPos pos) {
        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }
    
    private boolean isBelowSolidBlock(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
    }
    
    private boolean isOcelotSittingOnChest(final World worldIn, final BlockPos pos) {
        for (final Entity entity : worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            final EntityOcelot entityocelot = (EntityOcelot)entity;
            if (entityocelot.isSitting()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockChest.FACING).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockChest.FACING });
    }
}
