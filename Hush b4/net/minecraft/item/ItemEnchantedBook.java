// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemEnchantedBook extends Item
{
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
    
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return false;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return (this.getEnchantments(stack).tagCount() > 0) ? EnumRarity.UNCOMMON : super.getRarity(stack);
    }
    
    public NBTTagList getEnchantments(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getTagCompound();
        return (NBTTagList)((nbttagcompound != null && nbttagcompound.hasKey("StoredEnchantments", 9)) ? nbttagcompound.getTag("StoredEnchantments") : new NBTTagList());
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        final NBTTagList nbttaglist = this.getEnchantments(stack);
        if (nbttaglist != null) {
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final int j = nbttaglist.getCompoundTagAt(i).getShort("id");
                final int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                if (Enchantment.getEnchantmentById(j) != null) {
                    tooltip.add(Enchantment.getEnchantmentById(j).getTranslatedName(k));
                }
            }
        }
    }
    
    public void addEnchantment(final ItemStack stack, final EnchantmentData enchantment) {
        final NBTTagList nbttaglist = this.getEnchantments(stack);
        boolean flag = true;
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            if (nbttagcompound.getShort("id") == enchantment.enchantmentobj.effectId) {
                if (nbttagcompound.getShort("lvl") < enchantment.enchantmentLevel) {
                    nbttagcompound.setShort("lvl", (short)enchantment.enchantmentLevel);
                }
                flag = false;
                break;
            }
        }
        if (flag) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setShort("id", (short)enchantment.enchantmentobj.effectId);
            nbttagcompound2.setShort("lvl", (short)enchantment.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound2);
        }
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }
    
    public ItemStack getEnchantedItemStack(final EnchantmentData data) {
        final ItemStack itemstack = new ItemStack(this);
        this.addEnchantment(itemstack, data);
        return itemstack;
    }
    
    public void getAll(final Enchantment enchantment, final List<ItemStack> list) {
        for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
            list.add(this.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
        }
    }
    
    public WeightedRandomChestContent getRandom(final Random rand) {
        return this.getRandom(rand, 1, 1, 1);
    }
    
    public WeightedRandomChestContent getRandom(final Random rand, final int minChance, final int maxChance, final int weight) {
        final ItemStack itemstack = new ItemStack(Items.book, 1, 0);
        EnchantmentHelper.addRandomEnchantment(rand, itemstack, 30);
        return new WeightedRandomChestContent(itemstack, minChance, maxChance, weight);
    }
}
