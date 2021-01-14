package net.minecraft.block;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class BlockSponge extends Block {
    public static final PropertyBool WET_PROP = PropertyBool.create("wet");
    private static final String __OBFID = "CL_00000311";

    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty(WET_PROP, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((Boolean) state.getValue(WET_PROP)).booleanValue() ? 1 : 0;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setWet(worldIn, pos, state);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.setWet(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected void setWet(World worldIn, BlockPos p_176311_2_, IBlockState p_176311_3_) {
        if (!((Boolean) p_176311_3_.getValue(WET_PROP)).booleanValue() && this.absorbWater(worldIn, p_176311_2_)) {
            worldIn.setBlockState(p_176311_2_, p_176311_3_.withProperty(WET_PROP, Boolean.valueOf(true)), 2);
            worldIn.playAuxSFX(2001, p_176311_2_, Block.getIdFromBlock(Blocks.water));
        }
    }

    private boolean absorbWater(World worldIn, BlockPos p_176312_2_) {
        LinkedList var3 = Lists.newLinkedList();
        ArrayList var4 = Lists.newArrayList();
        var3.add(new Tuple(p_176312_2_, Integer.valueOf(0)));
        int var5 = 0;
        BlockPos var7;

        while (!var3.isEmpty()) {
            Tuple var6 = (Tuple) var3.poll();
            var7 = (BlockPos) var6.getFirst();
            int var8 = ((Integer) var6.getSecond()).intValue();
            EnumFacing[] var9 = EnumFacing.values();
            int var10 = var9.length;

            for (int var11 = 0; var11 < var10; ++var11) {
                EnumFacing var12 = var9[var11];
                BlockPos var13 = var7.offset(var12);

                if (worldIn.getBlockState(var13).getBlock().getMaterial() == Material.water) {
                    worldIn.setBlockState(var13, Blocks.air.getDefaultState(), 2);
                    var4.add(var13);
                    ++var5;

                    if (var8 < 6) {
                        var3.add(new Tuple(var13, Integer.valueOf(var8 + 1)));
                    }
                }
            }

            if (var5 > 64) {
                break;
            }
        }

        Iterator var14 = var4.iterator();

        while (var14.hasNext()) {
            var7 = (BlockPos) var14.next();
            worldIn.notifyNeighborsOfStateChange(var7, Blocks.air);
        }

        return var5 > 0;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(WET_PROP, Boolean.valueOf((meta & 1) == 1));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(WET_PROP)).booleanValue() ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{WET_PROP});
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (((Boolean) state.getValue(WET_PROP)).booleanValue()) {
            EnumFacing var5 = EnumFacing.random(rand);

            if (var5 != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(var5))) {
                double var6 = (double) pos.getX();
                double var8 = (double) pos.getY();
                double var10 = (double) pos.getZ();

                if (var5 == EnumFacing.DOWN) {
                    var8 -= 0.05D;
                    var6 += rand.nextDouble();
                    var10 += rand.nextDouble();
                } else {
                    var8 += rand.nextDouble() * 0.8D;

                    if (var5.getAxis() == EnumFacing.Axis.X) {
                        var10 += rand.nextDouble();

                        if (var5 == EnumFacing.EAST) {
                            ++var6;
                        } else {
                            var6 += 0.05D;
                        }
                    } else {
                        var6 += rand.nextDouble();

                        if (var5 == EnumFacing.SOUTH) {
                            ++var10;
                        } else {
                            var10 += 0.05D;
                        }
                    }
                }

                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }
}
