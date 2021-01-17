// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockButton extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool POWERED;
    private final boolean wooden;
    
    static {
        FACING = PropertyDirection.create("facing");
        POWERED = PropertyBool.create("powered");
    }
    
    protected BlockButton(final boolean wooden) {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockButton.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return this.wooden ? 30 : 20;
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
        return func_181088_a(worldIn, pos, side.getOpposite());
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (func_181088_a(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    protected static boolean func_181088_a(final World p_181088_0_, final BlockPos p_181088_1_, final EnumFacing p_181088_2_) {
        final BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
        return (p_181088_2_ == EnumFacing.DOWN) ? World.doesBlockHaveSolidTopSurface(p_181088_0_, blockpos) : p_181088_0_.getBlockState(blockpos).getBlock().isNormalCube();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return func_181088_a(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, facing).withProperty((IProperty<Comparable>)BlockButton.POWERED, false) : this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, EnumFacing.DOWN).withProperty((IProperty<Comparable>)BlockButton.POWERED, false);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING).getOpposite())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.canPlaceBlockAt(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.updateBlockBounds(worldIn.getBlockState(pos));
    }
    
    private void updateBlockBounds(final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockButton.FACING);
        final boolean flag = state.getValue((IProperty<Boolean>)BlockButton.POWERED);
        final float f = 0.25f;
        final float f2 = 0.375f;
        final float f3 = (flag ? 1 : 2) / 16.0f;
        final float f4 = 0.125f;
        final float f5 = 0.1875f;
        switch (enumfacing) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.375f, 0.3125f, f3, 0.625f, 0.6875f);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f3, 0.375f, 0.3125f, 1.0f, 0.625f, 0.6875f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.3125f, 0.375f, 0.0f, 0.6875f, 0.625f, f3);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.3125f, 0.375f, 1.0f - f3, 0.6875f, 0.625f, 1.0f);
                break;
            }
            case UP: {
                this.setBlockBounds(0.3125f, 0.0f, 0.375f, 0.6875f, 0.0f + f3, 0.625f);
                break;
            }
            case DOWN: {
                this.setBlockBounds(0.3125f, 1.0f - f3, 0.375f, 0.6875f, 1.0f, 0.625f);
                break;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, true), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockButton.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Boolean>)BlockButton.POWERED) ? ((state.getValue((IProperty<Comparable>)BlockButton.FACING) == side) ? 15 : 0) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            if (this.wooden) {
                this.checkForArrows(worldIn, pos, state);
            }
            else {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
                this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
                worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float f = 0.1875f;
        final float f2 = 0.125f;
        final float f3 = 0.125f;
        this.setBlockBounds(0.5f - f, 0.5f - f2, 0.5f - f3, 0.5f + f, 0.5f + f2, 0.5f + f3);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && this.wooden && !state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            this.checkForArrows(worldIn, pos, state);
        }
    }
    
    private void checkForArrows(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.updateBlockBounds(state);
        final List<? extends Entity> list = worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityArrow.class, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
        final boolean flag = !list.isEmpty();
        final boolean flag2 = state.getValue((IProperty<Boolean>)BlockButton.POWERED);
        if (flag && !flag2) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, true));
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!flag && flag2) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (flag) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    private void notifyNeighbors(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = null;
        switch (meta & 0x7) {
            case 0: {
                enumfacing = EnumFacing.DOWN;
                break;
            }
            case 1: {
                enumfacing = EnumFacing.EAST;
                break;
            }
            case 2: {
                enumfacing = EnumFacing.WEST;
                break;
            }
            case 3: {
                enumfacing = EnumFacing.SOUTH;
                break;
            }
            case 4: {
                enumfacing = EnumFacing.NORTH;
                break;
            }
            default: {
                enumfacing = EnumFacing.UP;
                break;
            }
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockButton.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        switch (state.getValue((IProperty<EnumFacing>)BlockButton.FACING)) {
            case EAST: {
                i = 1;
                break;
            }
            case WEST: {
                i = 2;
                break;
            }
            case SOUTH: {
                i = 3;
                break;
            }
            case NORTH: {
                i = 4;
                break;
            }
            default: {
                i = 5;
                break;
            }
            case DOWN: {
                i = 0;
                break;
            }
        }
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockButton.FACING, BlockButton.POWERED });
    }
}
