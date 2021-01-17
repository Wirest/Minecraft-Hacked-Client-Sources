// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonExtension extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<EnumPistonType> TYPE;
    public static final PropertyBool SHORT;
    
    static {
        FACING = PropertyDirection.create("facing");
        TYPE = PropertyEnum.create("type", EnumPistonType.class);
        SHORT = PropertyBool.create("short");
    }
    
    public BlockPistonExtension() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, EnumFacing.NORTH).withProperty(BlockPistonExtension.TYPE, EnumPistonType.DEFAULT).withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, false));
        this.setStepSound(BlockPistonExtension.soundTypePiston);
        this.setHardness(0.5f);
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING);
            if (enumfacing != null) {
                final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
                final Block block = worldIn.getBlockState(blockpos).getBlock();
                if (block == Blocks.piston || block == Blocks.sticky_piston) {
                    worldIn.setBlockToAir(blockpos);
                }
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(final World worldIn, BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING).getOpposite();
        pos = pos.offset(enumfacing);
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if ((iblockstate.getBlock() == Blocks.piston || iblockstate.getBlock() == Blocks.sticky_piston) && iblockstate.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.applyHeadBounds(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.applyCoreBounds(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void applyCoreBounds(final IBlockState state) {
        final float f = 0.25f;
        final float f2 = 0.375f;
        final float f3 = 0.625f;
        final float f4 = 0.25f;
        final float f5 = 0.75f;
        switch (state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)) {
            case DOWN: {
                this.setBlockBounds(0.375f, 0.25f, 0.375f, 0.625f, 1.0f, 0.625f);
                break;
            }
            case UP: {
                this.setBlockBounds(0.375f, 0.0f, 0.375f, 0.625f, 0.75f, 0.625f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.25f, 0.375f, 0.25f, 0.75f, 0.625f, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.25f, 0.375f, 0.0f, 0.75f, 0.625f, 0.75f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.375f, 0.25f, 0.25f, 0.625f, 0.75f, 1.0f);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, 0.375f, 0.25f, 0.75f, 0.625f, 0.75f);
                break;
            }
        }
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.applyHeadBounds(worldIn.getBlockState(pos));
    }
    
    public void applyHeadBounds(final IBlockState state) {
        final float f = 0.25f;
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING);
        if (enumfacing != null) {
            switch (enumfacing) {
                case DOWN: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
                    break;
                }
                case UP: {
                    this.setBlockBounds(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case NORTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.25f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.75f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.25f, 1.0f, 1.0f);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING);
        final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != Blocks.piston && iblockstate.getBlock() != Blocks.sticky_piston) {
            worldIn.setBlockToAir(pos);
        }
        else {
            iblockstate.getBlock().onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    public static EnumFacing getFacing(final int meta) {
        final int i = meta & 0x7;
        return (i > 5) ? null : EnumFacing.getFront(i);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return (worldIn.getBlockState(pos).getValue(BlockPistonExtension.TYPE) == EnumPistonType.STICKY) ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, getFacing(meta)).withProperty(BlockPistonExtension.TYPE, ((meta & 0x8) > 0) ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING).getIndex();
        if (state.getValue(BlockPistonExtension.TYPE) == EnumPistonType.STICKY) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonExtension.FACING, BlockPistonExtension.TYPE, BlockPistonExtension.SHORT });
    }
    
    public enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, "normal"), 
        STICKY("STICKY", 1, "sticky");
        
        private final String VARIANT;
        
        private EnumPistonType(final String name2, final int ordinal, final String name) {
            this.VARIANT = name;
        }
        
        @Override
        public String toString() {
            return this.VARIANT;
        }
        
        @Override
        public String getName() {
            return this.VARIANT;
        }
    }
}
