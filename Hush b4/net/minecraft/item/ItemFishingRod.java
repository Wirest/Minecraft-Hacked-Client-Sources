// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFishingRod extends Item
{
    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (playerIn.fishEntity != null) {
            final int i = playerIn.fishEntity.handleHookRetraction();
            itemStackIn.damageItem(i, playerIn);
            playerIn.swingItem();
        }
        else {
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (ItemFishingRod.itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(new EntityFishHook(worldIn, playerIn));
            }
            playerIn.swingItem();
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }
    
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return super.isItemTool(stack);
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
