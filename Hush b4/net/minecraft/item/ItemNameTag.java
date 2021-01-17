// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemNameTag extends Item
{
    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (!stack.hasDisplayName()) {
            return false;
        }
        if (target instanceof EntityLiving) {
            final EntityLiving entityliving = (EntityLiving)target;
            entityliving.setCustomNameTag(stack.getDisplayName());
            entityliving.enablePersistence();
            --stack.stackSize;
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target);
    }
}
