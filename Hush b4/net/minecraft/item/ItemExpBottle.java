// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemExpBottle extends Item
{
    public ItemExpBottle() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
        }
        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (ItemExpBottle.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!worldIn.isRemote) {
            worldIn.spawnEntityInWorld(new EntityExpBottle(worldIn, playerIn));
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
