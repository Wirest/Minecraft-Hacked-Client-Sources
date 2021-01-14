package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDigging extends Enchantment {
    private static final String __OBFID = "CL_00000104";

    protected EnchantmentDigging(int p_i45772_1_, ResourceLocation p_i45772_2_, int p_i45772_3_) {
        super(p_i45772_1_, p_i45772_2_, p_i45772_3_, EnumEnchantmentType.DIGGER);
        this.setName("digging");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_) {
        return 1 + 10 * (p_77321_1_ - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 5;
    }

    public boolean canApply(ItemStack p_92089_1_) {
        return p_92089_1_.getItem() == Items.shears ? true : super.canApply(p_92089_1_);
    }
}
