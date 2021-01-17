// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBucketMilk extends Item
{
    public ItemBucketMilk() {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        if (!worldIn.isRemote) {
            playerIn.clearActivePotions();
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return (stack.stackSize <= 0) ? new ItemStack(Items.bucket) : stack;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
}
