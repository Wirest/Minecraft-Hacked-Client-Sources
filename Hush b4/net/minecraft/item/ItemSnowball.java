// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSnowball extends Item
{
    public ItemSnowball() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
        }
        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (ItemSnowball.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!worldIn.isRemote) {
            worldIn.spawnEntityInWorld(new EntitySnowball(worldIn, playerIn));
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
