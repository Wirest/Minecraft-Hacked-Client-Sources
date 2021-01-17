// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemShears extends Item
{
    public ItemShears() {
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final Block blockIn, final BlockPos pos, final EntityLivingBase playerIn) {
        if (blockIn.getMaterial() != Material.leaves && blockIn != Blocks.web && blockIn != Blocks.tallgrass && blockIn != Blocks.vine && blockIn != Blocks.tripwire && blockIn != Blocks.wool) {
            return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
        }
        stack.damageItem(1, playerIn);
        return true;
    }
    
    @Override
    public boolean canHarvestBlock(final Block blockIn) {
        return blockIn == Blocks.web || blockIn == Blocks.redstone_wire || blockIn == Blocks.tripwire;
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block block) {
        return (block != Blocks.web && block.getMaterial() != Material.leaves) ? ((block == Blocks.wool) ? 5.0f : super.getStrVsBlock(stack, block)) : 15.0f;
    }
}
