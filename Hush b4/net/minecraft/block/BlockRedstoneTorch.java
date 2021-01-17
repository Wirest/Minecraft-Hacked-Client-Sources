// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Maps;
import java.util.List;
import net.minecraft.world.World;
import java.util.Map;

public class BlockRedstoneTorch extends BlockTorch
{
    private static Map<World, List<Toggle>> toggles;
    private final boolean isOn;
    
    static {
        BlockRedstoneTorch.toggles = (Map<World, List<Toggle>>)Maps.newHashMap();
    }
    
    private boolean isBurnedOut(final World worldIn, final BlockPos pos, final boolean turnOff) {
        if (!BlockRedstoneTorch.toggles.containsKey(worldIn)) {
            BlockRedstoneTorch.toggles.put(worldIn, (List<Toggle>)Lists.newArrayList());
        }
        final List<Toggle> list = BlockRedstoneTorch.toggles.get(worldIn);
        if (turnOff) {
            list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
        }
        int i = 0;
        for (int j = 0; j < list.size(); ++j) {
            final Toggle blockredstonetorch$toggle = list.get(j);
            if (blockredstonetorch$toggle.pos.equals(pos) && ++i >= 8) {
                return true;
            }
        }
        return false;
    }
    
    protected BlockRedstoneTorch(final boolean isOn) {
        this.isOn = isOn;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 2;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isOn) {
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isOn) {
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (this.isOn && state.getValue((IProperty<Comparable>)BlockRedstoneTorch.FACING) != side) ? 15 : 0;
    }
    
    private boolean shouldBeOff(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneTorch.FACING).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final boolean flag = this.shouldBeOff(worldIn, pos, state);
        final List<Toggle> list = BlockRedstoneTorch.toggles.get(worldIn);
        while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - list.get(0).time > 60L) {
            list.remove(0);
        }
        if (this.isOn) {
            if (flag) {
                worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneTorch.FACING, (EnumFacing)state.getValue((IProperty<V>)BlockRedstoneTorch.FACING)), 3);
                if (this.isBurnedOut(worldIn, pos, true)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
                    for (int i = 0; i < 5; ++i) {
                        final double d0 = pos.getX() + rand.nextDouble() * 0.6 + 0.2;
                        final double d2 = pos.getY() + rand.nextDouble() * 0.6 + 0.2;
                        final double d3 = pos.getZ() + rand.nextDouble() * 0.6 + 0.2;
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
                    }
                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                }
            }
        }
        else if (!flag && !this.isBurnedOut(worldIn, pos, false)) {
            worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneTorch.FACING, (EnumFacing)state.getValue((IProperty<V>)BlockRedstoneTorch.FACING)), 3);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.onNeighborChangeInternal(worldIn, pos, state) && this.isOn == this.shouldBeOff(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    @Override
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (side == EnumFacing.DOWN) ? this.getWeakPower(worldIn, pos, state, side) : 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isOn) {
            double d0 = pos.getX() + 0.5 + (rand.nextDouble() - 0.5) * 0.2;
            double d2 = pos.getY() + 0.7 + (rand.nextDouble() - 0.5) * 0.2;
            double d3 = pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) * 0.2;
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneTorch.FACING);
            if (enumfacing.getAxis().isHorizontal()) {
                final EnumFacing enumfacing2 = enumfacing.getOpposite();
                final double d4 = 0.27;
                d0 += 0.27 * enumfacing2.getFrontOffsetX();
                d2 += 0.22;
                d3 += 0.27 * enumfacing2.getFrontOffsetZ();
            }
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }
    
    @Override
    public boolean isAssociatedBlock(final Block other) {
        return other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch;
    }
    
    static class Toggle
    {
        BlockPos pos;
        long time;
        
        public Toggle(final BlockPos pos, final long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
