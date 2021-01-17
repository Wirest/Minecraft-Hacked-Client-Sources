// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSaddle extends Item
{
    public ItemSaddle() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (target instanceof EntityPig) {
            final EntityPig entitypig = (EntityPig)target;
            if (!entitypig.getSaddled() && !entitypig.isChild()) {
                entitypig.setSaddled(true);
                entitypig.worldObj.playSoundAtEntity(entitypig, "mob.horse.leather", 0.5f, 1.0f);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        this.itemInteractionForEntity(stack, null, target);
        return true;
    }
}
