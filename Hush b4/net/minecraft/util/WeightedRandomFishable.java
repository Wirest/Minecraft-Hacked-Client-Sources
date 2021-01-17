// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Random;
import net.minecraft.item.ItemStack;

public class WeightedRandomFishable extends WeightedRandom.Item
{
    private final ItemStack returnStack;
    private float maxDamagePercent;
    private boolean enchantable;
    
    public WeightedRandomFishable(final ItemStack returnStackIn, final int itemWeightIn) {
        super(itemWeightIn);
        this.returnStack = returnStackIn;
    }
    
    public ItemStack getItemStack(final Random random) {
        final ItemStack itemstack = this.returnStack.copy();
        if (this.maxDamagePercent > 0.0f) {
            final int i = (int)(this.maxDamagePercent * this.returnStack.getMaxDamage());
            int j = itemstack.getMaxDamage() - random.nextInt(random.nextInt(i) + 1);
            if (j > i) {
                j = i;
            }
            if (j < 1) {
                j = 1;
            }
            itemstack.setItemDamage(j);
        }
        if (this.enchantable) {
            EnchantmentHelper.addRandomEnchantment(random, itemstack, 30);
        }
        return itemstack;
    }
    
    public WeightedRandomFishable setMaxDamagePercent(final float maxDamagePercentIn) {
        this.maxDamagePercent = maxDamagePercentIn;
        return this;
    }
    
    public WeightedRandomFishable setEnchantable() {
        this.enchantable = true;
        return this;
    }
}
