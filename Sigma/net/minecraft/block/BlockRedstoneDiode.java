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

public abstract class BlockRedstoneDiode extends BlockDirectional {
    /**
     * Tells whether the repeater is powered or not
     */
    protected final boolean isRepeaterPowered;
    private static final String __OBFID = "CL_00000226";

    protected BlockRedstoneDiode(boolean p_i45400_1_) {
        super(Material.circuits);
        isRepeaterPowered = p_i45400_1_;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean func_176409_d(World worldIn, BlockPos p_176409_2_) {
        return World.doesBlockHaveSolidTopSurface(worldIn, p_176409_2_.offsetDown());
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
        if (!func_176405_b(worldIn, pos, state)) {
            boolean var5 = func_176404_e(worldIn, pos, state);

            if (isRepeaterPowered && !var5) {
                worldIn.setBlockState(pos, func_180675_k(state), 2);
            } else if (!isRepeaterPowered) {
                worldIn.setBlockState(pos, func_180674_e(state), 2);

                if (!var5) {
                    worldIn.func_175654_a(pos, func_180674_e(state).getBlock(), func_176399_m(state), -1);
                }
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    protected boolean func_176406_l(IBlockState p_176406_1_) {
        return isRepeaterPowered;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return isProvidingWeakPower(worldIn, pos, state, side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !func_176406_l(state) ? 0 : (state.getValue(BlockDirectional.AGE) == side ? func_176408_a(worldIn, pos, state) : 0);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (func_176409_d(worldIn, pos)) {
            func_176398_g(worldIn, pos, state);
        } else {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            EnumFacing[] var5 = EnumFacing.values();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                EnumFacing var8 = var5[var7];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var8), this);
            }
        }
    }

    protected void func_176398_g(World worldIn, BlockPos p_176398_2_, IBlockState p_176398_3_) {
        if (!func_176405_b(worldIn, p_176398_2_, p_176398_3_)) {
            boolean var4 = func_176404_e(worldIn, p_176398_2_, p_176398_3_);

            if ((isRepeaterPowered && !var4 || !isRepeaterPowered && var4) && !worldIn.isBlockTickPending(p_176398_2_, this)) {
                byte var5 = -1;

                if (func_176402_i(worldIn, p_176398_2_, p_176398_3_)) {
                    var5 = -3;
                } else if (isRepeaterPowered) {
                    var5 = -2;
                }

                worldIn.func_175654_a(p_176398_2_, this, func_176403_d(p_176398_3_), var5);
            }
        }
    }

    public boolean func_176405_b(IBlockAccess p_176405_1_, BlockPos p_176405_2_, IBlockState p_176405_3_) {
        return false;
    }

    protected boolean func_176404_e(World worldIn, BlockPos p_176404_2_, IBlockState p_176404_3_) {
        return func_176397_f(worldIn, p_176404_2_, p_176404_3_) > 0;
    }

    protected int func_176397_f(World worldIn, BlockPos p_176397_2_, IBlockState p_176397_3_) {
        EnumFacing var4 = (EnumFacing) p_176397_3_.getValue(BlockDirectional.AGE);
        BlockPos var5 = p_176397_2_.offset(var4);
        int var6 = worldIn.getRedstonePower(var5, var4);

        if (var6 >= 15) {
            return var6;
        } else {
            IBlockState var7 = worldIn.getBlockState(var5);
            return Math.max(var6, var7.getBlock() == Blocks.redstone_wire ? ((Integer) var7.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }

    protected int func_176407_c(IBlockAccess p_176407_1_, BlockPos p_176407_2_, IBlockState p_176407_3_) {
        EnumFacing var4 = (EnumFacing) p_176407_3_.getValue(BlockDirectional.AGE);
        EnumFacing var5 = var4.rotateY();
        EnumFacing var6 = var4.rotateYCCW();
        return Math.max(func_176401_c(p_176407_1_, p_176407_2_.offset(var5), var5), func_176401_c(p_176407_1_, p_176407_2_.offset(var6), var6));
    }

    protected int func_176401_c(IBlockAccess p_176401_1_, BlockPos p_176401_2_, EnumFacing p_176401_3_) {
        IBlockState var4 = p_176401_1_.getBlockState(p_176401_2_);
        Block var5 = var4.getBlock();
        return func_149908_a(var5) ? (var5 == Blocks.redstone_wire ? ((Integer) var4.getValue(BlockRedstoneWire.POWER)).intValue() : p_176401_1_.getStrongPower(p_176401_2_, p_176401_3_)) : 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (func_176404_e(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        func_176400_h(worldIn, pos, state);
    }

    protected void func_176400_h(World worldIn, BlockPos p_176400_2_, IBlockState p_176400_3_) {
        EnumFacing var4 = (EnumFacing) p_176400_3_.getValue(BlockDirectional.AGE);
        BlockPos var5 = p_176400_2_.offset(var4.getOpposite());
        worldIn.notifyBlockOfStateChange(var5, this);
        worldIn.notifyNeighborsOfStateExcept(var5, this, var4);
    }

    /**
     * Called when a player destroys this Block
     */
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if (isRepeaterPowered) {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }

        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    protected boolean func_149908_a(Block p_149908_1_) {
        return p_149908_1_.canProvidePower();
    }

    protected int func_176408_a(IBlockAccess p_176408_1_, BlockPos p_176408_2_, IBlockState p_176408_3_) {
        return 15;
    }

    public static boolean isRedstoneRepeaterBlockID(Block p_149909_0_) {
        return Blocks.unpowered_repeater.func_149907_e(p_149909_0_) || Blocks.unpowered_comparator.func_149907_e(p_149909_0_);
    }

    public boolean func_149907_e(Block p_149907_1_) {
        return p_149907_1_ == func_180674_e(getDefaultState()).getBlock() || p_149907_1_ == func_180675_k(getDefaultState()).getBlock();
    }

    public boolean func_176402_i(World worldIn, BlockPos p_176402_2_, IBlockState p_176402_3_) {
        EnumFacing var4 = ((EnumFacing) p_176402_3_.getValue(BlockDirectional.AGE)).getOpposite();
        BlockPos var5 = p_176402_2_.offset(var4);
        return BlockRedstoneDiode.isRedstoneRepeaterBlockID(worldIn.getBlockState(var5).getBlock()) ? worldIn.getBlockState(var5).getValue(BlockDirectional.AGE) != var4 : false;
    }

    protected int func_176399_m(IBlockState p_176399_1_) {
        return func_176403_d(p_176399_1_);
    }

    protected abstract int func_176403_d(IBlockState p_176403_1_);

    protected abstract IBlockState func_180674_e(IBlockState p_180674_1_);

    protected abstract IBlockState func_180675_k(IBlockState p_180675_1_);

    @Override
    public boolean isAssociatedBlock(Block other) {
        return func_149907_e(other);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
