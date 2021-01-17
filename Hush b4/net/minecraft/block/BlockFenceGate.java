// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockFenceGate extends BlockDirectional
{
    public static final PropertyBool OPEN;
    public static final PropertyBool POWERED;
    public static final PropertyBool IN_WALL;
    
    static {
        OPEN = PropertyBool.create("open");
        POWERED = PropertyBool.create("powered");
        IN_WALL = PropertyBool.create("in_wall");
    }
    
    public BlockFenceGate(final BlockPlanks.EnumType p_i46394_1_) {
        super(Material.wood, p_i46394_1_.func_181070_c());
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, false).withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis enumfacing$axis = state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis();
        if ((enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall)) || (enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.south()).getBlock() == Blocks.cobblestone_wall))) {
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, true);
        }
        return state;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid() && super.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            return null;
        }
        final EnumFacing.Axis enumfacing$axis = state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis();
        return (enumfacing$axis == EnumFacing.Axis.Z) ? new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + 0.375f, pos.getX() + 1, pos.getY() + 1.5f, pos.getZ() + 0.625f) : new AxisAlignedBB(pos.getX() + 0.375f, pos.getY(), pos.getZ(), pos.getX() + 0.625f, pos.getY() + 1.5f, pos.getZ() + 1);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis enumfacing$axis = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        }
        else {
            this.setBlockBounds(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
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
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockFenceGate.OPEN);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFenceGate.FACING, placer.getHorizontalFacing()).withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, false).withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, false);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false);
            worldIn.setBlockState(pos, state, 2);
        }
        else {
            final EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
            if (state.getValue((IProperty<Comparable>)BlockFenceGate.FACING) == enumfacing.getOpposite()) {
                state = state.withProperty((IProperty<Comparable>)BlockFenceGate.FACING, enumfacing);
            }
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, true);
            worldIn.setBlockState(pos, state, 2);
        }
        worldIn.playAuxSFXAtEntity(playerIn, ((boolean)state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) ? 1003 : 1006, pos, 0);
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);
            if (flag || neighborBlock.canProvidePower()) {
                if (flag && !state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN) && !state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED)) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, true).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, true), 2);
                    worldIn.playAuxSFXAtEntity(null, 1003, pos, 0);
                }
                else if (!flag && state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN) && state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED)) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, false), 2);
                    worldIn.playAuxSFXAtEntity(null, 1006, pos, 0);
                }
                else if (flag != state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED)) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, flag), 2);
                }
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFenceGate.FACING, EnumFacing.getHorizontal(meta)).withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, (meta & 0x4) != 0x0).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, (meta & 0x8) != 0x0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL });
    }
}
