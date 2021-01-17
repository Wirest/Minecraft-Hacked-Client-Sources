// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowFire extends Enchantment
{
    public EnchantmentArrowFire(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
        this.setName("arrowFire");
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
