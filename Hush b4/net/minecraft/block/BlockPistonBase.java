// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonBase extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool EXTENDED;
    private final boolean isSticky;
    
    static {
        FACING = PropertyDirection.create("facing");
        EXTENDED = PropertyBool.create("extended");
    }
    
    public BlockPistonBase(final boolean isSticky) {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, false));
        this.isSticky = isSticky;
        this.setStepSound(BlockPistonBase.soundTypePiston);
        this.setHardness(0.5f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.FACING, getFacingFromEntity(worldIn, pos, placer)), 2);
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, false);
    }
    
    private void checkForMove(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
        final boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
        if (flag && !state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            if (new BlockPistonStructureHelper(worldIn, pos, enumfacing, true).canMove()) {
                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
            }
        }
        else if (!flag && state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, false), 2);
            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
        }
    }
    
    private boolean shouldBeExtended(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)) {
                return true;
            }
        }
        if (worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
            return true;
        }
        final BlockPos blockpos = pos.up();
        EnumFacing[] values2;
        for (int length2 = (values2 = EnumFacing.values()).length, j = 0; j < length2; ++j) {
            final EnumFacing enumfacing2 = values2[j];
            if (enumfacing2 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing2), enumfacing2)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
        if (!worldIn.isRemote) {
            final boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
            if (flag && eventID == 1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true), 2);
                return false;
            }
            if (!flag && eventID == 0) {
                return false;
            }
        }
        if (eventID == 0) {
            if (!this.doMove(worldIn, pos, enumfacing, true)) {
                return false;
            }
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true), 2);
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "tile.piston.out", 0.5f, worldIn.rand.nextFloat() * 0.25f + 0.6f);
        }
        else if (eventID == 1) {
            final TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
            if (tileentity1 instanceof TileEntityPiston) {
                ((TileEntityPiston)tileentity1).clearPistonTileEntity();
            }
            worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.newTileEntity(this.getStateFromMeta(eventParam), enumfacing, false, true));
            if (this.isSticky) {
                final BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
                final Block block = worldIn.getBlockState(blockpos).getBlock();
                boolean flag2 = false;
                if (block == Blocks.piston_extension) {
                    final TileEntity tileentity2 = worldIn.getTileEntity(blockpos);
                    if (tileentity2 instanceof TileEntityPiston) {
                        final TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity2;
                        if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
                            tileentitypiston.clearPistonTileEntity();
                            flag2 = true;
                        }
                    }
                }
                if (!flag2 && block.getMaterial() != Material.air && canPush(block, worldIn, blockpos, enumfacing.getOpposite(), false) && (block.getMobilityFlag() == 0 || block == Blocks.piston || block == Blocks.sticky_piston)) {
                    this.doMove(worldIn, pos, enumfacing, false);
                }
            }
            else {
                worldIn.setBlockToAir(pos.offset(enumfacing));
            }
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "tile.piston.in", 0.5f, worldIn.rand.nextFloat() * 0.15f + 0.6f);
        }
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this && iblockstate.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            final float f = 0.25f;
            final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
            if (enumfacing != null) {
                switch (enumfacing) {
                    case DOWN: {
                        this.setBlockBounds(0.0f, 0.25f, 0.0f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case UP: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
                        break;
                    }
                    case NORTH: {
                        this.setBlockBounds(0.0f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case SOUTH: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                        break;
                    }
                    case WEST: {
                        this.setBlockBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case EAST: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                        break;
                    }
                }
            }
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    public static EnumFacing getFacing(final int meta) {
        final int i = meta & 0x7;
        return (i > 5) ? null : EnumFacing.getFront(i);
    }
    
    public static EnumFacing getFacingFromEntity(final World worldIn, final BlockPos clickedBlock, final EntityLivingBase entityIn) {
        if (MathHelper.abs((float)entityIn.posX - clickedBlock.getX()) < 2.0f && MathHelper.abs((float)entityIn.posZ - clickedBlock.getZ()) < 2.0f) {
            final double d0 = entityIn.posY + entityIn.getEyeHeight();
            if (d0 - clickedBlock.getY() > 2.0) {
                return EnumFacing.UP;
            }
            if (clickedBlock.getY() - d0 > 0.0) {
                return EnumFacing.DOWN;
            }
        }
        return entityIn.getHorizontalFacing().getOpposite();
    }
    
    public static boolean canPush(final Block blockIn, final World worldIn, final BlockPos pos, final EnumFacing direction, final boolean allowDestroy) {
        if (blockIn == Blocks.obsidian) {
            return false;
        }
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        if (pos.getY() < 0 || (direction == EnumFacing.DOWN && pos.getY() == 0)) {
            return false;
        }
        if (pos.getY() <= worldIn.getHeight() - 1 && (direction != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
            if (blockIn != Blocks.piston && blockIn != Blocks.sticky_piston) {
                if (blockIn.getBlockHardness(worldIn, pos) == -1.0f) {
                    return false;
                }
                if (blockIn.getMobilityFlag() == 2) {
                    return false;
                }
                if (blockIn.getMobilityFlag() == 1) {
                    return allowDestroy;
                }
            }
            else if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
                return false;
            }
            return !(blockIn instanceof ITileEntityProvider);
        }
        return false;
    }
    
    private boolean doMove(final World worldIn, final BlockPos pos, final EnumFacing direction, final boolean extending) {
        if (!extending) {
            worldIn.setBlockToAir(pos.offset(direction));
        }
        final BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
        final List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
        final List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
        if (!blockpistonstructurehelper.canMove()) {
            return false;
        }
        int i = list.size() + list2.size();
        final Block[] ablock = new Block[i];
        final EnumFacing enumfacing = extending ? direction : direction.getOpposite();
        for (int j = list2.size() - 1; j >= 0; --j) {
            final BlockPos blockpos = list2.get(j);
            final Block block = worldIn.getBlockState(blockpos).getBlock();
            block.dropBlockAsItem(worldIn, blockpos, worldIn.getBlockState(blockpos), 0);
            worldIn.setBlockToAir(blockpos);
            --i;
            ablock[i] = block;
        }
        for (int k = list.size() - 1; k >= 0; --k) {
            BlockPos blockpos2 = list.get(k);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos2);
            final Block block2 = iblockstate.getBlock();
            block2.getMetaFromState(iblockstate);
            worldIn.setBlockToAir(blockpos2);
            blockpos2 = blockpos2.offset(enumfacing);
            worldIn.setBlockState(blockpos2, Blocks.piston_extension.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, direction), 4);
            worldIn.setTileEntity(blockpos2, BlockPistonMoving.newTileEntity(iblockstate, direction, extending, false));
            --i;
            ablock[i] = block2;
        }
        final BlockPos blockpos3 = pos.offset(direction);
        if (extending) {
            final BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            final IBlockState iblockstate2 = Blocks.piston_head.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
            final IBlockState iblockstate3 = Blocks.piston_extension.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            worldIn.setBlockState(blockpos3, iblockstate3, 4);
            worldIn.setTileEntity(blockpos3, BlockPistonMoving.newTileEntity(iblockstate2, direction, true, false));
        }
        for (int l = list2.size() - 1; l >= 0; --l) {
            worldIn.notifyNeighborsOfStateChange(list2.get(l), ablock[i++]);
        }
        for (int i2 = list.size() - 1; i2 >= 0; --i2) {
            worldIn.notifyNeighborsOfStateChange(list.get(i2), ablock[i++]);
        }
        if (extending) {
            worldIn.notifyNeighborsOfStateChange(blockpos3, Blocks.piston_head);
            worldIn.notifyNeighborsOfStateChange(pos, this);
        }
        return true;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, EnumFacing.UP);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, getFacing(meta)).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonBase.FACING, BlockPistonBase.EXTENDED });
    }
}
