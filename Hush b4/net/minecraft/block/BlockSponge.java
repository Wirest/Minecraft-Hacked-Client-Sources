// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Queue;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockSponge extends Block
{
    public static final PropertyBool WET;
    
    static {
        WET = PropertyBool.create("wet");
    }
    
    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockSponge.WET, false));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".dry.name");
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockSponge.WET)) ? 1 : 0;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.tryAbsorb(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.tryAbsorb(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    protected void tryAbsorb(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!state.getValue((IProperty<Boolean>)BlockSponge.WET) && this.absorb(worldIn, pos)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockSponge.WET, true), 2);
            worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
        }
    }
    
    private boolean absorb(final World worldIn, final BlockPos pos) {
        final Queue<Tuple<BlockPos, Integer>> queue = (Queue<Tuple<BlockPos, Integer>>)Lists.newLinkedList();
        final ArrayList<BlockPos> arraylist = Lists.newArrayList();
        queue.add(new Tuple<BlockPos, Integer>(pos, 0));
        int i = 0;
        while (!queue.isEmpty()) {
            final Tuple<BlockPos, Integer> tuple = queue.poll();
            final BlockPos blockpos = tuple.getFirst();
            final int j = tuple.getSecond();
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, k = 0; k < length; ++k) {
                final EnumFacing enumfacing = values[k];
                final BlockPos blockpos2 = blockpos.offset(enumfacing);
                if (worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.water) {
                    worldIn.setBlockState(blockpos2, Blocks.air.getDefaultState(), 2);
                    arraylist.add(blockpos2);
                    ++i;
                    if (j < 6) {
                        queue.add(new Tuple<BlockPos, Integer>(blockpos2, j + 1));
                    }
                }
            }
            if (i > 64) {
                break;
            }
        }
        for (final BlockPos blockpos3 : arraylist) {
            worldIn.notifyNeighborsOfStateChange(blockpos3, Blocks.air);
        }
        return i > 0;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSponge.WET, (meta & 0x1) == 0x1);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockSponge.WET)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSponge.WET });
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue((IProperty<Boolean>)BlockSponge.WET)) {
            final EnumFacing enumfacing = EnumFacing.random(rand);
            if (enumfacing != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(enumfacing))) {
                double d0 = pos.getX();
                double d2 = pos.getY();
                double d3 = pos.getZ();
                if (enumfacing == EnumFacing.DOWN) {
                    d2 -= 0.05;
                    d0 += rand.nextDouble();
                    d3 += rand.nextDouble();
                }
                else {
                    d2 += rand.nextDouble() * 0.8;
                    if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                        d3 += rand.nextDouble();
                        if (enumfacing == EnumFacing.EAST) {
                            ++d0;
                        }
                        else {
                            d0 += 0.05;
                        }
                    }
                    else {
                        d0 += rand.nextDouble();
                        if (enumfacing == EnumFacing.SOUTH) {
                            ++d3;
                        }
                        else {
                            d3 += 0.05;
                        }
                    }
                }
                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
}
