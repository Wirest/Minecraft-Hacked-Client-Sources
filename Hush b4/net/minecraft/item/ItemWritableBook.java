// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemWritableBook extends Item
{
    public ItemWritableBook() {
        this.setMaxStackSize(1);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
    
    public static boolean isNBTValid(final NBTTagCompound nbt) {
        if (nbt == null) {
            return false;
        }
        if (!nbt.hasKey("pages", 9)) {
            return false;
        }
        final NBTTagList nbttaglist = nbt.getTagList("pages", 8);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final String s = nbttaglist.getStringTagAt(i);
            if (s == null) {
                return false;
            }
            if (s.length() > 32767) {
                return false;
            }
        }
        return true;
    }
}
