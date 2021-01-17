// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowInfinite extends Enchantment
{
    public EnchantmentArrowInfinite(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
        this.setName("arrowInfinite");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 20;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
