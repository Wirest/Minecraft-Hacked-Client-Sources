package net.minecraft.enchantment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

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

    private static final EnumEnchantmentType[] $VALUES = new EnumEnchantmentType[]{ALL, ARMOR, ARMOR_FEET, ARMOR_LEGS, ARMOR_TORSO, ARMOR_HEAD, WEAPON, DIGGER, FISHING_ROD, BREAKABLE, BOW};
    private static final String __OBFID = "CL_00000106";

    private EnumEnchantmentType(String p_i1927_1_, int p_i1927_2_) {}

    /**
     * Return true if the item passed can be enchanted by a enchantment of this type.
     */
    public boolean canEnchantItem(Item p_77557_1_)
    {
        if (this == ALL)
        {
            return true;
        }
        else if (this == BREAKABLE && p_77557_1_.isDamageable())
        {
            return true;
        }
        else if (p_77557_1_ instanceof ItemArmor)
        {
            if (this == ARMOR)
            {
                return true;
            }
            else
            {
                ItemArmor var2 = (ItemArmor)p_77557_1_;
                return var2.armorType == 0 ? this == ARMOR_HEAD : (var2.armorType == 2 ? this == ARMOR_LEGS : (var2.armorType == 1 ? this == ARMOR_TORSO : (var2.armorType == 3 ? this == ARMOR_FEET : false)));
            }
        }
        else
        {
            return p_77557_1_ instanceof ItemSword ? this == WEAPON : (p_77557_1_ instanceof ItemTool ? this == DIGGER : (p_77557_1_ instanceof ItemBow ? this == BOW : (p_77557_1_ instanceof ItemFishingRod ? this == FISHING_ROD : false)));
        }
    }
}
