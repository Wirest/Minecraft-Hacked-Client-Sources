// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCarrotOnAStick extends Item
{
    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
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
        if (playerIn.isRiding() && playerIn.ridingEntity instanceof EntityPig) {
            final EntityPig entitypig = (EntityPig)playerIn.ridingEntity;
            if (entitypig.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7) {
                entitypig.getAIControlledByPlayer().boostSpeed();
                itemStackIn.damageItem(7, playerIn);
                if (itemStackIn.stackSize == 0) {
                    final ItemStack itemstack = new ItemStack(Items.fishing_rod);
                    itemstack.setTagCompound(itemStackIn.getTagCompound());
                    return itemstack;
                }
            }
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
