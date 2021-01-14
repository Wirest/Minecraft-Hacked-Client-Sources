package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWalker extends Enchantment {
    private static final String __OBFID = "CL_00002155";

    public EnchantmentWaterWalker(int p_i45762_1_, ResourceLocation p_i45762_2_, int p_i45762_3_) {
        super(p_i45762_1_, p_i45762_2_, p_i45762_3_, EnumEnchantmentType.ARMOR_FEET);
        setName("waterWalker");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment
     * level passed.
     */
    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return p_77321_1_ * 10;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment
     * level passed.
     */
    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return getMinEnchantability(p_77317_1_) + 15;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
