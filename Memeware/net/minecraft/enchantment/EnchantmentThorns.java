package net.minecraft.enchantment;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EnchantmentThorns extends Enchantment {
    private static final String __OBFID = "CL_00000122";

    public EnchantmentThorns(int p_i45764_1_, ResourceLocation p_i45764_2_, int p_i45764_3_) {
        super(p_i45764_1_, p_i45764_2_, p_i45764_3_, EnumEnchantmentType.ARMOR_TORSO);
        this.setName("thorns");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_) {
        return 10 + 20 * (p_77321_1_ - 1);
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

    public boolean canApply(ItemStack p_92089_1_) {
        return p_92089_1_.getItem() instanceof ItemArmor ? true : super.canApply(p_92089_1_);
    }

    public void func_151367_b(EntityLivingBase p_151367_1_, Entity p_151367_2_, int p_151367_3_) {
        Random var4 = p_151367_1_.getRNG();
        ItemStack var5 = EnchantmentHelper.func_92099_a(Enchantment.thorns, p_151367_1_);

        if (func_92094_a(p_151367_3_, var4)) {
            p_151367_2_.attackEntityFrom(DamageSource.causeThornsDamage(p_151367_1_), (float) func_92095_b(p_151367_3_, var4));
            p_151367_2_.playSound("damage.thorns", 0.5F, 1.0F);

            if (var5 != null) {
                var5.damageItem(3, p_151367_1_);
            }
        } else if (var5 != null) {
            var5.damageItem(1, p_151367_1_);
        }
    }

    public static boolean func_92094_a(int p_92094_0_, Random p_92094_1_) {
        return p_92094_0_ <= 0 ? false : p_92094_1_.nextFloat() < 0.15F * (float) p_92094_0_;
    }

    public static int func_92095_b(int p_92095_0_, Random p_92095_1_) {
        return p_92095_0_ > 10 ? p_92095_0_ - 10 : 1 + p_92095_1_.nextInt(4);
    }
}
