// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockLever extends Block
{
    public static final PropertyEnum<EnumOrientation> FACING;
    public static final PropertyBool POWERED;
    
    static {
        FACING = PropertyEnum.create("facing", EnumOrientation.class);
        POWERED = PropertyBool.create("powered");
    }
    
    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty((IProperty<Comparable>)BlockLever.POWERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return func_181090_a(worldIn, pos, side.getOpposite());
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (func_181090_a(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    protected static boolean func_181090_a(final World p_181090_0_, final BlockPos p_181090_1_, final EnumFacing p_181090_2_) {
        return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockLever.POWERED, false);
        if (func_181090_a(worldIn, pos, facing.getOpposite())) {
            return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != facing && func_181090_a(worldIn, pos, ((EnumFacing)enumfacing).getOpposite())) {
                return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings((EnumFacing)enumfacing, placer.getHorizontalFacing()));
            }
        }
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
        }
        return iblockstate;
    }
    
    public static int getMetadataForFacing(final EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 5;
            }
            case NORTH: {
                return 4;
            }
            case SOUTH: {
                return 3;
            }
            case WEST: {
                return 2;
            }
            case EAST: {
                return 1;
            }
            default: {
                return -1;
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.func_181091_e(worldIn, pos, state) && !func_181090_a(worldIn, pos, state.getValue(BlockLever.FACING).getFacing().getOpposite())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean func_181091_e(final World p_181091_1_, final BlockPos p_181091_2_, final IBlockState p_181091_3_) {
        if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
            return true;
        }
        this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
        p_181091_1_.setBlockToAir(p_181091_2_);
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        float f = 0.1875f;
        switch (worldIn.getBlockState(pos).getValue(BlockLever.FACING)) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                break;
            }
            case UP_Z:
            case UP_X: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
                break;
            }
            case DOWN_X:
            case DOWN_Z: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.4f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
                break;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty((IProperty<Comparable>)BlockLever.POWERED);
        worldIn.setBlockState(pos, state, 3);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, ((boolean)state.getValue((IProperty<Boolean>)BlockLever.POWERED)) ? 0.6f : 0.5f);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        final EnumFacing enumfacing = state.getValue(BlockLever.FACING).getFacing();
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockLever.POWERED)) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            final EnumFacing enumfacing = state.getValue(BlockLever.FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockLever.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockLever.POWERED) ? ((state.getValue(BlockLever.FACING).getFacing() == side) ? 15 : 0) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockLever.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockLever.FACING).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockLever.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLever.FACING, BlockLever.POWERED });
    }
    
    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN), 
        EAST("EAST", 1, 1, "east", EnumFacing.EAST), 
        WEST("WEST", 2, 2, "west", EnumFacing.WEST), 
        SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH), 
        NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH), 
        UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP), 
        UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP), 
        DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);
        
        private static final EnumOrientation[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final EnumFacing facing;
        
        static {
            META_LOOKUP = new EnumOrientation[values().length];
            EnumOrientation[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumOrientation blocklever$enumorientation = values[i];
                EnumOrientation.META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
            }
        }
        
        private EnumOrientation(final String name2, final int ordinal, final int meta, final String name, final EnumFacing facing) {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public EnumFacing getFacing() {
            return this.facing;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumOrientation byMetadata(int meta) {
            if (meta < 0 || meta >= EnumOrientation.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumOrientation.META_LOOKUP[meta];
        }
        
        public static EnumOrientation forFacings(final EnumFacing clickedSide, final EnumFacing entityFacing) {
            switch (clickedSide) {
                case DOWN: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return EnumOrientation.DOWN_X;
                        }
                        case Z: {
                            return EnumOrientation.DOWN_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                        }
                    }
                    break;
                }
                case UP: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return EnumOrientation.UP_X;
                        }
                        case Z: {
                            return EnumOrientation.UP_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                        }
                    }
                    break;
                }
                case NORTH: {
                    return EnumOrientation.NORTH;
                }
                case SOUTH: {
                    return EnumOrientation.SOUTH;
                }
                case WEST: {
                    return EnumOrientation.WEST;
                }
                case EAST: {
                    return EnumOrientation.EAST;
                }
                default: {
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
                }
            }
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
