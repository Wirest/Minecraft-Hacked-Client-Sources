package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block {
    private static final String __OBFID = "CL_00000194";

    protected BlockBasePressurePlate(Material materialIn) {
        super(materialIn);
        setCreativeTab(CreativeTabs.tabRedstone);
        setTickRandomly(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        func_180668_d(access.getBlockState(pos));
    }

    protected void func_180668_d(IBlockState p_180668_1_) {
        boolean var2 = getRedstoneStrength(p_180668_1_) > 0;
        float var3 = 0.0625F;

        if (var2) {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
        } else {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
        }
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
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
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return canBePlacedOn(worldIn, pos.offsetDown());
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!canBePlacedOn(worldIn, pos.offsetDown())) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBePlacedOn(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos) || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops
     * to grow, etc.)
     */
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            int var5 = getRedstoneStrength(state);

            if (var5 > 0) {
                updateState(worldIn, pos, state, var5);
            }
        }
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            int var5 = getRedstoneStrength(state);

            if (var5 == 0) {
                updateState(worldIn, pos, state, var5);
            }
        }
    }

    /**
     * Updates the pressure plate when stepped on
     */
    protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
        int var5 = computeRedstoneStrength(worldIn, pos);
        boolean var6 = oldRedstoneStrength > 0;
        boolean var7 = var5 > 0;

        if (oldRedstoneStrength != var5) {
            state = setRedstoneStrength(state, var5);
            worldIn.setBlockState(pos, state, 2);
            updateNeighbors(worldIn, pos);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }

        if (!var7 && var6) {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
        } else if (var7 && !var6) {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (var7) {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }

    /**
     * Returns the cubic AABB inset by 1/8 on all sides
     */
    protected AxisAlignedBB getSensitiveAABB(BlockPos pos) {
        float var2 = 0.125F;
        return new AxisAlignedBB(pos.getX() + 0.125F, pos.getY(), pos.getZ() + 0.125F, pos.getX() + 1 - 0.125F, pos.getY() + 0.25D, pos.getZ() + 1 - 0.125F);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (getRedstoneStrength(state) > 0) {
            updateNeighbors(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Notify block and block below of changes
     */
    protected void updateNeighbors(World worldIn, BlockPos pos) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.offsetDown(), this);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return getRedstoneStrength(state);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.UP ? getRedstoneStrength(state) : 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return true;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    protected abstract int computeRedstoneStrength(World worldIn, BlockPos pos);

    protected abstract int getRedstoneStrength(IBlockState p_176576_1_);

    protected abstract IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_);
}
