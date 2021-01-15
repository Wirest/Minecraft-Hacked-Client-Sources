package net.minecraft.util;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class WeightedRandomFishable extends WeightedRandom.Item
{
    private final ItemStack returnStack;
    private float maxDamagePercent;
    private boolean enchantable;

    public WeightedRandomFishable(ItemStack returnStackIn, int itemWeightIn)
    {
        super(itemWeightIn);
        this.returnStack = returnStackIn;
    }

    public ItemStack getItemStack(Random random)
    {
        ItemStack var2 = this.returnStack.copy();

        if (this.maxDamagePercent > 0.0F)
        {
            int var3 = (int)(this.maxDamagePercent * this.returnStack.getMaxDamage());
            int var4 = var2.getMaxDamage() - random.nextInt(random.nextInt(var3) + 1);

            if (var4 > var3)
            {
                var4 = var3;
            }

            if (var4 < 1)
            {
                var4 = 1;
            }

            var2.setItemDamage(var4);
        }

        if (this.enchantable)
        {
            EnchantmentHelper.addRandomEnchantment(random, var2, 30);
        }

        return var2;
    }

    public WeightedRandomFishable setMaxDamagePercent(float maxDamagePercentIn)
    {
        this.maxDamagePercent = maxDamagePercentIn;
        return this;
    }

    public WeightedRandomFishable setEnchantable()
    {
        this.enchantable = true;
        return this;
    }
}
