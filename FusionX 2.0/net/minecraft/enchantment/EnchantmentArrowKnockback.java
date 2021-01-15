package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowKnockback extends Enchantment
{
    private static final String __OBFID = "CL_00000101";

    public EnchantmentArrowKnockback(int p_i45775_1_, ResourceLocation p_i45775_2_, int p_i45775_3_)
    {
        super(p_i45775_1_, p_i45775_2_, p_i45775_3_, EnumEnchantmentType.BOW);
        this.setName("arrowKnockback");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_)
    {
        return 12 + (p_77321_1_ - 1) * 20;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int p_77317_1_)
    {
        return this.getMinEnchantability(p_77317_1_) + 25;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 2;
    }
}
