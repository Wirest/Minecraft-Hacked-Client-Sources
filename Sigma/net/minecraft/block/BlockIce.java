package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable {
    private static final String __OBFID = "CL_00000259";

    public BlockIce() {
        super(Material.ice, false);
        slipperiness = 0.98F;
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        playerIn.addExhaustion(0.025F);

        if (canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(playerIn)) {
            ItemStack var8 = createStackedBlock(state);

            if (var8 != null) {
                Block.spawnAsEntity(worldIn, pos, var8);
            }
        } else {
            if (worldIn.provider.func_177500_n()) {
                worldIn.setBlockToAir(pos);
                return;
            }

            int var6 = EnchantmentHelper.getFortuneModifier(playerIn);
            dropBlockAsItem(worldIn, pos, state, var6);
            Material var7 = worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial();

            if (var7.blocksMovement() || var7.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
            }
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - getLightOpacity()) {
            if (worldIn.provider.func_177500_n()) {
                worldIn.setBlockToAir(pos);
            } else {
                dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockState(pos, Blocks.water.getDefaultState());
            }
        }
    }

    @Override
    public int getMobilityFlag() {
        return 0;
    }
}
