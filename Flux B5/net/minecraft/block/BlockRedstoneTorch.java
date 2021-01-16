package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch
{
    private static Map field_150112_b = Maps.newHashMap();
    private final boolean field_150113_a;
    private static final String __OBFID = "CL_00000298";

    private boolean func_176598_a(World worldIn, BlockPos p_176598_2_, boolean p_176598_3_)
    {
        if (!field_150112_b.containsKey(worldIn))
        {
            field_150112_b.put(worldIn, Lists.newArrayList());
        }

        List var4 = (List)field_150112_b.get(worldIn);

        if (p_176598_3_)
        {
            var4.add(new BlockRedstoneTorch.Toggle(p_176598_2_, worldIn.getTotalWorldTime()));
        }

        int var5 = 0;

        for (int var6 = 0; var6 < var4.size(); ++var6)
        {
            BlockRedstoneTorch.Toggle var7 = (BlockRedstoneTorch.Toggle)var4.get(var6);

            if (var7.field_180111_a.equals(p_176598_2_))
            {
                ++var5;

                if (var5 >= 8)
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(boolean p_i45423_1_)
    {
        this.field_150113_a = p_i45423_1_;
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs)null);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.field_150113_a)
        {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.field_150113_a)
        {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return this.field_150113_a && state.getValue(FACING_PROP) != side ? 15 : 0;
    }

    private boolean func_176597_g(World worldIn, BlockPos p_176597_2_, IBlockState p_176597_3_)
    {
        EnumFacing var4 = ((EnumFacing)p_176597_3_.getValue(FACING_PROP)).getOpposite();
        return worldIn.func_175709_b(p_176597_2_.offset(var4), var4);
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean var5 = this.func_176597_g(worldIn, pos, state);
        List var6 = (List)field_150112_b.get(worldIn);

        while (var6 != null && !var6.isEmpty() && worldIn.getTotalWorldTime() - ((BlockRedstoneTorch.Toggle)var6.get(0)).field_150844_d > 60L)
        {
            var6.remove(0);
        }

        if (this.field_150113_a)
        {
            if (var5)
            {
                worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING_PROP, state.getValue(FACING_PROP)), 3);

                if (this.func_176598_a(worldIn, pos, true))
                {
                    worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int var7 = 0; var7 < 5; ++var7)
                    {
                        double var8 = (double)pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
                        double var10 = (double)pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
                        double var12 = (double)pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var8, var10, var12, 0.0D, 0.0D, 0.0D, new int[0]);
                    }

                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                }
            }
        }
        else if (!var5 && !this.func_176598_a(worldIn, pos, false))
        {
            worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty(FACING_PROP, state.getValue(FACING_PROP)), 3);
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!this.func_176592_e(worldIn, pos, state))
        {
            if (this.field_150113_a == this.func_176597_g(worldIn, pos, state))
            {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
        }
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return side == EnumFacing.DOWN ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.field_150113_a)
        {
            double var5 = (double)((float)pos.getX() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double var7 = (double)((float)pos.getY() + 0.7F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double var9 = (double)((float)pos.getZ() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            EnumFacing var11 = (EnumFacing)state.getValue(FACING_PROP);

            if (var11.getAxis().isHorizontal())
            {
                EnumFacing var12 = var11.getOpposite();
                double var13 = 0.27000001072883606D;
                var5 += 0.27000001072883606D * (double)var12.getFrontOffsetX();
                var7 += 0.2199999988079071D;
                var9 += 0.27000001072883606D * (double)var12.getFrontOffsetZ();
            }

            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var5, var7, var9, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    public boolean isAssociatedBlock(Block other)
    {
        return other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch;
    }

    static class Toggle
    {
        BlockPos field_180111_a;
        long field_150844_d;
        private static final String __OBFID = "CL_00000299";

        public Toggle(BlockPos p_i45688_1_, long p_i45688_2_)
        {
            this.field_180111_a = p_i45688_1_;
            this.field_150844_d = p_i45688_2_;
        }
    }
}
