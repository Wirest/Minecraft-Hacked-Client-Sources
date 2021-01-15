package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockDirectional
{
    /** Tells whether the repeater is powered or not */
    protected final boolean isRepeaterPowered;

    protected BlockRedstoneDiode(boolean powered)
    {
        super(Material.circuits);
        this.isRepeaterPowered = powered;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    @Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.isLocked(worldIn, pos, state))
        {
            boolean var5 = this.shouldBePowered(worldIn, pos, state);

            if (this.isRepeaterPowered && !var5)
            {
                worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
            }
            else if (!this.isRepeaterPowered)
            {
                worldIn.setBlockState(pos, this.getPoweredState(state), 2);

                if (!var5)
                {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                }
            }
        }
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    protected boolean isPowered(IBlockState state)
    {
        return this.isRepeaterPowered;
    }

    @Override
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return this.isProvidingWeakPower(worldIn, pos, state, side);
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !this.isPowered(state) ? 0 : (state.getValue(FACING) == side ? this.getActiveSignal(worldIn, pos, state) : 0);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (this.canBlockStay(worldIn, pos))
        {
            this.updateState(worldIn, pos, state);
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            EnumFacing[] var5 = EnumFacing.values();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7)
            {
                EnumFacing var8 = var5[var7];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var8), this);
            }
        }
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.isLocked(worldIn, pos, state))
        {
            boolean var4 = this.shouldBePowered(worldIn, pos, state);

            if ((this.isRepeaterPowered && !var4 || !this.isRepeaterPowered && var4) && !worldIn.isBlockTickPending(pos, this))
            {
                byte var5 = -1;

                if (this.isFacingTowardsRepeater(worldIn, pos, state))
                {
                    var5 = -3;
                }
                else if (this.isRepeaterPowered)
                {
                    var5 = -2;
                }

                worldIn.updateBlockTick(pos, this, this.getDelay(state), var5);
            }
        }
    }

    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return false;
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing var4 = (EnumFacing)state.getValue(FACING);
        BlockPos var5 = pos.offset(var4);
        int var6 = worldIn.getRedstonePower(var5, var4);

        if (var6 >= 15)
        {
            return var6;
        }
        else
        {
            IBlockState var7 = worldIn.getBlockState(var5);
            return Math.max(var6, var7.getBlock() == Blocks.redstone_wire ? ((Integer)var7.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }

    protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing var4 = (EnumFacing)state.getValue(FACING);
        EnumFacing var5 = var4.rotateY();
        EnumFacing var6 = var4.rotateYCCW();
        return Math.max(this.getPowerOnSide(worldIn, pos.offset(var5), var5), this.getPowerOnSide(worldIn, pos.offset(var6), var6));
    }

    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState var4 = worldIn.getBlockState(pos);
        Block var5 = var4.getBlock();
        return this.canPowerSide(var5) ? (var5 == Blocks.redstone_wire ? ((Integer)var4.getValue(BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side)) : 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
	public boolean canProvidePower()
    {
        return true;
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (this.shouldBePowered(worldIn, pos, state))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.notifyNeighbors(worldIn, pos, state);
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing var4 = (EnumFacing)state.getValue(FACING);
        BlockPos var5 = pos.offset(var4.getOpposite());
        worldIn.notifyBlockOfStateChange(var5, this);
        worldIn.notifyNeighborsOfStateExcept(var5, this, var4);
    }

    /**
     * Called when a player destroys this Block
     */
    @Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.isRepeaterPowered)
        {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }

        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    protected boolean canPowerSide(Block blockIn)
    {
        return blockIn.canProvidePower();
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return 15;
    }

    public static boolean isRedstoneRepeaterBlockID(Block blockIn)
    {
        return Blocks.unpowered_repeater.isAssociated(blockIn) || Blocks.unpowered_comparator.isAssociated(blockIn);
    }

    public boolean isAssociated(Block other)
    {
        return other == this.getPoweredState(this.getDefaultState()).getBlock() || other == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing var4 = ((EnumFacing)state.getValue(FACING)).getOpposite();
        BlockPos var5 = pos.offset(var4);
        return isRedstoneRepeaterBlockID(worldIn.getBlockState(var5).getBlock()) ? worldIn.getBlockState(var5).getValue(FACING) != var4 : false;
    }

    protected int getTickDelay(IBlockState state)
    {
        return this.getDelay(state);
    }

    protected abstract int getDelay(IBlockState var1);

    protected abstract IBlockState getPoweredState(IBlockState var1);

    protected abstract IBlockState getUnpoweredState(IBlockState var1);

    @Override
	public boolean isAssociatedBlock(Block other)
    {
        return this.isAssociated(other);
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
