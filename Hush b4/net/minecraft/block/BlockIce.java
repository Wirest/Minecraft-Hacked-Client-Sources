// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.EnumSkyBlock;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockIce extends BlockBreakable
{
    public BlockIce() {
        super(Material.ice, false);
        this.slipperiness = 0.98f;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        player.addExhaustion(0.025f);
        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            final ItemStack itemstack = this.createStackedBlock(state);
            if (itemstack != null) {
                Block.spawnAsEntity(worldIn, pos, itemstack);
            }
        }
        else {
            if (worldIn.provider.doesWaterVaporize()) {
                worldIn.setBlockToAir(pos);
                return;
            }
            final int i = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            final Material material = worldIn.getBlockState(pos.down()).getBlock().getMaterial();
            if (material.blocksMovement() || material.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getLightOpacity()) {
            if (worldIn.provider.doesWaterVaporize()) {
                worldIn.setBlockToAir(pos);
            }
            else {
                this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockState(pos, Blocks.water.getDefaultState());
            }
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
}
