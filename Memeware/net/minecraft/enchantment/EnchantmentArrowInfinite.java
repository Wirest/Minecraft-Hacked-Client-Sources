package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowInfinite extends Enchantment {
    private static final String __OBFID = "CL_00000100";

    public EnchantmentArrowInfinite(int p_i45776_1_, ResourceLocation p_i45776_2_, int p_i45776_3_) {
        super(p_i45776_1_, p_i45776_2_, p_i45776_3_, EnumEnchantmentType.BOW);
        this.setName("arrowInfinite");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_) {
        return 20;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int p_77317_1_) {
        return 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 1;
    }
}
