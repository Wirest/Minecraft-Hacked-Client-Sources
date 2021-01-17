// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowKnockback extends Enchantment
{
    public EnchantmentArrowKnockback(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
        this.setName("arrowKnockback");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 12 + (enchantmentLevel - 1) * 20;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
