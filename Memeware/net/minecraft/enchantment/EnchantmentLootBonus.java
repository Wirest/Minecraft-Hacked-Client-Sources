package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus extends Enchantment {
    private static final String __OBFID = "CL_00000119";

    protected EnchantmentLootBonus(int p_i45767_1_, ResourceLocation p_i45767_2_, int p_i45767_3_, EnumEnchantmentType p_i45767_4_) {
        super(p_i45767_1_, p_i45767_2_, p_i45767_3_, p_i45767_4_);

        if (p_i45767_4_ == EnumEnchantmentType.DIGGER) {
            this.setName("lootBonusDigger");
        } else if (p_i45767_4_ == EnumEnchantmentType.FISHING_ROD) {
            this.setName("lootBonusFishing");
        } else {
            this.setName("lootBonus");
        }
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_) {
        return 15 + (p_77321_1_ - 1) * 9;
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
        return 3;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_) && p_77326_1_.effectId != silkTouch.effectId;
    }
}
