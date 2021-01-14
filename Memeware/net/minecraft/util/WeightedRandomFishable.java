package net.minecraft.util;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class WeightedRandomFishable extends WeightedRandom.Item {
    private final ItemStack returnStack;
    private float maxDamagePercent;
    private boolean enchantable;
    private static final String __OBFID = "CL_00001664";

    public WeightedRandomFishable(ItemStack p_i45317_1_, int p_i45317_2_) {
        super(p_i45317_2_);
        this.returnStack = p_i45317_1_;
    }

    public ItemStack getItemStack(Random p_150708_1_) {
        ItemStack var2 = this.returnStack.copy();

        if (this.maxDamagePercent > 0.0F) {
            int var3 = (int) (this.maxDamagePercent * (float) this.returnStack.getMaxDamage());
            int var4 = var2.getMaxDamage() - p_150708_1_.nextInt(p_150708_1_.nextInt(var3) + 1);

            if (var4 > var3) {
                var4 = var3;
            }

            if (var4 < 1) {
                var4 = 1;
            }

            var2.setItemDamage(var4);
        }

        if (this.enchantable) {
            EnchantmentHelper.addRandomEnchantment(p_150708_1_, var2, 30);
        }

        return var2;
    }

    public WeightedRandomFishable setMaxDamagePercent(float p_150709_1_) {
        this.maxDamagePercent = p_150709_1_;
        return this;
    }

    public WeightedRandomFishable setEnchantable() {
        this.enchantable = true;
        return this;
    }
}
