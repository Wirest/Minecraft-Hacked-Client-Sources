// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool OPEN;
    public static final PropertyEnum<DoorHalf> HALF;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        OPEN = PropertyBool.create("open");
        HALF = PropertyEnum.create("half", DoorHalf.class);
    }
    
    protected BlockTrapDoor(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, false).withProperty(BlockTrapDoor.HALF, DoorHalf.BOTTOM));
        final float f = 0.5f;
        final float f2 = 1.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
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
        return !worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockTrapDoor.OPEN);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBounds(worldIn.getBlockState(pos));
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float f = 0.1875f;
        this.setBlockBounds(0.0f, 0.40625f, 0.0f, 1.0f, 0.59375f, 1.0f);
    }
    
    public void setBounds(final IBlockState state) {
        if (state.getBlock() == this) {
            final boolean flag = state.getValue(BlockTrapDoor.HALF) == DoorHalf.TOP;
            final Boolean obool = state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN);
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING);
            final float f = 0.1875f;
            if (flag) {
                this.setBlockBounds(0.0f, 0.8125f, 0.0f, 1.0f, 1.0f, 1.0f);
            }
            else {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.1875f, 1.0f);
            }
            if (obool) {
                if (enumfacing == EnumFacing.NORTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
                }
                if (enumfacing == EnumFacing.SOUTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
                }
                if (enumfacing == EnumFacing.WEST) {
                    this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
                if (enumfacing == EnumFacing.EAST) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        state = state.cycleProperty((IProperty<Comparable>)BlockTrapDoor.OPEN);
        worldIn.setBlockState(pos, state, 2);
        worldIn.playAuxSFXAtEntity(playerIn, ((boolean)state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) ? 1003 : 1006, pos, 0);
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING).getOpposite());
            if (!isValidSupportBlock(worldIn.getBlockState(blockpos).getBlock())) {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            else {
                final boolean flag = worldIn.isBlockPowered(pos);
                if (flag || neighborBlock.canProvidePower()) {
                    final boolean flag2 = state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN);
                    if (flag2 != flag) {
                        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, flag), 2);
                        worldIn.playAuxSFXAtEntity(null, flag ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, facing).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, false);
            iblockstate = iblockstate.withProperty(BlockTrapDoor.HALF, (hitY > 0.5f) ? DoorHalf.TOP : DoorHalf.BOTTOM);
        }
        return iblockstate;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return !side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }
    
    protected static EnumFacing getFacing(final int meta) {
        switch (meta & 0x3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
            default: {
                return EnumFacing.EAST;
            }
        }
    }
    
    protected static int getMetaForFacing(final EnumFacing facing) {
        switch (facing) {
            case NORTH: {
                return 0;
            }
            case SOUTH: {
                return 1;
            }
            case WEST: {
                return 2;
            }
            default: {
                return 3;
            }
        }
    }
    
    private static boolean isValidSupportBlock(final Block blockIn) {
        return (blockIn.blockMaterial.isOpaque() && blockIn.isFullCube()) || blockIn == Blocks.glowstone || blockIn instanceof BlockSlab || blockIn instanceof BlockStairs;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, getFacing(meta)).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, (meta & 0x4) != 0x0).withProperty(BlockTrapDoor.HALF, ((meta & 0x8) == 0x0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= getMetaForFacing(state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING));
        if (state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) {
            i |= 0x4;
        }
        if (state.getValue(BlockTrapDoor.HALF) == DoorHalf.TOP) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTrapDoor.FACING, BlockTrapDoor.OPEN, BlockTrapDoor.HALF });
    }
    
    public enum DoorHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String name;
        
        private DoorHalf(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
