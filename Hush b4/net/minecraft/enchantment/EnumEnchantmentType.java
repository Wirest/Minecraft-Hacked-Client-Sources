// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;

public enum EnumEnchantmentType
{
    ALL("ALL", 0), 
    ARMOR("ARMOR", 1), 
    ARMOR_FEET("ARMOR_FEET", 2), 
    ARMOR_LEGS("ARMOR_LEGS", 3), 
    ARMOR_TORSO("ARMOR_TORSO", 4), 
    ARMOR_HEAD("ARMOR_HEAD", 5), 
    WEAPON("WEAPON", 6), 
    DIGGER("DIGGER", 7), 
    FISHING_ROD("FISHING_ROD", 8), 
    BREAKABLE("BREAKABLE", 9), 
    BOW("BOW", 10);
    
    private EnumEnchantmentType(final String name, final int ordinal) {
    }
    
    public boolean canEnchantItem(final Item p_77557_1_) {
        if (this == EnumEnchantmentType.ALL) {
            return true;
        }
        if (this == EnumEnchantmentType.BREAKABLE && p_77557_1_.isDamageable()) {
            return true;
        }
        if (!(p_77557_1_ instanceof ItemArmor)) {
            return (p_77557_1_ instanceof ItemSword) ? (this == EnumEnchantmentType.WEAPON) : ((p_77557_1_ instanceof ItemTool) ? (this == EnumEnchantmentType.DIGGER) : ((p_77557_1_ instanceof ItemBow) ? (this == EnumEnchantmentType.BOW) : (p_77557_1_ instanceof ItemFishingRod && this == EnumEnchantmentType.FISHING_ROD)));
        }
        if (this == EnumEnchantmentType.ARMOR) {
            return true;
        }
        final ItemArmor itemarmor = (ItemArmor)p_77557_1_;
        return (itemarmor.armorType == 0) ? (this == EnumEnchantmentType.ARMOR_HEAD) : ((itemarmor.armorType == 2) ? (this == EnumEnchantmentType.ARMOR_LEGS) : ((itemarmor.armorType == 1) ? (this == EnumEnchantmentType.ARMOR_TORSO) : (itemarmor.armorType == 3 && this == EnumEnchantmentType.ARMOR_FEET)));
    }
}
