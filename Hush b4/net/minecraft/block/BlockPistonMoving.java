// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;
    
    static {
        FACING = BlockPistonExtension.FACING;
        TYPE = BlockPistonExtension.TYPE;
    }
    
    public BlockPistonMoving() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, EnumFacing.NORTH).withProperty(BlockPistonMoving.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0f);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return null;
    }
    
    public static TileEntity newTileEntity(final IBlockState state, final EnumFacing facing, final boolean extending, final boolean renderHead) {
        return new TileEntityPiston(state, facing, extending, renderHead);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityPiston) {
            ((TileEntityPiston)tileentity).clearPistonTileEntity();
        }
        else {
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING).getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() instanceof BlockPistonBase && iblockstate.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            worldIn.setBlockToAir(blockpos);
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
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            final TileEntityPiston tileentitypiston = this.getTileEntity(worldIn, pos);
            if (tileentitypiston != null) {
                final IBlockState iblockstate = tileentitypiston.getPistonState();
                iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            }
        }
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        return null;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            worldIn.getTileEntity(pos);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntityPiston tileentitypiston = this.getTileEntity(worldIn, pos);
        if (tileentitypiston == null) {
            return null;
        }
        float f = tileentitypiston.getProgress(0.0f);
        if (tileentitypiston.isExtending()) {
            f = 1.0f - f;
        }
        return this.getBoundingBox(worldIn, pos, tileentitypiston.getPistonState(), f, tileentitypiston.getFacing());
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final TileEntityPiston tileentitypiston = this.getTileEntity(worldIn, pos);
        if (tileentitypiston != null) {
            final IBlockState iblockstate = tileentitypiston.getPistonState();
            final Block block = iblockstate.getBlock();
            if (block == this || block.getMaterial() == Material.air) {
                return;
            }
            float f = tileentitypiston.getProgress(0.0f);
            if (tileentitypiston.isExtending()) {
                f = 1.0f - f;
            }
            block.setBlockBoundsBasedOnState(worldIn, pos);
            if (block == Blocks.piston || block == Blocks.sticky_piston) {
                f = 0.0f;
            }
            final EnumFacing enumfacing = tileentitypiston.getFacing();
            this.minX = block.getBlockBoundsMinX() - enumfacing.getFrontOffsetX() * f;
            this.minY = block.getBlockBoundsMinY() - enumfacing.getFrontOffsetY() * f;
            this.minZ = block.getBlockBoundsMinZ() - enumfacing.getFrontOffsetZ() * f;
            this.maxX = block.getBlockBoundsMaxX() - enumfacing.getFrontOffsetX() * f;
            this.maxY = block.getBlockBoundsMaxY() - enumfacing.getFrontOffsetY() * f;
            this.maxZ = block.getBlockBoundsMaxZ() - enumfacing.getFrontOffsetZ() * f;
        }
    }
    
    public AxisAlignedBB getBoundingBox(final World worldIn, final BlockPos pos, final IBlockState extendingBlock, final float progress, final EnumFacing direction) {
        if (extendingBlock.getBlock() == this || extendingBlock.getBlock().getMaterial() == Material.air) {
            return null;
        }
        final AxisAlignedBB axisalignedbb = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos, extendingBlock);
        if (axisalignedbb == null) {
            return null;
        }
        double d0 = axisalignedbb.minX;
        double d2 = axisalignedbb.minY;
        double d3 = axisalignedbb.minZ;
        double d4 = axisalignedbb.maxX;
        double d5 = axisalignedbb.maxY;
        double d6 = axisalignedbb.maxZ;
        if (direction.getFrontOffsetX() < 0) {
            d0 -= direction.getFrontOffsetX() * progress;
        }
        else {
            d4 -= direction.getFrontOffsetX() * progress;
        }
        if (direction.getFrontOffsetY() < 0) {
            d2 -= direction.getFrontOffsetY() * progress;
        }
        else {
            d5 -= direction.getFrontOffsetY() * progress;
        }
        if (direction.getFrontOffsetZ() < 0) {
            d3 -= direction.getFrontOffsetZ() * progress;
        }
        else {
            d6 -= direction.getFrontOffsetZ() * progress;
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    private TileEntityPiston getTileEntity(final IBlockAccess worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityPiston) ? ((TileEntityPiston)tileentity) : null;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, BlockPistonExtension.getFacing(meta)).withProperty(BlockPistonMoving.TYPE, ((meta & 0x8) > 0) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING).getIndex();
        if (state.getValue(BlockPistonMoving.TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonMoving.FACING, BlockPistonMoving.TYPE });
    }
}
