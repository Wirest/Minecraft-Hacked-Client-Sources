package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentOxygen extends Enchantment {
    private static final String __OBFID = "CL_00000120";

    public EnchantmentOxygen(int p_i45766_1_, ResourceLocation p_i45766_2_, int p_i45766_3_) {
        super(p_i45766_1_, p_i45766_2_, p_i45766_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("oxygen");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_) {
        return 10 * p_77321_1_;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 30;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 3;
    }
}
